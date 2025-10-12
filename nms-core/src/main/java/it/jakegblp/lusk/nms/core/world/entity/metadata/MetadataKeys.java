package it.jakegblp.lusk.nms.core.world.entity.metadata;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import io.papermc.paper.entity.Bucketable;
import io.papermc.paper.math.Rotations;
import it.jakegblp.lusk.nms.core.world.entity.BitFlag;
import it.jakegblp.lusk.nms.core.world.entity.FlagByte;
import it.jakegblp.lusk.nms.core.world.entity.flags.abstracthorse.AbstractHorseFlag;
import it.jakegblp.lusk.nms.core.world.entity.flags.abstracthorse.AbstractHorseFlags;
import it.jakegblp.lusk.nms.core.world.entity.flags.armorstand.ArmorStandFlags;
import it.jakegblp.lusk.nms.core.world.entity.flags.armorstand.ArmorStandFlag;
import it.jakegblp.lusk.nms.core.world.entity.flags.bat.BatFlags;
import it.jakegblp.lusk.nms.core.world.entity.flags.bat.BatFlag;
import it.jakegblp.lusk.nms.core.world.entity.flags.bee.BeeFlags;
import it.jakegblp.lusk.nms.core.world.entity.flags.bee.BeeFlag;
import it.jakegblp.lusk.nms.core.world.entity.flags.blaze.BlazeFlag;
import it.jakegblp.lusk.nms.core.world.entity.flags.blaze.BlazeFlags;
import it.jakegblp.lusk.nms.core.world.entity.flags.entity.EntityFlag;
import it.jakegblp.lusk.nms.core.world.entity.flags.entity.EntityFlags;
import it.jakegblp.lusk.nms.core.world.entity.flags.fox.FoxFlags;
import it.jakegblp.lusk.nms.core.world.entity.flags.fox.FoxFlag;
import it.jakegblp.lusk.nms.core.world.entity.flags.irongolem.IronGolemFlag;
import it.jakegblp.lusk.nms.core.world.entity.flags.irongolem.IronGolemFlags;
import it.jakegblp.lusk.nms.core.world.entity.flags.livingentity.LivingEntityFlags;
import it.jakegblp.lusk.nms.core.world.entity.flags.livingentity.LivingEntityFlag;
import it.jakegblp.lusk.nms.core.world.entity.flags.mob.MobFlags;
import it.jakegblp.lusk.nms.core.world.entity.flags.mob.MobFlag;
import it.jakegblp.lusk.nms.core.world.entity.flags.panda.PandaFlags;
import it.jakegblp.lusk.nms.core.world.entity.flags.panda.PandaFlag;
import it.jakegblp.lusk.nms.core.world.entity.flags.player.PlayerFlags;
import it.jakegblp.lusk.nms.core.world.entity.flags.player.PlayerFlag;
import it.jakegblp.lusk.nms.core.world.entity.flags.projectile.arrow.AbstractArrowFlag;
import it.jakegblp.lusk.nms.core.world.entity.flags.projectile.arrow.AbstractArrowFlags;
import it.jakegblp.lusk.nms.core.world.entity.flags.sheep.SheepFlag;
import it.jakegblp.lusk.nms.core.world.entity.flags.sheep.SheepFlags;
import it.jakegblp.lusk.nms.core.world.entity.flags.snowgolem.SnowmanFlags;
import it.jakegblp.lusk.nms.core.world.entity.flags.snowgolem.SnowmanFlag;
import it.jakegblp.lusk.nms.core.world.entity.flags.spider.SpiderFlag;
import it.jakegblp.lusk.nms.core.world.entity.flags.spider.SpiderFlags;
import it.jakegblp.lusk.nms.core.world.entity.flags.tameable.TameableFlag;
import it.jakegblp.lusk.nms.core.world.entity.flags.tameable.TameableFlags;
import it.jakegblp.lusk.nms.core.world.entity.flags.textdisplay.TextDisplayFlag;
import it.jakegblp.lusk.nms.core.world.entity.flags.textdisplay.TextDisplayFlags;
import it.jakegblp.lusk.nms.core.world.entity.flags.vex.VexFlag;
import it.jakegblp.lusk.nms.core.world.entity.flags.vex.VexFlags;
import it.jakegblp.lusk.nms.core.world.entity.serialization.EntitySerializerKey;
import it.jakegblp.lusk.nms.core.world.entity.villager.VillagerData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Art;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;

import static it.jakegblp.lusk.common.ReflectionUtils.forClassName;
import static it.jakegblp.lusk.common.ReflectionUtils.forceInit;
import static it.jakegblp.lusk.nms.core.world.entity.serialization.EntitySerializerKey.Type.OPTIONAL;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MetadataKeys {
    public static final BiMap<@NotNull String, @NotNull MetadataKey> NAMED_KEYS = HashBiMap.create();
    public static final ListMultimap<@NotNull Class<? extends @NotNull Entity>, @NotNull MetadataKey<? extends @NotNull Object, ? extends @NotNull Object>> METADATA_ENTRIES = LinkedListMultimap.create();

    static {
        for (var keysInterface : new Class[]{
                EntityKeys.class,
                AreaEffectCloudKeys.class,
                EnderCrystalKeys.class,
                ExperienceOrbKeys.class,
                EnderEyeKeys.class,
                FallingBlockKeys.class,
                FireballKeys.class,
                FireworkKeys.class,
                InteractionKeys.class,
                ItemKeys.class,
                ItemFrameKeys.class,
                OminousItemSpawnerKeys.class,
                PaintingKeys.class,
                SmallFireballKeys.class,
                TNTPrimedKeys.class,
                WitherSkullKeys.class,
                FishHookKeys.class,
                AbstractArrowKeys.class,
                ArrowKeys.class,
                TridentKeys.class,
                DisplayKeys.class,
                BlockDisplayKeys.class,
                ItemDisplayKeys.class,
                TextDisplayKeys.class,
                LivingEntityKeys.class,
                ArmorStandKeys.class,
                PlayerKeys.class,
                MobKeys.class,
                BatKeys.class,
                EnderDragonKeys.class,
                GhastKeys.class,
                PhantomKeys.class,
                SlimeKeys.class,
                AllayKeys.class,
                IronGolemKeys.class,
                ShulkerKeys.class,
                SnowmanKeys.class,
                TadpoleKeys.class,
                AgeableKeys.class,
                GlowSquidKeys.class,
                ArmadilloKeys.class,
                AxolotlKeys.class,
                BeeKeys.class,
                ChickenKeys.class,
                CowKeys.class,
                FoxKeys.class,
                FrogKeys.class,
                HappyGhastKeys.class,
                GoatKeys.class,
                HoglinKeys.class,
                MushroomCowKeys.class,
                OcelotKeys.class,
                PandaKeys.class,
                PigKeys.class,
                PolarBearKeys.class,
                RabbitKeys.class,
                SheepKeys.class,
                SnifferKeys.class,
                StriderKeys.class,
                TurtleKeys.class,
                AbstractHorseKeys.class,
                CamelKeys.class,
                HorseKeys.class,
                ChestedHorseKeys.class,
                LlamaKeys.class,
                TameableKeys.class,
                CatKeys.class,
                ParrotKeys.class,
                WolfKeys.class,
                AbstractVillagerKeys.class,
                VillagerKeys.class,
                AbstractFishKeys.class,
                SalmonKeys.class,
                TropicalFishKeys.class,
                BlazeKeys.class,
                BoggedKeys.class,
                CreakingKeys.class,
                CreeperKeys.class,
                EndermanKeys.class,
                GuardianKeys.class,
                SkeletonKeys.class,
                SpiderKeys.class,
                VexKeys.class,
                WardenKeys.class,
                WitherKeys.class,
                ZoglinKeys.class,
                ZombieKeys.class,
                ZombieVillagerKeys.class,
                PiglinAbstractKeys.class,
                PiglinKeys.class,
                RaiderKeys.class,
                PillagerKeys.class,
                WitchKeys.class,
                SpellcasterKeys.class,
                ThrowableProjectileKeys.class,
                VehicleKeys.class,
                BoatKeys.class,
                MinecartKeys.class,
                CommandMinecartKeys.class,
                PoweredMinecartKeys.class}) {
            forceInit(keysInterface);
        }
    }


    public static <
            E extends Entity,
            B1 extends Enum<B1> & BitFlag<T1>,
            B2 extends Enum<B2> & BitFlag<T2>,
            F1 extends FlagByte<B1, E, T1>,
            F2 extends FlagByte<B2, E, T2>,
            T1,
            T2>
    MetadataBitFlagKey<E, B2, F2, T2> registerSemi(
            MetadataKey<E, F1> parentKey,
            B1 bitFlag
    ) {
        return (MetadataBitFlagKey<E, B2, F2, T2>) register(bitFlag.name(), new MetadataBitFlagKey<>(parentKey, bitFlag));
    }

    public static <
            E extends Entity,
            B extends Enum<B> & BitFlag<T>,
            F extends FlagByte<B, E, ?>,
            T>
    MetadataBitFlagKey<E, B, F, T> register(
            MetadataKey<E, F> parentKey,
            B bitFlag
    ) {
        return (MetadataBitFlagKey<E, B, F, T>) register(bitFlag.name(), new MetadataBitFlagKey<>(parentKey, bitFlag));
    }

    public static <E extends Entity, B extends Enum<B> & BitFlag<T>, F extends FlagByte<B, E, ?>, T> MetadataBitFlagKey<E, B, F, T> register(
            String name,
            MetadataKey<E, F> parentKey,
            B bitFlag
    ) {
        return (MetadataBitFlagKey<E, B, F, T>) register(name, new MetadataBitFlagKey<>(parentKey, bitFlag));
    }

    public static <E extends Entity, T> MetadataKey<E, T> register(
            String name,
            Class<E> entityClass,
            @Nullable Class<T> valueClass
    ) {
        return register(name, entityClass, valueClass, EntitySerializerKey.Type.NORMAL);
    }

    public static <E extends Entity, T> MetadataKey<E, T> register(
            String name,
            Class<E> entityClass,
            @Nullable Class<T> valueClass,
            @NotNull EntitySerializerKey.Type type
    ) {
        if (valueClass == null) return null;
        int nextId = getLastId(entityClass);
        return register(name, new MetadataKey<>(nextId, entityClass, valueClass, type));
    }

    public static <E extends Entity, T> MetadataKey<E, T> register(
            @NotNull String name,
            @NotNull MetadataKey<E, T> key
    ) {
        NAMED_KEYS.put(name, key);
        METADATA_ENTRIES.put(key.entityClass(), key);
        return key;
    }

    public static <E extends Entity, T> MetadataKey<E, T> register(
            String name,
            Class<E> entityClass,
            String valueClass
    ) {
        return register(name, entityClass, valueClass, EntitySerializerKey.Type.NORMAL);
    }

    @SuppressWarnings("unchecked")
    public static <E extends Entity, T> MetadataKey<E, T> register(
            String name,
            Class<E> entityClass,
            String stringClass,
            @NotNull EntitySerializerKey.Type type
    ) {
        return register(name, entityClass, (Class<T>) forClassName(stringClass), type);
    }

    public static <E extends Entity, T> MetadataKey<E, T> register(
            String name,
            String entityClass,
            Class<T> valueClass
    ) {
        return register(name, entityClass, valueClass, EntitySerializerKey.Type.NORMAL);
    }

    @SuppressWarnings("unchecked")
    public static <E extends Entity, T> MetadataKey<E, T> register(
            String name,
            String entityClass,
            Class<T> valueClass,
            @NotNull EntitySerializerKey.Type type
    ) {
        return register(name, (Class<E>) forClassName(entityClass), valueClass, type);
    }

    @SuppressWarnings("unchecked")
    public static <E extends Entity, T> MetadataKey<E, T> register(
            String name,
            String stringEntityClass,
            String stringValueClass,
            @NotNull EntitySerializerKey.Type type
    ) {
        return (MetadataKey<E, T>) register(name, (Class<? extends Entity>) forClassName(stringEntityClass), forClassName(stringValueClass), type);
    }

    public static <E extends Entity, T> MetadataKey<E, T> register(
            String name,
            String stringEntityClass,
            String stringValueClass
    ) {
        return register(name, stringEntityClass, stringValueClass, EntitySerializerKey.Type.NORMAL);
    }

    @SuppressWarnings("unchecked")
    private static int getLastId(Class<? extends Entity> entityClass) {
        Set<Class<?>> visited = new HashSet<>();
        Deque<Class<?>> toVisit = new ArrayDeque<>();
        toVisit.add(entityClass);
        int max = 0;
        while (!toVisit.isEmpty()) {
            Class<?> current = toVisit.pop();
            if (!visited.add(current)) continue;
            if (Entity.class.isAssignableFrom(current)) {
                var keys = METADATA_ENTRIES.get((Class<? extends Entity>) current);
                if (!keys.isEmpty()) {
                    int last = keys.get(keys.size() - 1).id() + 1;
                    if (last > max) max = last;
                }
            }
            Class<?> superclass = current.getSuperclass();
            if (superclass != null)
                toVisit.add(superclass);
            toVisit.addAll(Arrays.asList(current.getInterfaces()));
        }
        return max;
    }


    @ApiStatus.NonExtendable
    public interface EntityKeys {
        MetadataKey<Entity, EntityFlags> ENTITY_FLAGS = register("ENTITY_FLAGS", Entity.class, EntityFlags.class);
        MetadataKey<Entity, Boolean> BURNING = register(ENTITY_FLAGS, EntityFlag.BURNING);
        MetadataKey<Entity, Boolean> SNEAKING = register(ENTITY_FLAGS, EntityFlag.SNEAKING);
        MetadataKey<Entity, Boolean> SPRINTING = register(ENTITY_FLAGS, EntityFlag.SPRINTING);
        MetadataKey<Entity, Boolean> SWIMMING = register(ENTITY_FLAGS, EntityFlag.SWIMMING);
        MetadataKey<Entity, Boolean> INVISIBLE = register(ENTITY_FLAGS, EntityFlag.INVISIBLE);
        MetadataKey<Entity, Boolean> GLOWING = register(ENTITY_FLAGS, EntityFlag.GLOWING);
        MetadataKey<Entity, Boolean> GLIDING = register(ENTITY_FLAGS, EntityFlag.GLIDING);
        MetadataKey<Entity, Integer> AIR_TICKS = register("AIR_TICKS", Entity.class, Integer.class);
        MetadataKey<Entity, Component> CUSTOM_NAME = register("CUSTOM_NAME", Entity.class, Component.class, OPTIONAL);
        MetadataKey<Entity, Boolean> CUSTOM_NAME_VISIBILITY = register("CUSTOM_NAME_VISIBILITY", Entity.class, Boolean.class);
        MetadataKey<Entity, Boolean> SILENT = register("SILENT", Entity.class, Boolean.class);
        MetadataKey<Entity, Boolean> NO_GRAVITY = register("NO_GRAVITY", Entity.class, Boolean.class);
        MetadataKey<Entity, Pose> POSE = register("POSE", Entity.class, Pose.class);
        MetadataKey<Entity, Integer> TICKS_FROZEN = register("TICKS_FROZEN", Entity.class, Integer.class);
    }

    @ApiStatus.NonExtendable
    public interface AreaEffectCloudKeys {
        MetadataKey<AreaEffectCloud, Float> RADIUS = register("RADIUS", AreaEffectCloud.class, Float.class);
        MetadataKey<AreaEffectCloud, Boolean> SINGLE_POINT = register("SINGLE_POINT", AreaEffectCloud.class, Boolean.class);
        MetadataKey<AreaEffectCloud, Integer> PARTICLE = register("PARTICLE", AreaEffectCloud.class, Integer.class);
    }

    @ApiStatus.NonExtendable
    public interface EnderCrystalKeys {
        MetadataKey<EnderCrystal, BlockVector> BEAM_TARGET = register("BEAM_TARGET", EnderCrystal.class, BlockVector.class, OPTIONAL);
        MetadataKey<EnderCrystal, Boolean> SHOW_BOTTOM = register("SHOW_BOTTOM", EnderCrystal.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface ExperienceOrbKeys {
        MetadataKey<ExperienceOrb, Integer> EXPERIENCE_REWARD = register("EXPERIENCE_REWARD", ExperienceOrb.class, Integer.class);
    }

    @ApiStatus.NonExtendable
    public interface EnderEyeKeys {
        MetadataKey<EnderSignal, ItemStack> SIGNAL_ITEM = register("SIGNAL_ITEM", EnderSignal.class, ItemStack.class);
    }

    @ApiStatus.NonExtendable
    public interface FallingBlockKeys {
        MetadataKey<FallingBlock, BlockVector> SPAWN_POSITION = register("SPAWN_POSITION", FallingBlock.class, BlockVector.class);
    }

    @ApiStatus.NonExtendable
    public interface FireballKeys {
        MetadataKey<Fireball, ItemStack> FIREBALL_ITEM = register("FIREBALL_ITEM", Fireball.class, ItemStack.class);
    }

    @ApiStatus.NonExtendable
    public interface FireworkKeys {
        MetadataKey<Firework, ItemStack> FIREWORK_INFO = register("FIREWORK_INFO", Firework.class, ItemStack.class);
        MetadataKey<Firework, Integer> BOOSTER = register("BOOSTER", Firework.class, Integer.class, OPTIONAL);
        MetadataKey<Firework, Boolean> FROM_CROSSBOW = register("FROM_CROSSBOW", Firework.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface InteractionKeys {
        MetadataKey<Interaction, Float> WIDTH = register("WIDTH", "org.bukkit.entity.Interaction", Float.class);
        MetadataKey<Interaction, Float> HEIGHT = register("HEIGHT", "org.bukkit.entity.Interaction", Float.class);
        MetadataKey<Interaction, Boolean> RESPONSIVE = register("RESPONSIVE", "org.bukkit.entity.Interaction", Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface ItemKeys {
        MetadataKey<Item, ItemStack> ITEM = register("ITEM", Item.class, ItemStack.class);
    }

    @ApiStatus.NonExtendable
    public interface ItemFrameKeys {
        MetadataKey<ItemFrame, BlockFace> ITEM_FRAME_BLOCK_FACE = register("ITEM_FRAME_BLOCK_FACE", ItemFrame.class, BlockFace.class);
        MetadataKey<ItemFrame, ItemStack> FRAMED_ITEM = register("FRAMED_ITEM", ItemFrame.class, ItemStack.class);
        MetadataKey<ItemFrame, Integer> ROTATION = register("ROTATION", ItemFrame.class, Integer.class);
    }

    @ApiStatus.NonExtendable
    public interface OminousItemSpawnerKeys {
        MetadataKey<OminousItemSpawner, ItemStack> OMINOUS_SPAWNER_ITEM = register("OMINOUS_SPAWNER_ITEM", "org.bukkit.entity.OminousItemSpawner", ItemStack.class);
    }

    @ApiStatus.NonExtendable
    public interface PaintingKeys {
        MetadataKey<Painting, BlockFace> PAINTING_BLOCK_FACE = register("PAINTING_BLOCK_FACE", Painting.class, BlockFace.class);
        MetadataKey<Painting, Art> PAINTING_TYPE = register("PAINTING_TYPE", Painting.class, Art.class);
    }

    @ApiStatus.NonExtendable
    public interface SmallFireballKeys {
        MetadataKey<SmallFireball, ItemStack> SMALL_FIREBALL_ITEM = register("SMALL_FIREBALL_ITEM", SmallFireball.class, ItemStack.class);
    }

    @ApiStatus.NonExtendable
    public interface TNTPrimedKeys {
        MetadataKey<TNTPrimed, ItemStack> FUSE_TIME = register("FUSE_TIME", TNTPrimed.class, ItemStack.class);
        MetadataKey<TNTPrimed, BlockState> BLOCK_STATE_ID = register("BLOCK_STATE_ID", TNTPrimed.class, BlockState.class);
    }

    @ApiStatus.NonExtendable
    public interface WitherSkullKeys {
        MetadataKey<WitherSkull, Boolean> INVULNERABLE = register("INVULNERABLE", WitherSkull.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface FishHookKeys {
        MetadataKey<FishHook, Integer> HOOKED_ENTITY_ID = register("HOOKED_ENTITY_ID", FishHook.class, Integer.class);
        MetadataKey<FishHook, Boolean> CATCHABLE = register("CATCHABLE", FishHook.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface AbstractArrowKeys {
        MetadataKey<AbstractArrow, AbstractArrowFlags> ARROW_FLAGS = register("ARROW_FLAGS", AbstractArrow.class, AbstractArrowFlags.class);
        MetadataKey<AbstractArrow, Boolean> IS_CRITICAL = register(ARROW_FLAGS, AbstractArrowFlag.IS_CRITICAL);
        MetadataKey<AbstractArrow, Boolean> NO_CLIP = register(ARROW_FLAGS, AbstractArrowFlag.NO_CLIP);
        MetadataKey<AbstractArrow, Byte> PIERCING_LEVEL = register("PIERCING_LEVEL", AbstractArrow.class, Byte.class);
        MetadataKey<AbstractArrow, Boolean> IS_ON_GROUND = register("IS_ON_GROUND", AbstractArrow.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface ArrowKeys {
        MetadataKey<Arrow, Integer> ARROW_COLOR = register("ARROW_COLOR", Arrow.class, Integer.class);
    }

    @ApiStatus.NonExtendable
    public interface TridentKeys {
        MetadataKey<Trident, Byte> LOYALTY_LEVEL = register("LOYALTY_LEVEL", Trident.class, Byte.class);
        MetadataKey<Trident, Boolean> HAS_ENCHANTMENT_GLINT = register("HAS_ENCHANTMENT_GLINT", Trident.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface DisplayKeys {
        MetadataKey<Display, Integer> INTERPOLATION_DELAY = register("INTERPOLATION_DELAY", "org.bukkit.entity.Display", Integer.class);
        MetadataKey<Display, Integer> INTERPOLATION_DURATION = register("INTERPOLATION_DURATION", "org.bukkit.entity.Display", Integer.class);
        MetadataKey<Display, Integer> TELEPORT_INTERPOLATION_DURATION = register("TELEPORT_INTERPOLATION_DURATION", "org.bukkit.entity.Display", Integer.class);
        MetadataKey<Display, Vector3f> TRANSLATION = register("TRANSLATION", "org.bukkit.entity.Display", "org.joml.Vector3f");
        MetadataKey<Display, Vector3f> SCALE = register("SCALE", "org.bukkit.entity.Display", "org.joml.Vector3f");
        MetadataKey<Display, Quaternionf> ROTATION_LEFT = register("ROTATION_LEFT", "org.bukkit.entity.Display", "org.joml.Quaternionf");
        MetadataKey<Display, Quaternionf> ROTATION_RIGHT = register("ROTATION_RIGHT", "org.bukkit.entity.Display", "org.joml.Quaternionf");
        MetadataKey<Display, Display.Billboard> BILLBOARD = register("BILLBOARD", "org.bukkit.entity.Display", "org.bukkit.entity.Display$Billboard");
        MetadataKey<Display, Display.Brightness> BRIGHTNESS = register("BRIGHTNESS", "org.bukkit.entity.Display", "org.bukkit.entity.Display$Brightness");
        MetadataKey<Display, Float> VIEW_RANGE = register("VIEW_RANGE", "org.bukkit.entity.Display", Float.class);
        MetadataKey<Display, Float> SHADOW_RADIUS = register("SHADOW_RADIUS", "org.bukkit.entity.Display", Float.class);
        MetadataKey<Display, Float> SHADOW_STRENGTH = register("SHADOW_STRENGTH", "org.bukkit.entity.Display", Float.class);
        MetadataKey<Display, Float> DISPLAY_WIDTH = register("DISPLAY_WIDTH", "org.bukkit.entity.Display", Float.class);
        MetadataKey<Display, Float> DISPLAY_HEIGHT = register("DISPLAY_HEIGHT", "org.bukkit.entity.Display", Float.class);
        MetadataKey<Display, Float> GLOW_COLOR = register("GLOW_COLOR", "org.bukkit.entity.Display", Float.class);
    }

    @ApiStatus.NonExtendable
    public interface BlockDisplayKeys {
        MetadataKey<BlockDisplay, BlockState> DISPLAY_BLOCK = register("BLOCK_STATE", "org.bukkit.entity.BlockDisplay", BlockState.class);
    }

    @ApiStatus.NonExtendable
    public interface ItemDisplayKeys {
        MetadataKey<ItemDisplay, ItemStack> DISPLAY_ITEM = register("DISPLAY_ITEM", "org.bukkit.entity.ItemDisplay", ItemStack.class);
        MetadataKey<ItemDisplay, ItemDisplay.ItemDisplayTransform> TRANSFORM = register("TRANSFORM", "org.bukkit.entity.ItemDisplay", "org.bukkit.entity.ItemDisplay$ItemDisplayTransform");
    }

    @ApiStatus.NonExtendable
    public interface TextDisplayKeys {
        MetadataKey<TextDisplay, Component> TEXT = register("TEXT", "org.bukkit.entity.TextDisplay", Component.class);
        MetadataKey<TextDisplay, Integer> LINE_WIDTH = register("LINE_WIDTH", "org.bukkit.entity.TextDisplay", Integer.class);
        MetadataKey<TextDisplay, Integer> BACKGROUND_COLOR = register("BACKGROUND_COLOR", "org.bukkit.entity.TextDisplay", Integer.class);
        MetadataKey<TextDisplay, Byte> TEXT_OPACITY = register("TEXT_OPACITY", "org.bukkit.entity.TextDisplay", Byte.class);
        MetadataKey<TextDisplay, TextDisplayFlags> TEXT_FLAGS = register("TEXT_FLAGS", "org.bukkit.entity.TextDisplay", TextDisplayFlags.class);
        MetadataKey<TextDisplay, Boolean> HAS_SHADOW = registerSemi(TEXT_FLAGS, TextDisplayFlag.HAS_SHADOW);
        MetadataKey<TextDisplay, Boolean> SEE_THROUGH = registerSemi(TEXT_FLAGS, TextDisplayFlag.SEE_THROUGH);
        MetadataKey<TextDisplay, Boolean> DEFAULT_BACKGROUND = registerSemi(TEXT_FLAGS, TextDisplayFlag.DEFAULT_BACKGROUND);
        MetadataKey<TextDisplay, TextDisplay.TextAlignment> ALIGNMENT = registerSemi(TEXT_FLAGS, TextDisplayFlag.ALIGNMENT);
    }

    @ApiStatus.NonExtendable
    public interface LivingEntityKeys {
        MetadataKey<LivingEntity, LivingEntityFlags> LIVING_ENTITY_FLAGS = register("LIVING_ENTITY_FLAGS", LivingEntity.class, LivingEntityFlags.class);
        MetadataKey<LivingEntity, Boolean> HAND_ACTIVE = registerSemi(LIVING_ENTITY_FLAGS, LivingEntityFlag.HAND_ACTIVE);
        MetadataKey<LivingEntity, EquipmentSlot> HAND = registerSemi(LIVING_ENTITY_FLAGS, LivingEntityFlag.HAND);
        MetadataKey<LivingEntity, Boolean> RIPTIDE_ATTACK = registerSemi(LIVING_ENTITY_FLAGS, LivingEntityFlag.RIPTIDE_ATTACK);
        MetadataKey<LivingEntity, Float> HEALTH = register("HEALTH", LivingEntity.class, Float.class);
        MetadataKey<LivingEntity, Integer> POTION_EFFECT_COLOR = register("POTION_EFFECT_COLOR", LivingEntity.class, Integer.class);
        MetadataKey<LivingEntity, Boolean> POTION_EFFECT_AMBIENT = register("POTION_EFFECT_AMBIENT", LivingEntity.class, Boolean.class);
        MetadataKey<LivingEntity, Integer> ARROW_COUNT = register("ARROW_COUNT", LivingEntity.class, Integer.class);
        MetadataKey<LivingEntity, Integer> BEE_STINGER_COUNT = register("BEE_STINGER_COUNT", LivingEntity.class, Integer.class);
        MetadataKey<LivingEntity, BlockVector> SLEEPING_BED_LOCATION = register("SLEEPING_BED_LOCATION", LivingEntity.class, BlockVector.class, OPTIONAL);
    }

    @ApiStatus.NonExtendable
    public interface ArmorStandKeys {
        MetadataKey<ArmorStand, ArmorStandFlags> ARMOR_STAND_FLAGS = register("ARMOR_STAND_FLAGS", ArmorStand.class, ArmorStandFlags.class);
        MetadataKey<ArmorStand, Boolean> IS_SMALL = register(ARMOR_STAND_FLAGS, ArmorStandFlag.IS_SMALL);
        MetadataKey<ArmorStand, Boolean> HAS_ARMS = register(ARMOR_STAND_FLAGS, ArmorStandFlag.HAS_ARMS);
        MetadataKey<ArmorStand, Boolean> HAS_NO_BASEPLATE = register(ARMOR_STAND_FLAGS, ArmorStandFlag.HAS_NO_BASEPLATE);
        MetadataKey<ArmorStand, Boolean> IS_MARKER = register(ARMOR_STAND_FLAGS, ArmorStandFlag.IS_MARKER);
        MetadataKey<ArmorStand, Rotations> HEAD_ROTATION = register("HEAD_ROTATION", ArmorStand.class, Rotations.class);
        MetadataKey<ArmorStand, Rotations> BODY_ROTATION = register("BODY_ROTATION", ArmorStand.class, Rotations.class);
        MetadataKey<ArmorStand, Rotations> LEFT_ARM_ROTATION = register("LEFT_ARM_ROTATION", ArmorStand.class, Rotations.class);
        MetadataKey<ArmorStand, Rotations> RIGHT_ARM_ROTATION = register("RIGHT_ARM_ROTATION", ArmorStand.class, Rotations.class);
        MetadataKey<ArmorStand, Rotations> LEFT_LEG_ROTATION = register("LEFT_LEG_ROTATION", ArmorStand.class, Rotations.class);
        MetadataKey<ArmorStand, Rotations> RIGHT_LEG_ROTATION = register("RIGHT_LEG_ROTATION", ArmorStand.class, Rotations.class);
    }

    @ApiStatus.NonExtendable
    public interface PlayerKeys {
        MetadataKey<Player, Float> ADDITIONAL_HEARTS = register("ADDITIONAL_HEARTS", Player.class, Float.class);
        MetadataKey<Player, Integer> SCORE = register("SCORE", Player.class, Integer.class);
        MetadataKey<Player, PlayerFlags> PLAYER_FLAGS = register("PLAYER_FLAGS", Player.class, PlayerFlags.class);
        MetadataKey<Player, Boolean> CAPE = register(PLAYER_FLAGS, PlayerFlag.CAPE);
        MetadataKey<Player, Boolean> JACKET = register(PLAYER_FLAGS, PlayerFlag.JACKET);
        MetadataKey<Player, Boolean> LEFT_SLEEVE = register(PLAYER_FLAGS, PlayerFlag.LEFT_SLEEVE);
        MetadataKey<Player, Boolean> RIGHT_SLEEVE = register(PLAYER_FLAGS, PlayerFlag.RIGHT_SLEEVE);
        MetadataKey<Player, Boolean> LEFT_PANTS = register(PLAYER_FLAGS, PlayerFlag.LEFT_PANTS);
        MetadataKey<Player, Boolean> RIGHT_PANTS = register(PLAYER_FLAGS, PlayerFlag.RIGHT_PANTS);
        MetadataKey<Player, Boolean> HAT = register(PLAYER_FLAGS, PlayerFlag.HAT);
        MetadataKey<Player, Byte> MAIN_HAND = register("MAIN_HAND", Player.class, Byte.class);
        // TODO: implement nbt
        MetadataKey<Player, Object> LEFT_SHOULDER_DATA = register("LEFT_SHOULDER_DATA", Player.class, Object.class);
        MetadataKey<Player, Object> RIGHT_SHOULDER_DATA = register("RIGHT_SHOULDER_DATA", Player.class, Object.class);
    }

    @ApiStatus.NonExtendable
    public interface MobKeys {
        MetadataKey<Mob, MobFlags> MOB_FLAGS = register("MOB_FLAGS", Mob.class, MobFlags.class);
        MetadataKey<Mob, Boolean> NO_AI = register(MOB_FLAGS, MobFlag.NO_AI);
        MetadataKey<Mob, Boolean> IS_LEFT_HANDED = register(MOB_FLAGS, MobFlag.IS_LEFT_HANDED);
        MetadataKey<Mob, Boolean> IS_AGGRESSIVE = register(MOB_FLAGS, MobFlag.IS_AGGRESSIVE);
    }

    @ApiStatus.NonExtendable
    public interface BatKeys {
        MetadataKey<Bat, BatFlags> BAT_FLAGS = register("BAT_FLAGS", Bat.class, BatFlags.class);
        MetadataKey<Bat, Boolean> IS_HANGING = register(BAT_FLAGS, BatFlag.IS_HANGING);
    }

    @ApiStatus.NonExtendable
    public interface EnderDragonKeys {
        MetadataKey<EnderDragon, EnderDragon.Phase> DRAGON_PHASE = register("DRAGON_PHASE", EnderDragon.class, EnderDragon.Phase.class);
    }

    @ApiStatus.NonExtendable
    public interface GhastKeys {
        MetadataKey<Ghast, Boolean> ATTACKING = register("ATTACKING", Ghast.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface PhantomKeys {
        MetadataKey<Phantom, Integer> PHANTOM_SIZE = register("PHANTOM_SIZE", Phantom.class, Integer.class);
    }

    @ApiStatus.NonExtendable
    public interface SlimeKeys {
        MetadataKey<Slime, Integer> SLIME_SIZE = register("SLIME_SIZE", Slime.class, Integer.class);
    }

    @ApiStatus.NonExtendable
    public interface AllayKeys {
        MetadataKey<Allay, Boolean> DANCING = register("DANCING", Allay.class, Boolean.class);
        MetadataKey<Allay, Boolean> CAN_DUPLICATE = register("CAN_DUPLICATE", Allay.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface IronGolemKeys {
        MetadataKey<IronGolem, IronGolemFlags> IRON_GOLEM_FLAGS = register("IRON_GOLEM_FLAGS", IronGolem.class, IronGolemFlags.class);
        MetadataKey<IronGolem, Boolean> IS_PLAYER_CREATED = register(IRON_GOLEM_FLAGS, IronGolemFlag.IS_PLAYER_CREATED);
    }

    @ApiStatus.NonExtendable
    public interface ShulkerKeys {
        MetadataKey<Shulker, BlockFace> ATTACHED_FACE = register("ATTACHED_FACE", Shulker.class, BlockFace.class);
        MetadataKey<Shulker, Byte> SHIELD_HEIGHT = register("SHIELD_HEIGHT", Shulker.class, Byte.class);
        MetadataKey<Shulker, DyeColor> SHULKER_COLOR = register("SHULKER_COLOR", Shulker.class, DyeColor.class);
    }

    @ApiStatus.NonExtendable
    public interface SnowmanKeys {
        MetadataKey<Snowman, SnowmanFlags> SNOWMAN_FLAGS = register("SNOWMAN_FLAGS", Snowman.class, SnowmanFlags.class);
        MetadataKey<Snowman, Boolean> HAS_PUMPKIN = register(SNOWMAN_FLAGS, SnowmanFlag.HAS_PUMPKIN);
    }

    @ApiStatus.NonExtendable
    public interface TadpoleKeys {
        MetadataKey<Tadpole, Boolean> TADPOLE_IS_FROM_BUCKET = register("TADPOLE_IS_FROM_BUCKET", Tadpole.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface AgeableKeys {
        MetadataKey<Ageable, Boolean> IS_BABY_ANIMAL = register("IS_BABY_ANIMAL", Ageable.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface GlowSquidKeys {
        MetadataKey<GlowSquid, Integer> DARK_TICKS_REMAINING = register("DARK_TICKS_REMAINING", GlowSquid.class, Integer.class);
    }

    @ApiStatus.NonExtendable
    public interface ArmadilloKeys {
        MetadataKey<Armadillo, Armadillo.State> ARMADILLO_STATE = register("ARMADILLO_STATE", "org.bukkit.entity.Armadillo", "org.bukkit.entity.Armadillo$State");
    }

    @ApiStatus.NonExtendable
    public interface AxolotlKeys {
        MetadataKey<Axolotl, Axolotl.Variant> AXOLOTL_VARIANT = register("AXOLOTL_VARIANT", Axolotl.class, Axolotl.Variant.class);
        MetadataKey<Axolotl, Boolean> IS_PLAYING_DEAD = register("IS_PLAYING_DEAD", Axolotl.class, Boolean.class);
        MetadataKey<Axolotl, Boolean> IS_FROM_BUCKET = register("IS_FROM_BUCKET", Axolotl.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface BeeKeys {
        MetadataKey<Bee, BeeFlags> BEE_FLAGS = register("BEE_FLAGS", Bee.class, BeeFlags.class);
        MetadataKey<Bee, Boolean> IS_ANGRY = register(BEE_FLAGS, BeeFlag.IS_ANGRY);
        MetadataKey<Bee, Boolean> HAS_STUNG = register(BEE_FLAGS, BeeFlag.HAS_STUNG);
        MetadataKey<Bee, Boolean> HAS_NECTAR = register(BEE_FLAGS, BeeFlag.HAS_NECTAR);
        MetadataKey<Bee, Integer> BEE_ANGER_TICKS = register("BEE_ANGER_TICKS", Bee.class, Integer.class);
    }

    @ApiStatus.NonExtendable
    public interface ChickenKeys {
        MetadataKey<Chicken, Chicken.Variant> CHICKEN_VARIANT = register("CHICKEN_VARIANT", Chicken.class, "org.bukkit.entity.Chicken$Variant");
    }

    @ApiStatus.NonExtendable
    public interface CowKeys {
        MetadataKey<Cow, Cow.Variant> COW_VARIANT = register("COW_VARIANT", Cow.class, "org.bukkit.entity.Cow$Variant");
    }

    @ApiStatus.NonExtendable
    public interface FoxKeys {
        MetadataKey<Fox, Fox.Type> FOX_TYPE = register("FOX_TYPE", Fox.class, Fox.Type.class);
        MetadataKey<Fox, FoxFlags> FOX_FLAGS = register("FOX_FLAGS", Fox.class, FoxFlags.class);
        MetadataKey<Fox, Boolean> IS_SITTING = register("IS_FOX_SITTING", FOX_FLAGS, FoxFlag.IS_SITTING);
        MetadataKey<Fox, Boolean> IS_CROUCHING = register(FOX_FLAGS, FoxFlag.IS_CROUCHING);
        MetadataKey<Fox, Boolean> IS_INTERESTED = register("IS_FOX_INTERESTED", FOX_FLAGS, FoxFlag.IS_INTERESTED);
        MetadataKey<Fox, Boolean> IS_POUNCING = register(FOX_FLAGS, FoxFlag.IS_POUNCING);
        MetadataKey<Fox, Boolean> IS_SLEEPING = register(FOX_FLAGS, FoxFlag.IS_SLEEPING);
        MetadataKey<Fox, Boolean> IS_FACEPLANTED = register(FOX_FLAGS, FoxFlag.IS_FACEPLANTED);
        MetadataKey<Fox, Boolean> IS_DEFENDING = register(FOX_FLAGS, FoxFlag.IS_DEFENDING);
        MetadataKey<Fox, UUID> FIRST_TRUSTED_PLAYER = register("FIRST_TRUSTED_PLAYER", Fox.class, UUID.class);
        MetadataKey<Fox, UUID> SECOND_TRUSTED_PLAYER = register("SECOND_TRUSTED_PLAYER", Fox.class, UUID.class);
    }

    @ApiStatus.NonExtendable
    public interface FrogKeys {
        MetadataKey<Frog, Frog.Variant> FROG_VARIANT = register("FROG_VARIANT", "org.bukkit.entity.Frog", "org.bukkit.entity.Frog$Variant");
        MetadataKey<Frog, Integer> TONGUE_TARGET = register("TONGUE_TARGET", "org.bukkit.entity.Frog", Integer.class, OPTIONAL);
    }

    @ApiStatus.NonExtendable
    public interface HappyGhastKeys {
        MetadataKey<HappyGhast, Boolean> IS_LEASH_HOLDER = register("IS_LEASH_HOLDER", "org.bukkit.entity.HappyGhast", Boolean.class);
        MetadataKey<HappyGhast, Boolean> STAYS_STILL = register("STAYS_STILL", "org.bukkit.entity.HappyGhast", Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface GoatKeys {
        MetadataKey<Goat, Boolean> IS_SCREAMING_GOAT = register("IS_SCREAMING_GOAT", Goat.class, Boolean.class);
        MetadataKey<Goat, Boolean> HAS_LEFT_HORN = register("HAS_LEFT_HORN", Goat.class, Boolean.class);
        MetadataKey<Goat, Boolean> HAS_RIGHT_HORN = register("HAS_RIGHT_HORN", Goat.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface HoglinKeys {
        MetadataKey<Hoglin, Boolean> ZOMBIFICATION_IMMUNITY = register("ZOMBIFICATION_IMMUNITY", Hoglin.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface MushroomCowKeys {
        MetadataKey<MushroomCow, MushroomCow.Variant> MUSHROOM_COW_VARIANT = register("MUSHROOM_COW_VARIANT", MushroomCow.class, MushroomCow.Variant.class);
    }

    @ApiStatus.NonExtendable
    public interface OcelotKeys {
        MetadataKey<Ocelot, Boolean> IS_TRUSTING = register("IS_TRUSTING", Ocelot.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface PandaKeys {
        MetadataKey<Panda, Integer> BREED_TIMER = register("BREED_TIMER", Panda.class, Integer.class);
        MetadataKey<Panda, Integer> SNEEZE_TIMER = register("SNEEZE_TIMER", Panda.class, Integer.class);
        MetadataKey<Panda, Integer> EAT_TIMER = register("EAT_TIMER", Panda.class, Integer.class);
        MetadataKey<Panda, Panda.Gene> MAIN_GENE = register("MAIN_GENE", Panda.class, Panda.Gene.class);
        MetadataKey<Panda, Panda.Gene> HIDDEN_GENE = register("HIDDEN_GENE", Panda.class, Panda.Gene.class);
        MetadataKey<Panda, PandaFlags> PANDA_FLAGS = register("PANDA_FLAGS", Panda.class, PandaFlags.class);
        MetadataKey<Panda, Boolean> IS_SNEEZING = register(PANDA_FLAGS, PandaFlag.IS_SNEEZING);
        MetadataKey<Panda, Boolean> IS_ROLLING = register(PANDA_FLAGS, PandaFlag.IS_ROLLING);
        MetadataKey<Panda, Boolean> IS_PANDA_SITTING = register("IS_PANDA_SITTING", PANDA_FLAGS, PandaFlag.IS_SITTING);
        MetadataKey<Panda, Boolean> IS_ON_BACK = register(PANDA_FLAGS, PandaFlag.IS_ON_BACK);
    }

    @ApiStatus.NonExtendable
    public interface PigKeys {
        MetadataKey<Pig, Integer> CARROT_BOOST_TICKS = register("CARROT_BOOST_TICKS", Pig.class, Integer.class);
        MetadataKey<Pig, Pig.Variant> PIG_VARIANT = register("PIG_VARIANT", Pig.class, "org.bukkit.entity.Pig$Variant");
    }

    @ApiStatus.NonExtendable
    public interface PolarBearKeys {
        MetadataKey<PolarBear, Boolean> IS_STANDING_UP = register("IS_STANDING_UP", PolarBear.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface RabbitKeys {
        MetadataKey<Rabbit, Rabbit.Type> RABBIT_TYPE = register("RABBIT_TYPE", Rabbit.class, Rabbit.Type.class);
    }

    @ApiStatus.NonExtendable
    public interface SheepKeys {
        MetadataKey<Sheep, SheepFlags> SHEEP_FLAGS = register("SHEEP_FLAGS", Sheep.class, SheepFlags.class);
        MetadataKey<Sheep, Boolean> IS_SHEARED = registerSemi(SHEEP_FLAGS, SheepFlag.IS_SHEARED);
        MetadataKey<Sheep, DyeColor> WOOL_COLOR = registerSemi(SHEEP_FLAGS, SheepFlag.WOOL_COLOR);
    }

    @ApiStatus.NonExtendable
    public interface SnifferKeys {
        MetadataKey<Sniffer, Sniffer.State> SNIFFER_STATE = register("SNIFFER_STATE", "org.bukkit.entity.Sniffer", "org.bukkit.entity.Sniffer$State");
        MetadataKey<Sniffer, Integer> DROP_SEED_TICK = register("DROP_SEED_TICK", "org.bukkit.entity.Sniffer", Integer.class);
    }

    @ApiStatus.NonExtendable
    public interface StriderKeys {
        MetadataKey<Strider, Integer> FUNGUS_BOOST_TICKS = register("FUNGUS_BOOST_TICKS", Strider.class, Integer.class);
        MetadataKey<Strider, Boolean> IS_SHAKING = register("IS_SHAKING", Strider.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface TurtleKeys {
        MetadataKey<Turtle, Boolean> HAS_EGG = register("HAS_EGG", Turtle.class, Boolean.class);
        MetadataKey<Turtle, Boolean> IS_LAYING_EGG = register("IS_LAYING_EGG", Turtle.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface AbstractHorseKeys {
        MetadataKey<AbstractHorse, AbstractHorseFlags> HORSE_FLAGS = register("HORSE_FLAGS", AbstractHorse.class, AbstractHorseFlags.class);
        MetadataKey<AbstractHorse, Boolean> IS_TAME = register(HORSE_FLAGS, AbstractHorseFlag.IS_TAME);
        MetadataKey<AbstractHorse, Boolean> HAS_BRED = register(HORSE_FLAGS, AbstractHorseFlag.HAS_BRED);
        MetadataKey<AbstractHorse, Boolean> IS_EATING = register(HORSE_FLAGS, AbstractHorseFlag.IS_EATING);
        MetadataKey<AbstractHorse, Boolean> IS_REARING = register(HORSE_FLAGS, AbstractHorseFlag.IS_REARING);
        MetadataKey<AbstractHorse, Boolean> HAS_MOUTH_OPEN = register(HORSE_FLAGS, AbstractHorseFlag.HAS_MOUTH_OPEN);
    }

    @ApiStatus.NonExtendable
    public interface CamelKeys {
        MetadataKey<Camel, Boolean> IS_DASHING = register("IS_DASHING", "org.bukkit.entity.Camel", Boolean.class);
        MetadataKey<Camel, Long> LAST_POSE_CHANGE_TICK = register("LAST_POSE_CHANGE_TICK", "org.bukkit.entity.Camel", Long.class);
    }

    @ApiStatus.NonExtendable
    public interface HorseKeys {
        // todo: handle 2 in 1
        MetadataKey<Horse, Integer> HORSE_VARIANT = register("HORSE_VARIANT", Horse.class, Integer.class);
    }

    @ApiStatus.NonExtendable
    public interface ChestedHorseKeys {
        MetadataKey<ChestedHorse, Boolean> HAS_CHEST = register("HAS_CHEST", ChestedHorse.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface LlamaKeys {
        MetadataKey<Llama, Integer> LLAMA_STRENGTH = register("LLAMA_STRENGTH", Llama.class, Integer.class);
        MetadataKey<Llama, Llama.Color> LLAMA_VARIANT = register("LLAMA_VARIANT", Llama.class, Llama.Color.class);
    }

    @ApiStatus.NonExtendable
    public interface TameableKeys {
        MetadataKey<Tameable, TameableFlags> TAMEABLE_FLAGS = register("TAMEABLE_FLAGS", Tameable.class, TameableFlags.class);
        MetadataKey<Tameable, Boolean> IS_SITTING = register(TAMEABLE_FLAGS, TameableFlag.IS_SITTING);
        MetadataKey<Tameable, Boolean> IS_TAMED = register(TAMEABLE_FLAGS, TameableFlag.IS_TAMED);
        MetadataKey<Tameable, UUID> OWNER = register("OWNER", Tameable.class, UUID.class);
    }

    @ApiStatus.NonExtendable
    public interface CatKeys {
        MetadataKey<Cat, Cat.Type> CAT_VARIANT = register("CAT_VARIANT", Cat.class, Cat.Type.class);
        MetadataKey<Cat, Boolean> IS_LYING = register("IS_LYING", Cat.class, Boolean.class);
        MetadataKey<Cat, Boolean> IS_LOOKING_UP = register("IS_LOOKING_UP", Cat.class, Boolean.class);
        MetadataKey<Cat, DyeColor> CAT_COLLAR_COLOR = register("CAT_COLLAR_COLOR", Cat.class, DyeColor.class);
    }

    @ApiStatus.NonExtendable
    public interface ParrotKeys {
        MetadataKey<Parrot, Parrot.Variant> PARROT_VARIANT = register("PARROT_VARIANT", Parrot.class, Parrot.Variant.class);
    }

    @ApiStatus.NonExtendable
    public interface WolfKeys {
        MetadataKey<Wolf, Boolean> IS_INTERESTED = register("IS_WOLF_INTERESTED", Wolf.class, Boolean.class);
        MetadataKey<Wolf, DyeColor> WOLF_COLLAR_COLOR = register("WOLF_COLLAR_COLOR", Wolf.class, DyeColor.class);
        MetadataKey<Wolf, Integer> WOLF_ANGER_TICKS = register("WOLF_ANGER_TICKS", Wolf.class, Integer.class);
        MetadataKey<Wolf, Wolf.Variant> WOLF_VARIANT = register("WOLF_VARIANT", Wolf.class, Wolf.Variant.class);
        MetadataKey<Wolf, Wolf.SoundVariant> WOLF_SOUND_VARIANT = register("WOLF_SOUND_VARIANT", Wolf.class, Wolf.SoundVariant.class);
    }

    @ApiStatus.NonExtendable
    public interface AbstractVillagerKeys {
        MetadataKey<AbstractVillager, Integer> HEAD_SHAKE_TICKS = register("HEAD_SHAKE_TICKS", AbstractVillager.class, Integer.class);
    }

    @ApiStatus.NonExtendable
    public interface VillagerKeys {
        MetadataKey<Villager, VillagerData> VILLAGER_DATA = register("VILLAGER_DATA", Villager.class, VillagerData.class);
    }

    @ApiStatus.NonExtendable
    public interface AbstractFishKeys {
        MetadataKey<Bucketable, Boolean> FROM_BUCKET = register("FROM_BUCKET", Bucketable.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface SalmonKeys {
        MetadataKey<Salmon, Salmon.Variant> SALMON_VARIANT = register("SALMON_VARIANT", Salmon.class, "org.bukkit.entity.Salmon$Variant");
    }

    @ApiStatus.NonExtendable
    public interface TropicalFishKeys {
        MetadataKey<TropicalFish, TropicalFish.Pattern> TROPICAL_FISH_VARIANT = register("TROPICAL_FISH_VARIANT", TropicalFish.class, TropicalFish.Pattern.class);
    }

    @ApiStatus.NonExtendable
    public interface BlazeKeys {
        MetadataKey<Blaze, BlazeFlags> BLAZE_FLAGS = register("BLAZE_FLAGS", Blaze.class, BlazeFlags.class);
        MetadataKey<Blaze, Boolean> ON_FIRE = register(BLAZE_FLAGS, BlazeFlag.ON_FIRE);
    }

    @ApiStatus.NonExtendable
    public interface BoggedKeys {
        MetadataKey<Bogged, Boolean> IS_BOGGED_SHEARED = register("IS_BOGGED_SHEARED", "org.bukkit.entity.Bogged", Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface CreakingKeys {
        MetadataKey<Creaking, Boolean> CAN_MOVE = register("CAN_MOVE", "org.bukkit.entity.Creaking", Boolean.class);
        MetadataKey<Creaking, Boolean> IS_ACTIVE = register("IS_ACTIVE", "org.bukkit.entity.Creaking", Boolean.class);
        MetadataKey<Creaking, Boolean> IS_TEARING_DOWN = register("IS_TEARING_DOWN", "org.bukkit.entity.Creaking", Boolean.class);
        MetadataKey<Creaking, Location> HOME_POSITION = register("HOME_POSITION", "org.bukkit.entity.Creaking", Location.class, OPTIONAL);
    }

    @ApiStatus.NonExtendable
    public interface CreeperKeys {
        // todo: simplify
        MetadataKey<Creeper, Integer> STATE = register("STATE", Creeper.class, Integer.class);
        MetadataKey<Creeper, Boolean> IS_CHARGED = register("IS_CHARGED", Creeper.class, Boolean.class);
        MetadataKey<Creeper, Boolean> IS_IGNITED = register("IS_IGNITED", Creeper.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface EndermanKeys {
        MetadataKey<Enderman, BlockState> CARRIED_BLOCK = register("CARRIED_BLOCK", Enderman.class, BlockState.class);
        MetadataKey<Enderman, Boolean> IS_SCREAMING = register("IS_SCREAMING", Enderman.class, Boolean.class);
        MetadataKey<Enderman, Boolean> IS_STARING = register("IS_STARING", Enderman.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface GuardianKeys {
        MetadataKey<Guardian, Boolean> IS_RETRACTING_SPIKES = register("IS_RETRACTING_SPIKES", Guardian.class, Boolean.class);
        // todo: allow normal entities (converter-ish behavior)
        MetadataKey<Guardian, Integer> TARGET_ENTITY_ID = register("TARGET_ENTITY_ID", Guardian.class, Integer.class);
    }

    @ApiStatus.NonExtendable
    public interface SkeletonKeys {
        MetadataKey<Skeleton, Boolean> IS_CONVERTING_INTO_STRAY = register("IS_CONVERTING_INTO_STRAY", Skeleton.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface SpiderKeys {
        MetadataKey<Spider, SpiderFlags> SPIDER_FLAGS = register("SPIDER_FLAGS", Spider.class, SpiderFlags.class);
        MetadataKey<Spider, Boolean> IS_CLIMBING = register(SPIDER_FLAGS, SpiderFlag.IS_CLIMBING);
    }

    @ApiStatus.NonExtendable
    public interface VexKeys {
        MetadataKey<Vex, VexFlags> VEX_FLAGS = register("VEX_FLAGS", Vex.class, VexFlags.class);
        MetadataKey<Vex, Boolean> IS_ATTACKING = register(VEX_FLAGS, VexFlag.IS_ATTACKING);
    }

    @ApiStatus.NonExtendable
    public interface WardenKeys {
        MetadataKey<Warden, Integer> ANGER_LEVEL = register("ANGER_LEVEL", "org.bukkit.entity.Warden", Integer.class);
    }

    @ApiStatus.NonExtendable
    public interface WitherKeys {
        MetadataKey<Wither, Integer> CENTER_HEAD_TARGET = register("CENTER_HEAD_TARGET", Wither.class, Integer.class);
        MetadataKey<Wither, Integer> LEFT_HEAD_TARGET = register("LEFT_HEAD_TARGET", Wither.class, Integer.class);
        MetadataKey<Wither, Integer> RIGHT_HEAD_TARGET = register("RIGHT_HEAD_TARGET", Wither.class, Integer.class);
        MetadataKey<Wither, Integer> INVULNERABILITY_TICKS = register("INVULNERABILITY_TICKS", Wither.class, Integer.class);
    }

    @ApiStatus.NonExtendable
    public interface ZoglinKeys {
        MetadataKey<Zoglin, Boolean> IS_BABY_ZOGLIN = register("IS_BABY_ZOGLIN", Zoglin.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface ZombieKeys {
        MetadataKey<Zombie, Boolean> IS_BABY_ZOMBIE = register("IS_BABY_ZOMBIE", Zombie.class, Boolean.class);
        MetadataKey<Zombie, Integer> UNUSED_ZOMBIE = register("UNUSED_ZOMBIE", Zombie.class, Integer.class);
        MetadataKey<Zombie, Boolean> IS_CONVERTING_INTO_DROWNED = register("IS_CONVERTING_INTO_DROWNED", Zombie.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface ZombieVillagerKeys {
        MetadataKey<ZombieVillager, Boolean> IS_CONVERTING_INTO_VILLAGER = register("IS_CONVERTING_INTO_VILLAGER", ZombieVillager.class, Boolean.class);
        MetadataKey<ZombieVillager, VillagerData> ZOMBIE_VILLAGER_DATA = register("ZOMBIE_VILLAGER_DATA", ZombieVillager.class, VillagerData.class);
    }

    @ApiStatus.NonExtendable
    public interface PiglinAbstractKeys {
        MetadataKey<PiglinAbstract, Boolean> IS_IMMUNE_TO_ZOMBIFICATION = register("IS_IMMUNE_TO_ZOMBIFICATION", PiglinAbstract.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface PiglinKeys {
        MetadataKey<Piglin, Boolean> IS_BABY_PIGLIN = register("IS_BABY_PIGLIN", Piglin.class, Boolean.class);
        MetadataKey<Piglin, Boolean> IS_CHARGING_CROSSBOW = register("IS_CHARGING_CROSSBOW", Piglin.class, Boolean.class);
        MetadataKey<Piglin, Boolean> IS_DANCING = register("IS_DANCING", Piglin.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface RaiderKeys {
        MetadataKey<Raider, Boolean> IS_CELEBRATING = register("IS_CELEBRATING", Raider.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface PillagerKeys {
        MetadataKey<Pillager, Boolean> IS_CHARGING = register("IS_CHARGING", Pillager.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface WitchKeys {
        MetadataKey<Witch, Boolean> IS_DRINKING_POTION = register("IS_DRINKING_POTION", Witch.class, Boolean.class);
    }

    @ApiStatus.NonExtendable
    public interface SpellcasterKeys {
        MetadataKey<Spellcaster, Spellcaster.Spell> SPELL = register("SPELL", Spellcaster.class, Spellcaster.Spell.class);
    }

    @ApiStatus.NonExtendable
    public interface ThrowableProjectileKeys {
        MetadataKey<ThrowableProjectile, ItemStack> THROWN_ITEM = register("THROWN_ITEM", ThrowableProjectile.class, ItemStack.class);
    }

    @ApiStatus.NonExtendable
    public interface VehicleKeys {
        MetadataKey<Vehicle, Integer> SHAKING_POWER = register("SHAKING_POWER", Vehicle.class, Integer.class);
        MetadataKey<Vehicle, Integer> SHAKING_DIRECTION = register("SHAKING_DIRECTION", Vehicle.class, Integer.class);
        MetadataKey<Vehicle, Float> SHAKING_MULTIPLIER = register("SHAKING_MULTIPLIER", Vehicle.class, Float.class);
    }

    @ApiStatus.NonExtendable
    public interface BoatKeys {
        MetadataKey<Boat, Boolean> IS_LEFT_PADDLE_TURNING = register("IS_LEFT_PADDLE_TURNING", Boat.class, Boolean.class);
        MetadataKey<Boat, Boolean> IS_RIGHT_PADDLE_TURNING = register("IS_RIGHT_PADDLE_TURNING", Boat.class, Boolean.class);
        MetadataKey<Boat, Integer> SPLASH_TICKS = register("SPLASH_TICKS", Boat.class, Integer.class);
    }

    @ApiStatus.NonExtendable
    public interface MinecartKeys {
        MetadataKey<Minecart, BlockState> MINECART_BLOCK = register("MINECART_BLOCK", Minecart.class, BlockState.class);
        MetadataKey<Minecart, Integer> MINECART_BLOCK_ALTITUDE = register("MINECART_BLOCK_ALTITUDE", Minecart.class, Integer.class);
    }

    @ApiStatus.NonExtendable
    public interface CommandMinecartKeys {
        MetadataKey<CommandMinecart, String> COMMAND = register("COMMAND", CommandMinecart.class, String.class);
        MetadataKey<CommandMinecart, Component> LAST_OUTPUT = register("LAST_OUTPUT", CommandMinecart.class, Component.class);
    }

    @ApiStatus.NonExtendable
    public interface PoweredMinecartKeys {
        MetadataKey<PoweredMinecart, Boolean> HAS_FUEL = register("HAS_FUEL", PoweredMinecart.class, Boolean.class);
    }

}