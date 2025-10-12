package it.jakegblp.lusk.nms.impl.from_1_19_3;

import it.jakegblp.lusk.nms.core.adapters.EntityMetadataPacketAdapter;
import it.jakegblp.lusk.nms.core.adapters.EntityTypeAdapter;
import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityMetadataPacket;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataItem;
import it.jakegblp.lusk.nms.core.world.entity.serialization.EntitySerializerKey;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Registry;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;
import static it.jakegblp.lusk.nms.core.world.entity.serialization.EntitySerializerKey.Type.OPTIONAL;

public class From_1_19_3 implements
        EntityTypeAdapter<EntityType>, EntityMetadataPacketAdapter<ClientboundSetEntityDataPacket> {

    @Override
    public EntityType<?> toNMSEntityType(org.bukkit.entity.EntityType from) {
        return BuiltInRegistries.ENTITY_TYPE.getOptional((ResourceLocation) NMS.asNMSNamespacedKey(from.getKey())).orElseThrow();
    }

    @Override
    public org.bukkit.entity.EntityType fromNMSEntityType(EntityType to) {
        return Registry.ENTITY_TYPE.get(NMS.asNamespacedKey(BuiltInRegistries.ENTITY_TYPE.getKey(to)));
    }

    @Override
    public Class<EntityType> getNMSEntityTypeClass() {
        return EntityType.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClientboundSetEntityDataPacket toNMSEntityMetadataPacket(EntityMetadataPacket from) {
        // todo: turn most of this into an util method
        return new ClientboundSetEntityDataPacket(
                from.getEntityId(),
                (List<SynchedEntityData.DataValue<?>>) (List<?>) from.getEntityMetadata().items().stream()
                        .filter(Objects::nonNull)
                        .map(item -> {
                            System.out.println("converting item: " + item);
                            Object nmsValue = NMS.toNMSObject(item.value());
                            EntitySerializerKey.Type type = item.serializerType();
                            return new SynchedEntityData.DataValue<>(
                                    item.id(),
                                    (EntityDataSerializer<Object>) NMS.getEntityDataSerializer(
                                            NMS.getSerializableClass(item.valueClass()), type
                                    ), type == OPTIONAL ? Optional.ofNullable(nmsValue) : nmsValue);
                        }).toList()
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public EntityMetadataPacket fromNMSEntityMetadataPacket(ClientboundSetEntityDataPacket from) {
        List<SynchedEntityData.DataValue<?>> dataValueList = from.packedItems();
        EntityMetadata entityMetadata = new EntityMetadata();
        for (SynchedEntityData.DataValue<?> dataValue : dataValueList) {
            int id = dataValue.id();
            entityMetadata.setInternal(id, new MetadataItem<>(id, dataValue.value(), (EntitySerializerKey<Object>) NMS.getEntityDataSerializerKey(dataValue.serializer())));
        }
        return new EntityMetadataPacket(from.id(), entityMetadata);
    }

    @Override
    public Class<ClientboundSetEntityDataPacket> getNMSEntityMetadataPacketClass() {
        return ClientboundSetEntityDataPacket.class;
    }
}