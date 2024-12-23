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
            @Nullable String prefix,
            String property,
            String type) {
        register(condition, ExtendedPropertyType.BE, prefix, property, type);
    }

    public static void register(
            Class<? extends Condition> condition,
            ExtendedPropertyType propertyType,
            @Nullable String prefix,
            String property,
            String type) {
        if (type.contains("%"))
            throw new SkriptAPIException("The type argument must not contain any '%'s");
        prefix = prefix != null ? prefix + " " : "";
        Skript.registerCondition(condition,
                prefix + "%" + type + "% "+propertyType.getPattern(false)+" " + property,
                prefix + "%" + type + "% "+propertyType.getPattern(true)+" " + property);
    }

    public abstract String getPrefix();

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return getPrefix() + " " + super.toString(event, debug);
    }
}
