package it.jakegblp.lusk.utils;

import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffectType;

import static it.jakegblp.lusk.utils.Constants.HAS_WARDEN;

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
                && p.getVehicle() == null
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
}