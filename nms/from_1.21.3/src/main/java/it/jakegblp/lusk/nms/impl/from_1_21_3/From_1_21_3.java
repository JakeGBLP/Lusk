package it.jakegblp.lusk.nms.impl.from_1_21_3;

import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityTeleportPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerRotationPacket;
import it.jakegblp.lusk.nms.core.serialization.BufferCodecs;
import it.jakegblp.lusk.nms.core.serialization.Mappings;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entity.RelativeFlag;
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerRotationPacket;
import net.minecraft.world.entity.Relative;

public class From_1_21_3 {
    public static void registerCodecs(Mappings mappings, BufferCodecs codecs) {
        var entityTeleportPacketBufferCodec = codecs.register(EntityTeleportPacket.class, (buf, packet) -> packet.write(buf), EntityTeleportPacket::new);
        mappings.registerStreamCodecUnsafe(
                ClientboundEntityPositionSyncPacket.class,
                ClientboundEntityPositionSyncPacket.STREAM_CODEC,
                entityTeleportPacketBufferCodec
        );
        var playerRotationPacketBufferCodec = codecs.register(PlayerRotationPacket.class, (buf, packet) -> packet.write(buf), PlayerRotationPacket::new);
        mappings.registerStreamCodecUnsafe(
                ClientboundPlayerRotationPacket.class,
                ClientboundPlayerRotationPacket.STREAM_CODEC,
                playerRotationPacketBufferCodec
        );
        codecs.register(RelativeFlag.class);
        mappings.register(Relative.class, RelativeFlag.class);
    }
}