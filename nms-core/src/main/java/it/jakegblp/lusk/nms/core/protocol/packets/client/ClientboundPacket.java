package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.api.NMSApi;
import it.jakegblp.lusk.nms.core.async.ExecutionMode;
import it.jakegblp.lusk.nms.core.event.PacketWrapperEvent;
import it.jakegblp.lusk.nms.core.protocol.packets.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

public interface ClientboundPacket<E extends PacketWrapperEvent<?>> extends Packet {

    @Contract("_, _ -> new")
    E createEvent(Player player, boolean async);

    default void send(Player player) {
        if (isAsync())
            sendAsync(player);
        else
            sendSync(player);
    }

    default void send(Player player, ExecutionMode executionMode) {
        if (getExecutionMode() == executionMode || executionMode.isInherited()) {
            send(player);
            return;
        }
        switch (executionMode) {
            case ASYNCHRONOUS -> sendAsync(player);
            case SYNCHRONOUS -> sendSync(player);
        }
    }

    default void sendSync(Player player) {
        NMSApi.sendPacketInternal(player, this);
    }

    default void sendAsync(Player player) {
        this.processAsync().thenRunAsync(() -> sendSync(player),
                Bukkit.getScheduler().getMainThreadExecutor(NMS.getPlugin()));
    }

    @Override
    ClientboundPacket<E> copy();
}
