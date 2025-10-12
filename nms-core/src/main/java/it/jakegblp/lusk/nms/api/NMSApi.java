package it.jakegblp.lusk.nms.api;

import it.jakegblp.lusk.nms.core.protocol.packets.BundlePacket;
import it.jakegblp.lusk.nms.core.protocol.packets.Packet;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientBundlePacket;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

public class NMSApi {

    public static final Random RANDOM = new Random();
    @Getter
    private static int randomEntityId, randomTeleportId;

    public static int generateRandomEntityId() {
        randomEntityId = RANDOM.nextInt();
        return randomEntityId;
    }

    public static int generateRandomTeleportId() {
        randomTeleportId = RANDOM.nextInt();
        return randomTeleportId;
    }

    public static void sendPacket(Player player, ClientboundPacket packet) {
        NMS.sendPacket(player, packet.asNMS());
    }

    public static void sendPacket(Player[] players, ClientboundPacket packets) {
        for (Player player : players) {
            sendPacket(player, packets);
        }
    }

    public static void sendPackets(Player player, ClientboundPacket... packets) {
        for (ClientboundPacket packet : packets) {
            sendPacket(player, packet);
        }
    }

    public static void sendPackets(Player[] players, ClientboundPacket... packet) {
        for (Player player : players) {
            sendPackets(player, packet);
        }
    }

    public static void sendBundledPackets(Player[] players, ClientboundPacket... packets) {
        var bundle = new ClientBundlePacket(packets);
        for (Player player : players) {
            sendPacket(player, bundle);
        }
    }

    public static Object asNMSEquipmentSlot(EquipmentSlot equipmentSlot) {
        return NMS.asNMSEquipmentSlot(equipmentSlot);
    }

    public static EquipmentSlot asEquipmentSlot(Enum<?> nmsEquipmentSlot) {
        return NMS.asEquipmentSlot(nmsEquipmentSlot);
    }

    public static Object asNMSItemStack(ItemStack itemStack) {
        return NMS.asNMSItemStack(itemStack);
    }

    public static ItemStack asItemStack(Object nmsItemStack) {
        return NMS.asItemStack(nmsItemStack);
    }

}
