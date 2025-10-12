package it.jakegblp.lusk.nms.core.adapters;

import org.bukkit.entity.EntityType;

public interface EntityTypeAdapter<
        NMSEntityType
        > {
    NMSEntityType toNMSEntityType(EntityType from);

    EntityType fromNMSEntityType(NMSEntityType to);

    Class<NMSEntityType> getNMSEntityTypeClass();

    default boolean isNMSEntityType(Object object) {
        return getNMSEntityTypeClass().isInstance(object);
    }
}