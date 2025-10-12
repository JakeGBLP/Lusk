package it.jakegblp.lusk.nms.impl.to_1_17_1;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.mojang.datafixers.util.Pair;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import it.jakegblp.lusk.nms.api.NMSApi;
import it.jakegblp.lusk.nms.core.adapters.*;
import it.jakegblp.lusk.nms.core.protocol.packets.client.*;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataItem;
import it.jakegblp.lusk.nms.core.world.entity.serialization.EntitySerializerKey;
import lombok.Getter;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.IRegistry;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherSerializer;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntArrayList;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftVector;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static it.jakegblp.lusk.common.StructureTranslation.fromMapToPairList;
import static it.jakegblp.lusk.common.StructureTranslation.fromPairListToMap;
import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;
import static it.jakegblp.lusk.nms.core.world.entity.serialization.EntitySerializerKey.Type.OPTIONAL;

@Getter
@SuppressWarnings("rawtypes")
public class To_1_17_1 implements
        SetEquipmentPacketAdapter<PacketPlayOutEntityEquipment>,
        AddEntityPacketAdapter<PacketPlayOutSpawnEntity>,
        EntityTypeAdapter<EntityTypes>,
        EntityMetadataPacketAdapter<PacketPlayOutEntityMetadata>,
        PlayerPositionPacketAdapter<PacketPlayOutPosition.EnumPlayerTeleportFlags, PacketPlayOutPosition>,
        MajorChangesAdapter<
                        EnumItemSlot,
                        ItemStack,
                        Vec3D,
                        BlockPosition,
                        EntityPlayer,
                        EntityPose,
                        IChatBaseComponent,
                        Packet,
                        PlayerConnection,
                        NetworkManager,
                        PacketPlayOutBlockBreakAnimation,
                        PacketPlayOutAnimation,
                PacketPlayOutEntityDestroy
                        > {

    private final BiMap<Pose, EntityPose> poseMap;

    public To_1_17_1() {
        poseMap = ImmutableBiMap.of(org.bukkit.entity.Pose.SNEAKING, EntityPose.f);
    }

    @Override
    public PacketPlayOutSpawnEntity toNMSAddEntityPacket(AddEntityPacket from) {
        return new PacketPlayOutSpawnEntity(
                from.getEntityId(),
                from.getEntityUUID(),
                from.getX(),
                from.getY(),
                from.getZ(),
                from.getPitch(),
                from.getYaw(),
                (EntityTypes<?>) NMS.toNMSEntityType(from.getEntityType()),
                from.getData(),
                asNMSVector(from.getVelocity()));
    }

    @Override
    public AddEntityPacket fromNMSAddEntityPacket(PacketPlayOutSpawnEntity from) {
        return new AddEntityPacket(
                from.b(),
                from.c(),
                from.d(),
                from.e(),
                from.f(),
                from.j(),
                from.k(),
                fromNMSEntityType(from.l()),
                from.m(),
                new Vector(from.g(), from.h(), from.i())
        );
    }

    @Override
    public Class<PacketPlayOutSpawnEntity> getNMSAddEntityPacketClass() {
        return PacketPlayOutSpawnEntity.class;
    }

    @Override
    public Class<IChatBaseComponent> getNMSComponentClass() {
        return IChatBaseComponent.class;
    }

    @Override
    public void sendPacketInternal(PlayerConnection playerConnection, Packet packet) {
        playerConnection.sendPacket(packet);
    }

    @Override
    public NetworkManager getConnection(PlayerConnection playerConnection) {
        return playerConnection.a;
    }

    @Override
    public Channel getChannel(NetworkManager networkManager) {
        return networkManager.k;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PacketPlayOutEntityMetadata toNMSEntityMetadataPacket(EntityMetadataPacket from) {
        PacketDataSerializer serializer = new PacketDataSerializer(Unpooled.buffer());
        serializer.d(from.getEntityId());
        List<DataWatcher.Item<?>> items = new ArrayList<>();
        for (MetadataItem<? extends Entity, ?> item : from.getEntityMetadata().items()) {
            if (item != null) {
                Object nmsValue = NMS.toNMSObject(item.value());
                EntitySerializerKey.Type type = item.serializerType();
                items.add(new DataWatcher.Item<>(
                        ((DataWatcherSerializer<Object>)
                                NMS.getEntityDataSerializer(NMS.getSerializableClass(item.valueClass()), type))
                                .a(item.id()),
                        type == OPTIONAL ? Optional.ofNullable(nmsValue) : nmsValue
                ));
            }
        }
        DataWatcher.a(items, serializer);
        return new PacketPlayOutEntityMetadata(serializer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public EntityMetadataPacket fromNMSEntityMetadataPacket(PacketPlayOutEntityMetadata from) {
        List<DataWatcher.Item<?>> items = from.b();
        if (items == null) return null;
        EntityMetadata entityMetadata = new EntityMetadata();
        for (var dataValue : items) {
            var accessor = dataValue.a();
            int id = accessor.a();
            entityMetadata.setInternal(id, new MetadataItem<>(id, dataValue.b(), (EntitySerializerKey<Object>) NMS.getEntityDataSerializerKey(accessor.b())));
        }
        return new EntityMetadataPacket(from.c(), entityMetadata);
    }

    @Override
    public Class<PacketPlayOutEntityMetadata> getNMSEntityMetadataPacketClass() {
        return PacketPlayOutEntityMetadata.class;
    }

    @Override
    public EntityTypes<?> toNMSEntityType(EntityType from) {
        return IRegistry.Y.get((MinecraftKey) NMS.asNMSNamespacedKey(from.getKey()));
    }

    @Override
    public EntityType fromNMSEntityType(EntityTypes to) {
        return org.bukkit.Registry.ENTITY_TYPE.get(NMS.asNamespacedKey(IRegistry.Y.getKey(to)));
    }

    @Override
    public Class<EntityTypes> getNMSEntityTypeClass() {
        return EntityTypes.class;
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
    public Class<EnumItemSlot> getNMSEquipmentSlotClass() {
        return EnumItemSlot.class;
    }

    @Override
    public Vector asVector(Vec3D vec3D) {
        // Using craft classes usually errors when using different versions, here it's fine, but not future-proofed.
        return CraftVector.toBukkit(vec3D);
    }

    @Override
    public Vec3D asNMSVector(Vector vector) {
        // Using craft classes usually errors when using different versions, here it's fine, but not future-proofed.
        return CraftVector.toNMS(vector);
    }

    @Override
    public Class<Vec3D> getNMSVectorClass() {
        return Vec3D.class;
    }

    @Override
    public BlockVector asBlockVector(BlockPosition blockPosition) {
        return new BlockVector(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
    }

    @Override
    public BlockPosition asNMSBlockVector(BlockVector blockVector) {
        return new BlockPosition(blockVector.getBlockX(), blockVector.getBlockY(), blockVector.getBlockZ());
    }

    @Override
    public Class<BlockPosition> getNMSBlockVectorClass() {
        return BlockPosition.class;
    }

    @Override
    public Pose asPose(EntityPose entityPose) {
        return getPoseMap().inverse().getOrDefault(entityPose, org.bukkit.entity.Pose.valueOf(entityPose.toString()));
    }

    @Override
    public EntityPose asNMSPose(Pose pose) {
        return getPoseMap().getOrDefault(pose, EntityPose.valueOf(pose.toString()));
    }

    @Override
    public Class<EntityPose> getNMSPoseClass() {
        return EntityPose.class;
    }

    @Override
    public Player asPlayer(EntityPlayer entityPlayer) {
        return entityPlayer.getBukkitEntity();
    }

    @Override
    public Class<EntityPlayer> getNMSServerPlayerClass() {
        return EntityPlayer.class;
    }

    @Override
    public PlayerConnection getPlayerConnection(EntityPlayer entityPlayer) {
        return entityPlayer.b;
    }

    @Override
    public IChatBaseComponent asNMSComponent(net.kyori.adventure.text.Component component) {
        return IChatBaseComponent.ChatSerializer.a(GsonComponentSerializer.gson().serialize(component));
    }

    @Override
    public net.kyori.adventure.text.Component asComponent(IChatBaseComponent component) {
        return GsonComponentSerializer.gson().deserialize(IChatBaseComponent.ChatSerializer.a(component));
    }

    @Override
    public PacketPlayOutBlockBreakAnimation toNMSBlockDestructionPacket(BlockDestructionPacket from) {
        return new PacketPlayOutBlockBreakAnimation(from.getEntityId(), asNMSBlockVector(from.getPosition()), from.getBlockDestructionStage());
    }

    @Override
    public BlockDestructionPacket fromNMSBlockDestructionPacket(PacketPlayOutBlockBreakAnimation from) {
        return new BlockDestructionPacket(from.b(), asBlockVector(from.c()), from.d());
    }

    @Override
    public Class<PacketPlayOutBlockBreakAnimation> getNMSBlockDestructionPacketClass() {
        return PacketPlayOutBlockBreakAnimation.class;
    }

    @Override
    public PacketPlayOutAnimation toNMSEntityAnimationPacket(EntityAnimationPacket from) {
        PacketDataSerializer friendlyByteBuf = new PacketDataSerializer(Unpooled.buffer());
        friendlyByteBuf.d(from.getEntityId());
        friendlyByteBuf.writeByte(from.getEntityAnimationId());
        return new PacketPlayOutAnimation(friendlyByteBuf);
    }

    @Override
    public EntityAnimationPacket fromNMSEntityAnimationPacket(PacketPlayOutAnimation from) {
        return new EntityAnimationPacket(from.b(), from.c());
    }

    @Override
    public Class<PacketPlayOutAnimation> getNMSEntityAnimationPacketClass() {
        return PacketPlayOutAnimation.class;
    }

    @Override
    public PacketPlayOutEntityDestroy toNMSRemoveEntitiesPacket(RemoveEntitiesPacket from) {
        return new PacketPlayOutEntityDestroy(new IntArrayList(from.getEntityIds()));
    }

    @Override // todo: this doesnt work on 1.17 because the packet became list based on 1.17.1, decide what to do.
    public RemoveEntitiesPacket fromNMSRemoveEntitiesPacket(PacketPlayOutEntityDestroy from) {
        return new RemoveEntitiesPacket(from.b());
    }

    @Override
    public Class<PacketPlayOutEntityDestroy> getNMSRemoveEntitiesPacketClass() {
        return PacketPlayOutEntityDestroy.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PacketPlayOutEntityEquipment toNMSSetEquipmentPacket(SetEquipmentPacket from) {
        Object list = fromMapToPairList(from.getEquipment(), NMSApi::asNMSEquipmentSlot, NMSApi::asNMSItemStack);
        return new PacketPlayOutEntityEquipment(from.getEntityId(), (List<Pair<EnumItemSlot, ItemStack>>) list);
    }

    @Override
    public SetEquipmentPacket fromNMSSetEquipmentPacket(PacketPlayOutEntityEquipment from) {
        return new SetEquipmentPacket(from.b(), fromPairListToMap(from.c(), NMSApi::asEquipmentSlot, NMSApi::asItemStack));
    }

    @Override
    public Class<PacketPlayOutEntityEquipment> getNMSSetEquipmentPacketClass() {
        return PacketPlayOutEntityEquipment.class;
    }

    @Override
    public Class<PacketPlayOutPosition.EnumPlayerTeleportFlags> getNMSRelativeFlagClass() {
        return PacketPlayOutPosition.EnumPlayerTeleportFlags.class;
    }

    @Override
    public PacketPlayOutPosition asNMSPlayerPositionPacket(PlayerPositionPacket packet) {
        var position = packet.getPosition();
        return new PacketPlayOutPosition(
                position.getX(),
                position.getY(),
                position.getZ(),
                packet.getYaw(),
                packet.getPitch(),
                asNMSRelativeFlags(packet.getRelativeFlags()),
                packet.getTeleportId(),
                true
        );
    }

    @Override
    public PlayerPositionPacket fromNMSPlayerPositionPacket(PacketPlayOutPosition packet) {
        return new PlayerPositionPacket(
                packet.g(),
                new Vector(packet.b(), packet.c(), packet.d()),
                packet.e(),
                packet.f(),
                fromNMSRelativeFlag(packet.i())
        );
    }

    @Override
    public Class<PacketPlayOutPosition> getNMSPlayerPositionPacketClass() {
        return PacketPlayOutPosition.class;
    }
}
