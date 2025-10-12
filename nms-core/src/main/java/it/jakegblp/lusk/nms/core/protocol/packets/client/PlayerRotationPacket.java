package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.common.annotations.Availability;
import it.jakegblp.lusk.nms.core.protocol.packets.UnsupportedPacket;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.function.Supplier;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

/**
 * <a href="https://minecraft.wiki/w/Java_Edition_protocol/Packets#Player_Rotation">Player Rotation Packet</a>
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Availability(addedIn = "1.21.2")
public class PlayerRotationPacket implements ClientboundPacket, UnsupportedPacket {
    protected float yaw, pitch;

    /**
     * @throws UnsupportedOperationException if the server version is below 1.21.2
     */
    @Availability(addedIn = "1.21.2")
    public PlayerRotationPacket(float yaw, float pitch) {
        validate();
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * @throws UnsupportedOperationException if the server version is below 1.21.2
     */
    @Override
    public Object asNMS() {
        validate();
        return NMS.toNMSPlayerRotationPacket(this);
    }

    @Override
    public boolean check() {
        return NMS.getVersion().isGreaterOrEqual(Version.of(1,21,2));
    }

    @Override
    public Supplier<UnsupportedOperationException> getExceptionSupplier() {
        return () -> new UnsupportedOperationException("The 'Player Rotation' packet requires 1.21.2!");
    }

}
