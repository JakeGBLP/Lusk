package it.jakegblp.lusk.nms.impl.to_1_21_1;

import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityTeleportPacket;
import it.jakegblp.lusk.nms.core.util.BufferCodec;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entity.RelativeFlag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.world.entity.RelativeMovement;

public class To_1_21_1 {
    public static void registerCodecs(AbstractNMS nms) {
        nms.registerCodec(
                ClientboundTeleportEntityPacket.class, EntityTeleportPacket.class,
                (buffer, packet) -> ClientboundTeleportEntityPacket.STREAM_CODEC.encode(new FriendlyByteBuf(buffer.unwrap()), ((ClientboundTeleportEntityPacket) packet)),
                buffer -> ClientboundTeleportEntityPacket.STREAM_CODEC.decode(new FriendlyByteBuf(buffer.unwrap())),
                (buffer, packet) -> ((EntityTeleportPacket) packet).write(buffer),
                EntityTeleportPacket::new
        );
        nms.registerCodec(BufferCodec.ofEnum(RelativeMovement.class, RelativeFlag.class));
    }
}
