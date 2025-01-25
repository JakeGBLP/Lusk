package it.jakegblp.lusk.utils;

import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static ch.njol.skript.paperlib.PaperLib.isPaper;
import static it.jakegblp.lusk.utils.Constants.*;

public class EntityUtils {
    // https://github.com/PaperMC/Paper/commit/d714682f8fbcf87edada17b513cf76f499c9b355

    public static boolean shouldBurnDuringTheDay(LivingEntity entity) {
        if (!isPaper()) return false;
        if (entity instanceof Zombie zombie) return zombie.shouldBurnInDay();
        else if (MINECRAFT_1_18_2) {
            if (entity instanceof Phantom phantom) return phantom.shouldBurnInDay();
            else if (entity instanceof AbstractSkeleton skeleton) return skeleton.shouldBurnInDay();
        } else if (entity instanceof Skeleton skeleton) {
            try {
                return (boolean) Skeleton.class.getMethod("shouldBurnInDay").invoke(skeleton);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {}
        }
        return false;
    }

    public static void setShouldBurnDuringTheDay(LivingEntity entity, boolean value) {
        if (!isPaper()) return;
        if (entity instanceof Zombie zombie) zombie.setShouldBurnInDay(value);
        else if (MINECRAFT_1_18_2) {
            if (entity instanceof Phantom phantom) phantom.setShouldBurnInDay(value);
            else if (entity instanceof AbstractSkeleton skeleton) skeleton.setShouldBurnInDay(value);
        } else if (entity instanceof Skeleton skeleton) {
            try {
                Skeleton.class.getMethod("setShouldBurnInDay", boolean.class).invoke(skeleton, value);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {}
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
        } else if (PAPER_HAS_1_18_2_EXTENDED_ENTITY_API && entity instanceof Enderman enderman) {
            return enderman.isScreaming();
        }
        return false;
    }

    public static boolean isInterested(LivingEntity entity) {
        if (PAPER_HAS_1_18_2_EXTENDED_ENTITY_API && entity instanceof Fox fox) {
            return fox.isInterested();
        } else if (entity instanceof Wolf wolf) {
            return wolf.isInterested();
        }
        return false;
    }

    public static void setIsInterested(LivingEntity entity, boolean interested) {
        if (PAPER_HAS_1_18_2_EXTENDED_ENTITY_API && entity instanceof Fox fox) {
            fox.setInterested(interested);
        } else if (entity instanceof Wolf wolf) {
            wolf.setInterested(interested);
        }
    }

    public static void setIsAngry(LivingEntity entity, boolean bool) {
        if (entity instanceof PigZombie pigZombie) {
            pigZombie.setAngry(bool);
        } else if (entity instanceof Wolf wolf) {
            wolf.setAngry(bool);
        } else if (PAPER_HAS_1_18_2_EXTENDED_ENTITY_API && entity instanceof Enderman enderman) {
            enderman.setScreaming(bool);
        }
    }

    public static boolean isSitting(Entity entity) {
        if (entity instanceof Sittable sittable) {
            return sittable.isSitting();
        } else {
            return entity.getPose() == Pose.SITTING;
        }
    }

    public static void setIsSitting(Entity entity, boolean sitting) {
        if (entity instanceof Sittable sittable) {
            sittable.setSitting(sitting);
        } else if (isPaper() && MINECRAFT_1_20_1) {
            if (sitting && entity.getPose() != Pose.SITTING) {
                entity.setPose(Pose.SITTING);
            } else if (entity.getPose() == Pose.SITTING) {
                entity.setPose(Pose.STANDING);
            }
        }
    }

    public static boolean isScreaming(LivingEntity entity) {
        if (entity instanceof Goat goat) {
            return goat.isScreaming();
        } else if (PAPER_HAS_1_18_2_EXTENDED_ENTITY_API && entity instanceof Enderman enderman) {
            return enderman.isScreaming();
        }
        return false;
    }

    public static void setIsScreaming(LivingEntity entity, boolean screaming) {
        if (entity instanceof Goat goat) {
            goat.setScreaming(screaming);
        } else if (PAPER_HAS_1_18_2_EXTENDED_ENTITY_API && entity instanceof Enderman enderman) {
            enderman.setScreaming(screaming);
        }
    }

    public static boolean isRoaring(LivingEntity entity) {
        if (PAPER_HAS_1_19_2_EXTENDED_ENTITY_API && entity instanceof Ravager ravager)
            return ravager.getRoarTicks() > 0;
        else if (entity instanceof EnderDragon enderDragon)
            return enderDragon.getPhase() == EnderDragon.Phase.ROAR_BEFORE_ATTACK;
        else return entity.getPose() == Pose.ROARING;
    }

    public static boolean isConverting(LivingEntity entity) {
        if (entity instanceof PiglinAbstract piglinAbstract)
            return piglinAbstract.isConverting();
        else if (entity instanceof Skeleton skeleton)
            return skeleton.isConverting();
        else if (entity instanceof Hoglin hoglin)
            return hoglin.isConverting();
        else if (entity instanceof Zombie zombie)
            return zombie.isConverting();
        else return false;
    }

    public static boolean isDancing(LivingEntity entity) {
        if (entity instanceof Parrot parrot)
            return parrot.isDancing();
        else if (entity instanceof Piglin piglin)
            return piglin.isDancing();
        else if (entity instanceof Allay allay)
            return allay.isDancing();
        return false;
    }

    public static List<UUID> getArrayAsUUIDList(Object[] objects) {
        return Arrays.stream(objects).map(o -> {
            if (o instanceof Entity entity) return entity.getUniqueId();
            else if (o instanceof String string) return UUID.fromString(string);
            else if (o instanceof UUID aUuid) return aUuid;
            return null;
        }).filter(Objects::nonNull).toList();
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

    @Nullable
    public static ItemStack getEntityEquipmentSlot(LivingEntity livingEntity, EquipmentSlot equipmentSlot) {
        if (livingEntity instanceof Player player) {
            return player.getInventory().getItem(equipmentSlot);
        } else if (PAPER_1_21 && livingEntity.canUseEquipmentSlot(equipmentSlot)) {
            EntityEquipment entityEquipment = livingEntity.getEquipment();
            if (entityEquipment != null) {
                return entityEquipment.getItem(equipmentSlot);
            }
        }
        return null;
    }

    public static void setEntityEquipmentSlot(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack itemStack) {
        if (livingEntity instanceof Player player) {
            player.getInventory().setItem(equipmentSlot, itemStack);
        } else if (PAPER_1_21 && livingEntity.canUseEquipmentSlot(equipmentSlot)) {
            EntityEquipment entityEquipment = livingEntity.getEquipment();
            if (entityEquipment != null) {
                entityEquipment.setItem(equipmentSlot, itemStack);
            }
        }
    }
    //todo: add can pickup items for pre 2.8
    //todo: add isImmuneToZombification for hoglins and piglins
}
