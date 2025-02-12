package it.jakegblp.lusk.version.nms.v1_17_1.packets;

import it.jakegblp.lusk.api.packets.NMSPlayer;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.version.nms.v1_17_1.NMS_1_17_1.NMS;

public class NMSPlayer_1_17_1 extends NMSPlayer<ServerPlayer> {


    public NMSPlayer_1_17_1(ServerPlayer player, int id) {
        super(player, id);
    }

    @Override
    public void update(@NotNull Player player) {
        update(NMS.getNMSServerPlayer(player));
    }

    @Override
    public void update(ServerPlayer serverPlayer) {
        ClientboundPlayerInfoPacket packet = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, serverPlayer);
        NMS.sendPacket(packet, (Player) serverPlayer);
        //ChunkMap.TrackedEntity trackedEntity = nmsPlayer.serverLevel().getChunkSource().chunkMap.entityMap.get(nmsPlayer.getId());
        NMS.sendEntitySpawnPacket(serverPlayer, 0, serverPlayer.position());
    }

}