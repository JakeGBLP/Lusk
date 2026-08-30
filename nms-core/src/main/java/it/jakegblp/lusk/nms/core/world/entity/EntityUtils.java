package it.jakegblp.lusk.nms.core.world.entity;

import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.SkinParts;
import it.jakegblp.lusk.nms.core.world.entity.villager.VillagerData;
import it.jakegblp.lusk.nms.core.world.level.particles.ParticleWrapper;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.MainHand;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

public class EntityUtils {

    public static boolean isSprinting(Entity entity) {
        return NMS.isSprinting(entity);
    }

    public static void setSprinting(Entity entity, boolean sprinting) {
        NMS.setSprinting(entity, sprinting);
    }

    public static boolean isSwimming(Entity entity) {
        return NMS.isSwimming(entity);
    }

    public static void setSwimming(Entity entity, boolean swimming) {
        NMS.setSwimming(entity, swimming);
    }

    public static boolean isGliding(Entity entity) {
        return entity instanceof LivingEntity livingEntity && livingEntity.isGliding();
    }

    public static void setGliding(Entity entity, boolean gliding) {
        if (entity instanceof LivingEntity livingEntity) livingEntity.setGliding(gliding);
    }

    public static boolean hasBred(AbstractHorse abstractHorse) {
        return NMS.hasBred(abstractHorse);
    }

    public static void setBred(AbstractHorse abstractHorse, boolean bred) {
        NMS.setBred(abstractHorse, bred);
    }

    public static boolean isBurning(Blaze blaze) {
        return NMS.isBurning(blaze);
    }

    public static void setBurning(Blaze blaze, boolean burning) {
        NMS.setBurning(blaze, burning);
    }

    public static void setHandActive(LivingEntity livingEntity, boolean active) {
        NMS.setHandActive(livingEntity, active);
    }

    public static void setActiveItemHand(LivingEntity livingEntity, EquipmentSlot slot) {
        NMS.setActiveItemHand(livingEntity, slot);
    }

    public static SkinParts.Mutable getSkinParts(LivingEntity livingEntity) throws IllegalArgumentException {
        return switch (livingEntity) {
            case Player player -> player.getClientOption(ClientOption.SKIN_PARTS).mutableCopy();
            case Mannequin mannequin -> mannequin.getSkinParts();
            default -> throw new IllegalArgumentException("Invalid livingentity type: " + livingEntity.getClass().getName());
        };
    }

    public static void setSkinParts(LivingEntity livingEntity, SkinParts skinParts) {
        NMS.setSkinParts(livingEntity, skinParts);
    }

    public static int getAirTicks(Entity entity) {
        return NMS.getAirTicks(entity);
    }

    public static void setAirTicks(Entity entity, int ticks) {
        NMS.setAirTicks(entity, ticks);
    }

    public static boolean isSinglePoint(AreaEffectCloud areaEffectCloud) {
        return NMS.isSinglePoint(areaEffectCloud);
    }

    public static void setSinglePoint(AreaEffectCloud areaEffectCloud, boolean singlePoint) {
        NMS.setSinglePoint(areaEffectCloud, singlePoint);
    }

    public static ParticleWrapper getParticleWrapper(AreaEffectCloud areaEffectCloud) {
        return NMS.getParticleWrapper(areaEffectCloud);
    }

    public static void setParticleWrapper(AreaEffectCloud areaEffectCloud, ParticleWrapper particleWrapper) {
        NMS.setParticleWrapper(areaEffectCloud, particleWrapper);
    }

    public static BlockVector getStartingPosition(FallingBlock fallingBlock) {
        return NMS.getStartingPosition(fallingBlock);
    }

    public static void setStartingPosition(FallingBlock fallingBlock, BlockVector startingPosition) {
        NMS.setStartingPosition(fallingBlock, startingPosition);
    }

    public static @Nullable Integer getBooster(Firework firework) {
        return NMS.getBooster(firework);
    }

    public static void setBooster(Firework firework, @Nullable Integer boost) {
        NMS.setBooster(firework, boost);
    }

    public static boolean isBiting(FishHook fishHook) {
        return NMS.isBiting(fishHook);
    }

    public static void setBiting(FishHook fishHook, boolean biting) {
        NMS.setBiting(fishHook, biting);
    }

    public static void setRolling(Bee bee, boolean rolling) {
        NMS.setRolling(bee, rolling);
    }

    public static void setClimbing(Spider spider, boolean climbing) {
        NMS.setClimbing(spider, climbing);
    }

    public static boolean isSitting(Tameable tameable) {
        return NMS.isSitting(tameable);
    }

    public static void setSitting(Tameable tameable, boolean sitting) {
        NMS.setSitting(tameable, sitting);
    }

    public static ParticleWrapper getAreaEffectCloudDefaultParticle() {
        return NMS.getAreaEffectCloudDefaultParticle();
    }

    public static ParticleWrapper getParticleOptions(PotionEffect potionEffect) {
        return NMS.getParticleOptions(potionEffect);
    }

    public static @Nullable BlockVector getSleepingPosition(LivingEntity livingEntity) {
        return NMS.getSleepingPosition(livingEntity);
    }

    public static void setSleepingPosition(LivingEntity livingEntity, @Nullable BlockVector sleepingPositon) {
        NMS.setSleepingPosition(livingEntity, sleepingPositon);
    }

    public static MainHand getMainHand(LivingEntity livingEntity) {
        return NMS.getMainHand(livingEntity);
    }

    public static void setMainHand(LivingEntity livingEntity, MainHand mainHand) {
        NMS.setMainHand(livingEntity, mainHand);
    }

    public static void setArmadilloState(Armadillo armadillo, Armadillo.State state) {
        NMS.setArmadilloState(armadillo, state);
    }

    public static long getBeePersistentAngerEndTime(Bee bee) {
        return NMS.getBeePersistentAngerEndTime(bee);
    }

    public static void setBeePersistentAngerEndTime(Bee bee, long time) {
        NMS.setBeePersistentAngerEndTime(bee, time);
    }

    public static boolean isLeashHolder(HappyGhast happyGhast) {
        return NMS.isLeashHolder(happyGhast);
    }

    public static void setLeashHolder(HappyGhast happyGhast, boolean leashHolder) {
        NMS.setLeashHolder(happyGhast, leashHolder);
    }

    public static boolean staysStill(HappyGhast happyGhast) {
        return NMS.staysStill(happyGhast);
    }

    public static void setStaysStill(HappyGhast happyGhast, boolean staysStill) {
        NMS.setStaysStill(happyGhast, staysStill);
    }

    public static int getDropSeedAtTick(Sniffer sniffer) {
        return NMS.getDropSeedAtTick(sniffer);
    }

    public static void setDropSeedAtTick(Sniffer sniffer, int ticks) {
        NMS.setDropSeedAtTick(sniffer, ticks);
    }

    public static void setLayingEgg(Turtle turtle, boolean layingEgg) {
        NMS.setLayingEgg(turtle, layingEgg);
    }

    public static long getLastPoseChangeTick(Camel camel) {
        return NMS.getLastPoseChangeTick(camel);
    }

    public static void setLastPoseChangeTick(Camel camel, long tick) {
        NMS.setLastPoseChangeTick(camel, tick);
    }

    public static int getVariant(Horse horse) {
        return NMS.getVariant(horse);
    }

    public static void setVariant(Horse horse, int variant) {
        NMS.setVariant(horse, variant);
    }

    public static long getAngerEndTime(Wolf wolf) {
        return NMS.getAngerEndTime(wolf);
    }

    public static void setAngerEndTime(Wolf wolf, long time) {
        NMS.setAngerEndTime(wolf, time);
    }

    public static boolean isDashing(AbstractNautilus nautilus) {
        return NMS.isDashing(nautilus);
    }

    public static void setDashing(AbstractNautilus nautilus, boolean dashing) {
        NMS.setDashing(nautilus, dashing);
    }

    public static int getHeadShakeTicks(AbstractVillager villager) {
        return NMS.getHeadShakeTicks(villager);
    }

    public static void setHeadShakeTicks(AbstractVillager villager, int ticks) {
        NMS.setHeadShakeTicks(villager, ticks);
    }

    public static boolean canMove(Creaking creaking) {
        return NMS.canMove(creaking);
    }

    public static void setCanMove(Creaking creaking, boolean canMove) {
        NMS.setCanMove(creaking, canMove);
    }

    public static void setActive(Creaking creaking, boolean active) {
        NMS.setActive(creaking, active);
    }

    public static void setHome(Creaking creaking, @Nullable BlockVector home) {
        NMS.setHome(creaking, home);
    }

    public static boolean isTearingDown(Creaking creaking) {
        return NMS.isTearingDown(creaking);
    }

    public static void setTearingDown(Creaking creaking, boolean tearingDown) {
        NMS.setTearingDown(creaking, tearingDown);
    }

    public static int getState(Creeper creeper) {
        return NMS.getState(creeper);
    }

    public static void setState(Creeper creeper, int state) {
        NMS.setState(creeper, state);
    }

    public static void setMoving(Guardian guardian, boolean moving) {
        NMS.setMoving(guardian, moving);
    }

    public static int getLaserTargetId(Guardian guardian) {
        return NMS.getLaserTargetId(guardian);
    }

    public static void setLaserTargetId(Guardian guardian, int targetId) {
        NMS.setLaserTargetId(guardian, targetId);
    }

    public static void setClientAngerLevel(Warden warden, int angerLevel) {
        NMS.setClientAngerLevel(warden, angerLevel);
    }

    public static int getWitherHeadTargetId(Wither wither, Wither.Head head) {
        return NMS.getWitherHeadTargetId(wither, head);
    }

    public static void setWitherHeadTargetId(Wither wither, Wither.Head head, int targetId) {
        NMS.setWitherHeadTargetId(wither, head, targetId);
    }

    public static void setVillagerData(ZombieVillager zombieVillager, VillagerData villagerData) {
        NMS.setVillagerData(zombieVillager, villagerData);
    }

    public static boolean isChargingCrossbow(Pillager pillager) {
        return NMS.isChargingCrossbow(pillager);
    }

    public static void setChargingCrossbow(Pillager pillager, boolean charging) {
        NMS.setChargingCrossbow(pillager, charging);
    }

    public static void setWitchUsingItem(Witch witch, boolean usingItem) {
        NMS.setWitchUsingItem(witch, usingItem);
    }

    public static int getVehicleShakingPower(Vehicle vehicle) {
        return NMS.getVehicleShakingPower(vehicle);
    }

    public static void setVehicleShakingPower(Vehicle vehicle, int shakingPower) {
        NMS.setVehicleShakingPower(vehicle, shakingPower);
    }

    public static int getVehicleShakingDirection(Vehicle vehicle) {
        return NMS.getVehicleShakingDirection(vehicle);
    }

    public static void setVehicleShakingDirection(Vehicle vehicle, int shakingDirection) {
        NMS.setVehicleShakingDirection(vehicle, shakingDirection);
    }

    public static float getVehicleShakingMultiplier(Vehicle vehicle) {
        return NMS.getVehicleShakingMultiplier(vehicle);
    }

    public static void setVehicleShakingMultiplier(Vehicle vehicle, float shakingMultiplier) {
        NMS.setVehicleShakingMultiplier(vehicle, shakingMultiplier);
    }

    public static boolean isTurningLeftPaddle(Boat boat) {
        return NMS.isTurningLeftPaddle(boat);
    }

    public static void setTurningLeftPaddle(Boat boat, boolean turning) {
        NMS.setTurningLeftPaddle(boat, turning);
    }

    public static boolean isTurningRightPaddle(Boat boat) {
        return NMS.isTurningRightPaddle(boat);
    }

    public static void setTurningRightPaddle(Boat boat, boolean turning) {
        NMS.setTurningRightPaddle(boat, turning);
    }

    public static int getSplashTicks(Boat boat) {
        return NMS.getSplashTicks(boat);
    }

    public static void setSplashTicks(Boat boat, int splashTimer) {
        NMS.setSplashTicks(boat, splashTimer);
    }

    public static boolean hasFuel(PoweredMinecart poweredMinecart) {
        return NMS.hasFuel(poweredMinecart);
    }

    public static void setHasFuel(PoweredMinecart poweredMinecart, boolean hasFuel) {
        NMS.setHasFuel(poweredMinecart, hasFuel);
    }

    public static @Nullable Integer getLeftShoulderParrotVariantId(Player player) {
        return NMS.getLeftShoulderParrotVariantId(player);
    }

    public static void setLeftShoulderParrotVariantId(Player player, @Nullable Integer variantId) {
        NMS.setLeftShoulderParrotVariantId(player, variantId);
    }

    public static @Nullable Integer getRightShoulderParrotVariantId(Player player) {
        return NMS.getRightShoulderParrotVariantId(player);
    }

    public static void setRightShoulderParrotVariantId(Player player, @Nullable Integer variantId) {
        NMS.setRightShoulderParrotVariantId(player, variantId);
    }
}
