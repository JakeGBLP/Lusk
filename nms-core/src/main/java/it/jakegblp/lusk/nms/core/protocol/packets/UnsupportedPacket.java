package it.jakegblp.lusk.nms.core.protocol.packets;

import it.jakegblp.lusk.common.Validatable;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface UnsupportedPacket extends Validatable<UnsupportedOperationException>, Packet {

}
