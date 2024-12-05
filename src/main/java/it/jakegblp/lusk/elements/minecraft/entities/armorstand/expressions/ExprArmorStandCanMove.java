package it.jakegblp.lusk.elements.minecraft.entities.armorstand.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import it.jakegblp.lusk.utils.ArmorStandUtils;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.LuskUtils.registerVerboseBooleanPropertyExpression;

@Name("Armor Stand - can Move (Property)")
@Description("""
Gets and sets the `canMove` property of an armorstand entity or item.

Unlike other Armorstand properties, this one cannot be used on the armorstand item as of 1.21.3.
""")
@Examples({"set can move property of target to true", "set whether armor stand target can move to true"})
@Since("1.0.2")
@SuppressWarnings("unused")
public class ExprArmorStandCanMove extends SimplePropertyExpression<Entity, Boolean> {

    static {
        registerVerboseBooleanPropertyExpression(ExprArmorStandCanMove.class, Boolean.class, "[armor[ |-]stand]", "[can] move", "livingentities");
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[] {Boolean.class} : null;
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET && delta != null && delta[0] instanceof Boolean bool) {
            getExpr().stream(event).forEach(object -> ArmorStandUtils.setCanMove(object, bool));
        }
    }

    @Override
    public @Nullable Boolean convert(Entity from) {
        return ArmorStandUtils.canMove(from);
    }

    @Override
    protected String getPropertyName() {
        return "the armor stand can move property";
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }
}