package it.jakegblp.lusk.nms.core.adapters;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.collect.BiMap;
import io.netty.channel.*;
import it.jakegblp.lusk.common.reflection.SimpleClass;
import it.jakegblp.lusk.nms.core.events.*;
import it.jakegblp.lusk.nms.core.protocol.packets.client.*;
import it.jakegblp.lusk.nms.core.protocol.packets.server.ServerboundPacket;
import it.jakegblp.lusk.nms.core.world.player.ChatSessionData;
import lombok.SneakyThrows;
import it.jakegblp.lusk.nms.core.world.player.TeamParameters;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.nio.channels.ClosedChannelException;

public interface SharedBehaviorAdapter<
        NMSEquipmentSlot extends Enum<NMSEquipmentSlot>,
        NMSItemStack,
        NMSVector,
        NMSBlockPos,
        NMSServerPlayer,
        NMSPose,
        NMSComponent,
        NMSPacket,
        NMSNamespacedKey,
        NMSGameMode,
        NMSPlayerProfile,
        NMSChatSessionData,
        NMSEntityType,
        NMSServerGamePacketListenerImpl,
        NMSConnection,
        NMSTeamVisibility,
        NMSTeamCollisionRule,
        NMSDedicatedServer,
        NMSClientBundlePacket,
        NMSBlockDestructionPacket,
        NMSEntityAnimationPacket,
        NMSAddEntityPacket,
        NMSRemoveEntitiesPacket,
        NMSEntityMetadataPacket,
        NMSPlayerInfoUpdatePacket,
        NMSPlayerInfoUpdatePacketAction,
        NMSSystemChatPacket,
        NMSLevelParticlePacket,
        NMSAttributePacket,
        NMSSetCameraPacket,
        NMSSetPlayerTeamPacket,
        NMSTeamParameters,
        NMSEntityEventPacket
        > {

    String CRAFT_BUKKIT_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();

    Class<NMSPacket> getNMSPacketClass();

    default boolean isNMSPacket(Object object) {
        return getNMSPacketClass().isInstance(object);
    }

    default NMSEquipmentSlot asNMSEquipmentSlot(EquipmentSlot equipmentSlot) {
        return Enum.valueOf(getNMSEquipmentSlotClass(), equipmentSlot == EquipmentSlot.HAND ? "MAINHAND" : equipmentSlot.name().replace("_", ""));
    }

    default EquipmentSlot asEquipmentSlot(NMSEquipmentSlot equipmentSlot) {
        String name = equipmentSlot.name();
        if (name.contains("MAIN")) return EquipmentSlot.HAND;
        return EquipmentSlot.valueOf(equipmentSlot.name().replace("HAND", "_HAND"));
    }

    default NMSItemStack asNMSItemStack(ItemStack itemStack) {
        return new SimpleClass<>(getCraftItemStackClass()).getMethod("asNMSCopy", true, true, ItemStack.class).invoke(null, itemStack);
    }

    default ItemStack asItemStack(NMSItemStack itemStack) {
        return new SimpleClass<>(getCraftItemStackClass()).getMethod("asBukkitCopy", true, true, getNMSItemStackClass()).invoke(null, itemStack);
    }

    default Class<? extends ItemStack> getCraftItemStackClass() {
        return SimpleClass.quickClass(CRAFT_BUKKIT_PACKAGE + ".inventory.CraftItemStack");
    }

    Class<NMSItemStack> getNMSItemStackClass();

    Class<NMSEquipmentSlot> getNMSEquipmentSlotClass();

    BiMap<Pose, NMSPose> getPoseMap();

    Vector asVector(NMSVector nmsVector);

    NMSVector asNMSVector(Vector vector);

    Class<NMSVector> getNMSVectorClass();

    BlockVector asBlockVector(NMSBlockPos blockPosition);

    NMSBlockPos asNMSBlockVector(BlockVector blockVector);

    Class<NMSBlockPos> getNMSBlockVectorClass();

    Pose asPose(NMSPose nmsPose);

    NMSPose asNMSPose(Pose pose);

    Class<NMSPose> getNMSPoseClass();

    Player asPlayer(NMSServerPlayer serverPlayer);

    default NMSServerPlayer asServerPlayer(Player player) {
        return new SimpleClass<>(getCraftPlayerClass()).getMethod("getHandle", false, true).invoke(player);
    }

    Class<NMSServerPlayer> getNMSServerPlayerClass();

    default Class<? extends Player> getCraftPlayerClass() {
        return SimpleClass.quickClass(CRAFT_BUKKIT_PACKAGE + ".entity.CraftPlayer");
    }

    NMSServerGamePacketListenerImpl getPlayerConnection(NMSServerPlayer serverPlayer);

    default NMSServerGamePacketListenerImpl getPlayerConnection(Player player) {
        return getPlayerConnection(asServerPlayer(player));
    }

    NMSClientBundlePacket toNMSClientBundlePacket(ClientBundlePacket from);

    ClientBundlePacket fromNMSClientBundlePacket(NMSClientBundlePacket from);

    Class<NMSClientBundlePacket> getNMSClientBundlePacketClass();

    default boolean isNMSClientBundlePacket(Object object) {
        return getNMSClientBundlePacketClass().isInstance(object);
    }

    NMSAddEntityPacket toNMSAddEntityPacket(AddEntityPacket from);

    AddEntityPacket fromNMSAddEntityPacket(NMSAddEntityPacket from);

    Class<NMSAddEntityPacket> getNMSAddEntityPacketClass();

    default boolean isNMSAddEntityPacket(Object object) {
        return getNMSAddEntityPacketClass().isInstance(object);
    }

    NMSBlockDestructionPacket toNMSBlockDestructionPacket(BlockDestructionPacket from);

    BlockDestructionPacket fromNMSBlockDestructionPacket(NMSBlockDestructionPacket from);

    Class<NMSBlockDestructionPacket> getNMSBlockDestructionPacketClass();

    default boolean isNMSBlockDestructionPacket(Object object) {
        return getNMSBlockDestructionPacketClass().isInstance(object);
    }

    NMSEntityAnimationPacket toNMSEntityAnimationPacket(EntityAnimationPacket from);

    EntityAnimationPacket fromNMSEntityAnimationPacket(NMSEntityAnimationPacket from);

    Class<NMSEntityAnimationPacket> getNMSEntityAnimationPacketClass();

    default boolean isNMSEntityAnimationPacket(Object object) {
        return getNMSEntityAnimationPacketClass().isInstance(object);
    }

    NMSRemoveEntitiesPacket toNMSRemoveEntitiesPacket(RemoveEntitiesPacket from);

    RemoveEntitiesPacket fromNMSRemoveEntitiesPacket(NMSRemoveEntitiesPacket from);

    Class<NMSRemoveEntitiesPacket> getNMSRemoveEntitiesPacketClass();

    default boolean isNMSRemoveEntitiesPacket(Object object) {
        return getNMSRemoveEntitiesPacketClass().isInstance(object);
    }

    NMSEntityMetadataPacket toNMSEntityMetadataPacket(EntityMetadataPacket from);

    EntityMetadataPacket fromNMSEntityMetadataPacket(NMSEntityMetadataPacket from);

    Class<NMSEntityMetadataPacket> getNMSEntityMetadataPacketClass();

    default boolean isNMSEntityMetadataPacket(Object object) {
        return getNMSEntityMetadataPacketClass().isInstance(object);
    }

    NMSPlayerInfoUpdatePacket toNMSPlayerInfoUpdatePacket(PlayerInfoUpdatePacket from);

    PlayerInfoUpdatePacket fromNMSPlayerInfoUpdatePacket(NMSPlayerInfoUpdatePacket from);

    Class<NMSPlayerInfoUpdatePacket> getNMSPlayerInfoUpdatePacketClass();

    default boolean isNMSPlayerInfoUpdatePacket(Object object) {
        return getNMSPlayerInfoUpdatePacketClass().isInstance(object);
    }

    NMSPlayerInfoUpdatePacketAction toNMSPlayerInfoUpdatePacketAction(PlayerInfoUpdatePacket.Action<?> from);

    PlayerInfoUpdatePacket.Action<?> fromNMSPlayerInfoUpdatePacketAction(NMSPlayerInfoUpdatePacketAction from);

    Class<NMSPlayerInfoUpdatePacketAction> getNMSPlayerInfoUpdatePacketActionClass();

    default boolean isNMSPlayerInfoUpdatePacketAction(Object object) {
        return getNMSPlayerInfoUpdatePacketActionClass().isInstance(object);
    }


    //Please leave spaces so Poa can read this

    NMSSystemChatPacket toNMSSystemChatPacket(SystemChatPacket from);

    SystemChatPacket fromNMSSystemChatPacket(NMSSystemChatPacket from);

    Class<NMSSystemChatPacket> getNMSSystemChatPacketClass();

    default boolean isNMSSystemChatPacket(Object object) {
        return getNMSSystemChatPacketClass().isInstance(object);
    }

    //


    NMSAttributePacket toNMSAttributePacket(AttributePacket from);

    AttributePacket fromNMSAttributePacket(NMSAttributePacket from);

    Class<NMSAttributePacket> getNMSAttributePacketClass();

    default boolean isNMSAttributePacket(Object object) {
        return getNMSAttributePacketClass().isInstance(object);
    }


    NMSLevelParticlePacket toNMSLevelParticle(LevelParticlePacket from);

    LevelParticlePacket fromNMSLevelParticle(NMSLevelParticlePacket from);

    Class<NMSLevelParticlePacket> getNMSLevelParticleClass();

    default boolean isNMSLevelParticle(Object object){
        return getNMSLevelParticleClass().isInstance(object);
    }


    NMSSetPlayerTeamPacket toNMSSetPlayerTeamPacket(TeamPacket from);

    TeamPacket fromNMSSetPlayerTeamPacket(NMSSetPlayerTeamPacket from);

    Class<NMSSetPlayerTeamPacket> getNMSSetPlayerTeamPacketClass();

    default boolean isNMSSetPlayerTeamPacket(Object object) {
        return getNMSSetPlayerTeamPacketClass().isInstance(object);
    }

    NMSTeamParameters toNMSTeamParameters(TeamParameters from);

    TeamParameters fromNMSTeamParameters(NMSTeamParameters from);

    Class<NMSTeamParameters> getNMSTeamParametersClass();

    default boolean isNMSTeamParameters(Object object) {
        return getNMSTeamParametersClass().isInstance(object);
    }


    NMSEntityEventPacket toNMSEntityEventPacket(EntityEventPacket from);

    EntityEventPacket fromNMSEntityEventPacket(NMSEntityEventPacket from);

    Class<NMSEntityEventPacket> getNMSEntityEventPacketClass();

    default boolean isNMSEntityEventPacket(Object object) {
        return getNMSEntityEventPacketClass().isInstance(object);
    }


    NMSSetCameraPacket toNMSSetCameraPacket(SetCameraPacket from);

    SetCameraPacket fromNMSSetCameraPacket(NMSSetCameraPacket from);

    Class<NMSSetCameraPacket> getNMSSetCameraPacketClass();

    default boolean isNMSSetCameraPacket(Object object) {
        return getNMSSetCameraPacketClass().isInstance(object);
    }

    NMSEntityType toNMSEntityType(EntityType from);

    EntityType fromNMSEntityType(NMSEntityType to);

    Class<NMSEntityType> getNMSEntityTypeClass();

    default boolean isNMSEntityType(Object object) {
        return getNMSEntityTypeClass().isInstance(object);
    }

    NMSNamespacedKey asNMSNamespacedKey(NamespacedKey key);

    NamespacedKey asNamespacedKey(NMSNamespacedKey nmsNamespacedKey);

    Class<NMSNamespacedKey> getNMSNamespacedKeyClass();

    default boolean isNMSNamespacedKey(Object object) {
        return getNMSNamespacedKeyClass().isInstance(object);
    }

    NMSGameMode toNMSGameMode(GameMode from);

    GameMode fromNMSGameMode(NMSGameMode from);

    Class<NMSGameMode> getNMSGameModeClass();

    default boolean isNMSGameMode(Object object) {
        return getNMSGameModeClass().isInstance(object);
    }

    NMSPlayerProfile toNMSPlayerProfile(PlayerProfile from);

    PlayerProfile fromNMSPlayerProfile(NMSPlayerProfile from);

    Class<NMSPlayerProfile> getNMSPlayerProfileClass();

    default boolean isNMSPlayerProfile(Object object) {
        return getNMSPlayerProfileClass().isInstance(object);
    }

    NMSChatSessionData toNMSChatSessionData(ChatSessionData from);

    ChatSessionData fromNMSChatSessionData(NMSChatSessionData from);

    Class<NMSChatSessionData> getNMSChatSessionDataClass();

    default boolean isNMSChatSessionData(Object object) {
        return getNMSChatSessionDataClass().isInstance(object);
    }

    NMSComponent asNMSComponent(Component component);

    Component asComponent(NMSComponent nmsComponent);

    Class<NMSComponent> getNMSComponentClass();

    default boolean isNMSComponent(Object object) {
        return getNMSComponentClass().isInstance(object);
    }

    default void sendPacket(Player player, NMSPacket packet) {
        sendPacket(asServerPlayer(player), packet);
    }

    default void sendPacket(NMSServerPlayer player, NMSPacket packet) {
        sendPacketInternal(getPlayerConnection(player), packet);
    }

    void sendPacketInternal(NMSServerGamePacketListenerImpl packetListener, NMSPacket packet);

    NMSConnection getConnection(NMSServerGamePacketListenerImpl packetListener);

    Channel getChannel(NMSConnection connection);

    NMSTeamVisibility toNMSTeamVisibility(Team.OptionStatus optionStatus);

    NMSTeamCollisionRule toNMSTeamCollisionRule(Team.OptionStatus optionStatus);

    Team.OptionStatus fromNMSTeamVisibility(NMSTeamVisibility teamVisibility);

    Team.OptionStatus fromNMSTeamCollisionRule(NMSTeamCollisionRule teamCollisionRule);

    NMSDedicatedServer getDedicatedServer();

    default void uninjectPlayer(Player player) {
        uninjectPlayer(asServerPlayer(player));
    }

    default void uninjectPlayer(NMSServerPlayer serverPlayer) {
        ChannelPipeline pipeline = getChannel(getConnection(getPlayerConnection(serverPlayer))).pipeline();
        if (pipeline.get("packet_interceptor") != null) {
            pipeline.remove("packet_interceptor");
        }
    }

    default void injectPlayer(Player player, JavaPlugin plugin) {
        injectPlayer(player, asServerPlayer(player), plugin);
    }

    @SneakyThrows
    default void injectPlayer(Player player, NMSServerPlayer serverPlayer, JavaPlugin plugin) {
        ChannelPipeline pipeline = getChannel(getConnection(getPlayerConnection(serverPlayer))).pipeline();
        if (pipeline.get("packet_interceptor") != null) return;

        pipeline.addBefore("packet_handler", "packet_interceptor", new ChannelDuplexHandler() {

            private final PluginManager pluginManager = Bukkit.getPluginManager();

            private boolean inactive(ChannelHandlerContext ctx) {
                return !ctx.channel().isActive() || Bukkit.isStopping();
            }


            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                if (inactive(ctx)) return;
                if (!getNMSPacketClass().isInstance(msg)) {
                    super.channelRead(ctx, msg);
                    return;
                }

                if (inactive(ctx)) return;

                if (PrePacketReceiveEvent.getHandlerList().getRegisteredListeners().length > 0) {
                    PrePacketReceiveEvent pre = new PrePacketReceiveEvent(player, true);
                    pluginManager.callEvent(pre);
                    if (pre.isCancelled()) return;
                }
                Object newMsg = msg;
//                if (PacketReceiveEvent.getHandlerList().getRegisteredListeners().length > 0) {
//                    PacketReceiveEvent<ServerboundPacket> event = new PacketReceiveEvent<>(msg, player);
//                    pluginManager.callEvent(event);
//                    if (event.isCancelled()) return;
//                    else if (event.isResolved())
//                        newMsg = event.getPacket().asNMS();
//                }

                //todo fix this above (make the event async)

                try {
                    super.channelRead(ctx, newMsg);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @SuppressWarnings({"unchecked", "ExtractMethodRecommender"})
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                if (inactive(ctx)) {
                    promise.setSuccess();
                    return;
                }
                if (!getNMSPacketClass().isInstance(msg)) {
                    super.write(ctx, msg, promise);
                    return;
                }
                if (inactive(ctx)) {
                    promise.setSuccess();
                    return;
                }

                if (PrePacketSendEvent.getHandlerList().getRegisteredListeners().length > 0 && !isNMSClientBundlePacket(msg)) {
                    PrePacketSendEvent pre = new PrePacketSendEvent(player, true);
                    pluginManager.callEvent(pre);
                    if (pre.isCancelled()) {
                        promise.setSuccess();
                        return;
                    }
                }
                Object newMsg = msg;
//                if (PacketSendEvent.getHandlerList().getRegisteredListeners().length > 0) {
//                    PacketSendEvent<ClientboundPacket> event = new PacketSendEvent<>(msg, player);
//                    pluginManager.callEvent(event);
//                    if (event.isCancelled()) {
//                        promise.setSuccess();
//                        return;
//                    }
//                    if (event.isResolved()) {
//                        newMsg = event.getPacket().asNMS();
//                    }
//                }

                //todo fix this above (make the event async)



                if(isNMSLevelParticle(msg)){
                    final LevelParticlePacket packet = fromNMSLevelParticle((NMSLevelParticlePacket) msg);

                    final ParticleSendEvent particleSendEvent = new ParticleSendEvent(player, true);
                    particleSendEvent.setX(packet.getX());
                    particleSendEvent.setY(packet.getY());
                    particleSendEvent.setZ(packet.getZ());
                    particleSendEvent.setCount(packet.getCount());
                    particleSendEvent.setMaxSpeed(packet.getMaxSpeed());
                    particleSendEvent.setWorld(player.getWorld());
                    particleSendEvent.setXOffset(packet.getXDist());
                    particleSendEvent.setYOffset(packet.getYDist());
                    particleSendEvent.setZOffset(packet.getZDist());

                    particleSendEvent.setParticle(packet.getParticle().getType());

                    pluginManager.callEvent(particleSendEvent);

                    if(particleSendEvent.isCancelled())
                        return;
                }

                try {
                    super.write(ctx, newMsg, promise);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                if (cause instanceof ClosedChannelException) return;
                cause.printStackTrace();
                super.exceptionCaught(ctx, cause);
            }
        });
    }


}