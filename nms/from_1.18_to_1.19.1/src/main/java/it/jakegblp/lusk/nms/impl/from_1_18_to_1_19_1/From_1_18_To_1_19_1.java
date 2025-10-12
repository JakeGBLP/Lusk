package it.jakegblp.lusk.nms.impl.from_1_18_to_1_19_1;

import it.jakegblp.lusk.nms.core.adapters.EntityMetadataPacketAdapter;
import it.jakegblp.lusk.nms.core.adapters.EntityTypeAdapter;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataItem;
import it.jakegblp.lusk.nms.core.world.entity.serialization.EntitySerializerKey;
import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityMetadataPacket;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.*;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;
import static it.jakegblp.lusk.nms.core.world.entity.serialization.EntitySerializerKey.Type.OPTIONAL;

public class From_1_18_To_1_19_1 implements
        EntityTypeAdapter<EntityType>, EntityMetadataPacketAdapter<ClientboundSetEntityDataPacket> {

    @Override
    public EntityType<?> toNMSEntityType(org.bukkit.entity.EntityType from) {
        return Registry.ENTITY_TYPE.get((ResourceLocation) NMS.asNMSNamespacedKey(from.getKey()));
    }

    @Override
    public org.bukkit.entity.EntityType fromNMSEntityType(EntityType to) {
        return org.bukkit.Registry.ENTITY_TYPE.get(NMS.asNamespacedKey(Registry.ENTITY_TYPE.getKey(to)));
    }

    @Override
    public Class<EntityType> getNMSEntityTypeClass() {
        return EntityType.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClientboundSetEntityDataPacket toNMSEntityMetadataPacket(EntityMetadataPacket from) {
        SynchedEntityData synchedEntityData = new SynchedEntityData(new Entity(null, null) {
            @Override
            protected void defineSynchedData() {

            }

            @Override
            protected void readAdditionalSaveData(CompoundTag compoundTag) {

            }

            @Override
            protected void addAdditionalSaveData(CompoundTag compoundTag) {

            }

            @Override
            public Packet<?> getAddEntityPacket() {
                return null;
            }
        });
        synchedEntityData.assignValues((List<SynchedEntityData.DataItem<?>>) (List<?>) from.getEntityMetadata()
                .items()
                .stream()
                .filter(Objects::nonNull)
                .map(value -> {
                    Object nmsValue = NMS.toNMSObject(value.value());
                    EntitySerializerKey.Type type = value.serializerType();
                    return new SynchedEntityData.DataItem<>(
                            ((EntityDataSerializer<Object>) NMS.getEntityDataSerializer(
                                    NMS.getSerializableClass(value.valueClass()), type)
                            ).createAccessor(value.id()),
                            type == OPTIONAL ? Optional.ofNullable(nmsValue) : nmsValue);
                }).toList());
        return new ClientboundSetEntityDataPacket(from.getEntityId(), synchedEntityData, true);
    }

    @Override
    public EntityMetadataPacket fromNMSEntityMetadataPacket(ClientboundSetEntityDataPacket from) {
        List<SynchedEntityData.DataItem<?>> dataItems = from.getUnpackedData();
        if (dataItems == null) return null;
        EntityMetadata entityMetadata = new EntityMetadata();
        for (SynchedEntityData.DataItem<?> dataItem : dataItems) {
            var accessor = dataItem.getAccessor();
            int id = accessor.getId();
            entityMetadata.setInternal(id, new MetadataItem<>(id, dataItem.getValue(), (EntitySerializerKey<Object>) NMS.getEntityDataSerializerKey(accessor.getSerializer())));
        }
        return new EntityMetadataPacket(from.getId(), entityMetadata);
    }

    @Override
    public Class<ClientboundSetEntityDataPacket> getNMSEntityMetadataPacketClass() {
        return ClientboundSetEntityDataPacket.class;
    }
}
