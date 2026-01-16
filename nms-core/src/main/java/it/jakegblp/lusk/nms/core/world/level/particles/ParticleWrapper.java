package it.jakegblp.lusk.nms.core.world.level.particles;

import org.bukkit.Particle;
import org.jetbrains.annotations.Nullable;

public record ParticleWrapper(Particle particle, @Nullable Object data) {}
