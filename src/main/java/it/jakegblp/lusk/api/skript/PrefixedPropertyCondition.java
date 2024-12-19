package it.jakegblp.lusk.api.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAPIException;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.lang.Condition;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public abstract class PrefixedPropertyCondition<T> extends PropertyCondition<T> {

    public static void register(
            Class<? extends Condition> condition,
            String prefix,
            String property,
            String type) {
        register(condition, PropertyCondition.PropertyType.BE, prefix, property, type);
    }

    public static void register(
            Class<? extends Condition> condition,
            PropertyCondition.PropertyType propertyType,
            @Nullable String prefix,
            String property,
            String type) {
        if (type.contains("%"))
            throw new SkriptAPIException("The type argument must not contain any '%'s");
        prefix = prefix == null ? "" : prefix + " ";
        switch (propertyType) {
            case BE:
                Skript.registerCondition(condition,
                        prefix + "%" + type + "% (is|are) " + property,
                        prefix + "%" + type + "% (isn't|is not|aren't|are not) " + property);
                break;
            case CAN:
                Skript.registerCondition(condition,
                        prefix + "%" + type + "% can " + property,
                        prefix + "%" + type + "% (can't|cannot|can not) " + property);
                break;
            case HAVE:
                Skript.registerCondition(condition,
                        prefix + "%" + type + "% (has|have) " + property,
                        prefix + "%" + type + "% (doesn't|does not|do not|don't) have " + property);
                break;
            case WILL:
                Skript.registerCondition(condition,
                        prefix + "%" + type + "% will " + property,
                        prefix + "%" + type + "% (will (not|neither)|won't) " + property);
                break;
            default:
                assert false;
        }
    }

    public abstract String getPrefix();

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return getPrefix() + " " + super.toString(event, debug);
    }
}
