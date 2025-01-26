package it.jakegblp.lusk.elements.minecraft.entities.warden.types;

import it.jakegblp.lusk.api.GenericRelation;
import org.bukkit.entity.Warden;

import static it.jakegblp.lusk.utils.CompatibilityUtils.registerComparator;
import static it.jakegblp.lusk.utils.Constants.HAS_WARDEN;

@SuppressWarnings("unused")
public class WardenComparators {
    static {
        if (HAS_WARDEN) {
            registerComparator(Warden.class, Warden.AngerLevel.class, (warden, angerLevel) ->
                    warden.getAngerLevel().equals(angerLevel) ? GenericRelation.EQUAL : GenericRelation.NOT_EQUAL);
        }
    }

    public WardenComparators() {}
}
