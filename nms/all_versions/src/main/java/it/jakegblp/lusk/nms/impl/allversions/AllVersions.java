package it.jakegblp.lusk.nms.impl.allversions;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.papermc.paper.adventure.PaperAdventure;
import it.jakegblp.lusk.nms.core.adapters.SharedBehaviorAdapter;
import it.jakegblp.lusk.nms.core.protocol.packets.client.*;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataItem;
import it.jakegblp.lusk.nms.core.world.entity.serialization.EntitySerializerKey;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;
import static it.jakegblp.lusk.nms.core.world.entity.serialization.EntitySerializerKey.Type.OPTIONAL;

@Getter
public class AllVersions implements
        SharedBehaviorAdapter<
                EquipmentSlot,
                ItemStack,
                Vec3,
                BlockPos,
                ServerPlayer,
                Pose,
                Component,
                Packet,
                ResourceLocation,
                EntityType,
                ServerGamePacketListenerImpl,
                Connection,
                ClientboundBundlePacket,
                ClientboundBlockDestructionPacket,
                ClientboundAnimatePacket,
                ClientboundAddEntityPacket,
                ClientboundRemoveEntitiesPacket,
                ClientboundSetEntityDataPacket
                > {

    private final BiMap<org.bukkit.entity.Pose, Pose> poseMap;

    public AllVersions() {
        poseMap = ImmutableBiMap.of(org.bukkit.entity.Pose.SNEAKING, Pose.CROUCHING);
    }

    @Override
    public Class<Packet> getNMSPacketClass() {
        return Packet.class;
    }

    @Override
    public Class<ItemStack> getNMSItemStackClass() {
        return ItemStack.class;
    }

    @Override
    public Class<EquipmentSlot> getNMSEquipmentSlotClass() {
        return EquipmentSlot.class;
    }

    @Override
    public Vector asVector(Vec3 vec3) {
        return new Vector(vec3.x, vec3.y, vec3.z);
    }

    @Override
    public Vec3 asNMSVector(Vector vector) {
        return new Vec3(vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    public Class<Vec3> getNMSVectorClass() {
        return Vec3.class;
    }

    @Override
    public BlockVector asBlockVector(BlockPos blockPosition) {
        return new BlockVector(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
    }

    @Override
    public BlockPos asNMSBlockVector(BlockVector blockVector) {
        return new BlockPos(blockVector.getBlockX(), blockVector.getBlockY(), blockVector.getBlockZ());
    }

    @Override
    public Class<BlockPos> getNMSBlockVectorClass() {
        return BlockPos.class;
    }

    @Override
    public org.bukkit.entity.Pose asPose(Pose pose) {
        return getPoseMap().inverse().getOrDefault(pose, org.bukkit.entity.Pose.valueOf(pose.toString()));
    }

    @Override
    public Pose asNMSPose(org.bukkit.entity.Pose pose) {
        return getPoseMap().getOrDefault(pose, Pose.valueOf(pose.toString()));
    }

    @Override
    public Class<Pose> getNMSPoseClass() {
        return Pose.class;
    }

    @Override
    public Player asPlayer(ServerPlayer serverPlayer) {
        return serverPlayer.getBukkitEntity();
    }

    @Override
    public Class<ServerPlayer> getNMSServerPlayerClass() {
        return ServerPlayer.class;
    }

    @Override
    public ServerGamePacketListenerImpl getPlayerConnection(ServerPlayer serverPlayer) {
        return serverPlayer.connection;
    }


    @Override
    @SuppressWarnings("unchecked")
    public ClientboundBundlePacket toNMSClientBundlePacket(ClientBundlePacket from) {
        List<Packet<? super ClientGamePacketListener>> nmsPackets = new ArrayList<>();
        for (ClientboundPacket packet : from.getPackets())
            nmsPackets.add((Packet<ClientGamePacketListener>) packet.asNMS());
        return new ClientboundBundlePacket(nmsPackets);
    }

    @Override
    public ClientBundlePacket fromNMSClientBundlePacket(ClientboundBundlePacket from) {
        List<ClientboundPacket> packetList = new ArrayList<>();
        for (Packet<? super ClientGamePacketListener> subPacket : from.subPackets()) {
            it.jakegblp.lusk.nms.core.protocol.packets.Packet packet = NMS.fromNMSPacket(subPacket);
            if (packet instanceof ClientboundPacket clientboundPacket)
                packetList.add(clientboundPacket);
        }
        return new ClientBundlePacket(packetList);
    }

    @Override
    public Class<ClientboundBundlePacket> getNMSClientBundlePacketClass() {
        return ClientboundBundlePacket.class;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public ClientboundAddEntityPacket toNMSAddEntityPacket(AddEntityPacket from) {
        return new ClientboundAddEntityPacket(
                from.getEntityId(),
                from.getEntityUUID(),
                from.getX(),
                from.getY(),
                from.getZ(),
                from.getPitch(),
                from.getYaw(),
                toNMSEntityType(from.getEntityType()),
                from.getData(),
                asNMSVector(from.getVelocity()),
                from.getHeadYaw());
    }

    @Override
    public AddEntityPacket fromNMSAddEntityPacket(ClientboundAddEntityPacket from) {
        return new AddEntityPacket(
                from.getId(),
                from.getUUID(),
                from.getX(),
                from.getY(),
                from.getZ(),
                from.getXRot(),
                from.getYRot(),
                fromNMSEntityType(from.getType()),
                from.getData(),
                new Vector(from.getX(), from.getY(), from.getZ()),
                from.getYHeadRot()
        );
    }

    @Override
    public Class<ClientboundAddEntityPacket> getNMSAddEntityPacketClass() {
        return ClientboundAddEntityPacket.class;
    }

    @Override
    public Class<Component> getNMSComponentClass() {
        return Component.class;
    }

    @Override
    public void sendPacketInternal(ServerGamePacketListenerImpl serverGamePacketListener, Packet packet) {
        serverGamePacketListener.send(packet);
    }

    @Override
    public Connection getConnection(ServerGamePacketListenerImpl serverGamePacketListener) {
        return serverGamePacketListener.connection;
    }

    @Override
    public Channel getChannel(Connection connection) {
        return connection.channel;
    }

    @Override
    public ClientboundBlockDestructionPacket toNMSBlockDestructionPacket(BlockDestructionPacket from) {
        return new ClientboundBlockDestructionPacket(from.getEntityId(), asNMSBlockVector(from.getPosition()), from.getBlockDestructionStage());
    }

    @Override
    public BlockDestructionPacket fromNMSBlockDestructionPacket(ClientboundBlockDestructionPacket from) {
        return new BlockDestructionPacket(from.getId(), asBlockVector(from.getPos()), from.getProgress());
    }

    @Override
    public Class<ClientboundBlockDestructionPacket> getNMSBlockDestructionPacketClass() {
        return ClientboundBlockDestructionPacket.class;
    }

    @Override
    public ClientboundAnimatePacket toNMSEntityAnimationPacket(EntityAnimationPacket from) {
        FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
        friendlyByteBuf.writeVarInt(from.getEntityId());
        friendlyByteBuf.writeByte(from.getEntityAnimationId());
        return ClientboundAnimatePacket.STREAM_CODEC.decode(friendlyByteBuf);
    }

    @Override
    public EntityAnimationPacket fromNMSEntityAnimationPacket(ClientboundAnimatePacket from) {
        return new EntityAnimationPacket(from.getId(), from.getAction());
    }

    @Override
    public Class<ClientboundAnimatePacket> getNMSEntityAnimationPacketClass() {
        return ClientboundAnimatePacket.class;
    }

    @Override
    public ClientboundRemoveEntitiesPacket toNMSRemoveEntitiesPacket(RemoveEntitiesPacket from) {
        return new ClientboundRemoveEntitiesPacket(from.getEntityIds());
    }

    @Override
    public RemoveEntitiesPacket fromNMSRemoveEntitiesPacket(ClientboundRemoveEntitiesPacket from) {
        return new RemoveEntitiesPacket(from.getEntityIds());
    }

    @Override
    public Class<ClientboundRemoveEntitiesPacket> getNMSRemoveEntitiesPacketClass() {
        return ClientboundRemoveEntitiesPacket.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClientboundSetEntityDataPacket toNMSEntityMetadataPacket(EntityMetadataPacket from) {
        return new ClientboundSetEntityDataPacket(
                from.getEntityId(),
                (List<SynchedEntityData.DataValue<?>>) (List<?>) from.getEntityMetadata().items().stream()
                        .filter(Objects::nonNull)
                        .map(item -> {
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

    @Override
    public EntityType<?> toNMSEntityType(org.bukkit.entity.EntityType from) {
        return BuiltInRegistries.ENTITY_TYPE.getOptional(asNMSNamespacedKey(from.getKey())).orElseThrow();
    }

    @Override
    public org.bukkit.entity.EntityType fromNMSEntityType(EntityType to) {
        return Registry.ENTITY_TYPE.get(asNamespacedKey(BuiltInRegistries.ENTITY_TYPE.getKey(to)));
    }

    @Override
    public Class<EntityType> getNMSEntityTypeClass() {
        return EntityType.class;
    }

    @Override
    public ResourceLocation asNMSNamespacedKey(NamespacedKey key) {
        return PaperAdventure.asVanillaNullable(key);
    }

    @Override
    public NamespacedKey asNamespacedKey(ResourceLocation resourceLocation) {
        Key key = PaperAdventure.asAdventure(resourceLocation);
        return new NamespacedKey(key.namespace(), key.value());
    }

    @Override
    public Class<ResourceLocation> getNMSNamespacedKeyClass() {
        return ResourceLocation.class;
    }

    @Override
    public Component asNMSComponent(net.kyori.adventure.text.Component component) {
        return PaperAdventure.asVanilla(component);
    }

    @Override
    public net.kyori.adventure.text.Component asComponent(Component component) {
        return PaperAdventure.asAdventure(component);
    }
}
