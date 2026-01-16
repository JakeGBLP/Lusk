package it.jakegblp.lusk.nms.core.world.entity.villager;

import it.jakegblp.lusk.nms.core.util.PureNMSObject;
import org.bukkit.entity.Villager;

public record VillagerData(Villager.Type type, Villager.Profession profession, int level) implements PureNMSObject<Object> {
    public VillagerData {
        level = Math.max(1, level);
    }
}