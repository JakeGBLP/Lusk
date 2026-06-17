package it.jakegblp.lusk.nms.core.protocol.packets.server;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.protocol.packets.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

public interface ServerboundPacket extends Packet, Copyable<Packet> {

    default void receive(Player player) {
        if (isAsync())
            receiveAsync(player);
        else
            receiveSync(player);
    }

    // todo: implement
    default void receiveSync(Player player) {

    }

    default void receiveAsync(Player player) {
        this.processAsync().thenRunAsync(() -> receiveSync(player),
                Bukkit.getScheduler().getMainThreadExecutor(NMS.getPlugin()));
    }

    @Override
    ServerboundPacket copy();
}
