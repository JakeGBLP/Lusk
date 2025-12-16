package it.jakegblp.lusk.nms.core.world.player;

import java.util.UUID;

public record ChatSessionData(UUID sessionId, ProfilePublicKey profilePublicKey) {}