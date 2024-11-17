package it.jakegblp.lusk.utils;

import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.*;

public class EntityUtils {
    public static boolean shouldBurnDuringTheDay(LivingEntity entity) {
        return (entity instanceof Zombie zombie && zombie.shouldBurnInDay()) ||
                (entity instanceof AbstractSkeleton skeleton && skeleton.shouldBurnInDay()) ||
                (entity instanceof Phantom phantom && phantom.shouldBurnInDay());
    }

    public static void setShouldBurnDuringTheDay(boolean shouldBurnDuringTheDay, LivingEntity... entities) {
        for (LivingEntity entity : entities) {
            if (entity instanceof Zombie zombie) zombie.setShouldBurnInDay(shouldBurnDuringTheDay);
            else if (entity instanceof AbstractSkeleton skeleton) skeleton.setShouldBurnInDay(shouldBurnDuringTheDay);
            else if (entity instanceof Phantom phantom) phantom.setShouldBurnInDay(shouldBurnDuringTheDay);
        }
    }

    /**
     * Checks if an entity is in the position to inflict critical damage to another entity
     * (doesn't take into account if there is an entity to inflict the critical damage to).
     *
     * @param entity The entity to check this against.
     * @return Whether the given entity can inflict critical damage.
     */
    public static boolean canCriticalDamage(Entity entity) {
        return entity instanceof Player p
                && p.getFallDistance() > 0
                && !entity.isOnGround()
                && !p.isClimbing()
                && !p.isInWater()
                && !p.hasPotionEffect(PotionEffectType.BLINDNESS)
                && !p.hasPotionEffect(PotionEffectType.SLOW_FALLING)
                && !p.isInsideVehicle()
                && p.getAttackCooldown() > 0.9
                && !p.isSprinting();
    }

    public static boolean isAngry(LivingEntity entity) {
        if (entity instanceof PigZombie pigZombie) {
            return pigZombie.isAngry();
        } else if (entity instanceof Wolf wolf) {
            return wolf.isAngry();
        } else if (HAS_WARDEN && entity instanceof Warden warden) {
            return warden.getAngerLevel() == Warden.AngerLevel.ANGRY;
        } else if (entity instanceof Enderman enderman) {
            return enderman.isScreaming();
        }
        return false;
    }

    public static void setAngry(LivingEntity entity, boolean bool) {
        if (entity instanceof PigZombie pigZombie) {
            pigZombie.setAngry(bool);
        } else if (entity instanceof Wolf wolf) {
            wolf.setAngry(bool);
        } else if (entity instanceof Enderman enderman) {
            enderman.setScreaming(bool);
        }
    }

    @Nullable
    public static Object getVariant(LivingEntity livingEntity) {
        if (livingEntity instanceof Axolotl axolotl) {
            return axolotl.getVariant();
        } else if (livingEntity instanceof Frog frog) {
            return frog.getVariant();
        } else if (livingEntity instanceof MushroomCow mushroomCow) {
            return mushroomCow.getVariant();
        } else if (livingEntity instanceof Parrot parrot) {
            return parrot.getVariant();
        } else if (livingEntity instanceof Llama llama) {
            return llama.getColor();
        } else if (livingEntity instanceof Fox fox) {
            return fox.getFoxType();
        } else if (livingEntity instanceof Cat cat) {
            return cat.getCatType();
        } else if (livingEntity instanceof Rabbit rabbit) {
            return rabbit.getRabbitType();
        } else if (livingEntity instanceof Panda panda) {
            return panda.getMainGene();
        } else if (livingEntity instanceof TropicalFish tropicalFish) {
            return tropicalFish.getPattern();
        } else if (livingEntity instanceof Wolf wolf) {
            return wolf.getVariant();
        } else if (livingEntity instanceof Salmon salmon) {
            return salmon.getVariant();
        }
        return null;
    }

    public static void setVariant(LivingEntity livingEntity, Object unknownVariant) {
        if (livingEntity instanceof MushroomCow mushroomCow && unknownVariant instanceof MushroomCow.Variant variant) {
            mushroomCow.setVariant(variant);
        } else if (livingEntity instanceof Parrot parrot && unknownVariant instanceof Parrot.Variant variant) {
            parrot.setVariant(variant);
        } else if (livingEntity instanceof Llama llama && unknownVariant instanceof Llama.Color color) {
            llama.setColor(color);
        } else if (livingEntity instanceof Fox fox && unknownVariant instanceof Fox.Type type) {
            fox.setFoxType(type);
        } else if (livingEntity instanceof Cat cat && unknownVariant instanceof Cat.Type type) {
            cat.setCatType(type);
        } else if (livingEntity instanceof Rabbit rabbit && unknownVariant instanceof Rabbit.Type type) {
            rabbit.setRabbitType(type);
        } else if (livingEntity instanceof Panda panda && unknownVariant instanceof Panda.Gene gene) {
            panda.setMainGene(gene);
        } else if (livingEntity instanceof TropicalFish tropicalFish && unknownVariant instanceof TropicalFish.Pattern pattern) {
            tropicalFish.setPattern(pattern);
        } else if (HAS_FROG_VARIANT && livingEntity instanceof Frog frog && unknownVariant instanceof Frog.Variant variant) {
            frog.setVariant(variant);
        } else if (HAS_WOLF_VARIANT && livingEntity instanceof Wolf wolf && unknownVariant instanceof Wolf.Variant variant) {
            wolf.setVariant(variant);
        } else if (HAS_SALMON_VARIANT && livingEntity instanceof Salmon salmon && unknownVariant instanceof Salmon.Variant variant) {
            salmon.setVariant(variant);
        } else if (HAS_AXOLOTL_VARIANT && livingEntity instanceof Axolotl axolotl && unknownVariant instanceof Axolotl.Variant variant) {
            axolotl.setVariant(variant);
        }
    }
}