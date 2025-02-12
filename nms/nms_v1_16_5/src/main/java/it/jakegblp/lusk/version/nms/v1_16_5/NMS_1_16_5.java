package it.jakegblp.lusk.version.nms.v1_16_5;

import com.mojang.authlib.GameProfile;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import it.jakegblp.lusk.api.NMSAdapter;
import it.jakegblp.lusk.api.enums.EntityAnimation;
import it.jakegblp.lusk.api.packets.NMSPlayer;
import it.jakegblp.lusk.utils.NMSUtils;
import it.jakegblp.lusk.version.nms.v1_16_5.packets.NMSPlayer_1_16_5;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftIronGolem;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftVector;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static it.jakegblp.lusk.utils.NMSUtils.getIdOrRandom;

public class NMS_1_16_5 implements NMSAdapter<
        MinecraftKey,
        MinecraftServer,
        Packet<?>,
        EntityTypes<?>,
        WorldServer,
        EntityPlayer,
        EntityIronGolem,
        EntityLiving,
        Entity,
        GameProfile,
        Vec3D,
        BaseBlockPosition,
        BlockPosition,
        Block,
        NetworkManager,
        PlayerConnection
        > {

    public static NMS_1_16_5 NMS = null;

    public NMS_1_16_5() {
        NMS = this;
    }


    @Override
    public @NotNull ObfuscatedFields getObfuscatedFields() {
        return new ObfuscatedFields("a");
    }

    @Override
    public MinecraftKey getNMSNamespacedKey(NamespacedKey key) {
        return CraftNamespacedKey.toMinecraft(key);
    }

    @Override
    public WorldServer getNMSWorld(org.bukkit.World world) {
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
    public EntityLiving getNMSLivingEntity(org.bukkit.entity.LivingEntity entity) {
        return ((CraftLivingEntity) entity).getHandle();
    }

    @Override
    public EntityIronGolem getNMSIronGolem(org.bukkit.entity.IronGolem ironGolem) {
        return ((CraftIronGolem) ironGolem).getHandle();
    }

    @Override
    public Vec3D getNMSVector(Vector vector) {
        return CraftVector.toNMS(vector);
    }

    @Override
    public BlockPosition getNMSBlockPos(BlockVector blockVector) {
        return new BlockPosition(blockVector.getBlockX(), blockVector.getBlockY(), blockVector.getBlockZ());
    }

    @Override
    public EntityTypes<?> getNMSEntityType(org.bukkit.entity.EntityType entityType) {
        if (entityType == null) return null;
        return IRegistry.ENTITY_TYPE.get(getNMSNamespacedKey(entityType.getKey()));
    }

    @Override
    public EntityPlayer getNMSServerPlayer(org.bukkit.entity.Player player) {
        return ((CraftPlayer) player).getHandle();
    }


    @Override
    public PlayerConnection getPlayerPacketListener(EntityPlayer serverPlayer) {
        return serverPlayer.playerConnection;
    }

    @Override
    public @Nullable Channel getConnectionChannel(@NotNull NetworkManager connection) {
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
        getNMSEntity(entity).e(id);
    }

    @Override
    public EntityPlayer ServerPlayer(MinecraftServer minecraftServer, WorldServer level, GameProfile gameProfile) {
        return new EntityPlayer(minecraftServer, level, gameProfile, new PlayerInteractManager(level));
    }

    @Override
    public @NotNull NMSPlayer<EntityPlayer> NMSPlayer(@NotNull String name, @NotNull Location location, boolean update) {
        org.bukkit.World world = NMSUtils.getWorld(location.getWorld());
        WorldServer serverLevel = NMS.getNMSWorld(world);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        GameProfile gameProfile = NMS.GameProfile(offlinePlayer.getUniqueId(), offlinePlayer.getName());
        EntityPlayer serverPlayer = NMS.ServerPlayer(NMS.getNMSServer(), serverLevel, gameProfile);
        serverPlayer.setPosition(location.getX(), location.getY(), location.getZ());
        NMSPlayer<EntityPlayer> nmsPlayer = new NMSPlayer_1_16_5(serverPlayer, serverPlayer.getId());
        //if (update) ;
        return nmsPlayer;
    }

    @Override
    public GameProfile GameProfile(UUID uuid, String name) {
        return new GameProfile(uuid, name);
    }

    @Override
    public void offerFlower(EntityIronGolem ironGolem, boolean shouldOffer) {
        ironGolem.t(shouldOffer);
    }

    @Override
    public void travel(EntityLiving livingEntity, Vec3D vec3) {
        livingEntity.g(vec3);
    }

    @Override
    public void sendPacket(Packet<?> packet, EntityPlayer serverPlayer) {
        serverPlayer.playerConnection.sendPacket(packet);
    }

    @Override
    public void sendEntityAnimationPacket(org.bukkit.entity.Entity entity, EntityAnimation animation, Player... players) {
        sendPacket(new PacketPlayOutAnimation(getNMSEntity(entity), animation.getId()), players);
    }

    @Override
    public void sendBlockDestructionPacket(@Nullable Integer id, org.bukkit.block.Block block, int progress, Player... players) {
        sendPacket(new PacketPlayOutBlockBreakAnimation(getIdOrRandom(id), ((CraftBlock) block).getPosition(), progress), players);
    }

    @Override
    public void sendEntitySpawnPacket(@Nullable Integer id, UUID uuid, EntityTypes<?> entityType, Vec3D location, float pitch, float yaw, double headPitch, int data, Vec3D velocity, Player... players) {
        sendPacket(new PacketPlayOutSpawnEntity(getIdOrRandom(id), uuid, location.x, location.y, location.z, pitch, yaw, entityType, data, velocity), players);
    }

    @Override
    public void sendEntitySpawnPacket(Entity entity, int data, Vec3D vec3, Player... players) {
        sendEntitySpawnPacket(entity.getId(), entity.getUniqueID(), entity.getEntityType(), vec3, entity.yaw,
                entity.pitch, entity.getHeadRotation(), data, entity.getMot(), players);
    }

    @Override
    public void sendEntityMetadataPacket(int id, Player... players) {
        for (Player player : players) {
            EntityPlayer serverPlayer = getNMSServerPlayer(player);
            DataWatcher dataWatcher = serverPlayer.getDataWatcher();
            dataWatcher.set(new DataWatcherObject<>(8, DataWatcherRegistry.a), (byte) 1);
            PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(id, dataWatcher, false);
            sendPacket(packet, players);
        }
    }

    public void sendPlayerActionPacket(Player... players) {
        //new PacketDataSerializer()
        //PacketPlayInBlockDig packet = new PacketPlayInBlockDig(PacketPlayInBlockDig.EnumPlayerDigType.RELEASE_USE_ITEM, BlockPosition.ZERO, EnumDirection.DOWN);
        //for (Player player : players) {
        //    sendPacket(packet, player);
        //}
    }

    @Override
    public void handleIncomingPacket(ChannelHandlerContext ctx, Object msg) {

    }

    @Override
    public void handleOutgoingPacket(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {

    }

}