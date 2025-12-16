package it.jakegblp.lusk.nms.core.protocol.packets.client;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@AllArgsConstructor
@Getter
@Setter
public class LevelParticlePacket implements ClientboundPacket {

    private final double x;
    private final double y;
    private final double z;
    private final float xDist;
    private final float yDist;
    private final float zDist;
    private final float maxSpeed;
    private final int count;
    private final boolean overrideLimiter;
    private final boolean alwaysShow;
    private final Object particle; // todo ParticleOptions -> and in ParticleSendEvent (if we can be bothered)

    @Override
    public Object asNMS() {
        return NMS.toNMSLevelParticle(this);
    }
}
