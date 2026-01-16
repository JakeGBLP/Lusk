package it.jakegblp.lusk.nms.core.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PacketFlow {
    SERVERBOUND("serverbound"),
    CLIENTBOUND("clientbound");

    private final String id;

    public PacketFlow getOpposite() {
        return this == CLIENTBOUND ? SERVERBOUND : CLIENTBOUND;
    }

}
