package it.jakegblp.lusk.elements.classes;

import com.vdurmont.semver4j.Semver;
import org.jetbrains.annotations.NotNull;
import org.skriptlang.skript.lang.comparator.Comparator;
import org.skriptlang.skript.lang.comparator.Comparators;
import org.skriptlang.skript.lang.comparator.Relation;

public class AddonComparators {
    static {
        Comparators.registerComparator(Semver.class, Semver.class, new Comparator<>() {
            @Override
            public @NotNull Relation compare(Semver v1, Semver v2) {
                return v1.isGreaterThan(v2) ? Relation.GREATER : v2.isGreaterThan(v1) ? Relation.SMALLER : Relation.EQUAL;
            }

            @Override
            public boolean supportsOrdering() {
                return true;
            }
        });
    }

    public AddonComparators() {
    }
}