package it.jakegblp.lusk.elements.minecraft.entities.itemframe.types;

import org.bukkit.Rotation;
import org.jetbrains.annotations.NotNull;
import org.skriptlang.skript.lang.comparator.Comparator;
import org.skriptlang.skript.lang.comparator.Comparators;
import org.skriptlang.skript.lang.comparator.Relation;

@SuppressWarnings("unused")
public class ItemFrameComparators {
    static {
        Comparators.registerComparator(Rotation.class, Rotation.class, new Comparator<>() {
            @Override
            public @NotNull Relation compare(Rotation rotation1, Rotation rotation2) {
                int a = rotation1.ordinal();
                int b = rotation2.ordinal();
                if (a > b) {
                    return Relation.GREATER;
                } else if (a < b) {
                    return Relation.SMALLER;
                } else {
                    return Relation.EQUAL;
                }
            }

            @Override
            public boolean supportsOrdering() {
                return true;
            }
        });

    }

    public ItemFrameComparators() {}
}
