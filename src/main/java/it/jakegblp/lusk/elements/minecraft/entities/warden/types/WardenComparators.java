package it.jakegblp.lusk.elements.minecraft.entities.warden.types;

import org.bukkit.entity.Warden;
import org.jetbrains.annotations.NotNull;
import org.skriptlang.skript.lang.comparator.Comparator;
import org.skriptlang.skript.lang.comparator.Comparators;
import org.skriptlang.skript.lang.comparator.Relation;

import static it.jakegblp.lusk.utils.Constants.HAS_WARDEN;

@SuppressWarnings("unused")
public class WardenComparators {
    static {
        if (HAS_WARDEN) {
            Comparators.registerComparator(Warden.class, Warden.AngerLevel.class, new Comparator<>() {
                @Override
                public @NotNull Relation compare(Warden w1, Warden.AngerLevel w2) {
                    return w1.getAngerLevel().equals(w2) ? Relation.EQUAL : Relation.NOT_EQUAL;
                }

                @Override
                public boolean supportsOrdering() {
                    return true;
                }
            });
        }
    }

    public WardenComparators() {}
}
