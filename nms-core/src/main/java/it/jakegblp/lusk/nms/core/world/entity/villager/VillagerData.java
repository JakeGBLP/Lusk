package it.jakegblp.lusk.nms.core.world.entity.villager;

import org.bukkit.entity.Villager;

public record VillagerData(Villager.Type type, Villager.Profession profession, int level) {
    public VillagerData {
        level = Math.max(1, level);
    }
}