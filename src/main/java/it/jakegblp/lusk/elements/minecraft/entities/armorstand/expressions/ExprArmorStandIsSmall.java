package it.jakegblp.lusk.elements.minecraft.entities.armorstand.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import it.jakegblp.lusk.utils.ArmorStandUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.LuskUtils.registerVerboseBooleanPropertyExpression;

@Name("Armor Stand - is Small (Property)")
@Description("""
Gets and sets the `small` property of an armorstand entity or item, to do so with an armorstand item you must have Paper.
""")
@Examples({"set small property of target to true", "set whether armor stand target is small to true"})
@Since("1.0.2, 1.3 (item)")
@DocumentationId("9059")
@SuppressWarnings("unused")
public class ExprArmorStandIsSmall extends SimplePropertyExpression<Object, Boolean> {

    static {
        registerVerboseBooleanPropertyExpression(ExprArmorStandIsSmall.class, Boolean.class, "[armor[ |-]stand]", "[is] small", "armorstands/itemtypes");
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET -> new Class[]{Boolean.class};
            case RESET, DELETE -> new Class[0];
            default -> null;
        };
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        boolean hasArms = false;
        if (mode == Changer.ChangeMode.SET && delta != null && delta[0] instanceof Boolean bool) {
            hasArms = bool;
        }
        for (Object armorStand : getExpr().getAll(event)) {
            ArmorStandUtils.setIsSmall(armorStand, hasArms);
        }
    }

    @Override
    public @Nullable Boolean convert(Object from) {
        return ArmorStandUtils.isSmall(from);
    }

    @Override
    protected String getPropertyName() {
        return "the armor stand is small property";
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }
}