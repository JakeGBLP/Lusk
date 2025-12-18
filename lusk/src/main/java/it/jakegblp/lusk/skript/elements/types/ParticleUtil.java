package it.jakegblp.lusk.skript.elements.types;

import ch.njol.skript.Skript;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;

// poa stuff

public class ParticleUtil {

    private ParticleUtil() {
    }

    private static final Map<String, Particle> PARTICLES = new HashMap<>();
    private static final Map<Particle, String> PARTICLE_NAMES = new HashMap<>();
    private static final boolean HAS_PLAYER_FORCE = Skript.methodExists(Player.class, "spawnParticle",
            Particle.class, Location.class, int.class, double.class, double.class, double.class, double.class, Object.class, boolean.class);
    // Added in Minecraft 1.21.4
    public static final boolean HAS_TRAIL = Skript.classExists("org.bukkit.Particle$Trail");
    // Added in Minecraft 1.21.9
    public static final boolean HAS_SPELL = Skript.classExists("org.bukkit.Particle$Spell");

    static {
        Registry.PARTICLE_TYPE.forEach(particle -> {
            String key = particle.getKey().getKey();
            PARTICLES.put(key, particle);
            PARTICLE_NAMES.put(particle, key);
        });
    }

    /**
     * Get the Minecraft name of a particle
     *
     * @param particle Particle to get name of
     * @return Minecraft name of particle
     */
    public static String getName(Particle particle) {
        return PARTICLE_NAMES.get(particle);
    }


}