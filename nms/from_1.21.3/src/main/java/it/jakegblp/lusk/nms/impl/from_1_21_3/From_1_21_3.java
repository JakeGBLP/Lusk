package it.jakegblp.lusk.nms.impl.from_1_21_3;

import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityTeleportPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerRotationPacket;
import it.jakegblp.lusk.nms.core.util.BufferCodec;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entity.RelativeFlag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerRotationPacket;
import net.minecraft.world.entity.Relative;

public class From_1_21_3 {
    public static void registerCodecs(AbstractNMS nms) {
        nms.registerCodec(
                ClientboundEntityPositionSyncPacket.class, EntityTeleportPacket.class,
                (buffer, packet) -> ClientboundEntityPositionSyncPacket.STREAM_CODEC.encode(new FriendlyByteBuf(buffer.unwrap()), (ClientboundEntityPositionSyncPacket) packet),
                buffer -> ClientboundEntityPositionSyncPacket.STREAM_CODEC.decode(new FriendlyByteBuf(buffer.unwrap())),
                (buffer, packet) -> ((EntityTeleportPacket) packet).write(buffer),
                EntityTeleportPacket::new
        );
        nms.registerCodec(
                ClientboundPlayerRotationPacket.class, PlayerRotationPacket.class,
                (buffer, packet) -> ClientboundPlayerRotationPacket.STREAM_CODEC.encode(new FriendlyByteBuf(buffer.unwrap()), ((ClientboundPlayerRotationPacket) packet)),
                buffer -> ClientboundPlayerRotationPacket.STREAM_CODEC.decode(new FriendlyByteBuf(buffer.unwrap())),
                (buffer, packet) -> ((PlayerRotationPacket) packet).write(buffer),
                PlayerRotationPacket::new
        );
        nms.registerCodec(BufferCodec.ofEnum(Relative.class, RelativeFlag.class));
    }
}