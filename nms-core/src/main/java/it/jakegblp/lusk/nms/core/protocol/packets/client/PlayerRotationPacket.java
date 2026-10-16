package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.event.client.PlayerRotationPacketEvent;
import it.jakegblp.lusk.nms.core.protocol.packets.PacketWithRotation;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.entity.Player;

/**
 * <a href="https://minecraft.wiki/w/Java_Edition_protocol/Packets#Player_Rotation">Player Rotation Packet</a>
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PlayerRotationPacket implements ClientboundPacket<PlayerRotationPacketEvent>, PacketWithRotation {
    protected float yaw, pitch;

    public PlayerRotationPacket(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public PlayerRotationPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        yaw = buffer.readFloat();
        pitch = buffer.readFloat();
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        buffer.writeFloat(yaw);
        buffer.writeFloat(pitch);
    }

    @Override
    public PlayerRotationPacketEvent createEvent(Player player, boolean async) {
        return new PlayerRotationPacketEvent(this, player, async);
    }

    @Override
    public PlayerRotationPacket copy() {
        return new PlayerRotationPacket(yaw, pitch);
    }
}
