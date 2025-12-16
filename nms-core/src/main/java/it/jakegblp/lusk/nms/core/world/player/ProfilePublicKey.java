package it.jakegblp.lusk.nms.core.world.player;

import java.security.PublicKey;
import java.time.Instant;

public record ProfilePublicKey(Instant timestamp, PublicKey publicKey, byte[] signature) {
}
