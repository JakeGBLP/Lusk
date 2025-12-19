package it.jakegblp.lusk.nms.core.world.player;

import java.util.UUID;

public record ChatSessionData(UUID sessionId, ProfilePublicKey profilePublicKey) implements Cloneable {
    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public ChatSessionData clone() throws CloneNotSupportedException {
        return new ChatSessionData(sessionId, profilePublicKey.clone());
    }
}