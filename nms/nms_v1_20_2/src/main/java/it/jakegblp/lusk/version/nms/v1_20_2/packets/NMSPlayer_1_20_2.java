package it.jakegblp.lusk.version.nms.v1_20_2.packets;

import it.jakegblp.lusk.api.packets.NMSPlayer;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.Entry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;

import static it.jakegblp.lusk.version.nms.v1_20_2.NMS_1_20_2.NMS;

public class NMSPlayer_1_20_2 extends NMSPlayer<ServerPlayer> {

    private final Entry entry;

    public NMSPlayer_1_20_2(ServerPlayer player, int id) {
        super(player, id);
        entry = new Entry(getServerPlayer().getUUID(), getServerPlayer().getGameProfile(), true, 0,
                GameType.CREATIVE, getServerPlayer().getDisplayName(), null);
    }

    @Override
    public void update(@NotNull Player player) {
        update(NMS.getNMSServerPlayer(player));
    }

    @Override
    public void update(ServerPlayer serverPlayer) {
        NMS.sendPacket(new ClientboundPlayerInfoUpdatePacket(EnumSet.of(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER,
                ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED), List.of(serverPlayer)));
        //ChunkMap.TrackedEntity trackedEntity = nmsPlayer.serverLevel().getChunkSource().chunkMap.entityMap.get(nmsPlayer.getId());
        NMS.sendEntitySpawnPacket(serverPlayer, 0, serverPlayer.position());
    }

}