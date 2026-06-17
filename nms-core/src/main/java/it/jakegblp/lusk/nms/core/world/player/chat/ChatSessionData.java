package it.jakegblp.lusk.nms.core.world.player.chat;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.serialization.BufferSerializable;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ChatSessionData implements Copyable<ChatSessionData>, BufferSerializable<ChatSessionData> {
    private UUID sessionId;
    private ProfilePublicKey profilePublicKey;

    public ChatSessionData(SimpleByteBuf buffer) {
        read(buffer);
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeUUID(sessionId);
        buffer.writeProfilePublicKey(profilePublicKey);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        sessionId = buffer.readUUID();
        profilePublicKey = buffer.readProfilePublicKey();
    }

    @Override
    public ChatSessionData copy() {
        return new ChatSessionData(sessionId, profilePublicKey.copy());
    }
}