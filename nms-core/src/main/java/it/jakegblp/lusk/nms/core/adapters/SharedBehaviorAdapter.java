package it.jakegblp.lusk.nms.core.adapters;

import com.google.common.collect.BiMap;
import io.netty.channel.*;
import it.jakegblp.lusk.nms.core.events.PacketReceiveEvent;
import it.jakegblp.lusk.nms.core.events.PacketSendEvent;
import it.jakegblp.lusk.nms.core.protocol.packets.Packet;
import it.jakegblp.lusk.nms.core.protocol.packets.client.*;
import it.jakegblp.lusk.nms.core.protocol.packets.server.ServerboundPacket;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import static it.jakegblp.lusk.common.ReflectionUtils.*;
import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

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
        NMSEntityType,
        NMSServerGamePacketListenerImpl,
        NMSConnection,
        NMSClientBundlePacket,
        NMSBlockDestructionPacket,
        NMSEntityAnimationPacket,
        NMSAddEntityPacket,
        NMSRemoveEntitiesPacket,
        NMSEntityMetadataPacket
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

    @SuppressWarnings("unchecked")
    default NMSItemStack asNMSItemStack(ItemStack itemStack) {
        return (NMSItemStack) invokeSafely(getMethod(getCraftItemStackClass(), "asNMSCopy", true, true, ItemStack.class), null, itemStack);
    }

    default ItemStack asItemStack(NMSItemStack itemStack) {
        return (ItemStack) invokeSafely(getMethod(getCraftItemStackClass(), "asBukkitCopy", true, true, getNMSItemStackClass()), null, itemStack);
    }

    @SuppressWarnings("unchecked")
    default Class<? extends ItemStack> getCraftItemStackClass() {
        return (Class<? extends ItemStack>) forClassName(CRAFT_BUKKIT_PACKAGE + ".inventory.CraftItemStack");
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

    @SuppressWarnings("unchecked")
    default NMSServerPlayer asServerPlayer(Player player) {
        return (NMSServerPlayer) invokeSafely(getMethod(getCraftPlayerClass(), "getHandle", false, true), player);
    }

    Class<NMSServerPlayer> getNMSServerPlayerClass();

    @SuppressWarnings("unchecked")
    default Class<? extends Player> getCraftPlayerClass() {
        return (Class<? extends Player>) forClassName(CRAFT_BUKKIT_PACKAGE + ".entity.CraftPlayer");
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
        injectPlayer(asServerPlayer(player), plugin);
    }

    default void injectPlayer(NMSServerPlayer serverPlayer, JavaPlugin plugin) {
        ChannelPipeline pipeline = getChannel(getConnection(getPlayerConnection(serverPlayer))).pipeline();
        if (pipeline.get("packet_interceptor") != null) return;
        pipeline.addBefore("packet_handler", "packet_interceptor", new ChannelDuplexHandler() {

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                if (!ctx.channel().isActive()) return;
                if (!getNMSPacketClass().isInstance(msg)) {
                    super.channelRead(ctx, msg);
                    return;
                }
                if (Bukkit.isStopping()) return;
                Bukkit.getScheduler().runTask(plugin, () -> {
                    if (!ctx.channel().isActive()) return;
                    //System.out.println(msg.getClass());
                    ServerboundPacket packet = (ServerboundPacket) NMS.fromNMSPacket(msg);
                    PacketReceiveEvent<ServerboundPacket> event = new PacketReceiveEvent<>(packet);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) return;
                    Packet newPacket = event.getPacket();
                    ctx.executor().execute(() -> {
                        if (!ctx.channel().isActive()) return;
                        try {
                            super.channelRead(ctx, newPacket != packet ? newPacket.asNMS()  : msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                });
            }

            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                if (!ctx.channel().isActive()) {
                    promise.setSuccess();
                    return;
                }
                if (!getNMSPacketClass().isInstance(msg)) {
                    super.write(ctx, msg, promise);
                    return;
                }
                if (Bukkit.isStopping()) return;
                Bukkit.getScheduler().runTask(plugin, () -> {
                    if (!ctx.channel().isActive()) {
                        promise.setSuccess();
                        return;
                    }
                    ClientboundPacket packet = (ClientboundPacket) NMS.fromNMSPacket(msg);
                    PacketSendEvent<ClientboundPacket> event = new PacketSendEvent<>(packet);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        promise.setSuccess();
                        return;
                    }
                    ClientboundPacket newPacket = event.getPacket();
                    ctx.executor().execute(() -> {
                        if (!ctx.channel().isActive()) {
                            promise.setSuccess();
                            return;
                        }
                        try {
                            super.write(ctx, newPacket != packet ? newPacket.asNMS() : msg, promise);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                });
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                // todo: figure out a better solution
                if (cause instanceof java.nio.channels.ClosedChannelException) return;
                cause.printStackTrace();
                super.exceptionCaught(ctx, cause);
            }
        });

    }

}