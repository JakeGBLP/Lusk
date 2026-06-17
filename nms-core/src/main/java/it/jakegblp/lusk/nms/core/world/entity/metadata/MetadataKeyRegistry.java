package it.jakegblp.lusk.nms.core.world.entity.metadata;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import io.papermc.paper.entity.Bucketable;
import io.papermc.paper.math.Rotations;
import it.jakegblp.lusk.common.reflection.SimpleClass;
import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.nms.core.serialization.TypeKey;
import it.jakegblp.lusk.nms.core.world.entity.ServerEntityReference;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BitFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.abstracthorse.AbstractHorseFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.abstracthorse.AbstractHorseFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.armorstand.ArmorStandFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.armorstand.ArmorStandFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.bat.BatFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.bat.BatFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.bee.BeeFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.bee.BeeFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.blaze.BlazeFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.blaze.BlazeFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entity.EntityFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entity.EntityFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.fox.FoxFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.fox.FoxFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.irongolem.IronGolemFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.irongolem.IronGolemFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.livingentity.LivingEntityFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.livingentity.LivingEntityFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.mob.MobFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.mob.MobFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.panda.PandaFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.panda.PandaFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.player.PlayerFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.player.PlayerFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.projectile.arrow.AbstractArrowFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.projectile.arrow.AbstractArrowFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.sheep.SheepFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.sheep.SheepFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.snowgolem.SnowmanFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.snowgolem.SnowmanFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.spider.SpiderFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.spider.SpiderFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.tameable.TameableFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.tameable.TameableFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.textdisplay.TextDisplayFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.textdisplay.TextDisplayFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.vex.VexFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.vex.VexFlags;
import it.jakegblp.lusk.nms.core.world.entity.villager.VillagerData;
import net.kyori.adventure.text.Component;
import org.bukkit.Art;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionfc;

import java.util.*;

public final class MetadataKeyRegistry {

    private final @NotNull AbstractNMS nms;

    private final BiMap<String, MetadataKey<?, ?>> namedKeys = HashBiMap.create();
    private final ListMultimap<Class<? extends Entity>, MetadataKey<?, ?>> metadataEntries =
            LinkedListMultimap.create();

    public final EntityKeys ENTITY;
    public final AreaEffectCloudKeys AREA_EFFECT_CLOUD;
    public final EnderCrystalKeys ENDER_CRYSTAL;
    public final ExperienceOrbKeys EXPERIENCE_ORB;
    public final EnderEyeKeys ENDER_EYE;
    public final FallingBlockKeys FALLING_BLOCK;
    public final FireballKeys FIREBALL;
    public final FireworkKeys FIREWORK;
    public final InteractionKeys INTERACTION;
    public final ItemKeys ITEM;
    public final ItemFrameKeys ITEMFRAME;
    public final OminousItemSpawnerKeys OMINOUS_ITEM_SPAWNER;
    public final PaintingKeys PAINTING;
    public final SmallFireballKeys SMALL_FIREBALL;
    public final TNTPrimedKeys TNT_PRIMED;
    public final WitherSkullKeys WITHER_SKULL;
    public final FishHookKeys FISHHOOK;
    public final AbstractArrowKeys ABSTRACTARROW;
    public final ArrowKeys ARROW;
    public final TridentKeys TRIDENT;
    public final DisplayKeys DISPLAY;
    public final BlockDisplayKeys BLOCK_DISPLAY;
    public final ItemDisplayKeys ITEM_DISPLAY;
    public final TextDisplayKeys TEXT_DISPLAY;
    public final LivingEntityKeys LIVINGENTITY;
    public final ArmorStandKeys ARMORSTAND;
    public final PlayerKeys PLAYER;
    public final MobKeys MOB;
    public final BatKeys BAT;
    public final EnderDragonKeys ENDERDRAGON;
    public final GhastKeys GHAST;
    public final PhantomKeys PHANTOM;
    public final SlimeKeys SLIME;
    public final AllayKeys ALLAY;
    public final IronGolemKeys IRON_GOLEM;
    public final ShulkerKeys SHULKER;
    public final SnowmanKeys SNOWMAN;
    public final TadpoleKeys TADPOLE;
    public final AgeableKeys AGEABLE;
    public final GlowSquidKeys GLOW_SQUID;
    public final ArmadilloKeys ARMADILLO;
    public final AxolotlKeys AXOLOTL;
    public final BeeKeys BEE;
    public final ChickenKeys CHICKEN;
    public final CowKeys COW;
    public final FoxKeys FOX;
    public final FrogKeys FROG;
    public final HappyGhastKeys HAPPY_GHAST;
    public final GoatKeys GOAT;
    public final HoglinKeys HOGLIN;
    public final MushroomCowKeys MUSHROOM_COW;
    public final OcelotKeys OCELOT;
    public final PandaKeys PANDA;
    public final PigKeys PIG;
    public final PolarBearKeys POLARBEAR;
    public final RabbitKeys RABBIT;
    public final SheepKeys SHEEP;
    public final SnifferKeys SNIFFER;
    public final StriderKeys STRIDER;
    public final TurtleKeys TURTLE;
    public final AbstractHorseKeys ABSTRACT_HORSE;
    public final CamelKeys CAMEL;
    public final HorseKeys HORSE;
    public final ChestedHorseKeys CHESTED_HORSE;
    public final LlamaKeys LLAMA;
    public final TameableKeys TAMEABLE;
    public final CatKeys CAT;
    public final ParrotKeys PARROT;
    public final WolfKeys WOLF;
    public final AbstractVillagerKeys ABSTRACT_VILLAGER;
    public final VillagerKeys VILLAGER;
    public final AbstractFishKeys ABSTRACT_FISH;
    public final SalmonKeys SALMON;
    public final TropicalFishKeys TROPICAL_FISH;
    public final BlazeKeys BLAZE;
    public final BoggedKeys BOGGED;
    public final CreakingKeys CREAKING;
    public final CreeperKeys CREEPER;
    public final EndermanKeys ENDERMAN;
    public final GuardianKeys GUARDIAN;
    public final SkeletonKeys SKELETON;
    public final SpiderKeys SPIDER;
    public final VexKeys VEX;
    public final WardenKeys WARDEN;
    public final WitherKeys WITHER;
    public final ZoglinKeys ZOGLIN;
    public final ZombieKeys ZOMBIE;
    public final ZombieVillagerKeys ZOMBIE_VILLAGER;
    public final PiglinAbstractKeys PIGLIN_ABSTRACT;
    public final PiglinKeys PIGLIN;
    public final RaiderKeys RAIDER;
    public final PillagerKeys PILLAGER;
    public final WitchKeys WITCH;
    public final SpellcasterKeys SPELLCASTER;
    public final ThrowableProjectileKeys THROWABLE_PROJECTILE;
    public final VehicleKeys VEHICLE;
    public final BoatKeys BOAT;
    public final MinecartKeys MINECART;
    public final CommandMinecartKeys COMMAND_MINECART;
    
    public MetadataKeyRegistry(@NotNull AbstractNMS nms) {
        this.nms = nms;
        ENTITY = new EntityKeys(this);
        AREA_EFFECT_CLOUD = new AreaEffectCloudKeys(this);
        ENDER_CRYSTAL = new EnderCrystalKeys(this);
        EXPERIENCE_ORB = new ExperienceOrbKeys(this);
        ENDER_EYE = new EnderEyeKeys(this);
        FALLING_BLOCK = new FallingBlockKeys(this);
        FIREBALL = new FireballKeys(this);
        FIREWORK = new FireworkKeys(this);
        INTERACTION = new InteractionKeys(this);
        ITEM = new ItemKeys(this);
        ITEMFRAME = new ItemFrameKeys(this);
        OMINOUS_ITEM_SPAWNER = new OminousItemSpawnerKeys(this);
        PAINTING = new PaintingKeys(this);
        SMALL_FIREBALL = new SmallFireballKeys(this);
        TNT_PRIMED = new TNTPrimedKeys(this);
        WITHER_SKULL = new WitherSkullKeys(this);
        FISHHOOK = new FishHookKeys(this);
        ABSTRACTARROW = new AbstractArrowKeys(this);
        ARROW = new ArrowKeys(this);
        TRIDENT = new TridentKeys(this);
        DISPLAY = new DisplayKeys(this);
        BLOCK_DISPLAY = new BlockDisplayKeys(this);
        ITEM_DISPLAY = new ItemDisplayKeys(this);
        TEXT_DISPLAY = new TextDisplayKeys(this);
        LIVINGENTITY = new LivingEntityKeys(this);
        ARMORSTAND = new ArmorStandKeys(this);
        PLAYER = new PlayerKeys(this);
        MOB = new MobKeys(this);
        BAT = new BatKeys(this);
        ENDERDRAGON = new EnderDragonKeys(this);
        GHAST = new GhastKeys(this);
        PHANTOM = new PhantomKeys(this);
        SLIME = new SlimeKeys(this);
        ALLAY = new AllayKeys(this);
        IRON_GOLEM = new IronGolemKeys(this);
        SHULKER = new ShulkerKeys(this);
        SNOWMAN = new SnowmanKeys(this);
        TADPOLE = new TadpoleKeys(this);
        AGEABLE = new AgeableKeys(this);
        GLOW_SQUID = new GlowSquidKeys(this);
        ARMADILLO = new ArmadilloKeys(this);
        AXOLOTL = new AxolotlKeys(this);
        BEE = new BeeKeys(this);
        CHICKEN = new ChickenKeys(this);
        COW = new CowKeys(this);
        FOX = new FoxKeys(this);
        FROG = new FrogKeys(this);
        HAPPY_GHAST = new HappyGhastKeys(this);
        GOAT = new GoatKeys(this);
        HOGLIN = new HoglinKeys(this);
        MUSHROOM_COW = new MushroomCowKeys(this);
        OCELOT = new OcelotKeys(this);
        PANDA = new PandaKeys(this);
        PIG = new PigKeys(this);
        POLARBEAR = new PolarBearKeys(this);
        RABBIT = new RabbitKeys(this);
        SHEEP = new SheepKeys(this);
        SNIFFER = new SnifferKeys(this);
        STRIDER = new StriderKeys(this);
        TURTLE = new TurtleKeys(this);
        ABSTRACT_HORSE = new AbstractHorseKeys(this);
        CAMEL = new CamelKeys(this);
        HORSE = new HorseKeys(this);
        CHESTED_HORSE = new ChestedHorseKeys(this);
        LLAMA = new LlamaKeys(this);
        TAMEABLE = new TameableKeys(this);
        CAT = new CatKeys(this);
        PARROT = new ParrotKeys(this);
        WOLF = new WolfKeys(this);
        ABSTRACT_VILLAGER = new AbstractVillagerKeys(this);
        VILLAGER = new VillagerKeys(this);
        ABSTRACT_FISH = new AbstractFishKeys(this);
        SALMON = new SalmonKeys(this);
        TROPICAL_FISH = new TropicalFishKeys(this);
        BLAZE = new BlazeKeys(this);
        BOGGED = new BoggedKeys(this);
        CREAKING = new CreakingKeys(this);
        CREEPER = new CreeperKeys(this);
        ENDERMAN = new EndermanKeys(this);
        GUARDIAN = new GuardianKeys(this);
        SKELETON = new SkeletonKeys(this);
        SPIDER = new SpiderKeys(this);
        VEX = new VexKeys(this);
        WARDEN = new WardenKeys(this);
        WITHER = new WitherKeys(this);
        ZOGLIN = new ZoglinKeys(this);
        ZOMBIE = new ZombieKeys(this);
        ZOMBIE_VILLAGER = new ZombieVillagerKeys(this);
        PIGLIN_ABSTRACT = new PiglinAbstractKeys(this);
        PIGLIN = new PiglinKeys(this);
        RAIDER = new RaiderKeys(this);
        PILLAGER = new PillagerKeys(this);
        WITCH = new WitchKeys(this);
        SPELLCASTER = new SpellcasterKeys(this);
        THROWABLE_PROJECTILE = new ThrowableProjectileKeys(this);
        VEHICLE = new VehicleKeys(this);
        BOAT = new BoatKeys(this);
        MINECART = new MinecartKeys(this);
        COMMAND_MINECART = new CommandMinecartKeys(this);
    }

    public BiMap<String, MetadataKey<? extends Entity, ?>> namedKeys() {
        return namedKeys;
    }

    public ListMultimap<Class<? extends Entity>, MetadataKey<?, ?>> metadataEntries() {
        return metadataEntries;
    }

    public <E extends Entity,
            B extends BitFlag<T>,
            F extends FlagByte<B, E, ?>,
            T
            > MetadataBitFlagKey<E, B, F, T> register(
            String name,
            MetadataKey<E, F> parent,
            B bitFlag
    ) {
        MetadataBitFlagKey<E, B, F, T> key = new MetadataBitFlagKey<>(parent, bitFlag);
        namedKeys.put(name, key);
        metadataEntries.put(parent.entityClass(), key);
        return key;
    }

    @SuppressWarnings("unchecked")
    public <
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
        return (MetadataBitFlagKey<E, B2, F2, T2>) register(bitFlag.name(), new MetadataBitFlagKey<>(parentKey, bitFlag, nms));
    }

    public <
            E extends Entity,
            B extends Enum<B> & BitFlag<T>,
            F extends FlagByte<B, E, ?>,
            T>
    MetadataBitFlagKey<E, B, F, T> register(
            MetadataKey<E, F> parentKey,
            B bitFlag
    ) {
        return (MetadataBitFlagKey<E, B, F, T>) register(bitFlag.name(), new MetadataBitFlagKey<>(parentKey, bitFlag, nms));
    }

    public <E extends Entity, B extends Enum<B> & BitFlag<T>, F extends FlagByte<B, E, ?>, T> MetadataBitFlagKey<E, B, F, T> register(
            String name,
            MetadataKey<E, F> parentKey,
            B bitFlag
    ) {
        return (MetadataBitFlagKey<E, B, F, T>) register(name, new MetadataBitFlagKey<>(parentKey, bitFlag, nms));
    }

    public <E extends Entity, T> MetadataKey<E, T> register(
            String name,
            Class<E> entityClass,
            @Nullable Class<T> valueClass
    ) {
        if (valueClass == null) return null;
        int nextId = getNextId(entityClass);
        return register(name, new MetadataKey<>(nextId, entityClass, valueClass, valueClass, nms));
    }

    public <E extends Entity, T> MetadataKey<E, Optional<T>> registerOptional(
            String name,
            Class<E> entityClass,
            @Nullable Class<T> valueClass
    ) {
        if (valueClass == null) return null;
        int nextId = getNextId(entityClass);
        return register(name, new MetadataKey<>(nextId, entityClass, TypeKey.optional(valueClass), nms));
    }

    public <E extends Entity, T> MetadataKey<E, Optional<T>> registerOptional(
            String name,
            String typeClassName,
            @Nullable Class<T> valueClass
    ) {
        return registerOptional(name, SimpleClass.quickClass(typeClassName), valueClass);
    }

    public <E extends Entity, T> MetadataKey<E, T> registerHolder(
            String name,
            Class<E> entityClass,
            @Nullable Class<T> valueClass
    ) {
        if (valueClass == null) return null;
        int nextId = getNextId(entityClass);
        return register(name, new MetadataKey<>(nextId, entityClass, TypeKey.holder(valueClass), nms));
    }

    public <E extends Entity, T> MetadataKey<E, T> registerHolder(
            String name,
            Class<E> entityClass,
            @NotNull String className
    ) {
        return registerHolder(name, entityClass, SimpleClass.quickClass(className));
    }

    public <E extends Entity, T> MetadataKey<E, T> register(
            String name,
            Class<E> entityClass,
            @Nullable Class<T> frontEndClass,
            @Nullable Class<?> serializableClass
    ) {
        if (frontEndClass == null || serializableClass == null) return null;
        int nextId = getNextId(entityClass);
        return register(name, new MetadataKey<>(nextId, entityClass, frontEndClass, serializableClass, nms));
    }

    public <E extends Entity, T> MetadataKey<E, T> register(
            @NotNull String name,
            @NotNull MetadataKey<E, T> key
    ) {
        namedKeys.put(name, key);
        metadataEntries.put(key.entityClass(), key);
        return key;
    }

    public <E extends Entity, T> MetadataKey<E, T> register(
            String name,
            Class<E> entityClass,
            String stringClass
    ) {
        return register(name, entityClass, SimpleClass.quickClass(stringClass));
    }

    public <E extends Entity, T> MetadataKey<E, T> register(
            String name,
            String entityClass,
            Class<T> valueClass
    ) {
        return register(name, SimpleClass.quickClass(entityClass), valueClass);
    }

    public <E extends Entity, T> MetadataKey<E, T> register(
            String name,
            String stringEntityClass,
            String stringValueClass
    ) {
        return register(name, SimpleClass.quickClass(stringEntityClass), SimpleClass.quickClass(stringValueClass));
    }

    private int getNextId(Class<? extends Entity> entityClass) {
        int max = 0;
        for (Class<?> c : getHierarchy(entityClass)) {
            var list = metadataEntries.get((Class<? extends Entity>) c);
            if (!list.isEmpty())
                max = Math.max(max, list.getLast().id() + 1);
        }
        //System.out.println("-> getNextId for: " + entityClass+ ": " + max);
        return max;
    }

    private Set<Class<?>> getHierarchy(Class<?> root) {
        Set<Class<?>> out = new LinkedHashSet<>();
        Deque<Class<?>> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Class<?> c = stack.pop();
            if (!out.add(c)) continue;
            if (c.getSuperclass() != null) stack.push(c.getSuperclass());
            for (Class<?> i : c.getInterfaces()) stack.push(i);
        }
        return out;
    }

    public static final class EntityKeys {

        public final MetadataKey<Entity, EntityFlags> ENTITY_FLAGS;
        public final MetadataKey<Entity, Boolean> BURNING;
        public final MetadataKey<Entity, Boolean> SNEAKING;
        public final MetadataKey<Entity, Boolean> SPRINTING;
        public final MetadataKey<Entity, Boolean> SWIMMING;
        public final MetadataKey<Entity, Boolean> INVISIBLE;
        public final MetadataKey<Entity, Boolean> GLOWING;
        public final MetadataKey<Entity, Boolean> GLIDING;
        public final MetadataKey<Entity, Integer> AIR_TICKS;
        public final MetadataKey<Entity, Optional<Component>> CUSTOM_NAME;
        public final MetadataKey<Entity, Boolean> CUSTOM_NAME_VISIBILITY;
        public final MetadataKey<Entity, Boolean> SILENT;
        public final MetadataKey<Entity, Boolean> NO_GRAVITY;
        public final MetadataKey<Entity, Pose> POSE;
        public final MetadataKey<Entity, Integer> TICKS_FROZEN;

        public EntityKeys(MetadataKeyRegistry r) {
            ENTITY_FLAGS = r.register("ENTITY_FLAGS", Entity.class, EntityFlags.class, Byte.class);
            BURNING = r.register("BURNING", ENTITY_FLAGS, EntityFlag.BURNING);
            SNEAKING = r.register("SNEAKING", ENTITY_FLAGS, EntityFlag.SNEAKING);
            SPRINTING = r.register("SPRINTING", ENTITY_FLAGS, EntityFlag.SPRINTING);
            SWIMMING = r.register("SWIMMING", ENTITY_FLAGS, EntityFlag.SWIMMING);
            INVISIBLE = r.register("INVISIBLE", ENTITY_FLAGS, EntityFlag.INVISIBLE);
            GLOWING = r.register("GLOWING", ENTITY_FLAGS, EntityFlag.GLOWING);
            GLIDING = r.register("GLIDING", ENTITY_FLAGS, EntityFlag.GLIDING);
            AIR_TICKS = r.register("AIR_TICKS", Entity.class, Integer.class);
            CUSTOM_NAME = r.registerOptional("CUSTOM_NAME", Entity.class, Component.class);
            CUSTOM_NAME_VISIBILITY = r.register("CUSTOM_NAME_VISIBILITY", Entity.class, Boolean.class);
            SILENT = r.register("SILENT", Entity.class, Boolean.class);
            NO_GRAVITY = r.register("NO_GRAVITY", Entity.class, Boolean.class);
            POSE = r.register("POSE", Entity.class, Pose.class);
            TICKS_FROZEN = r.register("TICKS_FROZEN", Entity.class, Integer.class);
        }
    }

    public static final class AreaEffectCloudKeys {

        public final MetadataKey<AreaEffectCloud, Float> RADIUS;
        public final MetadataKey<AreaEffectCloud, Boolean> SINGLE_POINT;
        public final MetadataKey<AreaEffectCloud, Integer> PARTICLE;

        public AreaEffectCloudKeys(MetadataKeyRegistry r) {
            RADIUS = r.register("RADIUS", AreaEffectCloud.class, Float.class);
            SINGLE_POINT = r.register("SINGLE_POINT", AreaEffectCloud.class, Boolean.class);
            PARTICLE = r.register("PARTICLE", AreaEffectCloud.class, Integer.class);
        }
    }

    public static final class EnderCrystalKeys {
        public final MetadataKey<EnderCrystal, Optional<BlockVector>> BEAM_TARGET;
        public final MetadataKey<EnderCrystal, Boolean> SHOW_BOTTOM;

        public EnderCrystalKeys(MetadataKeyRegistry r) {
            BEAM_TARGET = r.registerOptional("BEAM_TARGET", EnderCrystal.class, BlockVector.class);
            SHOW_BOTTOM = r.register("SHOW_BOTTOM", EnderCrystal.class, Boolean.class);
        }

    }

    public static final class ExperienceOrbKeys {
        public final MetadataKey<ExperienceOrb, Integer> EXPERIENCE_REWARD;

        public ExperienceOrbKeys(MetadataKeyRegistry r) {
            EXPERIENCE_REWARD = r.register("EXPERIENCE_REWARD", ExperienceOrb.class, Integer.class);
        }

    }

    public static final class EnderEyeKeys {
        public final MetadataKey<EnderSignal, ItemStack> SIGNAL_ITEM;

        public EnderEyeKeys(MetadataKeyRegistry r) {
            SIGNAL_ITEM = r.register("SIGNAL_ITEM", EnderSignal.class, ItemStack.class);
        }

    }

    public static final class FallingBlockKeys {
        public final MetadataKey<FallingBlock, BlockVector> SPAWN_POSITION;

        public FallingBlockKeys(MetadataKeyRegistry r) {
            SPAWN_POSITION = r.register("SPAWN_POSITION", FallingBlock.class, BlockVector.class);
        }

    }

    public static final class FireballKeys {
        public final MetadataKey<Fireball, ItemStack> FIREBALL_ITEM;

        public FireballKeys(MetadataKeyRegistry r) {
            FIREBALL_ITEM = r.register("FIREBALL_ITEM", Fireball.class, ItemStack.class);
        }

    }

    public static final class FireworkKeys {
        public final MetadataKey<Firework, ItemStack> FIREWORK_INFO;
        public final MetadataKey<Firework, Optional<Integer>> BOOSTER;
        public final MetadataKey<Firework, Boolean> FROM_CROSSBOW;

        public FireworkKeys(MetadataKeyRegistry r) {
            FIREWORK_INFO = r.register("FIREWORK_INFO", Firework.class, ItemStack.class);
            BOOSTER = r.registerOptional("BOOSTER", Firework.class, Integer.class);
            FROM_CROSSBOW = r.register("FROM_CROSSBOW", Firework.class, Boolean.class);
        }

    }

    public static final class InteractionKeys {
        public final MetadataKey<Interaction, Float> WIDTH;
        public final MetadataKey<Interaction, Float> HEIGHT;
        public final MetadataKey<Interaction, Boolean> RESPONSIVE;

        public InteractionKeys(MetadataKeyRegistry r) {
            WIDTH = r.register("WIDTH", "org.bukkit.entity.Interaction", Float.class);
            HEIGHT = r.register("HEIGHT", "org.bukkit.entity.Interaction", Float.class);
            RESPONSIVE = r.register("RESPONSIVE", "org.bukkit.entity.Interaction", Boolean.class);
        }

    }

    public static final class ItemKeys {
        public final MetadataKey<Item, ItemStack> ITEM;

        public ItemKeys(MetadataKeyRegistry r) {
            ITEM = r.register("DROPPED_ITEM", Item.class, ItemStack.class);

        }

    }

    public static final class ItemFrameKeys {
        public final MetadataKey<ItemFrame, BlockFace> ITEM_FRAME_BLOCK_FACE;
        public final MetadataKey<ItemFrame, ItemStack> FRAMED_ITEM;
        public final MetadataKey<ItemFrame, Integer> ROTATION;

        public ItemFrameKeys(MetadataKeyRegistry r) {
            ITEM_FRAME_BLOCK_FACE = r.register("ITEM_FRAME_BLOCK_FACE", ItemFrame.class, BlockFace.class);
            FRAMED_ITEM = r.register("FRAMED_ITEM", ItemFrame.class, ItemStack.class);
            ROTATION = r.register("ROTATION", ItemFrame.class, Integer.class);

        }

    }

    public static final class OminousItemSpawnerKeys {
        public final MetadataKey<OminousItemSpawner, ItemStack> OMINOUS_SPAWNER_ITEM;

        public OminousItemSpawnerKeys(MetadataKeyRegistry r) {
            OMINOUS_SPAWNER_ITEM = r.register("OMINOUS_SPAWNER_ITEM", "org.bukkit.entity.OminousItemSpawner", ItemStack.class);

        }

    }

    public static final class PaintingKeys {
        public final MetadataKey<Painting, BlockFace> PAINTING_BLOCK_FACE;
        public final MetadataKey<Painting, Art> PAINTING_TYPE;

        public PaintingKeys(MetadataKeyRegistry r) {
            PAINTING_BLOCK_FACE = r.register("PAINTING_BLOCK_FACE", Painting.class, BlockFace.class);
            PAINTING_TYPE = r.registerHolder("PAINTING_TYPE", Painting.class, Art.class);
        }

    }

    public static final class SmallFireballKeys {
        public final MetadataKey<SmallFireball, ItemStack> SMALL_FIREBALL_ITEM;

        public SmallFireballKeys(MetadataKeyRegistry r) {
            SMALL_FIREBALL_ITEM = r.register("SMALL_FIREBALL_ITEM", SmallFireball.class, ItemStack.class);
        }

    }

    public static final class TNTPrimedKeys {
        public final MetadataKey<TNTPrimed, ItemStack> FUSE_TIME;
        public final MetadataKey<TNTPrimed, BlockData> TNT_BLOCK_DATA;

        public TNTPrimedKeys(MetadataKeyRegistry r) {
            FUSE_TIME = r.register("FUSE_TIME", TNTPrimed.class, ItemStack.class);
            TNT_BLOCK_DATA = r.register("TNT_BLOCK_DATA", TNTPrimed.class, BlockData.class);
        }

    }

    public static final class WitherSkullKeys {
        public final MetadataKey<WitherSkull, Boolean> INVULNERABLE;

        public WitherSkullKeys(MetadataKeyRegistry r) {
            INVULNERABLE = r.register("INVULNERABLE", WitherSkull.class, Boolean.class);
        }

    }

    public static final class FishHookKeys {
        public final MetadataKey<FishHook, Integer> HOOKED_ENTITY_ID;
        public final MetadataKey<FishHook, Boolean> CATCHABLE;

        public FishHookKeys(MetadataKeyRegistry r) {
            HOOKED_ENTITY_ID = r.register("HOOKED_ENTITY_ID", FishHook.class, Integer.class);
            CATCHABLE = r.register("CATCHABLE", FishHook.class, Boolean.class);
        }

    }

    public static final class AbstractArrowKeys {
        public final MetadataKey<AbstractArrow, AbstractArrowFlags> ARROW_FLAGS;

        public final MetadataKey<AbstractArrow, Boolean> IS_CRITICAL;

        public final MetadataKey<AbstractArrow, Boolean> NO_CLIP;

        public final MetadataKey<AbstractArrow, Byte> PIERCING_LEVEL;

        public final MetadataKey<AbstractArrow, Boolean> IS_ON_GROUND;


        public AbstractArrowKeys(MetadataKeyRegistry r) {
            ARROW_FLAGS = r.register("ARROW_FLAGS", AbstractArrow.class, AbstractArrowFlags.class, Byte.class);
            IS_CRITICAL = r.register(ARROW_FLAGS, AbstractArrowFlag.IS_CRITICAL);
            NO_CLIP = r.register(ARROW_FLAGS, AbstractArrowFlag.NO_CLIP);
            PIERCING_LEVEL = r.register("PIERCING_LEVEL", AbstractArrow.class, Byte.class);
            IS_ON_GROUND = r.register("IS_ON_GROUND", AbstractArrow.class, Boolean.class);
        }

    }

    public static final class ArrowKeys {
        public final MetadataKey<Arrow, Integer> ARROW_COLOR;

        public ArrowKeys(MetadataKeyRegistry r) {
            ARROW_COLOR = r.register("ARROW_COLOR", Arrow.class, Integer.class);
        }

    }

    public static final class TridentKeys {
        public final MetadataKey<Trident, Byte> LOYALTY_LEVEL;

        public final MetadataKey<Trident, Boolean> HAS_ENCHANTMENT_GLINT;

        public TridentKeys(MetadataKeyRegistry r) {
            LOYALTY_LEVEL = r.register("LOYALTY_LEVEL", Trident.class, Byte.class);
            HAS_ENCHANTMENT_GLINT = r.register("HAS_ENCHANTMENT_GLINT", Trident.class, Boolean.class);

        }

    }

    public static final class DisplayKeys {
        // todo: skript way to go from and to serializable types, we want timespan for this, not integer
        public final MetadataKey<Display, Integer> INTERPOLATION_DELAY;

        public final MetadataKey<Display, Integer> INTERPOLATION_DURATION;

        public final MetadataKey<Display, Integer> TELEPORT_INTERPOLATION_DURATION;

        public final MetadataKey<Display, Vector> TRANSLATION;

        public final MetadataKey<Display, Vector> SCALE;

        public final MetadataKey<Display, Quaternionfc> ROTATION_LEFT;

        public final MetadataKey<Display, Quaternionfc> ROTATION_RIGHT;
        public final MetadataKey<Display, Display.Billboard> BILLBOARD;
        public final MetadataKey<Display, Display.Brightness> BRIGHTNESS;
        public final MetadataKey<Display, Float> VIEW_RANGE;
        public final MetadataKey<Display, Float> SHADOW_RADIUS;
        public final MetadataKey<Display, Float> SHADOW_STRENGTH;
        public final MetadataKey<Display, Float> DISPLAY_WIDTH;
        public final MetadataKey<Display, Float> DISPLAY_HEIGHT;
        public final MetadataKey<Display, Integer> GLOW_COLOR;


        public DisplayKeys(MetadataKeyRegistry r) {
            INTERPOLATION_DELAY = r.register("INTERPOLATION_DELAY", Display.class, Integer.class);
            INTERPOLATION_DURATION = r.register("INTERPOLATION_DURATION", Display.class, Integer.class);
            TELEPORT_INTERPOLATION_DURATION = r.register("TELEPORT_INTERPOLATION_DURATION", Display.class, Integer.class);
            TRANSLATION = r.register("TRANSLATION", Display.class, Vector.class);
            SCALE = r.register("SCALE", Display.class, Vector.class);
            ROTATION_LEFT = r.register("ROTATION_LEFT", Display.class, Quaternionfc.class);
            ROTATION_RIGHT = r.register("ROTATION_RIGHT", Display.class, Quaternionfc.class);
            BILLBOARD = r.register("BILLBOARD", Display.class, Display.Billboard.class, Byte.class);
            BRIGHTNESS = r.register("BRIGHTNESS", Display.class, Display.Brightness.class, Integer.class);
            VIEW_RANGE = r.register("VIEW_RANGE", Display.class, Float.class);
            SHADOW_RADIUS = r.register("SHADOW_RADIUS", Display.class, Float.class);
            SHADOW_STRENGTH = r.register("SHADOW_STRENGTH", Display.class, Float.class);
            DISPLAY_WIDTH = r.register("DISPLAY_WIDTH", Display.class, Float.class);
            DISPLAY_HEIGHT = r.register("DISPLAY_HEIGHT", Display.class, Float.class);
            GLOW_COLOR = r.register("GLOW_COLOR", Display.class, Integer.class);
        }


    }

    public static final class BlockDisplayKeys {
        public final MetadataKey<BlockDisplay, BlockData> DISPLAY_BLOCK;


        public BlockDisplayKeys(MetadataKeyRegistry r) {
            DISPLAY_BLOCK = r.register("BLOCK", BlockDisplay.class, BlockData.class);
        }


    }

    public static final class ItemDisplayKeys {
        public final MetadataKey<ItemDisplay, ItemStack> DISPLAY_ITEM;
        public final MetadataKey<ItemDisplay, ItemDisplay.ItemDisplayTransform> TRANSFORM;

        public ItemDisplayKeys(MetadataKeyRegistry r) {
            DISPLAY_ITEM = r.register("ITEM", ItemDisplay.class, ItemStack.class);
            TRANSFORM = r.register("TRANSFORM", ItemDisplay.class, ItemDisplay.ItemDisplayTransform.class, Byte.class);
        }

    }

    public static final class TextDisplayKeys {
        public final MetadataKey<TextDisplay, Component> TEXT;

        public final MetadataKey<TextDisplay, Integer> LINE_WIDTH;

        public final MetadataKey<TextDisplay, Integer> BACKGROUND_COLOR;

        public final MetadataKey<TextDisplay, Byte> TEXT_OPACITY;

        public final MetadataKey<TextDisplay, TextDisplayFlags> TEXT_FLAGS;

        public final MetadataKey<TextDisplay, Boolean> HAS_SHADOW;

        public final MetadataKey<TextDisplay, Boolean> SEE_THROUGH;

        public final MetadataKey<TextDisplay, Boolean> DEFAULT_BACKGROUND;

        public final MetadataKey<TextDisplay, TextDisplay.TextAlignment> ALIGNMENT;

        public TextDisplayKeys(MetadataKeyRegistry r) {
            TEXT = r.register("TEXT", TextDisplay.class, Component.class);
            LINE_WIDTH = r.register("LINE_WIDTH", TextDisplay.class, Integer.class);
            BACKGROUND_COLOR = r.register("BACKGROUND_COLOR", TextDisplay.class, Integer.class);
            TEXT_OPACITY = r.register("TEXT_OPACITY", TextDisplay.class, Byte.class);
            TEXT_FLAGS = r.register("TEXT_FLAGS", TextDisplay.class, TextDisplayFlags.class, Byte.class);
            HAS_SHADOW = r.registerSemi(TEXT_FLAGS, TextDisplayFlag.HAS_SHADOW);
            SEE_THROUGH = r.registerSemi(TEXT_FLAGS, TextDisplayFlag.SEE_THROUGH);
            DEFAULT_BACKGROUND = r.registerSemi(TEXT_FLAGS, TextDisplayFlag.DEFAULT_BACKGROUND);
            ALIGNMENT = r.registerSemi(TEXT_FLAGS, TextDisplayFlag.ALIGNMENT);
        }


    }

    public static final class LivingEntityKeys {
        public final MetadataKey<LivingEntity, LivingEntityFlags> LIVING_ENTITY_FLAGS;
        public final MetadataKey<LivingEntity, Boolean> HAND_ACTIVE;
        public final MetadataKey<LivingEntity, EquipmentSlot> HAND;
        public final MetadataKey<LivingEntity, Boolean> RIPTIDE_ATTACK;
        public final MetadataKey<LivingEntity, Float> HEALTH;
        public final MetadataKey<LivingEntity, Integer> POTION_EFFECT_COLOR;
        public final MetadataKey<LivingEntity, Boolean> POTION_EFFECT_AMBIENT;
        public final MetadataKey<LivingEntity, Integer> ARROW_COUNT;
        public final MetadataKey<LivingEntity, Integer> BEE_STINGER_COUNT;
        public final MetadataKey<LivingEntity, Optional<BlockVector>> SLEEPING_BED_LOCATION;

        public LivingEntityKeys(MetadataKeyRegistry r) {
            LIVING_ENTITY_FLAGS = r.register("LIVING_ENTITY_FLAGS", LivingEntity.class, LivingEntityFlags.class, Byte.class);
            HAND_ACTIVE = r.registerSemi(LIVING_ENTITY_FLAGS, LivingEntityFlag.HAND_ACTIVE);
            HAND = r.registerSemi(LIVING_ENTITY_FLAGS, LivingEntityFlag.HAND);
            RIPTIDE_ATTACK = r.registerSemi(LIVING_ENTITY_FLAGS, LivingEntityFlag.RIPTIDE_ATTACK);
            HEALTH = r.register("HEALTH", LivingEntity.class, Float.class);
            POTION_EFFECT_COLOR = r.register("POTION_EFFECT_COLOR", LivingEntity.class, Integer.class);
            POTION_EFFECT_AMBIENT = r.register("POTION_EFFECT_AMBIENT", LivingEntity.class, Boolean.class);
            ARROW_COUNT = r.register("ARROW_COUNT", LivingEntity.class, Integer.class);
            BEE_STINGER_COUNT = r.register("BEE_STINGER_COUNT", LivingEntity.class, Integer.class);
            SLEEPING_BED_LOCATION = r.registerOptional("SLEEPING_BED_LOCATION", LivingEntity.class, BlockVector.class);

        }

    }

    public static final class ArmorStandKeys {
        public final MetadataKey<ArmorStand, ArmorStandFlags> ARMOR_STAND_FLAGS;
        public final MetadataKey<ArmorStand, Boolean> IS_SMALL;
        public final MetadataKey<ArmorStand, Boolean> HAS_ARMS;
        public final MetadataKey<ArmorStand, Boolean> HAS_NO_BASEPLATE;
        public final MetadataKey<ArmorStand, Boolean> IS_MARKER;
        public final MetadataKey<ArmorStand, Rotations> HEAD_ROTATION;
        public final MetadataKey<ArmorStand, Rotations> BODY_ROTATION;
        public final MetadataKey<ArmorStand, Rotations> LEFT_ARM_ROTATION;
        public final MetadataKey<ArmorStand, Rotations> RIGHT_ARM_ROTATION;
        public final MetadataKey<ArmorStand, Rotations> LEFT_LEG_ROTATION;
        public final MetadataKey<ArmorStand, Rotations> RIGHT_LEG_ROTATION;

        public ArmorStandKeys(MetadataKeyRegistry r) {
            ARMOR_STAND_FLAGS = r.register("ARMOR_STAND_FLAGS", ArmorStand.class, ArmorStandFlags.class, Byte.class);
            IS_SMALL = r.register(ARMOR_STAND_FLAGS, ArmorStandFlag.IS_SMALL);
            HAS_ARMS = r.register(ARMOR_STAND_FLAGS, ArmorStandFlag.HAS_ARMS);
            HAS_NO_BASEPLATE = r.register(ARMOR_STAND_FLAGS, ArmorStandFlag.HAS_NO_BASEPLATE);
            IS_MARKER = r.register(ARMOR_STAND_FLAGS, ArmorStandFlag.IS_MARKER);
            HEAD_ROTATION = r.register("HEAD_ROTATION", ArmorStand.class, Rotations.class);
            BODY_ROTATION = r.register("BODY_ROTATION", ArmorStand.class, Rotations.class);
            LEFT_ARM_ROTATION = r.register("LEFT_ARM_ROTATION", ArmorStand.class, Rotations.class);
            RIGHT_ARM_ROTATION = r.register("RIGHT_ARM_ROTATION", ArmorStand.class, Rotations.class);
            LEFT_LEG_ROTATION = r.register("LEFT_LEG_ROTATION", ArmorStand.class, Rotations.class);
            RIGHT_LEG_ROTATION = r.register("RIGHT_LEG_ROTATION", ArmorStand.class, Rotations.class);
        }

    }

    public static final class PlayerKeys {
        public final MetadataKey<Player, Float> ADDITIONAL_HEARTS;
        public final MetadataKey<Player, Integer> SCORE;

        //@Availability(addedIn = "", removedIn = "")
        public final MetadataKey<Player, PlayerFlags> PLAYER_FLAGS;
        public final MetadataKey<Player, Boolean> CAPE;
        public final MetadataKey<Player, Boolean> JACKET;
        public final MetadataKey<Player, Boolean> LEFT_SLEEVE;
        public final MetadataKey<Player, Boolean> RIGHT_SLEEVE;
        public final MetadataKey<Player, Boolean> LEFT_PANTS;
        public final MetadataKey<Player, Boolean> RIGHT_PANTS;
        public final MetadataKey<Player, Boolean> HAT;
        public final MetadataKey<Player, Byte> MAIN_HAND;

        // TODO: implement nbt
        //MetadataKey<Player, Object> LEFT_SHOULDER_DATA = register("LEFT_SHOULDER_DATA", Player.class, Object.class);
        //MetadataKey<Player, Object> RIGHT_SHOULDER_DATA = register("RIGHT_SHOULDER_DATA", Player.class, Object.class);

        public PlayerKeys(MetadataKeyRegistry r) {
            ADDITIONAL_HEARTS = r.register("ADDITIONAL_HEARTS", Player.class, Float.class);
            SCORE = r.register("SCORE", Player.class, Integer.class);
            PLAYER_FLAGS = r.register("PLAYER_FLAGS", Player.class, PlayerFlags.class, Byte.class);
            CAPE = r.register(PLAYER_FLAGS, PlayerFlag.CAPE);
            JACKET = r.register(PLAYER_FLAGS, PlayerFlag.JACKET);
            LEFT_SLEEVE = r.register(PLAYER_FLAGS, PlayerFlag.LEFT_SLEEVE);
            RIGHT_SLEEVE = r.register(PLAYER_FLAGS, PlayerFlag.RIGHT_SLEEVE);
            LEFT_PANTS = r.register(PLAYER_FLAGS, PlayerFlag.LEFT_PANTS);
            RIGHT_PANTS = r.register(PLAYER_FLAGS, PlayerFlag.RIGHT_PANTS);
            HAT = r.register(PLAYER_FLAGS, PlayerFlag.HAT);
            MAIN_HAND = r.register("MAIN_HAND", Player.class, Byte.class);
        }

    }

    public static final class MobKeys {
        public final MetadataKey<Mob, MobFlags> MOB_FLAGS;
        public final MetadataKey<Mob, Boolean> NO_AI;
        public final MetadataKey<Mob, Boolean> IS_LEFT_HANDED;
        public final MetadataKey<Mob, Boolean> IS_AGGRESSIVE;

        public MobKeys(MetadataKeyRegistry r) {
            MOB_FLAGS = r.register("MOB_FLAGS", Mob.class, MobFlags.class, Byte.class);
            NO_AI = r.register(MOB_FLAGS, MobFlag.NO_AI);
            IS_LEFT_HANDED = r.register(MOB_FLAGS, MobFlag.IS_LEFT_HANDED);
            IS_AGGRESSIVE = r.register(MOB_FLAGS, MobFlag.IS_AGGRESSIVE);
        }

    }

    public static final class BatKeys {
        public final MetadataKey<Bat, BatFlags> BAT_FLAGS;
        public final MetadataKey<Bat, Boolean> IS_HANGING;

        public BatKeys(MetadataKeyRegistry r) {
            BAT_FLAGS = r.register("BAT_FLAGS", Bat.class, BatFlags.class, Byte.class);
            IS_HANGING = r.register(BAT_FLAGS, BatFlag.IS_HANGING);
        }

    }

    public static final class EnderDragonKeys {
        public final MetadataKey<EnderDragon, EnderDragon.Phase> DRAGON_PHASE;

        public EnderDragonKeys(MetadataKeyRegistry r) {
            DRAGON_PHASE = r.register("DRAGON_PHASE", EnderDragon.class, EnderDragon.Phase.class, Integer.class);
        }

    }

    public static final class GhastKeys {
        public final MetadataKey<Ghast, Boolean> ATTACKING;

        public GhastKeys(MetadataKeyRegistry r) {
            ATTACKING = r.register("ATTACKING", Ghast.class, Boolean.class);
        }

    }

    public static final class PhantomKeys {
        public final MetadataKey<Phantom, Integer> PHANTOM_SIZE;

        public PhantomKeys(MetadataKeyRegistry r) {
            PHANTOM_SIZE = r.register("PHANTOM_SIZE", Phantom.class, Integer.class);
        }

    }

    public static final class SlimeKeys {
        public final MetadataKey<Slime, Integer> SLIME_SIZE;

        public SlimeKeys(MetadataKeyRegistry r) {
            SLIME_SIZE = r.register("SLIME_SIZE", Slime.class, Integer.class);
        }

    }

    public static final class AllayKeys {
        public final MetadataKey<Allay, Boolean> DANCING;
        public final MetadataKey<Allay, Boolean> CAN_DUPLICATE;

        public AllayKeys(MetadataKeyRegistry r) {
            DANCING = r.register("DANCING", Allay.class, Boolean.class);
            CAN_DUPLICATE = r.register("CAN_DUPLICATE", Allay.class, Boolean.class);
        }

    }

    public static final class IronGolemKeys {
        public final MetadataKey<IronGolem, IronGolemFlags> IRON_GOLEM_FLAGS;
        public final MetadataKey<IronGolem, Boolean> IS_PLAYER_CREATED;

        public IronGolemKeys(MetadataKeyRegistry r) {
            IRON_GOLEM_FLAGS = r.register("IRON_GOLEM_FLAGS", IronGolem.class, IronGolemFlags.class, Byte.class);
            IS_PLAYER_CREATED = r.register(IRON_GOLEM_FLAGS, IronGolemFlag.IS_PLAYER_CREATED);
        }

    }

    public static final class ShulkerKeys {
        public final MetadataKey<Shulker, BlockFace> ATTACHED_FACE;
        public final MetadataKey<Shulker, Byte> SHIELD_HEIGHT;
        public final MetadataKey<Shulker, DyeColor> SHULKER_COLOR;

        public ShulkerKeys(MetadataKeyRegistry r) {
            ATTACHED_FACE = r.register("ATTACHED_FACE", Shulker.class, BlockFace.class);
            SHIELD_HEIGHT = r.register("SHIELD_HEIGHT", Shulker.class, Byte.class);
            SHULKER_COLOR = r.register("SHULKER_COLOR", Shulker.class, DyeColor.class, Byte.class);
        }

    }

    public static final class SnowmanKeys {
        public final MetadataKey<Snowman, SnowmanFlags> SNOWMAN_FLAGS;
        public final MetadataKey<Snowman, Boolean> HAS_PUMPKIN;

        public SnowmanKeys(MetadataKeyRegistry r) {
            SNOWMAN_FLAGS = r.register("SNOWMAN_FLAGS", Snowman.class, SnowmanFlags.class, Byte.class);
            HAS_PUMPKIN = r.register(SNOWMAN_FLAGS, SnowmanFlag.HAS_PUMPKIN);
        }

    }

    public static final class TadpoleKeys {
        public final MetadataKey<Tadpole, Boolean> TADPOLE_IS_FROM_BUCKET;


        public TadpoleKeys(MetadataKeyRegistry r) {
            TADPOLE_IS_FROM_BUCKET = r.register("TADPOLE_IS_FROM_BUCKET", Tadpole.class, Boolean.class);

        }


    }

    public static final class AgeableKeys {
        public final MetadataKey<Ageable, Boolean> IS_BABY_ANIMAL;


        public AgeableKeys(MetadataKeyRegistry r) {
            IS_BABY_ANIMAL = r.register("IS_BABY_ANIMAL", Ageable.class, Boolean.class);

        }


    }

    public static final class GlowSquidKeys {
        public final MetadataKey<GlowSquid, Integer> DARK_TICKS_REMAINING;


        public GlowSquidKeys(MetadataKeyRegistry r) {
            DARK_TICKS_REMAINING = r.register("DARK_TICKS_REMAINING", GlowSquid.class, Integer.class);

        }


    }

    public static final class ArmadilloKeys {
        public final MetadataKey<Armadillo, Armadillo.State> ARMADILLO_STATE;

        public ArmadilloKeys(MetadataKeyRegistry r) {
            ARMADILLO_STATE = r.register("ARMADILLO_STATE", Armadillo.class, Armadillo.State.class, Integer.class);
        }

    }

    public static final class AxolotlKeys {
        public final MetadataKey<Axolotl, Boolean> IS_PLAYING_DEAD;
        public final MetadataKey<Axolotl, Boolean> IS_FROM_BUCKET;
        public final MetadataKey<Axolotl, Axolotl.Variant> AXOLOTL_VARIANT;

        public AxolotlKeys(MetadataKeyRegistry r) {
            IS_PLAYING_DEAD = r.register("IS_PLAYING_DEAD", Axolotl.class, Boolean.class);
            IS_FROM_BUCKET = r.register("IS_FROM_BUCKET", Axolotl.class, Boolean.class);
            AXOLOTL_VARIANT = r.register("AXOLOTL_VARIANT", Axolotl.class, Axolotl.Variant.class, Integer.class);
        }

    }

    public static final class BeeKeys {
        public final MetadataKey<Bee, BeeFlags> BEE_FLAGS;
        public final MetadataKey<Bee, Boolean> IS_ANGRY;
        public final MetadataKey<Bee, Boolean> HAS_STUNG;
        public final MetadataKey<Bee, Boolean> HAS_NECTAR;
        public final MetadataKey<Bee, Integer> BEE_ANGER_TICKS;

        public BeeKeys(MetadataKeyRegistry r) {
            BEE_FLAGS = r.register("BEE_FLAGS", Bee.class, BeeFlags.class, Byte.class);
            IS_ANGRY = r.register(BEE_FLAGS, BeeFlag.IS_ANGRY);
            HAS_STUNG = r.register(BEE_FLAGS, BeeFlag.HAS_STUNG);
            HAS_NECTAR = r.register(BEE_FLAGS, BeeFlag.HAS_NECTAR);
            BEE_ANGER_TICKS = r.register("BEE_ANGER_TICKS", Bee.class, Integer.class);
        }

    }

    public static final class ChickenKeys {
        public final MetadataKey<Chicken, Chicken.Variant> CHICKEN_VARIANT;

        public ChickenKeys(MetadataKeyRegistry r) {
            CHICKEN_VARIANT = r.registerHolder("CHICKEN_VARIANT", Chicken.class, "org.bukkit.entity.Chicken$Variant");
        }

    }

    public static final class CowKeys {
        public final MetadataKey<Cow, Cow.Variant> COW_VARIANT;

        public CowKeys(MetadataKeyRegistry r) {
            COW_VARIANT = r.registerHolder("COW_VARIANT", Cow.class, "org.bukkit.entity.Cow$Variant");
        }

    }

    public static final class FoxKeys {
        public final MetadataKey<Fox, FoxFlags> FOX_FLAGS;
        public final MetadataKey<Fox, Boolean> IS_SITTING;
        public final MetadataKey<Fox, Boolean> IS_CROUCHING;
        public final MetadataKey<Fox, Boolean> IS_INTERESTED;
        public final MetadataKey<Fox, Boolean> IS_POUNCING;
        public final MetadataKey<Fox, Boolean> IS_SLEEPING;
        public final MetadataKey<Fox, Boolean> IS_FACEPLANTED;
        public final MetadataKey<Fox, Boolean> IS_DEFENDING;
        public final MetadataKey<Fox, Optional<ServerEntityReference>> FIRST_TRUSTED_PLAYER;
        public final MetadataKey<Fox, Optional<ServerEntityReference>> SECOND_TRUSTED_PLAYER;
        public final MetadataKey<Fox, Fox.Type> FOX_TYPE;


        public FoxKeys(MetadataKeyRegistry r) {
            FOX_FLAGS = r.register("FOX_FLAGS", Fox.class, FoxFlags.class, Byte.class);
            IS_SITTING = r.register("IS_FOX_SITTING", FOX_FLAGS, FoxFlag.IS_SITTING);
            IS_CROUCHING = r.register(FOX_FLAGS, FoxFlag.IS_CROUCHING);
            IS_INTERESTED = r.register("IS_FOX_INTERESTED", FOX_FLAGS, FoxFlag.IS_INTERESTED);
            IS_POUNCING = r.register(FOX_FLAGS, FoxFlag.IS_POUNCING);
            IS_SLEEPING = r.register(FOX_FLAGS, FoxFlag.IS_SLEEPING);
            IS_FACEPLANTED = r.register(FOX_FLAGS, FoxFlag.IS_FACEPLANTED);
            IS_DEFENDING = r.register(FOX_FLAGS, FoxFlag.IS_DEFENDING);
            FIRST_TRUSTED_PLAYER = r.registerOptional("FIRST_TRUSTED_PLAYER", Fox.class, ServerEntityReference.class);
            SECOND_TRUSTED_PLAYER = r.registerOptional("SECOND_TRUSTED_PLAYER", Fox.class, ServerEntityReference.class);
            FOX_TYPE = r.register("FOX_TYPE", Fox.class, Fox.Type.class, Integer.class);
        }


    }

    public static final class FrogKeys {
        public final MetadataKey<Frog, Optional<Integer>> TONGUE_TARGET;
        public final MetadataKey<Frog, Frog.Variant> FROG_VARIANT;

        public FrogKeys(MetadataKeyRegistry r) {
            TONGUE_TARGET = r.registerOptional("TONGUE_TARGET", Frog.class, Integer.class);
            FROG_VARIANT = r.registerHolder("FROG_VARIANT", Frog.class, Frog.Variant.class);
        }

    }

    public static final class HappyGhastKeys {
        public final MetadataKey<HappyGhast, Boolean> IS_LEASH_HOLDER;
        public final MetadataKey<HappyGhast, Boolean> STAYS_STILL;

        public HappyGhastKeys(MetadataKeyRegistry r) {
            IS_LEASH_HOLDER = r.register("IS_LEASH_HOLDER", "org.bukkit.entity.HappyGhast", Boolean.class);
            STAYS_STILL = r.register("STAYS_STILL", "org.bukkit.entity.HappyGhast", Boolean.class);
        }

    }

    public static final class GoatKeys {
        public final MetadataKey<Goat, Boolean> IS_SCREAMING_GOAT;
        public final MetadataKey<Goat, Boolean> HAS_LEFT_HORN;
        public final MetadataKey<Goat, Boolean> HAS_RIGHT_HORN;

        public GoatKeys(MetadataKeyRegistry r) {
            IS_SCREAMING_GOAT = r.register("IS_SCREAMING_GOAT", Goat.class, Boolean.class);
            HAS_LEFT_HORN = r.register("HAS_LEFT_HORN", Goat.class, Boolean.class);
            HAS_RIGHT_HORN = r.register("HAS_RIGHT_HORN", Goat.class, Boolean.class);
        }

    }

    public static final class HoglinKeys {
        public final MetadataKey<Hoglin, Boolean> ZOMBIFICATION_IMMUNITY;

        public HoglinKeys(MetadataKeyRegistry r) {
            ZOMBIFICATION_IMMUNITY = r.register("ZOMBIFICATION_IMMUNITY", Hoglin.class, Boolean.class);
        }

    }

    public static final class MushroomCowKeys {
        public final MetadataKey<MushroomCow, MushroomCow.Variant> MUSHROOM_COW_VARIANT;

        public MushroomCowKeys(MetadataKeyRegistry r) {
            MUSHROOM_COW_VARIANT = r.register("MUSHROOM_COW_VARIANT", MushroomCow.class, MushroomCow.Variant.class, Integer.class);
        }

    }

    public static final class OcelotKeys {
        public final MetadataKey<Ocelot, Boolean> IS_TRUSTING;

        public OcelotKeys(MetadataKeyRegistry r) {
            IS_TRUSTING = r.register("IS_TRUSTING", Ocelot.class, Boolean.class);
        }

    }

    public static final class PandaKeys {
        public final MetadataKey<Panda, Integer> BREED_TIMER;
        public final MetadataKey<Panda, Integer> SNEEZE_TIMER;
        public final MetadataKey<Panda, Integer> EAT_TIMER;
        public final MetadataKey<Panda, PandaFlags> PANDA_FLAGS;
        public final MetadataKey<Panda, Boolean> IS_SNEEZING;
        public final MetadataKey<Panda, Boolean> IS_ROLLING;
        public final MetadataKey<Panda, Boolean> IS_PANDA_SITTING;
        public final MetadataKey<Panda, Boolean> IS_ON_BACK;
        public final MetadataKey<Panda, Panda.Gene> MAIN_GENE;
        public final MetadataKey<Panda, Panda.Gene> HIDDEN_GENE;

        public PandaKeys(MetadataKeyRegistry r) {
            BREED_TIMER = r.register("BREED_TIMER", Panda.class, Integer.class);
            SNEEZE_TIMER = r.register("SNEEZE_TIMER", Panda.class, Integer.class);
            EAT_TIMER = r.register("EAT_TIMER", Panda.class, Integer.class);
            PANDA_FLAGS = r.register("PANDA_FLAGS", Panda.class, PandaFlags.class, Byte.class);
            IS_SNEEZING = r.register(PANDA_FLAGS, PandaFlag.IS_SNEEZING);
            IS_ROLLING = r.register(PANDA_FLAGS, PandaFlag.IS_ROLLING);
            IS_PANDA_SITTING = r.register("IS_PANDA_SITTING", PANDA_FLAGS, PandaFlag.IS_SITTING);
            IS_ON_BACK = r.register(PANDA_FLAGS, PandaFlag.IS_ON_BACK);
            MAIN_GENE = r.register("MAIN_GENE", Panda.class, Panda.Gene.class, Byte.class);
            HIDDEN_GENE = r.register("HIDDEN_GENE", Panda.class, Panda.Gene.class, Byte.class);
        }

    }

    public static final class PigKeys {
        public final MetadataKey<Pig, Integer> CARROT_BOOST_TICKS;
        public final MetadataKey<Pig, Pig.Variant> PIG_VARIANT;

        public PigKeys(MetadataKeyRegistry r) {
            CARROT_BOOST_TICKS = r.register("CARROT_BOOST_TICKS", Pig.class, Integer.class);
            PIG_VARIANT = r.registerHolder("PIG_VARIANT", Pig.class, "org.bukkit.entity.Pig$Variant");
        }

    }

    public static final class PolarBearKeys {
        public final MetadataKey<PolarBear, Boolean> IS_STANDING_UP;

        public PolarBearKeys(MetadataKeyRegistry r) {
            IS_STANDING_UP = r.register("IS_STANDING_UP", PolarBear.class, Boolean.class);
        }

    }

    public static final class RabbitKeys {
        public final MetadataKey<Rabbit, Rabbit.Type> RABBIT_TYPE;

        public RabbitKeys(MetadataKeyRegistry r) {
            RABBIT_TYPE = r.register("RABBIT_TYPE", Rabbit.class, Rabbit.Type.class, Byte.class);
        }

    }

    public static final class SheepKeys {
        public final MetadataKey<Sheep, SheepFlags> SHEEP_FLAGS;
        public final MetadataKey<Sheep, Boolean> IS_SHEARED;
        public final MetadataKey<Sheep, DyeColor> WOOL_COLOR;

        public SheepKeys(MetadataKeyRegistry r) {
            SHEEP_FLAGS = r.register("SHEEP_FLAGS", Sheep.class, SheepFlags.class, Byte.class);
            IS_SHEARED = r.registerSemi(SHEEP_FLAGS, SheepFlag.IS_SHEARED);
            WOOL_COLOR = r.registerSemi(SHEEP_FLAGS, SheepFlag.WOOL_COLOR);
        }

    }

    public static final class SnifferKeys {
        public final MetadataKey<Sniffer, Integer> DROP_SEED_TICK;
        public final MetadataKey<Sniffer, Sniffer.State> SNIFFER_STATE;

        public SnifferKeys(MetadataKeyRegistry r) {
            DROP_SEED_TICK = r.register("DROP_SEED_TICK", Sniffer.class, Integer.class);
            SNIFFER_STATE = r.register("SNIFFER_STATE", Sniffer.class, Sniffer.State.class);
        }

    }

    public static final class StriderKeys {
        public final MetadataKey<Strider, Integer> FUNGUS_BOOST_TICKS;
        public final MetadataKey<Strider, Boolean> IS_SHAKING;

        public StriderKeys(MetadataKeyRegistry r) {
            FUNGUS_BOOST_TICKS = r.register("FUNGUS_BOOST_TICKS", Strider.class, Integer.class);
            IS_SHAKING = r.register("IS_SHAKING", Strider.class, Boolean.class);
        }

    }

    public static final class TurtleKeys {
        public final MetadataKey<Turtle, Boolean> HAS_EGG;
        public final MetadataKey<Turtle, Boolean> IS_LAYING_EGG;

        public TurtleKeys(MetadataKeyRegistry r) {
            HAS_EGG = r.register("HAS_EGG", Turtle.class, Boolean.class);
            IS_LAYING_EGG = r.register("IS_LAYING_EGG", Turtle.class, Boolean.class);
        }

    }

    public static final class AbstractHorseKeys {
        public final MetadataKey<AbstractHorse, AbstractHorseFlags> HORSE_FLAGS;
        public final MetadataKey<AbstractHorse, Boolean> IS_TAME;
        public final MetadataKey<AbstractHorse, Boolean> HAS_BRED;
        public final MetadataKey<AbstractHorse, Boolean> IS_EATING;
        public final MetadataKey<AbstractHorse, Boolean> IS_REARING;
        public final MetadataKey<AbstractHorse, Boolean> HAS_MOUTH_OPEN;

        public AbstractHorseKeys(MetadataKeyRegistry r) {
            HORSE_FLAGS = r.register("HORSE_FLAGS", AbstractHorse.class, AbstractHorseFlags.class, Byte.class);
            IS_TAME = r.register(HORSE_FLAGS, AbstractHorseFlag.IS_TAME);
            HAS_BRED = r.register(HORSE_FLAGS, AbstractHorseFlag.HAS_BRED);
            IS_EATING = r.register(HORSE_FLAGS, AbstractHorseFlag.IS_EATING);
            IS_REARING = r.register(HORSE_FLAGS, AbstractHorseFlag.IS_REARING);
            HAS_MOUTH_OPEN = r.register(HORSE_FLAGS, AbstractHorseFlag.HAS_MOUTH_OPEN);
        }

    }

    public static final class CamelKeys {
        public final MetadataKey<Camel, Boolean> IS_DASHING;
        public final MetadataKey<Camel, Long> LAST_POSE_CHANGE_TICK;

        public CamelKeys(MetadataKeyRegistry r) {
            IS_DASHING = r.register("IS_DASHING", Camel.class, Boolean.class);
            LAST_POSE_CHANGE_TICK = r.register("LAST_POSE_CHANGE_TICK", Camel.class, Long.class);
        }


    }

    public static final class HorseKeys {
        // todo: handle 2 in 1
        public final MetadataKey<Horse, Integer> HORSE_VARIANT;

        public HorseKeys(MetadataKeyRegistry r) {
            HORSE_VARIANT = r.register("HORSE_VARIANT", Horse.class, Integer.class);
        }

    }

    public static final class ChestedHorseKeys {
        public final MetadataKey<ChestedHorse, Boolean> HAS_CHEST;

        public ChestedHorseKeys(MetadataKeyRegistry r) {
            HAS_CHEST = r.register("HAS_CHEST", ChestedHorse.class, Boolean.class);
        }

    }

    public static final class LlamaKeys {
        public final MetadataKey<Llama, Integer> LLAMA_STRENGTH;
        public final MetadataKey<Llama, Llama.Color> LLAMA_VARIANT;

        public LlamaKeys(MetadataKeyRegistry r) {
            LLAMA_STRENGTH = r.register("LLAMA_STRENGTH", Llama.class, Integer.class);
            LLAMA_VARIANT = r.register("LLAMA_VARIANT", Llama.class, Llama.Color.class, Byte.class);
        }

    }

    public static final class TameableKeys {
        public final MetadataKey<Tameable, TameableFlags> TAMEABLE_FLAGS;
        public final MetadataKey<Tameable, Boolean> IS_SITTING;
        public final MetadataKey<Tameable, Boolean> IS_TAMED;
        public final MetadataKey<Tameable, Optional<ServerEntityReference>> OWNER;

        public TameableKeys(MetadataKeyRegistry r) {
            TAMEABLE_FLAGS = r.register("TAMEABLE_FLAGS", Tameable.class, TameableFlags.class, Byte.class);
            IS_SITTING = r.register(TAMEABLE_FLAGS, TameableFlag.IS_SITTING);
            IS_TAMED = r.register(TAMEABLE_FLAGS, TameableFlag.IS_TAMED);
            OWNER = r.registerOptional("OWNER", Tameable.class, ServerEntityReference.class);
        }

    }

    public static final class CatKeys {
        public final MetadataKey<Cat, Boolean> IS_LYING;
        public final MetadataKey<Cat, Boolean> IS_LOOKING_UP;
        public final MetadataKey<Cat, DyeColor> CAT_COLLAR_COLOR;
        public final MetadataKey<Cat, Cat.Type> CAT_VARIANT;

        public CatKeys(MetadataKeyRegistry r) {
            IS_LYING = r.register("IS_LYING", Cat.class, Boolean.class);
            IS_LOOKING_UP = r.register("IS_LOOKING_UP", Cat.class, Boolean.class);
            CAT_COLLAR_COLOR = r.register("CAT_COLLAR_COLOR", Cat.class, DyeColor.class, Integer.class);
            CAT_VARIANT = r.registerHolder("CAT_VARIANT", Cat.class, Cat.Type.class);
        }

    }

    public static final class ParrotKeys {
        public final MetadataKey<Parrot, Parrot.Variant> PARROT_VARIANT;

        public ParrotKeys(MetadataKeyRegistry r) {
            PARROT_VARIANT = r.register("PARROT_VARIANT", Parrot.class, Parrot.Variant.class, Integer.class);
        }

    }

    public static final class WolfKeys {
        public final MetadataKey<Wolf, Boolean> IS_INTERESTED;
        public final MetadataKey<Wolf, DyeColor> WOLF_COLLAR_COLOR;
        public final MetadataKey<Wolf, Integer> WOLF_ANGER_TICKS;
        public final MetadataKey<Wolf, Wolf.Variant> WOLF_VARIANT;
        public final MetadataKey<Wolf, Wolf.SoundVariant> WOLF_SOUND_VARIANT;

        public WolfKeys(MetadataKeyRegistry r) {
            IS_INTERESTED = r.register("IS_WOLF_INTERESTED", Wolf.class, Boolean.class);
            WOLF_COLLAR_COLOR = r.register("WOLF_COLLAR_COLOR", Wolf.class, DyeColor.class, Integer.class);
            WOLF_ANGER_TICKS = r.register("WOLF_ANGER_TICKS", Wolf.class, Integer.class);
            WOLF_VARIANT = r.registerHolder("WOLF_VARIANT", Wolf.class, Wolf.Variant.class);
            WOLF_SOUND_VARIANT = r.registerHolder("WOLF_SOUND_VARIANT", Wolf.class, Wolf.SoundVariant.class);
        }

    }

    public static final class AbstractVillagerKeys {
        public final MetadataKey<AbstractVillager, Integer> HEAD_SHAKE_TICKS;

        public AbstractVillagerKeys(MetadataKeyRegistry r) {
            HEAD_SHAKE_TICKS = r.register("HEAD_SHAKE_TICKS", AbstractVillager.class, Integer.class);
        }

    }

    public static final class VillagerKeys {
        public final MetadataKey<Villager, VillagerData> VILLAGER_DATA;

        public VillagerKeys(MetadataKeyRegistry r) {
            VILLAGER_DATA = r.register("VILLAGER_DATA", Villager.class, VillagerData.class);
        }

    }

    public static final class AbstractFishKeys {
        public final MetadataKey<Bucketable, Boolean> FROM_BUCKET;

        public AbstractFishKeys(MetadataKeyRegistry r) {
            FROM_BUCKET = r.register("FROM_BUCKET", Bucketable.class, Boolean.class);
        }

    }

    public static final class SalmonKeys {
        public final MetadataKey<Salmon, Salmon.Variant> SALMON_VARIANT;
        public SalmonKeys(MetadataKeyRegistry r) {
            SALMON_VARIANT = r.register("SALMON_VARIANT", Salmon.class, Salmon.Variant.class, Integer.class);
        }

    }

    public static final class TropicalFishKeys {
        public final MetadataKey<TropicalFish, TropicalFish.Pattern> TROPICAL_FISH_VARIANT;

        public TropicalFishKeys(MetadataKeyRegistry r) {
            TROPICAL_FISH_VARIANT = r.register("TROPICAL_FISH_VARIANT", TropicalFish.class, TropicalFish.Pattern.class, Integer.class);
        }

    }

    public static final class BlazeKeys {
        public final MetadataKey<Blaze, BlazeFlags> BLAZE_FLAGS;
        public final MetadataKey<Blaze, Boolean> ON_FIRE;

        public BlazeKeys(MetadataKeyRegistry r) {
            BLAZE_FLAGS = r.register("BLAZE_FLAGS", Blaze.class, BlazeFlags.class, Byte.class);
            ON_FIRE = r.register(BLAZE_FLAGS, BlazeFlag.ON_FIRE);
        }

    }

    public static final class BoggedKeys {
        public final MetadataKey<Bogged, Boolean> IS_BOGGED_SHEARED;

        public BoggedKeys(MetadataKeyRegistry r) {
            IS_BOGGED_SHEARED = r.register("IS_BOGGED_SHEARED", "org.bukkit.entity.Bogged", Boolean.class);
        }

    }

    public static final class CreakingKeys {
        public final MetadataKey<Creaking, Boolean> CAN_MOVE;
        public final MetadataKey<Creaking, Boolean> IS_ACTIVE;
        public final MetadataKey<Creaking, Boolean> IS_TEARING_DOWN;
        public final MetadataKey<Entity, Optional<Location>> HOME_POSITION;

        public CreakingKeys(MetadataKeyRegistry r) {
            CAN_MOVE = r.register("CAN_MOVE", "org.bukkit.entity.Creaking", Boolean.class);
            IS_ACTIVE = r.register("IS_ACTIVE", "org.bukkit.entity.Creaking", Boolean.class);
            IS_TEARING_DOWN = r.register("IS_TEARING_DOWN", "org.bukkit.entity.Creaking", Boolean.class);
            HOME_POSITION = r.registerOptional("HOME_POSITION", "org.bukkit.entity.Creaking", Location.class);
        }

    }

    public static final class CreeperKeys {
        // todo: simplify
        public final MetadataKey<Creeper, Integer> STATE;
        public final MetadataKey<Creeper, Boolean> IS_CHARGED;
        public final MetadataKey<Creeper, Boolean> IS_IGNITED;

        public CreeperKeys(MetadataKeyRegistry r) {
            STATE = r.register("STATE", Creeper.class, Integer.class);
            IS_CHARGED = r.register("IS_CHARGED", Creeper.class, Boolean.class);
            IS_IGNITED = r.register("IS_IGNITED", Creeper.class, Boolean.class);
        }

    }

    public static final class EndermanKeys {
        public final MetadataKey<Enderman, BlockData> CARRIED_BLOCK;
        public final MetadataKey<Enderman, Boolean> IS_SCREAMING;
        public final MetadataKey<Enderman, Boolean> IS_STARING;

        public EndermanKeys(MetadataKeyRegistry r) {
            CARRIED_BLOCK = r.register("CARRIED_BLOCK", Enderman.class, BlockData.class);
            IS_SCREAMING = r.register("IS_SCREAMING", Enderman.class, Boolean.class);
            IS_STARING = r.register("IS_STARING", Enderman.class, Boolean.class);
        }

    }

    public static final class GuardianKeys {
        public final MetadataKey<Guardian, Boolean> IS_RETRACTING_SPIKES;
        // todo: allow normal entities (converter-ish behavior) ------ entity ref!!
        public final MetadataKey<Guardian, Integer> TARGET_ENTITY_ID;

        public GuardianKeys(MetadataKeyRegistry r) {
            IS_RETRACTING_SPIKES = r.register("IS_RETRACTING_SPIKES", Guardian.class, Boolean.class);
            TARGET_ENTITY_ID = r.register("TARGET_ENTITY_ID", Guardian.class, Integer.class);
        }

    }

    public static final class SkeletonKeys {
        public final MetadataKey<Skeleton, Boolean> IS_CONVERTING_INTO_STRAY;

        public SkeletonKeys(MetadataKeyRegistry r) {
            IS_CONVERTING_INTO_STRAY = r.register("IS_CONVERTING_INTO_STRAY", Skeleton.class, Boolean.class);
        }

    }

    public static final class SpiderKeys {
        public final MetadataKey<Spider, SpiderFlags> SPIDER_FLAGS;
        public final MetadataKey<Spider, Boolean> IS_CLIMBING;

        public SpiderKeys(MetadataKeyRegistry r) {
            SPIDER_FLAGS = r.register("SPIDER_FLAGS", Spider.class, SpiderFlags.class, Byte.class);
            IS_CLIMBING = r.register(SPIDER_FLAGS, SpiderFlag.IS_CLIMBING);
        }

    }

    public static final class VexKeys {
        public final MetadataKey<Vex, VexFlags> VEX_FLAGS;
        public final MetadataKey<Vex, Boolean> IS_ATTACKING;

        public VexKeys(MetadataKeyRegistry r) {
            VEX_FLAGS = r.register("VEX_FLAGS", Vex.class, VexFlags.class, Byte.class);
            IS_ATTACKING = r.register(VEX_FLAGS, VexFlag.IS_ATTACKING);
        }

    }

    public static final class WardenKeys {
        public final MetadataKey<Warden, Integer> ANGER_LEVEL;

        public WardenKeys(MetadataKeyRegistry r) {
            ANGER_LEVEL = r.register("ANGER_LEVEL", "org.bukkit.entity.Warden", Integer.class);
        }

    }

    public static final class WitherKeys {
        public final MetadataKey<Wither, Integer> CENTER_HEAD_TARGET;
        public final MetadataKey<Wither, Integer> LEFT_HEAD_TARGET;
        public final MetadataKey<Wither, Integer> RIGHT_HEAD_TARGET;
        public final MetadataKey<Wither, Integer> INVULNERABILITY_TICKS;

        public WitherKeys(MetadataKeyRegistry r) {
            CENTER_HEAD_TARGET = r.register("CENTER_HEAD_TARGET", Wither.class, Integer.class);
            LEFT_HEAD_TARGET = r.register("LEFT_HEAD_TARGET", Wither.class, Integer.class);
            RIGHT_HEAD_TARGET = r.register("RIGHT_HEAD_TARGET", Wither.class, Integer.class);
            INVULNERABILITY_TICKS = r.register("INVULNERABILITY_TICKS", Wither.class, Integer.class);
        }

    }

    public static final class ZoglinKeys {
        public final MetadataKey<Zoglin, Boolean> IS_BABY_ZOGLIN;

        public ZoglinKeys(MetadataKeyRegistry r) {
            IS_BABY_ZOGLIN = r.register("IS_BABY_ZOGLIN", Zoglin.class, Boolean.class);
        }

    }

    public static final class ZombieKeys {
        public final MetadataKey<Zombie, Boolean> IS_BABY_ZOMBIE;
        public final MetadataKey<Zombie, Integer> UNUSED_ZOMBIE;
        public final MetadataKey<Zombie, Boolean> IS_CONVERTING_INTO_DROWNED;

        public ZombieKeys(MetadataKeyRegistry r) {
            IS_BABY_ZOMBIE = r.register("IS_BABY_ZOMBIE", Zombie.class, Boolean.class);
            UNUSED_ZOMBIE = r.register("UNUSED_ZOMBIE", Zombie.class, Integer.class);
            IS_CONVERTING_INTO_DROWNED = r.register("IS_CONVERTING_INTO_DROWNED", Zombie.class, Boolean.class);

        }


    }

    public static final class ZombieVillagerKeys {
        public final MetadataKey<ZombieVillager, Boolean> IS_CONVERTING_INTO_VILLAGER;

        public final MetadataKey<ZombieVillager, VillagerData> ZOMBIE_VILLAGER_DATA;


        public ZombieVillagerKeys(MetadataKeyRegistry r) {
            IS_CONVERTING_INTO_VILLAGER = r.register("IS_CONVERTING_INTO_VILLAGER", ZombieVillager.class, Boolean.class);
            ZOMBIE_VILLAGER_DATA = r.register("ZOMBIE_VILLAGER_DATA", ZombieVillager.class, VillagerData.class);

        }


    }

    public static final class PiglinAbstractKeys {
        public final MetadataKey<PiglinAbstract, Boolean> IS_IMMUNE_TO_ZOMBIFICATION;


        public PiglinAbstractKeys(MetadataKeyRegistry r) {
            IS_IMMUNE_TO_ZOMBIFICATION = r.register("IS_IMMUNE_TO_ZOMBIFICATION", PiglinAbstract.class, Boolean.class);

        }


    }

    public static final class PiglinKeys {
        public final MetadataKey<Piglin, Boolean> IS_BABY_PIGLIN;

        public final MetadataKey<Piglin, Boolean> IS_CHARGING_CROSSBOW;

        public final MetadataKey<Piglin, Boolean> IS_DANCING;


        public PiglinKeys(MetadataKeyRegistry r) {
            IS_BABY_PIGLIN = r.register("IS_BABY_PIGLIN", Piglin.class, Boolean.class);
            IS_CHARGING_CROSSBOW = r.register("IS_CHARGING_CROSSBOW", Piglin.class, Boolean.class);
            IS_DANCING = r.register("IS_DANCING", Piglin.class, Boolean.class);

        }


    }

    public static final class RaiderKeys {
        public final MetadataKey<Raider, Boolean> IS_CELEBRATING;


        public RaiderKeys(MetadataKeyRegistry r) {
            IS_CELEBRATING = r.register("IS_CELEBRATING", Raider.class, Boolean.class);

        }


    }

    public static final class PillagerKeys {
        public final MetadataKey<Pillager, Boolean> IS_CHARGING;


        public PillagerKeys(MetadataKeyRegistry r) {
            IS_CHARGING = r.register("IS_CHARGING", Pillager.class, Boolean.class);

        }


    }

    public static final class WitchKeys {
        public final MetadataKey<Witch, Boolean> IS_DRINKING_POTION;


        public WitchKeys(MetadataKeyRegistry r) {
            IS_DRINKING_POTION = r.register("IS_DRINKING_POTION", Witch.class, Boolean.class);

        }


    }

    public static final class SpellcasterKeys {
        public final MetadataKey<Spellcaster, Spellcaster.Spell> SPELL;

        public SpellcasterKeys(MetadataKeyRegistry r) {
            SPELL = r.register("SPELL", Spellcaster.class, Spellcaster.Spell.class, Byte.class);
        }

    }

    public static final class ThrowableProjectileKeys {
        public final MetadataKey<ThrowableProjectile, ItemStack> THROWN_ITEM;

        public ThrowableProjectileKeys(MetadataKeyRegistry r) {
            THROWN_ITEM = r.register("THROWN_ITEM", ThrowableProjectile.class, ItemStack.class);
        }

    }

    public static final class VehicleKeys {
        public final MetadataKey<Vehicle, Integer> SHAKING_POWER;

        public final MetadataKey<Vehicle, Integer> SHAKING_DIRECTION;

        public final MetadataKey<Vehicle, Float> SHAKING_MULTIPLIER;


        public VehicleKeys(MetadataKeyRegistry r) {
            SHAKING_POWER = r.register("SHAKING_POWER", Vehicle.class, Integer.class);
            SHAKING_DIRECTION = r.register("SHAKING_DIRECTION", Vehicle.class, Integer.class);
            SHAKING_MULTIPLIER = r.register("SHAKING_MULTIPLIER", Vehicle.class, Float.class);

        }


    }

    public static final class BoatKeys {
        public final MetadataKey<Boat, Boolean> IS_LEFT_PADDLE_TURNING;

        public final MetadataKey<Boat, Boolean> IS_RIGHT_PADDLE_TURNING;

        public final MetadataKey<Boat, Integer> SPLASH_TICKS;


        public BoatKeys(MetadataKeyRegistry r) {
            IS_LEFT_PADDLE_TURNING = r.register("IS_LEFT_PADDLE_TURNING", Boat.class, Boolean.class);
            IS_RIGHT_PADDLE_TURNING = r.register("IS_RIGHT_PADDLE_TURNING", Boat.class, Boolean.class);
            SPLASH_TICKS = r.register("SPLASH_TICKS", Boat.class, Integer.class);

        }


    }

    public static final class MinecartKeys {
        public final MetadataKey<Minecart, BlockData> MINECART_BLOCK;

        public final MetadataKey<Minecart, Integer> MINECART_BLOCK_ALTITUDE;


        public MinecartKeys(MetadataKeyRegistry r) {
            MINECART_BLOCK = r.register("MINECART_BLOCK", Minecart.class, BlockData.class);
            MINECART_BLOCK_ALTITUDE = r.register("MINECART_BLOCK_ALTITUDE", Minecart.class, Integer.class);

        }


    }

    public static final class CommandMinecartKeys {
        public final MetadataKey<CommandMinecart, String> COMMAND;

        public final MetadataKey<CommandMinecart, Component> LAST_OUTPUT;


        public CommandMinecartKeys(MetadataKeyRegistry r) {
            COMMAND = r.register("COMMAND", CommandMinecart.class, String.class);
            LAST_OUTPUT = r.register("LAST_OUTPUT", CommandMinecart.class, Component.class);

        }


    }

    public static final class PoweredMinecartKeys {
        public final MetadataKey<PoweredMinecart, Boolean> HAS_FUEL;


        public PoweredMinecartKeys(MetadataKeyRegistry r) {
            HAS_FUEL = r.register("HAS_FUEL", PoweredMinecart.class, Boolean.class);

        }

    }

}