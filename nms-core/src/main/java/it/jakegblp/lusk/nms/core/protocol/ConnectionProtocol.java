package it.jakegblp.lusk.nms.core.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConnectionProtocol {
    HANDSHAKING("handshake"),
    PLAY("play"),
    STATUS("status"),
    LOGIN("login"),
    CONFIGURATION("configuration");

    private final String id;
}
