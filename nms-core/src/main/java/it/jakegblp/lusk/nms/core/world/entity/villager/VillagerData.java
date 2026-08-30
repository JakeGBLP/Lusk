package it.jakegblp.lusk.nms.core.world.entity.villager;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.util.PureNMSObject;
import it.jakegblp.lusk.nms.core.world.entity.EntityUtils;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record VillagerData(Villager.Type type, Villager.Profession profession, int level) implements PureNMSObject<Object>, Copyable<VillagerData> {
    public VillagerData {
        level = Math.max(1, level);
    }

    public VillagerData(Villager villager) {
        this(villager.getVillagerType(), villager.getProfession(), villager.getVillagerLevel());
    }

    @SuppressWarnings("ConstantConditions")
    public VillagerData(ZombieVillager zombieVillager) {
        this(zombieVillager.getVillagerType(), zombieVillager.getVillagerProfession(), 1);
    }

    public VillagerData() {
        this(Villager.Type.PLAINS, Villager.Profession.NONE, 1);
    }

    public void apply(Villager villager) {
        villager.setVillagerType(type);
        villager.setProfession(profession);
        villager.setVillagerLevel(level);
    }
    public void apply(ZombieVillager zombieVillager) {
        EntityUtils.setVillagerData(zombieVillager, this);
    }

    @Override
    public VillagerData copy() {
        return new VillagerData(type, profession, level);
    }
}