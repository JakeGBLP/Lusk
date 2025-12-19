package it.jakegblp.lusk.nms.core.world.player;

import java.security.PublicKey;
import java.time.Instant;

public record ProfilePublicKey(Instant timestamp, PublicKey publicKey, byte[] signature) implements Cloneable {
    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public ProfilePublicKey clone() throws CloneNotSupportedException {
        return new ProfilePublicKey(timestamp, publicKey, signature.clone());
    }
}
