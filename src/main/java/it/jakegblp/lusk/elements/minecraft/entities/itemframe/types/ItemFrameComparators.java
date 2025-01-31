package it.jakegblp.lusk.elements.minecraft.entities.itemframe.types;

import it.jakegblp.lusk.api.GenericRelation;
import org.bukkit.Rotation;

import static it.jakegblp.lusk.utils.SkriptUtils.registerComparator;

@SuppressWarnings("unused")
public class ItemFrameComparators {
    static {
        registerComparator(Rotation.class, Rotation.class, (rotation1, rotation2) -> {
            int a = rotation1.ordinal();
            int b = rotation2.ordinal();
            if (a > b) {
                return GenericRelation.GREATER;
            } else if (a < b) {
                return GenericRelation.SMALLER;
            } else {
                return GenericRelation.EQUAL;
            }
        });
    }

    public ItemFrameComparators() {}
}
