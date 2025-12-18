package it.jakegblp.lusk.nms.core.world.level;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Particle;

@AllArgsConstructor
@Getter
@Setter
public class ParticleOptions {
    //todo all implementations (eg, dust ect) and add in the ParticleSendEvent

    Particle type;

}
