package it.jakegblp.lusk.utils;

import ch.njol.skript.entity.EntityData;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static it.jakegblp.lusk.utils.Constants.*;

public class EntityUtils {
    // https://github.com/PaperMC/Paper/commit/d714682f8fbcf87edada17b513cf76f499c9b355

    public static final Class<?> COW_CLASS;
    public static final @Nullable Method COW_GET_VARIANT_METHOD;
    public static final @Nullable Method COW_SET_VARIANT_METHOD;

    static {
        if (HAS_COW_VARIANT) {
            try {
                COW_CLASS = Class.forName("org.bukkit.entity.Cow");
                COW_GET_VARIANT_METHOD = COW_CLASS.getDeclaredMethod("getVariant");
                COW_SET_VARIANT_METHOD = COW_CLASS.getDeclaredMethod("setVariant", Cow.Variant.class);
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                throw new RuntimeException("Cow Variant couldn't be obtained, please report this.", e);
            }
        } else {
            COW_CLASS = null;
            COW_GET_VARIANT_METHOD = null;
            COW_SET_VARIANT_METHOD = null;
        }
    }

    public static boolean shouldBurnDuringTheDay(LivingEntity entity) {
        return switch (entity) {
            case Zombie zombie -> zombie.shouldBurnInDay();
            case AbstractSkeleton skeleton -> skeleton.shouldBurnInDay();
            case Phantom phantom -> phantom.shouldBurnInDay();
            default -> false;
        };
    }

    public static void setShouldBurnDuringTheDay(LivingEntity entity, boolean value) {
        if (entity instanceof Zombie zombie) zombie.setShouldBurnInDay(value);
        else if (entity instanceof AbstractSkeleton skeleton) skeleton.setShouldBurnInDay(value);
        else if (entity instanceof Phantom phantom) phantom.setShouldBurnInDay(value);
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
        return switch (entity) {
            case PigZombie pigZombie -> pigZombie.isAngry();
            case Wolf wolf -> wolf.isAngry();
            case Warden warden -> warden.getAngerLevel() == Warden.AngerLevel.ANGRY;
            case Enderman enderman -> enderman.isScreaming();
            case null, default -> false;
        };
    }

    public static boolean isInterested(LivingEntity entity) {
        return switch (entity) {
            case Fox fox -> fox.isInterested();
            case Wolf wolf -> wolf.isInterested();
            default -> false;
        };
    }

    public static void setIsInterested(LivingEntity entity, boolean interested) {
        if (entity instanceof Fox fox) {
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
        } else if (entity instanceof Enderman enderman) {
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
        } else if (MINECRAFT_1_20_1) {
            if (sitting && entity.getPose() != Pose.SITTING) {
                entity.setPose(Pose.SITTING);
            } else if (entity.getPose() == Pose.SITTING) {
                entity.setPose(Pose.STANDING);
            }
        }
    }

    public static boolean isScreaming(LivingEntity entity) {
        return switch (entity) {
            case Goat goat -> goat.isScreaming();
            case Enderman enderman -> enderman.isScreaming();
            default -> false;
        };
    }

    public static void setIsScreaming(LivingEntity entity, boolean screaming) {
        if (entity instanceof Goat goat) {
            goat.setScreaming(screaming);
        } else if (entity instanceof Enderman enderman) {
            enderman.setScreaming(screaming);
        }
    }

    public static boolean isRoaring(LivingEntity entity) {
        return switch (entity) {
            case Ravager ravager -> ravager.getRoarTicks() > 0;
            case EnderDragon enderDragon -> enderDragon.getPhase() == EnderDragon.Phase.ROAR_BEFORE_ATTACK;
            default -> entity.getPose() == Pose.ROARING;
        };
    }

    public static boolean isConverting(LivingEntity entity) {
        return switch (entity) {
            case PiglinAbstract piglinAbstract -> piglinAbstract.isConverting();
            case Skeleton skeleton -> skeleton.isConverting();
            case Hoglin hoglin -> hoglin.isConverting();
            case Zombie zombie -> zombie.isConverting();
            case null, default -> false;
        };
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

    public static void setCowVariant(AbstractCow cow, Cow.Variant variant) {
        if (COW_CLASS.isAssignableFrom(cow.getClass())) {
            try {
                COW_SET_VARIANT_METHOD.invoke(cow, variant);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Cow Variant couldn't be obtained, please report this.", e);
            }
        }
    }

    public static Cow.Variant getCowVariant(AbstractCow cow) {
        if (COW_CLASS.isAssignableFrom(cow.getClass())) {
            try {
                return (Cow.Variant) COW_GET_VARIANT_METHOD.invoke(cow);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Cow Variant couldn't be obtained, please report this.", e);
            }
        }
        return null;
    }

    @Nullable
    public static Object getVariant(LivingEntity livingEntity) {
        return switch (livingEntity) {
            case Axolotl axolotl -> axolotl.getVariant();
            case Frog frog -> frog.getVariant();
            case MushroomCow mushroomCow -> mushroomCow.getVariant();
            case Parrot parrot -> parrot.getVariant();
            case Llama llama -> llama.getColor();
            case Fox fox -> fox.getFoxType();
            case Cat cat -> cat.getCatType();
            case Rabbit rabbit -> rabbit.getRabbitType();
            case Panda panda -> panda.getMainGene();
            case TropicalFish tropicalFish -> tropicalFish.getPattern();
            case Wolf wolf -> wolf.getVariant();
            case Salmon salmon when HAS_SALMON_VARIANT -> salmon.getVariant();
            case Pig pig when HAS_PIG_VARIANT -> pig.getVariant();
            case Chicken chicken when HAS_CHICKEN_VARIANT -> chicken.getVariant();
            case null -> null;
            default -> {
                if (HAS_COW_VARIANT && livingEntity instanceof AbstractCow abstractCow) {
                    yield getCowVariant(abstractCow);
                }
                yield null;
            }
        };
    }

    public static void setVariant(LivingEntity livingEntity, Object unknownVariant) {
        switch (livingEntity) {
            case MushroomCow mushroomCow when unknownVariant instanceof MushroomCow.Variant variant ->
                    mushroomCow.setVariant(variant);
            case Parrot parrot when unknownVariant instanceof Parrot.Variant variant -> parrot.setVariant(variant);
            case Llama llama when unknownVariant instanceof Llama.Color color -> llama.setColor(color);
            case Fox fox when unknownVariant instanceof Fox.Type type -> fox.setFoxType(type);
            case Cat cat when unknownVariant instanceof Cat.Type type -> cat.setCatType(type);
            case Rabbit rabbit when unknownVariant instanceof Rabbit.Type type -> rabbit.setRabbitType(type);
            case Panda panda when unknownVariant instanceof Panda.Gene gene -> panda.setMainGene(gene);
            case TropicalFish tropicalFish when unknownVariant instanceof TropicalFish.Pattern pattern ->
                    tropicalFish.setPattern(pattern);
            case Frog frog when unknownVariant instanceof Frog.Variant variant -> frog.setVariant(variant);
            case Wolf wolf when unknownVariant instanceof Wolf.Variant variant -> wolf.setVariant(variant);
            case Salmon salmon when HAS_SALMON_VARIANT && unknownVariant instanceof Salmon.Variant variant ->
                    salmon.setVariant(variant);
            case Axolotl axolotl when unknownVariant instanceof Axolotl.Variant variant -> axolotl.setVariant(variant);
            case Chicken chicken when HAS_CHICKEN_VARIANT && unknownVariant instanceof Chicken.Variant variant ->
                    chicken.setVariant(variant);
            case Pig pig when HAS_PIG_VARIANT && unknownVariant instanceof Pig.Variant variant ->
                    pig.setVariant(variant);
            default -> {
                if (HAS_COW_VARIANT && livingEntity instanceof AbstractCow abstractCow && unknownVariant instanceof Cow.Variant variant)
                    setCowVariant(abstractCow, variant);
            }
        }
    }

    @Nullable
    public static ItemStack getEntityEquipmentSlot(LivingEntity livingEntity, EquipmentSlot equipmentSlot) {
        if (livingEntity instanceof Player player) {
            return player.getInventory().getItem(equipmentSlot);
        } else if (livingEntity.canUseEquipmentSlot(equipmentSlot)) {
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
        } else if (livingEntity.canUseEquipmentSlot(equipmentSlot)) {
            EntityEquipment entityEquipment = livingEntity.getEquipment();
            if (entityEquipment != null) {
                entityEquipment.setItem(equipmentSlot, itemStack);
            }
        }
    }

    @Nullable
    public static EntityData<?> toEntityData(EntityType entityType) {
        return entityType == null ? null : ch.njol.skript.bukkitutil.EntityUtils.toSkriptEntityData(entityType);
    }

    @Nullable
    public static EntityType toEntityType(EntityData<?> entityType) {
        return entityType == null ? null : ch.njol.skript.bukkitutil.EntityUtils.toBukkitEntityType(entityType);
    }

    //todo: add can pickup items for pre 2.8
    //todo: add isImmuneToZombification for hoglins and piglins
}
