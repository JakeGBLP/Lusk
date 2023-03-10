package me.jake.lusk.elements.classes;

import ch.njol.skript.classes.Comparator;
import ch.njol.skript.registrations.Comparators;
import me.jake.lusk.classes.Version;
import org.jetbrains.annotations.NotNull;

public class AddonComparators {
    public AddonComparators() {}

    static {
        // 1.0.0

        // Custom

        // Version (>|<)[=] Version
        Comparators.registerComparator(Version.class, Version.class, new Comparator<>() {
            @Override
            public @NotNull Relation compare(Version v1, Version v2) {
                return v1.isNewerThan(v2) ? Relation.GREATER : v2.isNewerThan(v1) ? Relation.SMALLER : Relation.EQUAL;
            }

            @Override
            public boolean supportsOrdering() {
                return true;
            }
        });
    }
}