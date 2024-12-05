package it.jakegblp.lusk.elements.minecraft.entities.armorstand.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import it.jakegblp.lusk.utils.ArmorStandUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.LuskUtils.registerVerboseBooleanPropertyExpression;

@Name("Armor Stand - is Marker (Property)")
@Description("""
Gets and sets the `marker` property of an armorstand entity or item, to do so with an armorstand item you must have Paper.
""")
@Examples({"set marker of target to true", "set whether armor stand target is marker to true"})
@Since("1.0.2, 1.3 (item)")
@SuppressWarnings("unused")
public class ExprArmorStandIsMarker extends SimplePropertyExpression<Object, Boolean> {

    static {
        registerVerboseBooleanPropertyExpression(ExprArmorStandIsMarker.class, Boolean.class, "[armor[ |-]stand]", "[is] marker", "livingentities/itemtypes");
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[] {Boolean.class} : null;
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET && delta != null && delta[0] instanceof Boolean bool) {
            getExpr().stream(event).forEach(object -> ArmorStandUtils.setIsMarker(object, bool));
        }
    }

    @Override
    public @Nullable Boolean convert(Object from) {
        return ArmorStandUtils.isMarker(from);
    }

    @Override
    protected String getPropertyName() {
        return "the armor stand is marker property";
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }
}