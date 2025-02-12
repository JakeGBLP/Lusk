package it.jakegblp.lusk.version.nms.v1_16_5.packets;

import it.jakegblp.lusk.api.packets.NMSPlayer;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.version.nms.v1_16_5.NMS_1_16_5.NMS;

public class NMSPlayer_1_16_5 extends NMSPlayer<EntityPlayer> {


    public NMSPlayer_1_16_5(EntityPlayer player, int id) {
        super(player, id);
    }

    @Override
    public void update(@NotNull Player player) {
        update(NMS.getNMSServerPlayer(player));
    }

    @Override
    public void update(EntityPlayer serverPlayer) {
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, serverPlayer);
        NMS.sendPacket(packet, (Player) serverPlayer);
        //ChunkMap.TrackedEntity trackedEntity = nmsPlayer.serverLevel().getChunkSource().chunkMap.entityMap.get(nmsPlayer.getId());
        NMS.sendEntitySpawnPacket(serverPlayer, 0, serverPlayer.getPositionVector());
    }

}