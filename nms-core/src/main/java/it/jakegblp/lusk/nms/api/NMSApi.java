package it.jakegblp.lusk.nms.api;

import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.async.Asyncable;
import it.jakegblp.lusk.nms.core.async.ExecutionMode;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientBundlePacket;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

// todo: add send packets sync and async variants (without the ExecutionMode parameter)
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

    public static void sendPacketInternal(Player player, @NotNull ClientboundPacket packet) {
        NMS.sendPacket(player, packet.asNMS());
    }

    public static @NotNull CompletableFuture<Void> processPacket(ClientboundPacket[] packets, boolean async) {
        if (async) {
            return CompletableFuture.allOf(CommonUtils.map(CompletableFuture.class, packets, Asyncable::processAsync));
        } else {
            for (ClientboundPacket packet : packets)
                packet.process();
            return CompletableFuture.completedFuture(null);
        }
    }

    public static @NotNull CompletableFuture<Void> sendPackets(Player[] players, ClientboundPacket[] packets, @Nullable ExecutionMode executionMode) {
        boolean async = (executionMode != null && executionMode.isAsync()) || ExecutionMode.getAsyncablePrioritizedMode(List.of(packets)).isAsync();
        Bukkit.getLogger().info("async: "+async);
        CompletableFuture<Void> future = processPacket(packets, async)
                .thenRun(() -> {
                    for (Player player : players) {
                        for (ClientboundPacket packet : packets) {
                            sendPacketInternal(player, packet);
                        }
                    }
                });

        if (!async) {
            future.join();
        }

        return future;
    }

    public static @NotNull CompletableFuture<Void> sendPacket(Player[] players, ClientboundPacket packet, @Nullable ExecutionMode executionMode) {
        return sendPackets(players, new ClientboundPacket[]{packet}, executionMode);
    }

    public static @NotNull CompletableFuture<Void> sendPackets(Player[] players, ClientboundPacket... packets) {
        return sendPackets(players, packets, null);
    }

    public static @NotNull CompletableFuture<Void> sendBundledPackets(Player[] players, ClientboundPacket[] packets, @Nullable ExecutionMode executionMode) {
        return sendPackets(players, new ClientboundPacket[]{new ClientBundlePacket(packets)}, executionMode);
    }

    public static @NotNull CompletableFuture<Void> sendBundledPackets(Player[] players, ClientboundPacket... packets) {
        return sendPackets(players, new ClientBundlePacket(packets));
    }

}
