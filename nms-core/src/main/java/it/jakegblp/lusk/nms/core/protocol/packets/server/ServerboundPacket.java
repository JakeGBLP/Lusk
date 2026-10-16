package it.jakegblp.lusk.nms.core.protocol.packets.server;

import it.jakegblp.lusk.nms.core.event.PacketWrapperEvent;
import it.jakegblp.lusk.nms.core.protocol.packets.Packet;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import it.jakegblp.lusk.nms.core.serialization.BufferSerializable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

public interface ServerboundPacket<E extends PacketWrapperEvent<?>> extends Packet, BufferSerializable<ClientboundPacket<E>> {

    @Contract("_, _ -> new")
    E createEvent(Player player, boolean async);

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
    ServerboundPacket<E> copy();
}
