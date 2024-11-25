package it.jakegblp.lusk.utils;

import ch.njol.skript.util.Timespan;
import org.bukkit.attribute.Attribute;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.HAS_SCALE_ATTRIBUTE;

public class DeprecationUtils {
    @Nullable
    public static Attribute getScaleAttribute() {
        try {
            return (Attribute) Attribute.class.getDeclaredField("GENERIC_SCALE").get(null);
        } catch (final NoSuchFieldException | SecurityException | IllegalAccessException ignored) {}
        if (HAS_SCALE_ATTRIBUTE) {
            return Attribute.SCALE;
        }
        return null;
    }

    //todo: implement to allow pre 2.7/8 to use timespans when lusk has 2.10 dependency
    public static Timespan fromTicks(long ticks) {
        return null;
    }
}
