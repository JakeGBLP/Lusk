package it.jakegblp.lusk.version.nms.v1_21_4;

import com.mojang.authlib.GameProfile;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import it.jakegblp.lusk.api.NMSAdapter;
import it.jakegblp.lusk.api.enums.EntityAnimation;
import it.jakegblp.lusk.api.packets.NMSPlayer;
import it.jakegblp.lusk.utils.NMSUtils;
import it.jakegblp.lusk.version.nms.v1_21_4.packets.NMSPlayer_1_21_4;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.*;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_21_R3.CraftServer;
import org.bukkit.craftbukkit.v1_21_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_21_R3.entity.*;
import org.bukkit.craftbukkit.v1_21_R3.util.CraftBlockVector;
import org.bukkit.craftbukkit.v1_21_R3.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.v1_21_R3.util.CraftVector;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.UUID;

import static it.jakegblp.lusk.utils.NMSUtils.getIdOrRandom;
import static it.jakegblp.lusk.utils.ReflectionUtils.getFieldValue;

public class NMS_1_21_4 implements NMSAdapter<
        ResourceLocation,
        MinecraftServer,
        Packet<?>,
        EntityType<?>,
        ServerLevel,
        ServerPlayer,
        IronGolem,
        LivingEntity,
        Entity,
        GameProfile,
        Vec3,
        Vec3i,
        BlockPos,
        Block,
        Connection,
        ServerCommonPacketListenerImpl
        > {

    public static NMS_1_21_4 NMS = null;

    public NMS_1_21_4() {
        NMS = this;
    }


    @Override
    public @NotNull ObfuscatedFields getObfuscatedFields() {
        return new ObfuscatedFields("e");
    }

    @Override
    public ResourceLocation getNMSNamespacedKey(NamespacedKey key) {
        return CraftNamespacedKey.toMinecraft(key);
    }

    @Override
    public ServerLevel getNMSWorld(World world) {
        return ((CraftWorld) world).getHandle();
    }

    @Override
    public Block getNMSBlock(org.bukkit.block.Block block) {
        return ((CraftBlock) block).getNMS().getBlock();
    }

    @Override
    public Entity getNMSEntity(org.bukkit.entity.Entity entity) {
        return ((CraftEntity) entity).getHandle();
    }

    @Override
    public LivingEntity getNMSLivingEntity(org.bukkit.entity.LivingEntity entity) {
        return ((CraftLivingEntity) entity).getHandle();
    }

    @Override
    public IronGolem getNMSIronGolem(org.bukkit.entity.IronGolem ironGolem) {
        return ((CraftIronGolem) ironGolem).getHandle();
    }

    @Override
    public Vec3 getNMSVector(Vector vector) {
        return CraftVector.toNMS(vector);
    }

    @Override
    public BlockPos getNMSBlockPos(BlockVector blockVector) {
        return CraftBlockVector.toBlockPosition(blockVector);
    }

    @Override
    public EntityType<?> getNMSEntityType(org.bukkit.entity.EntityType entityType) {
        return CraftEntityType.bukkitToMinecraft(entityType);
    }

    @Override
    public ServerPlayer getNMSServerPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    @Override
    public ServerCommonPacketListenerImpl getPlayerPacketListener(ServerPlayer serverPlayer) {
        return serverPlayer.connection;
    }

    @Override
    @Nullable
    public Channel getConnectionChannel(@NotNull Connection connection) {
        return connection.channel;
    }

    @Override
    public MinecraftServer getNMSServer() {
        return ((CraftServer) Bukkit.getServer()).getServer();
    }

    @Override
    public int getEntityId(org.bukkit.entity.Entity entity) {
        return getNMSEntity(entity).getId();
    }

    @Override
    public void setEntityId(org.bukkit.entity.Entity entity, int id) {
        getNMSEntity(entity).setId(id);
    }

    @Override
    public ServerPlayer ServerPlayer(MinecraftServer minecraftServer, ServerLevel level, GameProfile gameProfile) {
        return new ServerPlayer(minecraftServer, level, gameProfile, ClientInformation.createDefault());
    }

    @Override
    public @NotNull NMSPlayer<ServerPlayer> NMSPlayer(@NotNull String name, @NotNull Location location, boolean update) {
        World world = NMSUtils.getWorld(location.getWorld());
        ServerLevel serverLevel = NMS.getNMSWorld(world);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        GameProfile gameProfile = NMS.GameProfile(offlinePlayer.getUniqueId(), offlinePlayer.getName());
        ServerPlayer serverPlayer = NMS.ServerPlayer(NMS.getNMSServer(), serverLevel, gameProfile);
        serverPlayer.setPos(location.getX(), location.getY(), location.getZ());
        NMSPlayer<ServerPlayer> nmsPlayer = new NMSPlayer_1_21_4(serverPlayer, serverPlayer.getId());
        //if (update) ;
        return nmsPlayer;
    }

    @Override
    public GameProfile GameProfile(UUID uuid, String name) {
        return new GameProfile(uuid, name);
    }

    @Override
    public void offerFlower(IronGolem ironGolem, boolean shouldOffer) {
        ironGolem.offerFlower(shouldOffer);
    }

    @Override
    public void travel(LivingEntity livingEntity, Vec3 vec3) {
        livingEntity.travel(vec3);
    }

    @Override
    public void sendPacket(Packet<?> packet, ServerPlayer serverPlayer) {
        serverPlayer.connection.send(packet);
    }

    @Override
    public void sendEntityAnimationPacket(org.bukkit.entity.Entity entity, EntityAnimation animation, Player... players) {
        sendPacket(new ClientboundAnimatePacket(getNMSEntity(entity), animation.getId()), players);
    }

    @Override
    public void sendBlockDestructionPacket(@Nullable Integer id, org.bukkit.block.Block block, int progress, Player... players) {
        sendPacket(new ClientboundBlockDestructionPacket(getIdOrRandom(id), ((CraftBlock) block).getPosition(), progress), players);
    }

    @Override
    public void sendEntitySpawnPacket(@Nullable Integer id, UUID uuid, EntityType<?> entityType, Vec3 location, float pitch, float yaw, double headPitch, int data, Vec3 velocity, Player... players) {
        sendPacket(new ClientboundAddEntityPacket(getIdOrRandom(id), uuid, location.x, location.y, location.z, pitch, yaw, entityType, data, velocity, headPitch));
    }

    @Override
    public void sendEntitySpawnPacket(Entity entity, int data, Vec3 vec3, Player... players) {
        sendEntitySpawnPacket(entity.getId(), entity.getUUID(), entity.getType(), vec3, entity.getYRot(),
                entity.getXRot(), entity.getYHeadRot(), data, entity.getDeltaMovement(), players);
    }

    @Override // todo: only works for main hand, figure out why
    public void sendEntityMetadataPacket(int id, Player... players) {
        for (Player player : players) {
            //setInteracting(player, true);
            ServerPlayer serverPlayer = getNMSServerPlayer(player);
            SynchedEntityData dataWatcher = serverPlayer.getEntityData();
            dataWatcher.set(new EntityDataAccessor<>(6, EntityDataSerializers.POSE), Pose.SHOOTING);
            dataWatcher.set(new EntityDataAccessor<>(8, EntityDataSerializers.BYTE), (byte) 1);
            ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket(id, dataWatcher.getNonDefaultValues());
            sendPacket(packet, player);
        }
    }
    @ApiStatus.Experimental
    public void setInteracting(org.bukkit.entity.Entity entity, boolean value) {
        // 8 is the interact accessor (corresponds to the interaction state)
        EntityDataAccessor<Byte> accessor = getAccessor(entity, 8);
        getNMSEntity(entity).getEntityData().set(accessor, (byte) (value ? 1 : 0));
    }

    @ApiStatus.Experimental
    public <T> EntityDataAccessor<T> getAccessor(org.bukkit.entity.Entity entity, int id) {
        SynchedEntityData synchedData = getNMSEntity(entity).getEntityData();
        try {
            // Use reflection to get the private "dataItems" field from SynchedEntityData
            Field dataItemsField = SynchedEntityData.class.getDeclaredField("e");
            dataItemsField.setAccessible(true);

            // Get the list of data items from the synched data
            SynchedEntityData.DataItem<?>[] dataItems = (SynchedEntityData.DataItem<?>[]) dataItemsField.get(synchedData);

            // Iterate over data items to find the one with the matching ID
            for (SynchedEntityData.DataItem<?> dataItem : dataItems) {
                if (dataItem.getAccessor().id() == id) {
                    return (EntityDataAccessor<T>) dataItem.getAccessor();
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    @ApiStatus.Experimental
    public void sendPlayerActionPacket(Player... players) {
        ServerboundPlayerActionPacket packet = new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM, BlockPos.ZERO, Direction.DOWN);
        for (Player player : players) {
            sendPacket(packet, player);
        }
    }

    @Override
    public void handleIncomingPacket(ChannelHandlerContext ctx, Object msg) {

    }

    @Override
    public void handleOutgoingPacket(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {

    }

}
