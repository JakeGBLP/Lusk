package it.jakegblp.lusk.nms.core.protocol.packets.client;


import it.jakegblp.lusk.nms.core.world.level.ParticleOptions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@AllArgsConstructor
@Getter
@Setter
public class LevelParticlePacket implements ClientboundPacket, Cloneable {

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
    private final ParticleOptions particle;

    @Override
    public Object asNMS() {
        return NMS.toNMSLevelParticle(this);
    }

    @Override
    public LevelParticlePacket clone() {
        try {
            LevelParticlePacket clone = (LevelParticlePacket) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
