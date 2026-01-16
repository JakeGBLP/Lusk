package it.jakegblp.lusk.nms.core.world.player;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.util.BufferSerializable;
import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.PublicKey;
import java.time.Instant;

@Data
@AllArgsConstructor
public class ProfilePublicKey implements Copyable<ProfilePublicKey>, BufferSerializable<ProfilePublicKey> {
    private Instant timestamp;
    private PublicKey publicKey;
    private byte[] signature;

    public ProfilePublicKey(SimpleByteBuf buffer) {
        read(buffer);
    }

    @Override
    public ProfilePublicKey copy() {
        return new ProfilePublicKey(timestamp, publicKey, signature.clone());
    }

    public boolean hasExpired() {
        return timestamp.isBefore(Instant.now());
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeInstant(timestamp);
        buffer.writePublicKey(publicKey);
        buffer.writeByteArray(signature);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        timestamp = buffer.readInstant();
        publicKey = buffer.readPublicKey();
        signature = buffer.readByteArray(4096);
    }
}
