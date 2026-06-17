package it.jakegblp.lusk.nms.impl.to_1_21_1;

import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityTeleportPacket;
import it.jakegblp.lusk.nms.core.serialization.BufferCodecs;
import it.jakegblp.lusk.nms.core.serialization.Mappings;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entity.RelativeFlag;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.world.entity.RelativeMovement;

public class To_1_21_1 {
    public static void registerCodecs(Mappings mappings, BufferCodecs codecs) {
        var entityTeleportPacketBufferCodec = codecs.register(EntityTeleportPacket.class, (buf, packet) -> packet.write(buf), EntityTeleportPacket::new);
        mappings.registerStreamCodecUnsafe(
                ClientboundTeleportEntityPacket.class,
                ClientboundTeleportEntityPacket.STREAM_CODEC,
                entityTeleportPacketBufferCodec
        );
        codecs.register(RelativeFlag.class);
        mappings.register(RelativeMovement.class, RelativeFlag.class);
    }
}
