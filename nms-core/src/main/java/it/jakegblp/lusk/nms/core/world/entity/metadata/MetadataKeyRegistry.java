package it.jakegblp.lusk.nms.core.world.entity.metadata;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import io.papermc.paper.command.CommandBlockHolder;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import io.papermc.paper.entity.Bucketable;
import io.papermc.paper.math.Rotations;
import io.papermc.paper.registry.RegistryKey;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.common.annotations.Availability;
import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.nms.core.util.NullabilityUtils;
import it.jakegblp.lusk.nms.core.world.entity.EntityUtils;
import it.jakegblp.lusk.nms.core.world.entity.ServerEntityReference;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.Flag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.abstracthorse.AbstractHorseFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.armorstand.ArmorStandFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.arrow.AbstractArrowFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.avatar.AvatarFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.bat.BatFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.bee.BeeFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.blaze.BlazeFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.entity.EntityFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.fox.FoxFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.irongolem.IronGolemFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.livingentity.LivingEntityFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.mob.MobFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.panda.PandaFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.sheep.SheepFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.snowgolem.SnowmanFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.spider.SpiderFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.tameable.TameableFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.textdisplay.TextDisplayFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.vex.VexFlags;
import it.jakegblp.lusk.nms.core.world.entity.metadata.news.*;
import it.jakegblp.lusk.nms.core.world.entity.villager.VillagerData;
import it.jakegblp.lusk.nms.core.world.level.LevelUtils;
import it.jakegblp.lusk.nms.core.world.level.particles.ParticleWrapper;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.material.Colorable;
import org.bukkit.material.Directional;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class MetadataKeyRegistry {

    private final @NotNull AbstractNMS nms;
    private final boolean is_26_1, is_26_2;

    private final ListMultimap<EntityClasses<?>, EntityDataKey<?,?,?,?,?>> metadataEntries = LinkedListMultimap.create();

    public final EntityKeys ENTITY;
    public final AreaEffectCloudKeys AREA_EFFECT_CLOUD;
    public final EnderCrystalKeys ENDER_CRYSTAL;
    public final ExperienceOrbKeys EXPERIENCE_ORB;
    public final EnderEyeKeys ENDER_EYE;
    public final FallingBlockKeys FALLING_BLOCK;
    public final LargeFireballKeys FIREBALL;
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
    public final AvatarKeys AVATAR;
    public final MannequinKeys MANNEQUIN;
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
    public final AbstractNautilusKeys ABSTRACT_NAUTILUS;
    public final ZombieNautilusKeys ZOMBIE_NAUTILUS;
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
        var version = nms.getVersion();
        is_26_1 = version.isGreaterOrEqual(Version.of(26,1));
        is_26_2 = version.isGreaterOrEqual(Version.of(26,2));
        ENTITY = new EntityKeys(this);
        AREA_EFFECT_CLOUD = new AreaEffectCloudKeys(this);
        ENDER_CRYSTAL = new EnderCrystalKeys(this);
        EXPERIENCE_ORB = new ExperienceOrbKeys(this);
        ENDER_EYE = new EnderEyeKeys(this);
        FALLING_BLOCK = new FallingBlockKeys(this);
        FIREBALL = new LargeFireballKeys(this);
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
        AVATAR = new AvatarKeys(this);
        MANNEQUIN = new MannequinKeys(this);
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
        ABSTRACT_NAUTILUS = new AbstractNautilusKeys(this);
        ZOMBIE_NAUTILUS = new ZombieNautilusKeys(this);
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

    public ListMultimap<EntityClasses<?>, EntityDataKey<?,?,?,?,?>> metadataEntries() {
        return metadataEntries;
    }

    @NullMarked
    @Contract("_, _, _, _ -> new")
    public <E extends Entity,
            T extends FlagByte<E>
            >
    FlagsEntityDataKey<E, T> register(
            String name,
            List<Class<? extends E>> entityClasses,
            Class<T> type,
            T defaultValue
    ) {
        return register(new FlagsEntityDataKey<>(
                name,
                getNextId(entityClasses),
                new EntityClasses<>(entityClasses),
                nms.getEntityDataSerializerSimple(Byte.class),
                type,
                defaultValue
        ));
    }

    @NullMarked
    @Contract("_, _, _, _ -> new")
    public <E extends Entity,
            T extends FlagByte<E>
            >
    FlagsEntityDataKey<E, T> registerFlags(
            String name,
            Class<? extends E> entityClass,
            Class<T> type,
            T defaultValue
    ) {
        return register(new FlagsEntityDataKey<>(
                name,
                getNextId(entityClass),
                new EntityClasses<>(entityClass),
                nms.getEntityDataSerializerSimple(Byte.class),
                type,
                defaultValue
        ));
    }

    @NullMarked
    @Contract("_, _, _ -> new")
    public <E extends Entity,
            T extends FlagByte<E>
            >
    FlagsEntityDataKey<E, T> registerFlags(
            String name,
            Class<? extends E> entityClass,
            Class<T> type
    ) {
        return register(new FlagsEntityDataKey<>(
                name,
                getNextId(entityClass),
                new EntityClasses<>(entityClass),
                nms.getEntityDataSerializerSimple(Byte.class),
                type
        ));
    }

    @NullMarked
    @Contract("_, _, _ -> new")
    public <E extends Entity,
            T extends FlagByte<E>
            >
    FlagsEntityDataKey<E, T> registerFlags(
            String name,
            List<Class<? extends E>> entityClasses,
            Class<T> type
    ) {
        return register(new FlagsEntityDataKey<>(
                name,
                getNextId(entityClasses),
                new EntityClasses<>(entityClasses),
                nms.getEntityDataSerializerSimple(Byte.class),
                type
        ));
    }

    @NullMarked
    @Contract("_, _ -> new")
    public <E extends Entity,
            B extends FlagByte<E>,
            A>
    FlagEntityDataKey<E, B, A> registerFlag(
            FlagsEntityDataKey<E, B> parent,
            Flag<E, A, ?> flag
    ) {
        return register(parent.flag(flag));
    }

    @NullMarked
    @Contract("_, _, _, _, _, _ -> new")
    public <E extends Entity,
            T
            >
    SplitEntityDataKey<E, T> registerSplit(
            String name,
            Class<? extends E> entityClass,
            Class<T> type,
            Function<E, T> getter,
            BiConsumer<E, T> setter,
            T defaultValue
    ) {
        return register(new SplitEntityDataKey<>(
                name,
                getNextId(entityClass),
                new EntityClasses<>(entityClass),
                nms.getEntityDataSerializerSimple(Byte.class),
                type,
                getter,
                setter,
                defaultValue
        ));
    }

    @NullMarked
    @Contract("_, _, _, _, _, _ -> new")
    public <E extends Entity, ParentType, ComponentType> SplitComponentEntityDataKey<E, ParentType, ComponentType> registerSplitComponent(
            String name,
            SplitEntityDataKey<E, ParentType> parent,
            Class<ComponentType> componentType,
            Function<ParentType, ComponentType> extractor,
            BiFunction<ParentType, ComponentType, ParentType> inserter,
            ComponentType defaultValue
    ) {
        return register(parent.component(name, componentType, extractor, inserter, defaultValue));
    }

    @NullMarked
    @Contract("_, _, _, _, _ -> new")
    public <E extends Entity> BooleanEntityDataKey<E> registerBoolean(
            String name,
            List<Class<? extends E>> entityClasses,
            Function<E, Boolean> getter,
            BiConsumer<E, Boolean> setter,
            boolean defaultValue
    ) {
        return register(new BooleanEntityDataKey<>(
                name,
                getNextId(entityClasses),
                new EntityClasses<>(entityClasses),
                nms.getEntityDataSerializerSimple(Boolean.class),
                getter,
                setter,
                defaultValue
        ));
    }

    @NullMarked
    @Contract("_, _, _, _ -> new")
    public <E extends Entity> BooleanEntityDataKey<E> registerBoolean(
            String name,
            List<Class<? extends E>> entityClasses,
            Function<E, Boolean> getter,
            BiConsumer<E, Boolean> setter
    ) {
        return register(new BooleanEntityDataKey<>(
                name,
                getNextId(entityClasses),
                new EntityClasses<>(entityClasses),
                nms.getEntityDataSerializerSimple(Boolean.class),
                getter,
                setter,
                false
        ));
    }

    @NullMarked
    @Contract("_, _, _, _, _ -> new")
    public <E extends Entity> BooleanEntityDataKey<E> registerBoolean(
            String name,
            Class<? extends E> entityClass,
            Function<E, Boolean> getter,
            BiConsumer<E, Boolean> setter,
            boolean defaultValue
    ) {
        return register(new BooleanEntityDataKey<>(
                name,
                getNextId(entityClass),
                new EntityClasses<>(entityClass),
                nms.getEntityDataSerializerSimple(Boolean.class),
                getter,
                setter,
                defaultValue
        ));
    }

    @NullMarked
    @Contract("_, _, _, _ -> new")
    public <E extends Entity> BooleanEntityDataKey<E> registerBoolean(
            String name,
            Class<? extends E> entityClass,
            Function<E, Boolean> getter,
            @Nullable BiConsumer<E, Boolean> setter
    ) {
        return register(new BooleanEntityDataKey<>(
                name,
                getNextId(entityClass),
                new EntityClasses<>(entityClass),
                nms.getEntityDataSerializerSimple(Boolean.class),
                getter,
                setter,
                false
        ));
    }

    @NullMarked
    @Contract("_, _, _, _, _ -> new")
    public <E extends Entity> BooleanEntityDataKey<E> registerInvertedBoolean(
            String name,
            List<Class<? extends E>> entityClasses,
            Function<E, Boolean> getter,
            BiConsumer<E, Boolean> setter,
            boolean defaultValue
    ) {
        return register(new BooleanEntityDataKey<>(
                name,
                getNextId(entityClasses),
                new EntityClasses<>(entityClasses),
                nms.getEntityDataSerializerSimple(Boolean.class),
                getter,
                setter,
                defaultValue,
                true
        ));
    }

    @NullMarked
    @Contract("_, _, _, _, _ -> new")
    public <E extends Entity> BooleanEntityDataKey<E> registerInvertedBoolean(
            String name,
            Class<? extends E> entityClass,
            Function<E, Boolean> getter,
            BiConsumer<E, Boolean> setter,
            boolean defaultValue
    ) {
        return register(new BooleanEntityDataKey<>(
                name,
                getNextId(entityClass),
                new EntityClasses<>(entityClass),
                nms.getEntityDataSerializerSimple(Boolean.class),
                getter,
                setter,
                defaultValue,
                true
        ));
    }

    @NullMarked
    @Contract("_, _, _, _ -> new")
    public <E extends Entity> BooleanEntityDataKey<E> registerInvertedBoolean(
            String name,
            List<Class<? extends E>> entityClasses,
            Function<E, Boolean> getter,
            BiConsumer<E, Boolean> setter
    ) {
        return registerInvertedBoolean(name, entityClasses, getter, setter, false);
    }

    @NullMarked
    @Contract("_, _, _, _ -> new")
    public <E extends Entity> BooleanEntityDataKey<E> registerInvertedBoolean(
            String name,
            Class<? extends E> entityClass,
            Function<E, Boolean> getter,
            BiConsumer<E, Boolean> setter
    ) {
        return registerInvertedBoolean(name,entityClass, getter, setter, false);
    }

    @NullMarked
    @Contract("_, _, _, _, _, _ -> new")
    public <E extends Entity, T> OptionalEntityDataKey<E, T> registerOptional(
            String name,
            List<Class<? extends E>> entityClasses,
            Class<T> type,
            Function<E, @Nullable T> getter,
            BiConsumer<E, @Nullable T> setter,
            @Nullable T defaultValue
    ) {
        return register(new OptionalEntityDataKey<>(
                name,
                getNextId(entityClasses),
                new EntityClasses<>(entityClasses),
                nms.getEntityDataSerializerOptional(type),
                type,
                getter,
                setter,
                defaultValue
        ));
    }

    @NullMarked
    @Contract("_, _, _, _, _, _ -> new")
    public <E extends Entity, T> OptionalEntityDataKey<E, T> registerOptional(
            String name,
            Class<? extends E> entityClass,
            Class<T> type,
            Function<E, @Nullable T> getter,
            BiConsumer<E, @Nullable T> setter,
            @Nullable T defaultValue
    ) {
        return register(new OptionalEntityDataKey<>(
                name,
                getNextId(entityClass),
                new EntityClasses<>(entityClass),
                nms.getEntityDataSerializerOptional(type),
                type,
                getter,
                setter,
                defaultValue
        ));
    }

    @NullMarked
    @Contract("_, _, _, _, _ -> new")
    public <E extends Entity, T> OptionalEntityDataKey<E, T> registerOptional(
            String name,
            List<Class<? extends E>> entityClasses,
            Class<T> type,
            Function<E, @Nullable T> getter,
            BiConsumer<E, @Nullable T> setter
    ) {
        return registerOptional(name, entityClasses, type, getter, setter, null);
    }

    @NullMarked
    @Contract("_, _, _, _, _ -> new")
    public <E extends Entity, T> OptionalEntityDataKey<E, T> registerOptional(
            String name,
            Class<? extends E> entityClass,
            Class<T> type,
            Function<E, @Nullable T> getter,
            BiConsumer<E, @Nullable T> setter
    ) {
        return registerOptional(name, entityClass, type, getter, setter, null);
    }

    @Contract("_, _, _ -> new")
    @SuppressWarnings("unchecked")
    public <E extends Entity, OptionalEntity extends Entity> OptionalEntityDataKey<E, OptionalEntity> registerDependantOptionalEntity(
            String name,
            Class<OptionalEntity> type,
            SimpleEntityDataKey<E, Integer> parentKey
    ) {
        return new OptionalEntityDataKey<>(
                name,
                parentKey.getId(),
                parentKey.getEntityClasses(),
                parentKey.getSerializer(),
                type,
                e -> {
                    var obtainedEntity = LevelUtils.getEntityFromID(parentKey.get(e), e.getWorld());
                    return type.isInstance(obtainedEntity) ? (OptionalEntity) obtainedEntity : null;
                },
                (entity, optionalEntity) -> parentKey.set(entity, optionalEntity == null ? parentKey.getDefaultValue() : optionalEntity.getEntityId())
        );
    }

    @NullMarked
    @Contract("_, _, _, _, _, _ -> new")
    public <E extends Entity, T> ListEntityDataKey<E, T> registerList(
            String name,
            List<Class<? extends E>> entityClasses,
            Class<T> type,
            Function<E, List<T>> getter,
            @Nullable BiConsumer<E, List<T>> setter,
            List<T> defaultValue
    ) {
        return register(new ListEntityDataKey<>(
                name,
                getNextId(entityClasses),
                new EntityClasses<>(entityClasses),
                nms.getEntityDataSerializerList(type),
                type,
                getter,
                setter,
                defaultValue
        ));
    }

    @NullMarked
    @Contract("_, _, _, _, _, _ -> new")
    public <E extends Entity, T> ListEntityDataKey<E, T> registerList(
            String name,
            Class<? extends E> entityClass,
            Class<T> type,
            Function<E, List<T>> getter,
            @Nullable BiConsumer<E, List<T>> setter,
            List<T> defaultValue
    ) {
        return register(new ListEntityDataKey<>(
                name,
                getNextId(entityClass),
                new EntityClasses<>(entityClass),
                nms.getEntityDataSerializerList(type),
                type,
                getter,
                setter,
                defaultValue
        ));
    }

    @NullMarked
    @Contract("_, _, _, _, _ -> new")
    public <E extends Entity, T> ListEntityDataKey<E, T> registerList(
            String name,
            List<Class<? extends E>> entityClasses,
            Class<T> type,
            Function<E, List<T>> getter,
            @Nullable BiConsumer<E, List<T>> setter
    ) {
        return register(new ListEntityDataKey<>(
                name,
                getNextId(entityClasses),
                new EntityClasses<>(entityClasses),
                nms.getEntityDataSerializerList(type),
                type,
                getter,
                setter
        ));
    }

    @NullMarked
    @Contract("_, _, _, _, _ -> new")
    public <E extends Entity, T> ListEntityDataKey<E, T> registerList(
            String name,
            Class<? extends E> entityClass,
            Class<T> type,
            Function<E, List<T>> getter,
            @Nullable BiConsumer<E, List<T>> setter
    ) {
        return register(new ListEntityDataKey<>(
                name,
                getNextId(entityClass),
                new EntityClasses<>(entityClass),
                nms.getEntityDataSerializerList(type),
                type,
                getter,
                setter
        ));
    }

    @NullMarked
    @Contract("_, _, _, _, _, _, _ -> new")
    public <E extends Entity, T extends Keyed> RegistryIntEntityDataKey<E, T> registerIntKeyed(
            String name,
            List<Class<? extends E>> entityClasses,
            Class<T> type,
            Function<E, T> getter,
            BiConsumer<E, T> setter,
            RegistryKey<T> registryKey,
            T defaultValue
    ) {
        return register(new RegistryIntEntityDataKey<>(
                name,
                getNextId(entityClasses),
                new EntityClasses<>(entityClasses),
                nms.getEntityDataSerializerHolder(type),
                type,
                getter,
                setter,
                registryKey,
                defaultValue
        ));
    }

    @NullMarked
    @Contract("_, _, _, _, _, _, _ -> new")
    public <E extends Entity, T extends Keyed> RegistryIntEntityDataKey<E, T> registerIntKeyed(
            String name,
            Class<? extends E> entityClass,
            Class<T> type,
            Function<E, T> getter,
            BiConsumer<E, T> setter,
            RegistryKey<T> registryKey,
            T defaultValue
    ) {
        return register(new RegistryIntEntityDataKey<>(
                name,
                getNextId(entityClass),
                new EntityClasses<>(entityClass),
                nms.getEntityDataSerializerHolder(type),
                type,
                getter,
                setter,
                registryKey,
                defaultValue
        ));
    }

    public <E extends Entity, T> SimpleEntityDataKey<E, T> register(
            String name,
            List<Class<? extends E>> entityClasses,
            Class<T> type,
            Function<E, T> getter,
            @Nullable BiConsumer<E, T> setter,
            T defaultValue
    ) {
        return register(new SimpleEntityDataKey<>(
                name,
                getNextId(entityClasses),
                new EntityClasses<>(entityClasses),
                nms.getEntityDataSerializerSimple(type),
                type,
                getter,
                setter,
                defaultValue
        ));
    }

    @Contract("_, _, _, _, _, _ -> new")
    public <E extends Entity, T> @NotNull SimpleEntityDataKey<E, T> register(
            String name,
            Class<? extends E> entityClass,
            Class<T> type,
            Function<E, T> getter,
            @Nullable BiConsumer<E, T> setter,
            T defaultValue
    ) {
        return register(new SimpleEntityDataKey<>(
                name,
                getNextId(entityClass),
                new EntityClasses<>(entityClass),
                nms.getEntityDataSerializerSimple(type),
                type,
                getter,
                setter,
                defaultValue
        ));
    }

    @Contract("_, _, _, _, _, _ -> new")
    public <E extends Entity, T extends Enum<T>> @NotNull EnumByteEntityDataKey<E, T> registerEnumByte(
            String name,
            List<Class<? extends E>> entityClasses,
            Class<T> type,
            Function<E, T> getter,
            BiConsumer<E, T> setter,
            T defaultValue
    ) {
        return register(new EnumByteEntityDataKey<>(
                name,
                getNextId(entityClasses),
                new EntityClasses<>(entityClasses),
                nms.getEntityDataSerializerSimple(type),
                type,
                getter,
                setter,
                defaultValue
        ));
    }

    @Contract("_, _, _, _, _, _ -> new")
    public <E extends Entity, T extends Enum<T>> @NotNull EnumByteEntityDataKey<E, T> registerEnumByte(
            String name,
            Class<? extends E> entityClass,
            Class<T> type,
            Function<E, T> getter,
            BiConsumer<E, T> setter,
            T defaultValue
    ) {
        return register(new EnumByteEntityDataKey<>(
                name,
                getNextId(entityClass),
                new EntityClasses<>(entityClass),
                nms.getEntityDataSerializerSimple(Byte.class),
                type,
                getter,
                setter,
                defaultValue
        ));
    }

    @Contract("_, _, _, _, _ -> new")
    public <E extends Entity, T extends Enum<T>> @NotNull EnumByteEntityDataKey<E, T> registerEnumByte(
            String name,
            List<Class<? extends E>> entityClasses,
            Class<T> type,
            Function<E, T> getter,
            BiConsumer<E, T> setter
    ) {
        return register(new EnumByteEntityDataKey<>(
                name,
                getNextId(entityClasses),
                new EntityClasses<>(entityClasses),
                nms.getEntityDataSerializerSimple(Byte.class),
                type,
                getter,
                setter
        ));
    }

    @Contract("_, _, _, _, _ -> new")
    public <E extends Entity, T extends Enum<T>> @NotNull EnumByteEntityDataKey<E, T> registerEnumByte(
            String name,
            Class<? extends E> entityClass,
            Class<T> type,
            Function<E, T> getter,
            BiConsumer<E, T> setter
    ) {
        return register(new EnumByteEntityDataKey<>(
                name,
                getNextId(entityClass),
                new EntityClasses<>(entityClass),
                nms.getEntityDataSerializerSimple(Byte.class),
                type,
                getter,
                setter
        ));
    }

    @Contract("_, _, _, _, _, _ -> new")
    public <E extends Entity, T extends Enum<T>> @NotNull EnumIntEntityDataKey<E, T> registerEnumInt(
            String name,
            List<Class<? extends E>> entityClasses,
            Class<T> type,
            Function<E, T> getter,
            BiConsumer<E, T> setter,
            T defaultValue
    ) {
        return register(new EnumIntEntityDataKey<>(
                name,
                getNextId(entityClasses),
                new EntityClasses<>(entityClasses),
                nms.getEntityDataSerializerSimple(Integer.class),
                type,
                getter,
                setter,
                defaultValue
        ));
    }

    @Contract("_, _, _, _, _, _ -> new")
    public <E extends Entity, T extends Enum<T>> @NotNull EnumIntEntityDataKey<E, T> registerEnumInt(
            String name,
            Class<? extends E> entityClass,
            Class<T> type,
            Function<E, T> getter,
            BiConsumer<E, T> setter,
            T defaultValue
    ) {
        return register(new EnumIntEntityDataKey<>(
                name,
                getNextId(entityClass),
                new EntityClasses<>(entityClass),
                nms.getEntityDataSerializerSimple(Integer.class),
                type,
                getter,
                setter,
                defaultValue
        ));
    }

    @Contract("_, _, _, _, _ -> new")
    public <E extends Entity, T extends Enum<T>> @NotNull EnumIntEntityDataKey<E, T> registerEnumInt(
            String name,
            List<Class<? extends E>> entityClasses,
            Class<T> type,
            Function<E, T> getter,
            BiConsumer<E, T> setter
    ) {
        return register(new EnumIntEntityDataKey<>(
                name,
                getNextId(entityClasses),
                new EntityClasses<>(entityClasses),
                nms.getEntityDataSerializerSimple(Integer.class),
                type,
                getter,
                setter
        ));
    }

    @Contract("_, _, _, _, _ -> new")
    public <E extends Entity, T extends Enum<T>> @NotNull EnumIntEntityDataKey<E, T> registerEnumInt(
            String name,
            Class<? extends E> entityClass,
            Class<T> type,
            Function<E, T> getter,
            BiConsumer<E, T> setter
    ) {
        return register(new EnumIntEntityDataKey<>(
                name,
                getNextId(entityClass),
                new EntityClasses<>(entityClass),
                nms.getEntityDataSerializerSimple(Integer.class),
                type,
                getter,
                setter
        ));
    }

    @Contract("_, _, _, _, _ -> new")
    public <E extends Entity, T extends Enum<T>> @NotNull PureEnumEntityDataKey<E, T> registerSerializableEnum(
            String name,
            Class<? extends E> entityClass,
            Class<T> type,
            Function<E, T> getter,
            BiConsumer<E, T> setter
    ) {
        return register(new PureEnumEntityDataKey<>(
                name,
                getNextId(entityClass),
                new EntityClasses<>(entityClass),
                nms.getEntityDataSerializerSimple(type),
                type,
                getter,
                setter
        ));
    }

    public <E extends Entity, Pretty, Internal> @NotNull MappedEntityDataKey<E, Pretty, Internal> registerMapped(
            String name,
            List<Class<? extends E>> entityClasses,
            Class<Pretty> prettyType,
            Class<Internal> internalType,
            Function<E, Pretty> getter,
            BiConsumer<E, Pretty> setter,
            Function<Internal, Pretty> prettifier,
            Function<Pretty, Internal> internalizer,
            Pretty defaultValue
    ) {
        return register(new MappedEntityDataKey<>(
                name,
                getNextId(entityClasses),
                new EntityClasses<>(entityClasses),
                nms.getEntityDataSerializerSimple(internalType),
                prettyType,
                internalType,
                getter,
                setter,
                prettifier,
                internalizer,
                defaultValue
        ));
    }

    public <E extends Entity, Pretty, Internal> @NotNull MappedEntityDataKey<E, Pretty, Internal> registerMapped(
            String name,
            Class<? extends E> entityClass,
            Class<Pretty> prettyType,
            Class<Internal> internalType,
            Function<E, Pretty> getter,
            BiConsumer<E, Pretty> setter,
            Function<Internal, Pretty> prettifier,
            Function<Pretty, Internal> internalizer,
            Pretty defaultValue
    ) {
        return register(new MappedEntityDataKey<>(
                name,
                getNextId(entityClass),
                new EntityClasses<>(entityClass),
                nms.getEntityDataSerializerSimple(internalType),
                prettyType,
                internalType,
                getter,
                setter,
                prettifier,
                internalizer,
                defaultValue
        ));
    }

    @Contract("_, -> param1")
    private <BaseEntity extends Entity, PrettyType, InternalType, PrettyTypeGeneric, InternalTypeGeneric,
            K extends EntityDataKey<BaseEntity, PrettyType, InternalType, PrettyTypeGeneric, InternalTypeGeneric>> @NotNull K register(
            @NotNull K key
    ) {
        metadataEntries.put(key.getEntityClasses(), key);
        return key;
    }

    @NullMarked
    public Iterable<EntityDataKey<?, ?, ?, ?, ?>> getApplicableKeys(
            Iterable<Class<? extends Entity>> entityClasses
    ) {
        List<EntityDataKey<?, ?, ?, ?, ?>> keys = new ArrayList<>();

        for (var entry : metadataEntries.entries()) {
            for (var entityClass : entityClasses) {
                if (entry.getKey().isAssignableFrom(entityClass)) {
                    keys.add(entry.getValue());
                    break;
                }
            }
        }
        return keys;
    }

    @NullMarked
    public Iterable<EntityDataKey<?, ?, ?, ?, ?>> getApplicableKeys(Class<? extends Entity> entityClass) {
        List<EntityDataKey<?, ?, ?, ?, ?>> keys = new ArrayList<>();
        for (var entry : metadataEntries.entries())
            if (entry.getKey().isAssignableFrom(entityClass))
                keys.add(entry.getValue());
        return keys;
    }

    @NullMarked
    @SuppressWarnings({"unchecked", "rawtypes"})
    private <E extends Entity> @Range(from = 0, to = 255) int getNextId(Iterable<Class<? extends E>> entityClasses) {
        int max = 0;
        for (var key : getApplicableKeys((Iterable<Class<? extends Entity>>) (Iterable) entityClasses))
            max = Math.max(max, key.getId() + 1);
        return max;
    }

    @NullMarked
    private int getNextId(Class<? extends Entity> entityClass) {
        int max = 0;
        for (var key : getApplicableKeys(entityClass)) {
            max = Math.max(max, key.getId() + 1);
        }
        return max;
    }

    public static class EntityKeys {
        public final FlagsEntityDataKey<Entity, EntityFlags> ENTITY_FLAGS;
        public final FlagEntityDataKey<Entity, EntityFlags, Boolean> BURNING;
        public final FlagEntityDataKey<Entity, EntityFlags, Boolean> SNEAKING;
        public final FlagEntityDataKey<Entity, EntityFlags, Boolean> UNUSED_RIDING;
        public final FlagEntityDataKey<Entity, EntityFlags, Boolean> SPRINTING;
        public final FlagEntityDataKey<Entity, EntityFlags, Boolean> SWIMMING;
        public final FlagEntityDataKey<Entity, EntityFlags, Boolean> INVISIBLE;
        public final FlagEntityDataKey<Entity, EntityFlags, Boolean> GLOWING;
        public final FlagEntityDataKey<Entity, EntityFlags, Boolean> GLIDING;
        public final SimpleEntityDataKey<Entity, Integer> AIR_TICKS;
        public final OptionalEntityDataKey<Entity, Component> CUSTOM_NAME;
        public final BooleanEntityDataKey<Entity> CUSTOM_NAME_VISIBILITY;
        public final BooleanEntityDataKey<Entity> SILENT;
        public final BooleanEntityDataKey<Entity> NO_GRAVITY;
        public final PureEnumEntityDataKey<Entity, Pose> POSE;
        public final SimpleEntityDataKey<Entity, Integer> TICKS_FROZEN;

        public EntityKeys(MetadataKeyRegistry r) {
            ENTITY_FLAGS = r.registerFlags("ENTITY_FLAGS", Entity.class, EntityFlags.class);
            BURNING = r.registerFlag(ENTITY_FLAGS, EntityFlags.BURNING);
            SNEAKING = r.registerFlag(ENTITY_FLAGS, EntityFlags.SNEAKING);
            UNUSED_RIDING = r.registerFlag(ENTITY_FLAGS, EntityFlags.UNUSED);
            SPRINTING = r.registerFlag(ENTITY_FLAGS, EntityFlags.SPRINTING);
            SWIMMING = r.registerFlag(ENTITY_FLAGS, EntityFlags.SWIMMING);
            INVISIBLE = r.registerFlag(ENTITY_FLAGS, EntityFlags.INVISIBLE);
            GLOWING = r.registerFlag(ENTITY_FLAGS, EntityFlags.GLOWING);
            GLIDING = r.registerFlag(ENTITY_FLAGS, EntityFlags.GLIDING);
            AIR_TICKS = r.register("AIR_TICKS", Entity.class, Integer.class, EntityUtils::getAirTicks, EntityUtils::setAirTicks, 300);
            CUSTOM_NAME = r.registerOptional("CUSTOM_NAME", Entity.class, Component.class, Nameable::customName, Nameable::customName);
            CUSTOM_NAME_VISIBILITY = r.registerBoolean("CUSTOM_NAME_VISIBILITY", Entity.class, Entity::isCustomNameVisible, Entity::setCustomNameVisible);
            SILENT = r.registerBoolean("SILENT", Entity.class, Entity::isSilent, Entity::setSilent);
            NO_GRAVITY = r.registerInvertedBoolean("NO_GRAVITY", Entity.class, Entity::hasGravity, Entity::setGravity);
            POSE = r.registerSerializableEnum("POSE", Entity.class, Pose.class, Entity::getPose, Entity::setPose);
            TICKS_FROZEN = r.register("TICKS_FROZEN", Entity.class, Integer.class, Entity::getFreezeTicks, Entity::setFreezeTicks, 0);
        }
    }

    public static class AreaEffectCloudKeys {
        public final SimpleEntityDataKey<AreaEffectCloud, Float> RADIUS;
        public final BooleanEntityDataKey<AreaEffectCloud> SINGLE_POINT;
        public final SimpleEntityDataKey<AreaEffectCloud, ParticleWrapper> PARTICLE;

        public AreaEffectCloudKeys(MetadataKeyRegistry r) { 
            RADIUS = r.register("RADIUS", AreaEffectCloud.class, Float.class, AreaEffectCloud::getRadius, AreaEffectCloud::setRadius, 3f);
            SINGLE_POINT = r.registerBoolean("SINGLE_POINT", AreaEffectCloud.class, EntityUtils::isSinglePoint, EntityUtils::setSinglePoint);
            PARTICLE = r.register("PARTICLE", AreaEffectCloud.class, ParticleWrapper.class, EntityUtils::getParticleWrapper, EntityUtils::setParticleWrapper, EntityUtils.getAreaEffectCloudDefaultParticle());
        }

    }

    public static class EnderCrystalKeys {
        public final OptionalEntityDataKey<EnderCrystal, BlockVector> BEAM_TARGET;
        public final BooleanEntityDataKey<EnderCrystal> SHOW_BOTTOM;

        public EnderCrystalKeys(MetadataKeyRegistry r) { 
            BEAM_TARGET = r.registerOptional("BEAM_TARGET", EnderCrystal.class, BlockVector.class, enderCrystal -> NullabilityUtils.convertIfNotNull(enderCrystal.getBeamTarget(), location -> location.toVector().toBlockVector()), (enderCrystal, blockVector) -> enderCrystal.setBeamTarget(NullabilityUtils.convertIfNotNull(blockVector, blockVector1 -> blockVector1.toLocation(enderCrystal.getWorld()))));
            SHOW_BOTTOM = r.registerBoolean("SHOW_BOTTOM", EnderCrystal.class, EnderCrystal::isShowingBottom, EnderCrystal::setShowingBottom, true);
        }

    }

    public static class ExperienceOrbKeys {
        public final SimpleEntityDataKey<ExperienceOrb, Integer> EXPERIENCE_REWARD;

        public ExperienceOrbKeys(MetadataKeyRegistry r) { 
            EXPERIENCE_REWARD = r.register("EXPERIENCE_REWARD", ExperienceOrb.class, Integer.class, ExperienceOrb::getExperience, ExperienceOrb::setExperience, 0);
        }

    }

    public static class EnderEyeKeys {
        public final SimpleEntityDataKey<EnderSignal, ItemStack> SIGNAL_ITEM;

        public EnderEyeKeys(MetadataKeyRegistry r) { 
            SIGNAL_ITEM = r.register("SIGNAL_ITEM", EnderSignal.class, ItemStack.class, EnderSignal::getItem, EnderSignal::setItem, ItemStack.of(Material.ENDER_EYE));
        }

    }

    public static class FallingBlockKeys {
        public final SimpleEntityDataKey<FallingBlock, BlockVector> SPAWN_POSITION;

        public FallingBlockKeys(MetadataKeyRegistry r) {
            SPAWN_POSITION = r.register("SPAWN_POSITION", FallingBlock.class, BlockVector.class, EntityUtils::getStartingPosition, EntityUtils::setStartingPosition, new BlockVector());
        }

    }

    public static class LargeFireballKeys {
        public final SimpleEntityDataKey<LargeFireball, ItemStack> FIREBALL_ITEM;

        public LargeFireballKeys(MetadataKeyRegistry r) { 
            FIREBALL_ITEM = r.register("FIREBALL_ITEM", LargeFireball.class, ItemStack.class, LargeFireball::getDisplayItem, LargeFireball::setDisplayItem, ItemStack.of(Material.FIRE_CHARGE));
        }

    }

    public static final class FireworkKeys {
        public final SimpleEntityDataKey<Firework, ItemStack> FIREWORK_INFO;
        public final OptionalEntityDataKey<Firework, Integer> BOOSTER;
        public final BooleanEntityDataKey<Firework> FROM_CROSSBOW;

        public FireworkKeys(MetadataKeyRegistry r) {
            FIREWORK_INFO = r.register("FIREWORK_INFO", Firework.class, ItemStack.class, Firework::getItem, Firework::setItem, ItemStack.of(Material.FIREWORK_ROCKET));
            BOOSTER = r.registerOptional("BOOSTER", Firework.class, Integer.class, EntityUtils::getBooster, EntityUtils::setBooster);
            FROM_CROSSBOW = r.registerBoolean("FROM_CROSSBOW", Firework.class, Firework::isShotAtAngle, Firework::setShotAtAngle);
        }

    }

    public static final class InteractionKeys {
        public final SimpleEntityDataKey<Interaction, Float> WIDTH;
        public final SimpleEntityDataKey<Interaction, Float> HEIGHT;
        public final BooleanEntityDataKey<Interaction> RESPONSIVE;

        public InteractionKeys(MetadataKeyRegistry r) {
            WIDTH = r.register("WIDTH", Interaction.class, Float.class, Interaction::getInteractionWidth, Interaction::setInteractionWidth, 1f);
            HEIGHT = r.register("HEIGHT", Interaction.class, Float.class, Interaction::getInteractionHeight, Interaction::setInteractionHeight, 1f);
            RESPONSIVE = r.registerBoolean("RESPONSIVE", Interaction.class, Interaction::isResponsive, Interaction::setResponsive);
        }

    }

    public static final class ItemKeys {
        public final SimpleEntityDataKey<Item, ItemStack> ITEM;

        public ItemKeys(MetadataKeyRegistry r) {
            ITEM = r.register("DROPPED_ITEM", Item.class, ItemStack.class, Item::getItemStack, Item::setItemStack, ItemStack.empty());
        }

    }

    public static final class ItemFrameKeys {
        public final SimpleEntityDataKey<ItemFrame, BlockFace> ITEM_FRAME_BLOCK_FACE;
        public final SimpleEntityDataKey<ItemFrame, ItemStack> FRAMED_ITEM;
        public final MappedEntityDataKey<ItemFrame, Rotation, Integer> ROTATION;

        public ItemFrameKeys(MetadataKeyRegistry r) {
            ITEM_FRAME_BLOCK_FACE = r.register("ITEM_FRAME_BLOCK_FACE", ItemFrame.class, BlockFace.class, Entity::getFacing, Directional::setFacingDirection, BlockFace.SOUTH);
            FRAMED_ITEM = r.register("FRAMED_ITEM", ItemFrame.class, ItemStack.class, ItemFrame::getItem, ItemFrame::setItem, ItemStack.empty());
            ROTATION = r.registerMapped("ROTATION", ItemFrame.class, Rotation.class, Integer.class, ItemFrame::getRotation, ItemFrame::setRotation, integer -> Rotation.values()[integer], Enum::ordinal, Rotation.NONE);
        }

    }

    public static final class OminousItemSpawnerKeys {
        public final SimpleEntityDataKey<OminousItemSpawner, ItemStack> OMINOUS_SPAWNER_ITEM;

        public OminousItemSpawnerKeys(MetadataKeyRegistry r) {
            OMINOUS_SPAWNER_ITEM = r.register("OMINOUS_SPAWNER_ITEM", OminousItemSpawner.class, ItemStack.class, OminousItemSpawner::getItem, OminousItemSpawner::setItem, ItemStack.empty());
        }

    }

    public static final class PaintingKeys {
        public final SimpleEntityDataKey<Painting, BlockFace> PAINTING_BLOCK_FACE;
        public final RegistryIntEntityDataKey<Painting, Art> PAINTING_TYPE;

        public PaintingKeys(MetadataKeyRegistry r) {
            PAINTING_BLOCK_FACE = r.register("PAINTING_BLOCK_FACE", Painting.class, BlockFace.class, Entity::getFacing, Directional::setFacingDirection, BlockFace.SOUTH);
            PAINTING_TYPE = r.registerIntKeyed("PAINTING_TYPE", Painting.class, Art.class, Painting::getArt, (painting, art) -> painting.setArt(art, true), RegistryKey.PAINTING_VARIANT, Art.KEBAB);
        }

    }

    public static final class SmallFireballKeys {
        public final SimpleEntityDataKey<SmallFireball, ItemStack> SMALL_FIREBALL_ITEM;

        public SmallFireballKeys(MetadataKeyRegistry r) {
            SMALL_FIREBALL_ITEM = r.register("SMALL_FIREBALL_ITEM", SmallFireball.class, ItemStack.class, SizedFireball::getDisplayItem, SizedFireball::setDisplayItem, ItemStack.empty());
        }

    }

    public static final class TNTPrimedKeys {
        public final SimpleEntityDataKey<TNTPrimed, Integer> FUSE_TIME;
        public final SimpleEntityDataKey<TNTPrimed, BlockData> TNT_BLOCK_DATA;

        public TNTPrimedKeys(MetadataKeyRegistry r) {
            FUSE_TIME = r.register("FUSE_TIME", TNTPrimed.class, Integer.class, TNTPrimed::getFuseTicks, TNTPrimed::setFuseTicks, 80);
            TNT_BLOCK_DATA = r.register("TNT_BLOCK_DATA", TNTPrimed.class, BlockData.class, TNTPrimed::getBlockData, TNTPrimed::setBlockData, BlockType.TNT.createBlockData());
        }

    }

    public static final class WitherSkullKeys {
        public final BooleanEntityDataKey<WitherSkull> INVULNERABLE;

        public WitherSkullKeys(MetadataKeyRegistry r) {
            INVULNERABLE = r.registerBoolean("INVULNERABLE", WitherSkull.class, WitherSkull::isCharged, WitherSkull::setCharged);
        }

    }

    public static final class FishHookKeys {
        public final SimpleEntityDataKey<FishHook, Integer> HOOKED_ENTITY_ID;
        public final BooleanEntityDataKey<FishHook> CATCHABLE;

        public FishHookKeys(MetadataKeyRegistry r) {
            HOOKED_ENTITY_ID = r.register("HOOKED_ENTITY_ID", FishHook.class, Integer.class, fishHook -> {
                var hookedEntity = fishHook.getHookedEntity();
                return hookedEntity == null ? 0 : hookedEntity.getEntityId();
            }, (fishHook, integer) -> fishHook.setHookedEntity(integer == null ? null : LevelUtils.getEntityFromID(integer, fishHook.getWorld())), 0);
            CATCHABLE = r.registerBoolean("CATCHABLE", FishHook.class, EntityUtils::isBiting, EntityUtils::setBiting);
        }

    }

    public static final class AbstractArrowKeys {
        public final FlagsEntityDataKey<AbstractArrow, AbstractArrowFlags> ARROW_FLAGS;
        public final FlagEntityDataKey<AbstractArrow, AbstractArrowFlags, Boolean> IS_CRITICAL;
        public final FlagEntityDataKey<AbstractArrow, AbstractArrowFlags, Boolean> NO_CLIP;
        public final SimpleEntityDataKey<AbstractArrow, Byte> PIERCING_LEVEL;
        public final BooleanEntityDataKey<AbstractArrow> IS_ON_GROUND;

        public AbstractArrowKeys(MetadataKeyRegistry r) {
            ARROW_FLAGS = r.registerFlags("ARROW_FLAGS", AbstractArrow.class, AbstractArrowFlags.class);
            IS_CRITICAL = r.registerFlag(ARROW_FLAGS, AbstractArrowFlags.IS_CRITICAL);
            NO_CLIP = r.registerFlag(ARROW_FLAGS, AbstractArrowFlags.NO_CLIP);
            PIERCING_LEVEL = r.register("PIERCING_LEVEL", AbstractArrow.class, Byte.class, abstractArrow -> (byte) abstractArrow.getPierceLevel(), (abstractArrow, aByte) -> abstractArrow.setPierceLevel(aByte), (byte) 0);
            IS_ON_GROUND = r.registerBoolean("IS_ON_GROUND", AbstractArrow.class, AbstractArrow::isCritical, AbstractArrow::setCritical);
        }

    }

    public static final class ArrowKeys {
        public final MappedEntityDataKey<Arrow, Color, Integer> ARROW_COLOR;

        public ArrowKeys(MetadataKeyRegistry r) {
            ARROW_COLOR = r.registerMapped("ARROW_COLOR", Arrow.class, Color.class, Integer.class, Arrow::getColor, Arrow::setColor, integer -> integer == -1 ? null : Color.fromARGB(integer), color -> color != null ? color.asARGB() : -1, null);
        }

    }

    public static final class TridentKeys {
        public final SimpleEntityDataKey<Trident, Byte> LOYALTY_LEVEL;
        public final BooleanEntityDataKey<Trident> HAS_ENCHANTMENT_GLINT;

        public TridentKeys(MetadataKeyRegistry r) {
            LOYALTY_LEVEL = r.register("LOYALTY_LEVEL", Trident.class, Byte.class, trident -> (byte) trident.getLoyaltyLevel(), (trident, aByte) -> trident.setLoyaltyLevel(aByte), (byte) 0);
            HAS_ENCHANTMENT_GLINT = r.registerBoolean("HAS_ENCHANTMENT_GLINT", Trident.class, Trident::hasGlint, Trident::setGlint);
        }

    }

    public static final class DisplayKeys {
        public final SimpleEntityDataKey<Display, Integer> INTERPOLATION_DELAY;
        public final SimpleEntityDataKey<Display, Integer> INTERPOLATION_DURATION;
        public final SimpleEntityDataKey<Display, Integer> TELEPORT_INTERPOLATION_DURATION;
        public final SimpleEntityDataKey<Display, Vector> TRANSLATION;
        public final SimpleEntityDataKey<Display, Vector> SCALE;
        public final SimpleEntityDataKey<Display, Quaternionfc> ROTATION_LEFT;
        public final SimpleEntityDataKey<Display, Quaternionfc> ROTATION_RIGHT;
        public final EnumByteEntityDataKey<Display, Display.Billboard> BILLBOARD;
        public final MappedEntityDataKey<Display, Display.Brightness, Integer> BRIGHTNESS;
        public final SimpleEntityDataKey<Display, Float> VIEW_RANGE;
        public final SimpleEntityDataKey<Display, Float> SHADOW_RADIUS;
        public final SimpleEntityDataKey<Display, Float> SHADOW_STRENGTH;
        public final SimpleEntityDataKey<Display, Float> DISPLAY_WIDTH;
        public final SimpleEntityDataKey<Display, Float> DISPLAY_HEIGHT;
        public final MappedEntityDataKey<Display, Color, Integer> GLOW_COLOR;

        public DisplayKeys(MetadataKeyRegistry r) {
            INTERPOLATION_DELAY = r.register("INTERPOLATION_DELAY", Display.class, Integer.class, Display::getInterpolationDelay, Display::setInterpolationDelay, 0);
            INTERPOLATION_DURATION = r.register("INTERPOLATION_DURATION", Display.class, Integer.class, Display::getInterpolationDuration, Display::setInterpolationDuration, 0);
            TELEPORT_INTERPOLATION_DURATION = r.register("TELEPORT_INTERPOLATION_DURATION", Display.class, Integer.class, Display::getTeleportDuration, Display::setTeleportDuration, 0);
            TRANSLATION = r.register("TRANSLATION", Display.class, Vector.class, display -> Vector.fromJOML(display.getTransformation().getTranslation()), (display, vector) -> {
                var transformation = display.getTransformation();
                display.setTransformation(new Transformation(vector.toVector3f(), transformation.getLeftRotation(), transformation.getScale(), transformation.getRightRotation()));
            }, new Vector());
            SCALE = r.register("SCALE", Display.class, Vector.class, display -> Vector.fromJOML(display.getTransformation().getScale()), (display, vector) -> {
                var transformation = display.getTransformation();
                display.setTransformation(new Transformation(transformation.getTranslation(), transformation.getLeftRotation(), vector.toVector3f(), transformation.getRightRotation()));
            }, new Vector(1, 1, 1));
            ROTATION_LEFT = r.register("ROTATION_LEFT", Display.class, Quaternionfc.class, display -> display.getTransformation().getLeftRotation(), (display, quaternionfc) -> {
                var transformation = display.getTransformation();
                display.setTransformation(new Transformation(transformation.getTranslation(), new Quaternionf(quaternionfc), transformation.getScale(), transformation.getRightRotation()));
            }, new Quaternionf(0, 0, 0, 1));
            ROTATION_RIGHT = r.register("ROTATION_RIGHT", Display.class, Quaternionfc.class, display -> display.getTransformation().getRightRotation(), (display, quaternionfc) -> {
                var transformation = display.getTransformation();
                display.setTransformation(new Transformation(transformation.getTranslation(), transformation.getLeftRotation(), transformation.getScale(), new Quaternionf(quaternionfc)));
            }, new Quaternionf(0, 0, 0, 1));
            BILLBOARD = r.registerEnumByte("BILLBOARD", Display.class, Display.Billboard.class, Display::getBillboard, Display::setBillboard, Display.Billboard.FIXED);
            BRIGHTNESS = r.registerMapped("BRIGHTNESS", Display.class, Display.Brightness.class, Integer.class, Display::getBrightness, Display::setBrightness, integer -> integer == -1 ? null : new Display.Brightness((integer >> 4) & 0xFFFF, (integer >> 20) & 0xFFFF), brightness -> brightness == null ? -1 : brightness.getBlockLight() << 4 | brightness.getSkyLight() << 20, null);
            VIEW_RANGE = r.register("VIEW_RANGE", Display.class, Float.class, Display::getViewRange, Display::setViewRange, 1f);
            SHADOW_RADIUS = r.register("SHADOW_RADIUS", Display.class, Float.class, Display::getShadowRadius, Display::setShadowRadius, 0f);
            SHADOW_STRENGTH = r.register("SHADOW_STRENGTH", Display.class, Float.class, Display::getShadowStrength, Display::setShadowStrength, 1f);
            DISPLAY_WIDTH = r.register("DISPLAY_WIDTH", Display.class, Float.class, Display::getDisplayWidth, Display::setDisplayWidth, 0f);
            DISPLAY_HEIGHT = r.register("DISPLAY_HEIGHT", Display.class, Float.class, Display::getDisplayHeight, Display::setDisplayHeight, 0f);
            GLOW_COLOR = r.registerMapped("GLOW_COLOR", Display.class, Color.class, Integer.class, Display::getGlowColorOverride, Display::setGlowColorOverride, integer -> integer == -1 ? null : Color.fromRGB(integer & 16777215), color -> color == null ? -1 : color.asRGB(), null); // unsure if this AND operation is needed
        }

    }

    public static final class BlockDisplayKeys {
        public final SimpleEntityDataKey<BlockDisplay, BlockData> DISPLAY_BLOCK;

        public BlockDisplayKeys(MetadataKeyRegistry r) {
            DISPLAY_BLOCK = r.register("BLOCK", BlockDisplay.class, BlockData.class, BlockDisplay::getBlock, BlockDisplay::setBlock, BlockType.AIR.createBlockData());
        }

    }

    public static final class ItemDisplayKeys {
        public final SimpleEntityDataKey<ItemDisplay, ItemStack> DISPLAY_ITEM;
        public final EnumByteEntityDataKey<ItemDisplay, ItemDisplay.ItemDisplayTransform> TRANSFORM;

        public ItemDisplayKeys(MetadataKeyRegistry r) {
            DISPLAY_ITEM = r.register("ITEM", ItemDisplay.class, ItemStack.class, ItemDisplay::getItemStack, ItemDisplay::setItemStack, ItemStack.empty());
            TRANSFORM = r.registerEnumByte("TRANSFORM", ItemDisplay.class, ItemDisplay.ItemDisplayTransform.class, ItemDisplay::getItemDisplayTransform, ItemDisplay::setItemDisplayTransform, ItemDisplay.ItemDisplayTransform.NONE);
        }

    }

    public static final class TextDisplayKeys {
        public final SimpleEntityDataKey<TextDisplay, Component> TEXT;
        public final SimpleEntityDataKey<TextDisplay, Integer> LINE_WIDTH;
        public final MappedEntityDataKey<TextDisplay, Color, Integer> BACKGROUND_COLOR;
        public final SimpleEntityDataKey<TextDisplay, Byte> TEXT_OPACITY;
        public final FlagsEntityDataKey<TextDisplay, TextDisplayFlags> TEXT_FLAGS;
        public final FlagEntityDataKey<TextDisplay, TextDisplayFlags, Boolean> HAS_SHADOW;
        public final FlagEntityDataKey<TextDisplay, TextDisplayFlags, Boolean> SEE_THROUGH;
        public final FlagEntityDataKey<TextDisplay, TextDisplayFlags, Boolean> DEFAULT_BACKGROUND;
        public final FlagEntityDataKey<TextDisplay, TextDisplayFlags, TextDisplay.TextAlignment> ALIGNMENT;

        public TextDisplayKeys(MetadataKeyRegistry r) {
            TEXT = r.register("TEXT", TextDisplay.class, Component.class, TextDisplay::text, TextDisplay::text, Component.empty());
            LINE_WIDTH = r.register("LINE_WIDTH", TextDisplay.class, Integer.class, TextDisplay::getLineWidth, TextDisplay::setLineWidth, 200);
            BACKGROUND_COLOR = r.registerMapped("BACKGROUND_COLOR", TextDisplay.class, Color.class, Integer.class, TextDisplay::getBackgroundColor, TextDisplay::setBackgroundColor, integer -> integer == 1073741824 ? null : Color.fromARGB(integer), color -> color == null ? 1073741824 : color.asARGB(), null);
            TEXT_OPACITY = r.register("TEXT_OPACITY", TextDisplay.class, Byte.class, TextDisplay::getTextOpacity, TextDisplay::setTextOpacity, (byte) -1);
            TEXT_FLAGS = r.registerFlags("TEXT_FLAGS", TextDisplay.class, TextDisplayFlags.class);
            HAS_SHADOW = r.registerFlag(TEXT_FLAGS, TextDisplayFlags.HAS_SHADOW);
            SEE_THROUGH = r.registerFlag(TEXT_FLAGS, TextDisplayFlags.SEE_THROUGH);
            DEFAULT_BACKGROUND = r.registerFlag(TEXT_FLAGS, TextDisplayFlags.DEFAULT_BACKGROUND);
            ALIGNMENT = r.registerFlag(TEXT_FLAGS, TextDisplayFlags.ALIGNMENT);
        }

    }

    public static final class LivingEntityKeys {
        public final FlagsEntityDataKey<LivingEntity, LivingEntityFlags> LIVING_ENTITY_FLAGS;
        public final FlagEntityDataKey<LivingEntity, LivingEntityFlags, Boolean> HAND_ACTIVE;
        public final FlagEntityDataKey<LivingEntity, LivingEntityFlags, EquipmentSlot> HAND;
        public final FlagEntityDataKey<LivingEntity, LivingEntityFlags, Boolean> RIPTIDE_ATTACK;
        public final SimpleEntityDataKey<LivingEntity, Float> HEALTH;
        public final ListEntityDataKey<LivingEntity, ParticleWrapper> POTION_EFFECT_COLOR;
        public final BooleanEntityDataKey<LivingEntity> POTION_EFFECT_AMBIENT;
        public final SimpleEntityDataKey<LivingEntity, Integer> ARROW_COUNT;
        public final SimpleEntityDataKey<LivingEntity, Integer> BEE_STINGER_COUNT;
        public final OptionalEntityDataKey<LivingEntity, BlockVector> SLEEPING_BED_LOCATION;

        public LivingEntityKeys(MetadataKeyRegistry r) {
            LIVING_ENTITY_FLAGS = r.registerFlags("LIVING_ENTITY_FLAGS", LivingEntity.class, LivingEntityFlags.class);
            HAND_ACTIVE = r.registerFlag(LIVING_ENTITY_FLAGS, LivingEntityFlags.HAND_ACTIVE);
            HAND = r.registerFlag(LIVING_ENTITY_FLAGS, LivingEntityFlags.HAND);
            RIPTIDE_ATTACK = r.registerFlag(LIVING_ENTITY_FLAGS, LivingEntityFlags.RIPTIDE_ATTACK);
            HEALTH = r.register("HEALTH", LivingEntity.class, Float.class, livingEntity -> (float) livingEntity.getHealth(), (livingEntity, aFloat) ->  livingEntity.setHealth(aFloat), 1f);
            POTION_EFFECT_COLOR = r.registerList("POTION_EFFECT_COLOR", LivingEntity.class, ParticleWrapper.class, livingEntity -> CommonUtils.filterMap(livingEntity.getActivePotionEffects(), PotionEffect::hasParticles, EntityUtils::getParticleOptions), null, List.of());
            POTION_EFFECT_AMBIENT = r.registerBoolean("POTION_EFFECT_AMBIENT", LivingEntity.class, livingEntity -> livingEntity.getActivePotionEffects().stream().allMatch(potionEffect -> potionEffect.hasParticles() && !potionEffect.isAmbient()), null);
            ARROW_COUNT = r.register("ARROW_COUNT", LivingEntity.class, Integer.class, LivingEntity::getArrowsInBody, LivingEntity::setArrowsInBody, 0);
            BEE_STINGER_COUNT = r.register("BEE_STINGER_COUNT", LivingEntity.class, Integer.class, LivingEntity::getBeeStingersInBody, LivingEntity::setBeeStingersInBody, 0);
            SLEEPING_BED_LOCATION = r.registerOptional("SLEEPING_BED_LOCATION", LivingEntity.class, BlockVector.class, EntityUtils::getSleepingPosition, EntityUtils::setSleepingPosition);
        }

    }

    public static final class ArmorStandKeys {
        public final FlagsEntityDataKey<ArmorStand, ArmorStandFlags> ARMOR_STAND_FLAGS;
        public final FlagEntityDataKey<ArmorStand, ArmorStandFlags, Boolean> IS_SMALL;
        public final FlagEntityDataKey<ArmorStand, ArmorStandFlags, Boolean> HAS_ARMS;
        public final FlagEntityDataKey<ArmorStand, ArmorStandFlags, Boolean> HAS_NO_BASEPLATE;
        public final FlagEntityDataKey<ArmorStand, ArmorStandFlags, Boolean> IS_MARKER;
        public final SimpleEntityDataKey<ArmorStand, Rotations> HEAD_ROTATION;
        public final SimpleEntityDataKey<ArmorStand, Rotations> BODY_ROTATION;
        public final SimpleEntityDataKey<ArmorStand, Rotations> LEFT_ARM_ROTATION;
        public final SimpleEntityDataKey<ArmorStand, Rotations> RIGHT_ARM_ROTATION;
        public final SimpleEntityDataKey<ArmorStand, Rotations> LEFT_LEG_ROTATION;
        public final SimpleEntityDataKey<ArmorStand, Rotations> RIGHT_LEG_ROTATION;

        public ArmorStandKeys(MetadataKeyRegistry r) {
            ARMOR_STAND_FLAGS = r.registerFlags("ARMOR_STAND_FLAGS", ArmorStand.class, ArmorStandFlags.class);
            IS_SMALL = r.registerFlag(ARMOR_STAND_FLAGS, ArmorStandFlags.IS_SMALL);
            HAS_ARMS = r.registerFlag(ARMOR_STAND_FLAGS, ArmorStandFlags.HAS_ARMS);
            HAS_NO_BASEPLATE = r.registerFlag(ARMOR_STAND_FLAGS, ArmorStandFlags.HAS_NO_BASEPLATE);
            IS_MARKER = r.registerFlag(ARMOR_STAND_FLAGS, ArmorStandFlags.IS_MARKER);
            HEAD_ROTATION = r.register("HEAD_ROTATION", ArmorStand.class, Rotations.class, ArmorStand::getHeadRotations, ArmorStand::setHeadRotations, Rotations.ZERO);
            BODY_ROTATION = r.register("BODY_ROTATION", ArmorStand.class, Rotations.class, ArmorStand::getBodyRotations, ArmorStand::setBodyRotations, Rotations.ZERO);
            LEFT_ARM_ROTATION = r.register("LEFT_ARM_ROTATION", ArmorStand.class, Rotations.class, ArmorStand::getLeftArmRotations, ArmorStand::setLeftArmRotations, Rotations.ofDegrees(-10, 0, -10));
            RIGHT_ARM_ROTATION = r.register("RIGHT_ARM_ROTATION", ArmorStand.class, Rotations.class, ArmorStand::getRightArmRotations, ArmorStand::setRightArmRotations, Rotations.ofDegrees(-15, 0, -10));
            LEFT_LEG_ROTATION = r.register("LEFT_LEG_ROTATION", ArmorStand.class, Rotations.class, ArmorStand::getLeftLegRotations, ArmorStand::setLeftLegRotations, Rotations.ofDegrees(-1, 0, -1));
            RIGHT_LEG_ROTATION = r.register("RIGHT_LEG_ROTATION", ArmorStand.class, Rotations.class, ArmorStand::getRightLegRotations, ArmorStand::setRightLegRotations, Rotations.ofDegrees(1, 0, 1));
        }

    }

    public static final class AvatarKeys {
        public final MappedEntityDataKey<LivingEntity, MainHand, Byte> MAIN_HAND;
        public final FlagsEntityDataKey<LivingEntity, AvatarFlags> PLAYER_FLAGS;
        public final FlagEntityDataKey<LivingEntity, AvatarFlags, Boolean> CAPE;
        public final FlagEntityDataKey<LivingEntity, AvatarFlags, Boolean> JACKET;
        public final FlagEntityDataKey<LivingEntity, AvatarFlags, Boolean> LEFT_SLEEVE;
        public final FlagEntityDataKey<LivingEntity, AvatarFlags, Boolean> RIGHT_SLEEVE;
        public final FlagEntityDataKey<LivingEntity, AvatarFlags, Boolean> LEFT_PANTS;
        public final FlagEntityDataKey<LivingEntity, AvatarFlags, Boolean> RIGHT_PANTS;
        public final FlagEntityDataKey<LivingEntity, AvatarFlags, Boolean> HAT;

        public AvatarKeys(MetadataKeyRegistry r) {
            var classes = List.of(Player.class, Mannequin.class);
            MAIN_HAND = r.registerMapped("MAIN_HAND", classes, MainHand.class, Byte.class, EntityUtils::getMainHand, EntityUtils::setMainHand, aByte -> MainHand.values()[aByte], mainHand -> (byte) mainHand.ordinal(), MainHand.RIGHT);
            PLAYER_FLAGS = r.registerFlags("PLAYER_FLAGS", classes, AvatarFlags.class);
            CAPE = r.registerFlag(PLAYER_FLAGS, AvatarFlags.CAPE);
            JACKET = r.registerFlag(PLAYER_FLAGS, AvatarFlags.JACKET);
            LEFT_SLEEVE = r.registerFlag(PLAYER_FLAGS, AvatarFlags.LEFT_SLEEVE);
            RIGHT_SLEEVE = r.registerFlag(PLAYER_FLAGS, AvatarFlags.RIGHT_SLEEVE);
            LEFT_PANTS = r.registerFlag(PLAYER_FLAGS, AvatarFlags.LEFT_PANTS);
            RIGHT_PANTS = r.registerFlag(PLAYER_FLAGS, AvatarFlags.RIGHT_PANTS);
            HAT = r.registerFlag(PLAYER_FLAGS, AvatarFlags.HAT);
        }
    }

    public static final class MannequinKeys {
        public final SimpleEntityDataKey<Mannequin, ResolvableProfile> PROFILE;
        public final BooleanEntityDataKey<Mannequin> IMMOVABLE;
        public final OptionalEntityDataKey<Mannequin, Component> DESCRIPTION;

        public MannequinKeys(MetadataKeyRegistry r) {
            PROFILE = r.register("PROFILE", Mannequin.class, ResolvableProfile.class, Mannequin::getProfile, Mannequin::setProfile, Mannequin.defaultProfile());
            IMMOVABLE = r.registerBoolean("IMMOVABLE", Mannequin.class, Mannequin::isImmovable, Mannequin::setImmovable);
            DESCRIPTION = r.registerOptional("DESCRIPTION", Mannequin.class, Component.class, Mannequin::getDescription, Mannequin::setDescription);
        }
    }

    public static final class PlayerKeys {
        public final SimpleEntityDataKey<Player, Float> ADDITIONAL_HEARTS;
        public final SimpleEntityDataKey<Player, Integer> SCORE;
        public final OptionalEntityDataKey<Player, Integer> LEFT_SHOULDER_DATA;
        public final OptionalEntityDataKey<Player, Integer> RIGHT_SHOULDER_DATA;

        public PlayerKeys(MetadataKeyRegistry r) {
            ADDITIONAL_HEARTS = r.register("ADDITIONAL_HEARTS", Player.class, Float.class, player -> (float) player.getAbsorptionAmount(), (player, aFloat) -> player.setAbsorptionAmount(aFloat), 0f);
            SCORE = r.register("SCORE", Player.class, Integer.class, Player::getDeathScreenScore, Player::setDeathScreenScore, 0);
            LEFT_SHOULDER_DATA = r.registerOptional("LEFT_SHOULDER_DATA", Player.class, Integer.class, EntityUtils::getLeftShoulderParrotVariantId, EntityUtils::setLeftShoulderParrotVariantId);
            RIGHT_SHOULDER_DATA = r.registerOptional("RIGHT_SHOULDER_DATA", Player.class, Integer.class, EntityUtils::getRightShoulderParrotVariantId, EntityUtils::setRightShoulderParrotVariantId);
        }
    }

    public static final class MobKeys {
        public final FlagsEntityDataKey<Mob, MobFlags> MOB_FLAGS;
        public final FlagEntityDataKey<Mob, MobFlags, Boolean> NO_AI;
        public final FlagEntityDataKey<Mob, MobFlags, Boolean> IS_LEFT_HANDED;
        public final FlagEntityDataKey<Mob, MobFlags, Boolean> IS_AGGRESSIVE;

        public MobKeys(MetadataKeyRegistry r) {
            MOB_FLAGS = r.registerFlags("MOB_FLAGS", Mob.class, MobFlags.class);
            NO_AI = r.registerFlag(MOB_FLAGS, MobFlags.NO_AI);
            IS_LEFT_HANDED = r.registerFlag(MOB_FLAGS, MobFlags.IS_LEFT_HANDED);
            IS_AGGRESSIVE = r.registerFlag(MOB_FLAGS, MobFlags.IS_AGGRESSIVE);
        }

    }

    public static final class BatKeys {
        public final FlagsEntityDataKey<Bat, BatFlags> BAT_FLAGS;
        public final FlagEntityDataKey<Bat, BatFlags, Boolean> IS_HANGING;

        public BatKeys(MetadataKeyRegistry r) {
            BAT_FLAGS = r.registerFlags("BAT_FLAGS", Bat.class, BatFlags.class);
            IS_HANGING = r.registerFlag(BAT_FLAGS, BatFlags.IS_HANGING);
        }

    }

    public static final class EnderDragonKeys {
        public final EnumIntEntityDataKey<EnderDragon, EnderDragon.Phase> DRAGON_PHASE;

        public EnderDragonKeys(MetadataKeyRegistry r) {
            DRAGON_PHASE = r.registerEnumInt("DRAGON_PHASE", EnderDragon.class, EnderDragon.Phase.class, EnderDragon::getPhase, EnderDragon::setPhase, EnderDragon.Phase.CIRCLING);
        }

    }

    public static final class GhastKeys {
        public final BooleanEntityDataKey<Ghast> ATTACKING;

        public GhastKeys(MetadataKeyRegistry r) {
            ATTACKING = r.registerBoolean("ATTACKING", Ghast.class, Ghast::isCharging, Ghast::setCharging);
        }

    }

    public static final class PhantomKeys {
        public final SimpleEntityDataKey<Phantom, Integer> PHANTOM_SIZE;

        public PhantomKeys(MetadataKeyRegistry r) {
            PHANTOM_SIZE = r.register("PHANTOM_SIZE", Phantom.class, Integer.class, Phantom::getSize, Phantom::setSize, 0);
        }

    }
    public static final class SlimeKeys {
        @Availability(removedIn = "26.2")
        public final SimpleEntityDataKey<Slime, Integer> SLIME_SIZE;

        public SlimeKeys(MetadataKeyRegistry r) {
            if (r.is_26_2)
                SLIME_SIZE = null;
            else
                SLIME_SIZE = r.register("SLIME_SIZE", Slime.class, Integer.class, Slime::getSize, Slime::setSize, 1);
        }

    }

    public static final class AllayKeys {
        public final BooleanEntityDataKey<Allay> DANCING;
        public final BooleanEntityDataKey<Allay> CAN_DUPLICATE;

        public AllayKeys(MetadataKeyRegistry r) {
            DANCING = r.registerBoolean("DANCING", Allay.class, Allay::isDancing, (allay, aBoolean) -> {
                if (aBoolean) allay.startDancing();
                else allay.stopDancing();
            });
            CAN_DUPLICATE = r.registerBoolean("CAN_DUPLICATE", Allay.class, Allay::canDuplicate, Allay::setCanDuplicate, true);
        }

    }

    public static final class IronGolemKeys {
        public final FlagsEntityDataKey<IronGolem, IronGolemFlags> IRON_GOLEM_FLAGS;
        public final FlagEntityDataKey<IronGolem, IronGolemFlags, Boolean> IS_PLAYER_CREATED;

        public IronGolemKeys(MetadataKeyRegistry r) {
            IRON_GOLEM_FLAGS = r.registerFlags("IRON_GOLEM_FLAGS", IronGolem.class, IronGolemFlags.class);
            IS_PLAYER_CREATED = r.registerFlag(IRON_GOLEM_FLAGS, IronGolemFlags.IS_PLAYER_CREATED);
        }

    }

    public static final class ShulkerKeys {
        public final SimpleEntityDataKey<Shulker, BlockFace> ATTACHED_FACE;
        public final SimpleEntityDataKey<Shulker, Byte> SHIELD_HEIGHT;
        public final EnumByteEntityDataKey<Shulker, DyeColor> SHULKER_COLOR;

        public ShulkerKeys(MetadataKeyRegistry r) {
            ATTACHED_FACE = r.register("ATTACHED_FACE", Shulker.class, BlockFace.class, Shulker::getAttachedFace, Shulker::setAttachedFace, BlockFace.DOWN);
            SHIELD_HEIGHT = r.register("SHIELD_HEIGHT", Shulker.class, Byte.class, shulker -> (byte) shulker.getPeek(), (shulker, aByte) -> shulker.setPeek(aByte), (byte) 0);
            SHULKER_COLOR = r.registerEnumByte("SHULKER_COLOR", Shulker.class, DyeColor.class, Colorable::getColor, Colorable::setColor, null);
        }

    }

    public static final class SnowmanKeys {
        public final FlagsEntityDataKey<Snowman, SnowmanFlags> SNOWMAN_FLAGS;
        public final FlagEntityDataKey<Snowman, SnowmanFlags, Boolean> HAS_PUMPKIN;

        public SnowmanKeys(MetadataKeyRegistry r) {
            SNOWMAN_FLAGS = r.registerFlags("SNOWMAN_FLAGS", Snowman.class, SnowmanFlags.class);
            HAS_PUMPKIN = r.registerFlag(SNOWMAN_FLAGS, SnowmanFlags.HAS_PUMPKIN);
        }

    }

    public static final class TadpoleKeys {
        public final BooleanEntityDataKey<Tadpole> TADPOLE_IS_FROM_BUCKET;
        @Availability(addedIn = "26.1")
        public final BooleanEntityDataKey<Tadpole> IS_AGE_LOCKED;

        public TadpoleKeys(MetadataKeyRegistry r) {
            TADPOLE_IS_FROM_BUCKET = r.registerBoolean("TADPOLE_IS_FROM_BUCKET", Tadpole.class, Bucketable::isFromBucket, Bucketable::setFromBucket);
            if (r.is_26_1)
                IS_AGE_LOCKED = r.registerBoolean("IS_AGE_LOCKED", Tadpole.class, Tadpole::getAgeLock, Tadpole::setAgeLock);
            else
                IS_AGE_LOCKED = null;
        }

    }

    public static final class AgeableKeys {
        public final BooleanEntityDataKey<Ageable> IS_BABY_ANIMAL;
        @Availability(addedIn = "26.1")
        public final BooleanEntityDataKey<Ageable> IS_AGE_LOCKED;

        public AgeableKeys(MetadataKeyRegistry r) {
            IS_BABY_ANIMAL = r.registerBoolean("IS_BABY_ANIMAL", Ageable.class, ageable -> !ageable.isAdult(), (ageable, bool) -> {
                if (bool) ageable.setBaby();
                else ageable.setAdult();
            });
            if (r.is_26_1)
                IS_AGE_LOCKED = r.registerBoolean("IS_AGE_LOCKED", Ageable.class, Ageable::getAgeLock, Ageable::setAgeLock);
            else
                IS_AGE_LOCKED = null;
        }

    }

    public static final class GlowSquidKeys {
        public final SimpleEntityDataKey<GlowSquid, Integer> DARK_TICKS_REMAINING;

        public GlowSquidKeys(MetadataKeyRegistry r) {
            DARK_TICKS_REMAINING = r.register("DARK_TICKS_REMAINING", GlowSquid.class, Integer.class, GlowSquid::getDarkTicksRemaining, GlowSquid::setDarkTicksRemaining, 0);
        }

    }

    public static final class ArmadilloKeys {
        public final EnumIntEntityDataKey<Armadillo, Armadillo.State> ARMADILLO_STATE;

        public ArmadilloKeys(MetadataKeyRegistry r) {
            ARMADILLO_STATE = r.registerEnumInt("ARMADILLO_STATE", Armadillo.class, Armadillo.State.class, Armadillo::getState, EntityUtils::setArmadilloState);
        }

    }

    public static final class AxolotlKeys {
        public final EnumIntEntityDataKey<Axolotl, Axolotl.Variant> AXOLOTL_VARIANT;
        public final BooleanEntityDataKey<Axolotl> IS_PLAYING_DEAD;
        public final BooleanEntityDataKey<Axolotl> IS_FROM_BUCKET;

        public AxolotlKeys(MetadataKeyRegistry r) {
            AXOLOTL_VARIANT = r.registerEnumInt("AXOLOTL_VARIANT", Axolotl.class, Axolotl.Variant.class, Axolotl::getVariant, Axolotl::setVariant);
            IS_PLAYING_DEAD = r.registerBoolean("IS_PLAYING_DEAD", Axolotl.class, Axolotl::isPlayingDead, Axolotl::setPlayingDead);
            IS_FROM_BUCKET = r.registerBoolean("IS_FROM_BUCKET", Axolotl.class, Axolotl::isFromBucket, Axolotl::setFromBucket);
        }

    }

    public static final class BeeKeys {
        public final FlagsEntityDataKey<Bee, BeeFlags> BEE_FLAGS;
        public final FlagEntityDataKey<Bee, BeeFlags, Boolean> IS_ANGRY;
        public final FlagEntityDataKey<Bee, BeeFlags, Boolean> HAS_STUNG;
        public final FlagEntityDataKey<Bee, BeeFlags, Boolean> HAS_NECTAR;
        public final SimpleEntityDataKey<Bee, Long> BEE_ANGER_TICKS;

        public BeeKeys(MetadataKeyRegistry r) {
            BEE_FLAGS = r.registerFlags("BEE_FLAGS", Bee.class, BeeFlags.class);
            IS_ANGRY = r.registerFlag(BEE_FLAGS, BeeFlags.IS_ANGRY);
            HAS_STUNG = r.registerFlag(BEE_FLAGS, BeeFlags.HAS_STUNG);
            HAS_NECTAR = r.registerFlag(BEE_FLAGS, BeeFlags.HAS_NECTAR);
            BEE_ANGER_TICKS = r.register("BEE_ANGER_TICKS", Bee.class, Long.class, EntityUtils::getBeePersistentAngerEndTime, EntityUtils::setBeePersistentAngerEndTime, -1L);
        }

    }

    public static final class ChickenKeys {
        public final RegistryIntEntityDataKey<Chicken, Chicken.Variant> CHICKEN_VARIANT;
        @Availability(addedIn = "26.1")
        public final RegistryIntEntityDataKey<Chicken, Chicken.SoundVariant> CHICKEN_SOUND_VARIANT;

        public ChickenKeys(MetadataKeyRegistry r) {
            CHICKEN_VARIANT = r.registerIntKeyed("CHICKEN_VARIANT", Chicken.class, Chicken.Variant.class, Chicken::getVariant, Chicken::setVariant, RegistryKey.CHICKEN_VARIANT, Chicken.Variant.TEMPERATE);
            if (r.is_26_1)
                CHICKEN_SOUND_VARIANT = r.registerIntKeyed("CHICKEN_SOUND_VARIANT", Chicken.class, Chicken.SoundVariant.class, Chicken::getSoundVariant, Chicken::setSoundVariant, RegistryKey.CHICKEN_SOUND_VARIANT, Chicken.SoundVariant.CLASSIC);
            else
                CHICKEN_SOUND_VARIANT = null;
        }

    }

    public static final class CowKeys {
        public final RegistryIntEntityDataKey<Cow, Cow.Variant> COW_VARIANT;
        @Availability(addedIn = "26.1")
        public final RegistryIntEntityDataKey<Cow, Cow.SoundVariant> COW_SOUND_VARIANT;

        public CowKeys(MetadataKeyRegistry r) {
            COW_VARIANT = r.registerIntKeyed("COW_VARIANT", Cow.class, Cow.Variant.class, Cow::getVariant, Cow::setVariant,  RegistryKey.COW_VARIANT, Cow.Variant.TEMPERATE);
            if (r.is_26_1)
                COW_SOUND_VARIANT = r.registerIntKeyed("COW_SOUND_VARIANT", Cow.class, Cow.SoundVariant.class, Cow::getSoundVariant, Cow::setSoundVariant, RegistryKey.COW_SOUND_VARIANT, Cow.SoundVariant.CLASSIC);
            else
                COW_SOUND_VARIANT = null;
        }

    }

    public static final class FoxKeys {
        public final EnumIntEntityDataKey<Fox, Fox.Type> FOX_TYPE;
        public final FlagsEntityDataKey<Fox, FoxFlags> FOX_FLAGS;
        public final FlagEntityDataKey<Fox, FoxFlags, Boolean> IS_SITTING, IS_CROUCHING, IS_INTERESTED, IS_POUNCING, IS_SLEEPING, IS_FACEPLANTED, IS_DEFENDING;
        @SuppressWarnings("rawtypes")
        public final OptionalEntityDataKey<Fox, ServerEntityReference> FIRST_TRUSTED_PLAYER, SECOND_TRUSTED_PLAYER;

        public FoxKeys(MetadataKeyRegistry r) {
            FOX_TYPE = r.registerEnumInt("FOX_TYPE", Fox.class, Fox.Type.class, Fox::getFoxType, Fox::setFoxType);
            FOX_FLAGS = r.registerFlags("FOX_FLAGS", Fox.class, FoxFlags.class);
            IS_SITTING = r.registerFlag(FOX_FLAGS, FoxFlags.IS_SITTING);
            IS_CROUCHING = r.registerFlag(FOX_FLAGS, FoxFlags.IS_CROUCHING);
            IS_INTERESTED = r.registerFlag(FOX_FLAGS, FoxFlags.IS_INTERESTED);
            IS_POUNCING = r.registerFlag(FOX_FLAGS, FoxFlags.IS_POUNCING);
            IS_SLEEPING = r.registerFlag(FOX_FLAGS, FoxFlags.IS_SLEEPING);
            IS_FACEPLANTED = r.registerFlag(FOX_FLAGS, FoxFlags.IS_FACEPLANTED);
            IS_DEFENDING = r.registerFlag(FOX_FLAGS, FoxFlags.IS_DEFENDING);
            // this is very scuffed, but it should be fine!
            FIRST_TRUSTED_PLAYER = r.registerOptional("FIRST_TRUSTED_PLAYER", Fox.class, ServerEntityReference.class, fox -> {
                var trustedPlayer = fox.getFirstTrustedPlayer();
                return trustedPlayer == null ? null : ServerEntityReference.of(trustedPlayer.getUniqueId());
            }, (fox, serverEntityReference) -> {
                fox.setFirstTrustedPlayer(serverEntityReference == null ? null : new AnimalTamer() {
                    @Override
                    public @Nullable String getName() {
                        return null;
                    }

                    @Override
                    public @NotNull UUID getUniqueId() {
                        return serverEntityReference.getUUID();
                    }
                });
            });
            // this is very scuffed, but it should be fine!
            SECOND_TRUSTED_PLAYER = r.registerOptional("SECOND_TRUSTED_PLAYER", Fox.class, ServerEntityReference.class, fox -> {
                var trustedPlayer = fox.getSecondTrustedPlayer();
                return trustedPlayer == null ? null : ServerEntityReference.of(trustedPlayer.getUniqueId());
            }, (fox, serverEntityReference) -> {
                fox.setSecondTrustedPlayer(serverEntityReference == null ? null : new AnimalTamer() {
                    @Override
                    public @Nullable String getName() {
                        return null;
                    }

                    @Override
                    public @NotNull UUID getUniqueId() {
                        return serverEntityReference.getUUID();
                    }
                });
            });
        }

    }

    public static final class FrogKeys {
        public final RegistryIntEntityDataKey<Frog, Frog.Variant> FROG_VARIANT;
        public final OptionalEntityDataKey<Frog, Integer> TONGUE_TARGET;

        public FrogKeys(MetadataKeyRegistry r) {
            FROG_VARIANT = r.registerIntKeyed("FROG_VARIANT", Frog.class, Frog.Variant.class, Frog::getVariant, Frog::setVariant, RegistryKey.FROG_VARIANT, Frog.Variant.TEMPERATE);
            TONGUE_TARGET = r.registerOptional("TONGUE_TARGET", Frog.class, Integer.class, frog -> {
                var tongueTarget = frog.getTongueTarget();
                return tongueTarget == null ? 0 : tongueTarget.getEntityId();
            }, (frog, integer) -> frog.setTongueTarget(integer == null ? null : LevelUtils.getEntityFromID(integer, frog.getWorld())), 0);
        }

    }

    public static final class HappyGhastKeys {
        public final BooleanEntityDataKey<HappyGhast> IS_LEASH_HOLDER;
        public final BooleanEntityDataKey<HappyGhast> STAYS_STILL;

        public HappyGhastKeys(MetadataKeyRegistry r) {
            IS_LEASH_HOLDER = r.registerBoolean("IS_LEASH_HOLDER", HappyGhast.class, EntityUtils::isLeashHolder, EntityUtils::setLeashHolder);
            STAYS_STILL = r.registerBoolean("STAYS_STILL", HappyGhast.class, EntityUtils::staysStill, EntityUtils::setStaysStill);
        }

    }

    public static final class GoatKeys {
        public final BooleanEntityDataKey<Goat> IS_SCREAMING_GOAT;
        public final BooleanEntityDataKey<Goat> HAS_LEFT_HORN;
        public final BooleanEntityDataKey<Goat> HAS_RIGHT_HORN;

        public GoatKeys(MetadataKeyRegistry r) {
            IS_SCREAMING_GOAT = r.registerBoolean("IS_SCREAMING_GOAT", Goat.class, Goat::isScreaming, Goat::setScreaming);
            HAS_LEFT_HORN = r.registerBoolean("HAS_LEFT_HORN", Goat.class, Goat::hasLeftHorn, Goat::setLeftHorn);
            HAS_RIGHT_HORN = r.registerBoolean("HAS_RIGHT_HORN", Goat.class, Goat::hasRightHorn, Goat::setRightHorn);
        }

    }

    public static final class HoglinKeys {
        public final BooleanEntityDataKey<Hoglin> ZOMBIFICATION_IMMUNITY;

        public HoglinKeys(MetadataKeyRegistry r) {
            ZOMBIFICATION_IMMUNITY = r.registerBoolean("ZOMBIFICATION_IMMUNITY", Hoglin.class, Hoglin::isImmuneToZombification, Hoglin::setImmuneToZombification);
        }

    }

    public static final class MushroomCowKeys {
        public final EnumIntEntityDataKey<MushroomCow, MushroomCow.Variant> MUSHROOM_COW_VARIANT;

        public MushroomCowKeys(MetadataKeyRegistry r) {
            MUSHROOM_COW_VARIANT = r.registerEnumInt("MUSHROOM_COW_VARIANT", MushroomCow.class, MushroomCow.Variant.class, MushroomCow::getVariant, MushroomCow::setVariant);
        }

    }

    public static final class OcelotKeys {
        public final BooleanEntityDataKey<Ocelot> IS_TRUSTING;

        public OcelotKeys(MetadataKeyRegistry r) {
            IS_TRUSTING = r.registerBoolean("IS_TRUSTING", Ocelot.class, Ocelot::isTrusting, Ocelot::setTrusting);
        }

    }

    public static final class PandaKeys {
        public final SimpleEntityDataKey<Panda, Integer> BREED_TIMER;
        public final SimpleEntityDataKey<Panda, Integer> SNEEZE_TIMER;
        public final SimpleEntityDataKey<Panda, Integer> EAT_TIMER;
        public final EnumByteEntityDataKey<Panda, Panda.Gene> MAIN_GENE, HIDDEN_GENE;
        public final FlagsEntityDataKey<Panda, PandaFlags> PANDA_FLAGS;
        public final FlagEntityDataKey<Panda, PandaFlags, Boolean> IS_SNEEZING, IS_ROLLING, IS_PANDA_SITTING, IS_ON_BACK;

        public PandaKeys(MetadataKeyRegistry r) {
            BREED_TIMER = r.register("BREED_TIMER", Panda.class, Integer.class, Panda::getUnhappyTicks, Panda::setUnhappyTicks, 0);
            SNEEZE_TIMER = r.register("SNEEZE_TIMER", Panda.class, Integer.class, Panda::getSneezeTicks, Panda::setSneezeTicks, 0);
            EAT_TIMER = r.register("EAT_TIMER", Panda.class, Integer.class, Panda::getEatingTicks, Panda::setEatingTicks, 0);
            MAIN_GENE = r.registerEnumByte("MAIN_GENE", Panda.class, Panda.Gene.class, Panda::getMainGene, Panda::setMainGene);
            HIDDEN_GENE = r.registerEnumByte("HIDDEN_GENE", Panda.class, Panda.Gene.class, Panda::getHiddenGene, Panda::setHiddenGene);
            PANDA_FLAGS = r.registerFlags("PANDA_FLAGS", Panda.class, PandaFlags.class);
            IS_SNEEZING = r.registerFlag(PANDA_FLAGS, PandaFlags.IS_SNEEZING);
            IS_ROLLING = r.registerFlag(PANDA_FLAGS, PandaFlags.IS_ROLLING);
            IS_PANDA_SITTING = r.registerFlag(PANDA_FLAGS, PandaFlags.IS_SITTING);
            IS_ON_BACK = r.registerFlag(PANDA_FLAGS, PandaFlags.IS_ON_BACK);
        }

    }

    public static final class PigKeys {
        public final SimpleEntityDataKey<Pig, Integer> CARROT_BOOST_TICKS;
        public final RegistryIntEntityDataKey<Pig, Pig.Variant> PIG_VARIANT;
        @Availability(addedIn = "26.1")
        public final RegistryIntEntityDataKey<Pig, Pig.SoundVariant> PIG_SOUND_VARIANT;

        public PigKeys(MetadataKeyRegistry r) {
            CARROT_BOOST_TICKS = r.register("CARROT_BOOST_TICKS", Pig.class, Integer.class, Pig::getBoostTicks, Pig::setBoostTicks, 0);
            PIG_VARIANT = r.registerIntKeyed("PIG_VARIANT", Pig.class, Pig.Variant.class, Pig::getVariant, Pig::setVariant, RegistryKey.PIG_VARIANT, Pig.Variant.TEMPERATE);
            if (r.is_26_1)
                PIG_SOUND_VARIANT = r.registerIntKeyed("PIG_SOUND_VARIANT", Pig.class, Pig.SoundVariant.class, Pig::getSoundVariant, Pig::setSoundVariant, RegistryKey.PIG_SOUND_VARIANT, Pig.SoundVariant.CLASSIC);
            else
                PIG_SOUND_VARIANT = null;
        }

    }

    public static final class PolarBearKeys {
        public final BooleanEntityDataKey<PolarBear> IS_STANDING_UP;

        public PolarBearKeys(MetadataKeyRegistry r) {
            IS_STANDING_UP = r.registerBoolean("IS_STANDING_UP", PolarBear.class, PolarBear::isStanding, PolarBear::setStanding);
        }

    }

    public static final class RabbitKeys {
        public final EnumIntEntityDataKey<Rabbit, Rabbit.Type> RABBIT_TYPE;

        public RabbitKeys(MetadataKeyRegistry r) {
            RABBIT_TYPE = r.registerEnumInt("RABBIT_TYPE", Rabbit.class, Rabbit.Type.class, Rabbit::getRabbitType, Rabbit::setRabbitType);
        }

    }

    public static final class SheepKeys {
        public final FlagsEntityDataKey<Sheep, SheepFlags> SHEEP_FLAGS;
        public final FlagEntityDataKey<Sheep, SheepFlags, Boolean> IS_SHEARED;
        public final FlagEntityDataKey<Sheep, SheepFlags, DyeColor> WOOL_COLOR;

        public SheepKeys(MetadataKeyRegistry r) {
            SHEEP_FLAGS = r.registerFlags("SHEEP_FLAGS", Sheep.class, SheepFlags.class);
            IS_SHEARED = r.registerFlag(SHEEP_FLAGS, SheepFlags.IS_SHEARED);
            WOOL_COLOR = r.registerFlag(SHEEP_FLAGS, SheepFlags.WOOL_COLOR);
        }

    }

    public static final class SnifferKeys {
        public final EnumIntEntityDataKey<Sniffer, Sniffer.State> SNIFFER_STATE;
        public final SimpleEntityDataKey<Sniffer, Integer> DROP_SEED_TICK;

        public SnifferKeys(MetadataKeyRegistry r) {
            SNIFFER_STATE = r.registerEnumInt("SNIFFER_STATE", Sniffer.class, Sniffer.State.class, Sniffer::getState, Sniffer::setState);
            DROP_SEED_TICK = r.register("DROP_SEED_TICK", Sniffer.class, Integer.class, EntityUtils::getDropSeedAtTick, EntityUtils::setDropSeedAtTick, 0);
        }

    }

    public static final class StriderKeys {
        public final SimpleEntityDataKey<Strider, Integer> FUNGUS_BOOST_TICKS;
        public final BooleanEntityDataKey<Strider> IS_SHAKING;

        public StriderKeys(MetadataKeyRegistry r) {
            FUNGUS_BOOST_TICKS = r.register("FUNGUS_BOOST_TICKS", Strider.class, Integer.class, Strider::getBoostTicks, Strider::setBoostTicks, 0);
            IS_SHAKING = r.registerBoolean("IS_SHAKING", Strider.class, Strider::isShivering, Strider::setShivering);
        }

    }

    public static final class TurtleKeys {
        public final BooleanEntityDataKey<Turtle> HAS_EGG, IS_LAYING_EGG;

        public TurtleKeys(MetadataKeyRegistry r) {
            HAS_EGG = r.registerBoolean("HAS_EGG", Turtle.class, Turtle::hasEgg, Turtle::setHasEgg);
            IS_LAYING_EGG = r.registerBoolean("IS_LAYING_EGG", Turtle.class, Turtle::isLayingEgg, EntityUtils::setLayingEgg);
        }

    }

    public static final class AbstractHorseKeys {
        public final FlagsEntityDataKey<AbstractHorse, AbstractHorseFlags> HORSE_FLAGS;
        public final FlagEntityDataKey<AbstractHorse, AbstractHorseFlags, Boolean> IS_TAME, HAS_BRED, IS_EATING, IS_REARING, HAS_MOUTH_OPEN;

        public AbstractHorseKeys(MetadataKeyRegistry r) {
            HORSE_FLAGS = r.registerFlags("HORSE_FLAGS", AbstractHorse.class, AbstractHorseFlags.class);
            IS_TAME = r.registerFlag(HORSE_FLAGS, AbstractHorseFlags.IS_TAME);
            HAS_BRED = r.registerFlag(HORSE_FLAGS, AbstractHorseFlags.HAS_BRED);
            IS_EATING = r.registerFlag(HORSE_FLAGS, AbstractHorseFlags.IS_EATING);
            IS_REARING = r.registerFlag(HORSE_FLAGS, AbstractHorseFlags.IS_REARING);
            HAS_MOUTH_OPEN = r.registerFlag(HORSE_FLAGS, AbstractHorseFlags.HAS_MOUTH_OPEN);
        }

    }

    public static final class CamelKeys {
        public final BooleanEntityDataKey<Camel> IS_DASHING;
        public final SimpleEntityDataKey<Camel, Long> LAST_POSE_CHANGE_TICK;

        public CamelKeys(MetadataKeyRegistry r) {
            IS_DASHING = r.registerBoolean("IS_DASHING", Camel.class, Camel::isDashing, Camel::setDashing);
            LAST_POSE_CHANGE_TICK = r.register("LAST_POSE_CHANGE_TICK", Camel.class, Long.class, EntityUtils::getLastPoseChangeTick, EntityUtils::setLastPoseChangeTick, 0L);
        }

    }

    public static final class HorseKeys {
        public final SplitEntityDataKey<Horse, Integer> HORSE_VARIANT;
        public final SplitComponentEntityDataKey<Horse, Integer, Horse.Color> HORSE_COLOR;
        public final SplitComponentEntityDataKey<Horse, Integer, Horse.Style> HORSE_STYLE;

        public HorseKeys(MetadataKeyRegistry r) {
            HORSE_VARIANT = r.registerSplit("HORSE_VARIANT", Horse.class, Integer.class, EntityUtils::getVariant, EntityUtils::setVariant, 0);
            HORSE_COLOR = r.registerSplitComponent("HORSE_COLOR", HORSE_VARIANT, Horse.Color.class, integer -> Horse.Color.values()[integer & 0xFF], (integer, color) -> (integer & ~0xFF) | (color.ordinal() & 0xFF), Horse.Color.WHITE);
            HORSE_STYLE = r.registerSplitComponent("HORSE_STYLE", HORSE_VARIANT, Horse.Style.class, integer -> Horse.Style.values()[(integer >>> 8) & 0xFF], (integer, style) -> (integer & ~(0xFF << 8)) | ((style.ordinal() & 0xFF) << 8), Horse.Style.NONE);
        }

    }

    public static final class ChestedHorseKeys {
        public final BooleanEntityDataKey<ChestedHorse> HAS_CHEST;

        public ChestedHorseKeys(MetadataKeyRegistry r) {
            HAS_CHEST = r.registerBoolean("HAS_CHEST", ChestedHorse.class, ChestedHorse::isCarryingChest, ChestedHorse::setCarryingChest, false);
        }

    }

    public static final class LlamaKeys {
        public final SimpleEntityDataKey<Llama, Integer> LLAMA_STRENGTH;
        public final EnumIntEntityDataKey<Llama, Llama.Color> LLAMA_VARIANT;

        public LlamaKeys(MetadataKeyRegistry r) {
            LLAMA_STRENGTH = r.register("LLAMA_STRENGTH", Llama.class, Integer.class, Llama::getStrength, Llama::setStrength, 0);
            LLAMA_VARIANT = r.registerEnumInt("LLAMA_VARIANT", Llama.class, Llama.Color.class, Llama::getColor, Llama::setColor, Llama.Color.CREAMY);
        }

    }

    public static final class TameableKeys {
        public final FlagsEntityDataKey<Tameable, TameableFlags> TAMEABLE_FLAGS;
        public final FlagEntityDataKey<Tameable, TameableFlags, Boolean> IS_SITTING, IS_TAMED;
        @SuppressWarnings("rawtypes")
        public final OptionalEntityDataKey<Tameable, ServerEntityReference> OWNER;

        public TameableKeys(MetadataKeyRegistry r) {
            TAMEABLE_FLAGS = r.registerFlags("TAMEABLE_FLAGS", Tameable.class, TameableFlags.class);
            IS_SITTING = r.registerFlag(TAMEABLE_FLAGS, TameableFlags.IS_SITTING);
            IS_TAMED = r.registerFlag(TAMEABLE_FLAGS, TameableFlags.IS_TAMED);
            // this is very scuffed, but it should be fine!
            OWNER = r.registerOptional("OWNER", Tameable.class, ServerEntityReference.class, ServerEntityReference::of, (tameable, reference) -> tameable.setOwner(reference == null ? null : new AnimalTamer() {
                @Override
                public @Nullable String getName() {
                    return null;
                }

                @Override
                public @NotNull UUID getUniqueId() {
                    return reference.getUUID();
                }
            }));
        }

    }

    public static final class CatKeys {
        public final RegistryIntEntityDataKey<Cat, Cat.Type> CAT_VARIANT;
        public final BooleanEntityDataKey<Cat> IS_LYING, IS_LOOKING_UP;
        public final EnumIntEntityDataKey<Cat, DyeColor> CAT_COLLAR_COLOR;
        @Availability(addedIn = "26.1")
        public final RegistryIntEntityDataKey<Cat, Cat.SoundVariant> CAT_SOUND_VARIANT;

        public CatKeys(MetadataKeyRegistry r) {
            CAT_VARIANT = r.registerIntKeyed("CAT_VARIANT", Cat.class, Cat.Type.class, Cat::getCatType, Cat::setCatType, RegistryKey.CAT_VARIANT, Cat.Type.BLACK);
            IS_LYING = r.registerBoolean("IS_LYING", Cat.class, Cat::isLyingDown, Cat::setLyingDown);
            IS_LOOKING_UP = r.registerBoolean("IS_LOOKING_UP", Cat.class, Cat::isHeadUp, Cat::setHeadUp);
            CAT_COLLAR_COLOR = r.registerEnumInt("CAT_COLLAR_COLOR", Cat.class, DyeColor.class, Cat::getCollarColor, Cat::setCollarColor, DyeColor.RED);
            if (r.is_26_1)
                CAT_SOUND_VARIANT = r.registerIntKeyed("CAT_SOUND_VARIANT", Cat.class, Cat.SoundVariant.class, Cat::getSoundVariant, Cat::setSoundVariant, RegistryKey.CAT_SOUND_VARIANT, Cat.SoundVariant.CLASSIC);
            else
                CAT_SOUND_VARIANT = null;
        }

    }

    public static final class ParrotKeys {
        public final EnumIntEntityDataKey<Parrot, Parrot.Variant> PARROT_VARIANT;

        public ParrotKeys(MetadataKeyRegistry r) {
            PARROT_VARIANT = r.registerEnumInt("PARROT_VARIANT", Parrot.class, Parrot.Variant.class, Parrot::getVariant, Parrot::setVariant, Parrot.Variant.RED);
        }

    }

    public static final class WolfKeys {
        public final BooleanEntityDataKey<Wolf> IS_INTERESTED;
        public final EnumIntEntityDataKey<Wolf, DyeColor> WOLF_COLLAR_COLOR;
        public final SimpleEntityDataKey<Wolf, Long> WOLF_ANGER_TICKS;
        public final RegistryIntEntityDataKey<Wolf, Wolf.Variant> WOLF_VARIANT;
        public final RegistryIntEntityDataKey<Wolf, Wolf.SoundVariant> WOLF_SOUND_VARIANT;

        public WolfKeys(MetadataKeyRegistry r) {
            IS_INTERESTED = r.registerBoolean("IS_WOLF_INTERESTED", Wolf.class, Wolf::isInterested, Wolf::setInterested);
            WOLF_COLLAR_COLOR = r.registerEnumInt("WOLF_COLLAR_COLOR", Wolf.class, DyeColor.class, Wolf::getCollarColor, Wolf::setCollarColor, DyeColor.RED);
            WOLF_ANGER_TICKS = r.register("WOLF_ANGER_TICKS", Wolf.class, Long.class, EntityUtils::getAngerEndTime, EntityUtils::setAngerEndTime, -1L);
            WOLF_VARIANT = r.registerIntKeyed("WOLF_VARIANT", Wolf.class, Wolf.Variant.class, Wolf::getVariant, Wolf::setVariant, RegistryKey.WOLF_VARIANT, Wolf.Variant.PALE);
            WOLF_SOUND_VARIANT = r.registerIntKeyed("WOLF_SOUND_VARIANT", Wolf.class, Wolf.SoundVariant.class, Wolf::getSoundVariant, Wolf::setSoundVariant, RegistryKey.WOLF_SOUND_VARIANT, Wolf.SoundVariant.CLASSIC);
        }

    }

    public static final class AbstractNautilusKeys {
        public final BooleanEntityDataKey<AbstractNautilus> IS_DASHING;

        public AbstractNautilusKeys(MetadataKeyRegistry r) {
            IS_DASHING = r.registerBoolean("IS_DASHING", AbstractNautilus.class, EntityUtils::isDashing, EntityUtils::setDashing);
        }
    }

    public static final class ZombieNautilusKeys {
        public final RegistryIntEntityDataKey<ZombieNautilus, ZombieNautilus.Variant> NAUTILUS_VARIANT;

        public ZombieNautilusKeys(MetadataKeyRegistry r) {
            NAUTILUS_VARIANT = r.registerIntKeyed("NAUTILUS_VARIANT", ZombieNautilus.class, ZombieNautilus.Variant.class, ZombieNautilus::getVariant, ZombieNautilus::setVariant, RegistryKey.ZOMBIE_NAUTILUS_VARIANT, ZombieNautilus.Variant.TEMPERATE);
        }

    }

    public static final class AbstractVillagerKeys {
        public final SimpleEntityDataKey<AbstractVillager, Integer> HEAD_SHAKE_TICKS;

        public AbstractVillagerKeys(MetadataKeyRegistry r) {
            HEAD_SHAKE_TICKS = r.register("HEAD_SHAKE_TICKS", AbstractVillager.class, Integer.class, EntityUtils::getHeadShakeTicks, EntityUtils::setHeadShakeTicks, 0);
        }

    }

    public static final class VillagerKeys {
        public final SimpleEntityDataKey<Villager, VillagerData> VILLAGER_DATA;
        // todo: villager data finalized boolean

        public VillagerKeys(MetadataKeyRegistry r) {
            VILLAGER_DATA = r.register("VILLAGER_DATA", Villager.class, VillagerData.class, VillagerData::new, (villager, villagerData) -> villagerData.apply(villager), new VillagerData());
        }

    }

    public static final class AbstractFishKeys {
        public final BooleanEntityDataKey<Bucketable> IS_FROM_BUCKET;

        public AbstractFishKeys(MetadataKeyRegistry r) {
            IS_FROM_BUCKET = r.registerBoolean("FROM_BUCKET", Bucketable.class, Bucketable::isFromBucket, Bucketable::setFromBucket);
        }

    }

    public static final class SalmonKeys {
        public final EnumIntEntityDataKey<Salmon, Salmon.Variant> SALMON_VARIANT;

        public SalmonKeys(MetadataKeyRegistry r) {
            SALMON_VARIANT = r.registerEnumInt("SALMON_VARIANT", Salmon.class, Salmon.Variant.class, Salmon::getVariant, Salmon::setVariant, Salmon.Variant.MEDIUM);
        }

    }

    public static final class TropicalFishKeys {
        public final EnumIntEntityDataKey<TropicalFish, TropicalFish.Pattern> TROPICAL_FISH_VARIANT;

        public TropicalFishKeys(MetadataKeyRegistry r) {
            TROPICAL_FISH_VARIANT = r.registerEnumInt("TROPICAL_FISH_VARIANT", TropicalFish.class, TropicalFish.Pattern.class, TropicalFish::getPattern, TropicalFish::setPattern);
        }

    }

    public static final class BlazeKeys {
        public final FlagsEntityDataKey<Blaze, BlazeFlags> BLAZE_FLAGS;
        public final FlagEntityDataKey<Blaze, BlazeFlags, Boolean> ON_FIRE;

        public BlazeKeys(MetadataKeyRegistry r) {
            BLAZE_FLAGS = r.registerFlags("BLAZE_FLAGS", Blaze.class, BlazeFlags.class);
            ON_FIRE = r.registerFlag(BLAZE_FLAGS, BlazeFlags.ON_FIRE);
        }

    }

    public static final class BoggedKeys {
        public final BooleanEntityDataKey<Bogged> IS_BOGGED_SHEARED;

        public BoggedKeys(MetadataKeyRegistry r) {
            IS_BOGGED_SHEARED = r.registerBoolean("IS_BOGGED_SHEARED", Bogged.class, Bogged::isSheared, Bogged::setSheared);
        }

    }

    public static final class CreakingKeys {
        public final BooleanEntityDataKey<Creaking> CAN_MOVE;
        public final BooleanEntityDataKey<Creaking> IS_ACTIVE;
        public final BooleanEntityDataKey<Creaking> IS_TEARING_DOWN;
        public final OptionalEntityDataKey<Creaking, BlockVector> HOME_POSITION;

        public CreakingKeys(MetadataKeyRegistry r) {
            CAN_MOVE = r.registerBoolean("CAN_MOVE", Creaking.class, EntityUtils::canMove, EntityUtils::setCanMove, true);
            IS_ACTIVE = r.registerBoolean("IS_ACTIVE", Creaking.class, Creaking::isActive, EntityUtils::setActive);
            IS_TEARING_DOWN = r.registerBoolean("IS_TEARING_DOWN", Creaking.class, EntityUtils::isTearingDown, EntityUtils::setTearingDown);
            HOME_POSITION = r.registerOptional("HOME_POSITION", Creaking.class, BlockVector.class, creaking -> {
                var home = creaking.getHome();
                return home == null ? null : home.toVector().toBlockVector();
            }, EntityUtils::setHome);
        }

    }

    public static final class CreeperKeys {
        public final SimpleEntityDataKey<Creeper, Integer> STATE;
        public final BooleanEntityDataKey<Creeper> IS_CHARGED;
        public final BooleanEntityDataKey<Creeper> IS_IGNITED;

        public CreeperKeys(MetadataKeyRegistry r) {
            STATE = r.register("STATE", Creeper.class, Integer.class, EntityUtils::getState, EntityUtils::setState, -1);
            IS_CHARGED = r.registerBoolean("IS_CHARGED", Creeper.class, Creeper::isPowered, Creeper::setPowered);
            IS_IGNITED = r.registerBoolean("IS_IGNITED", Creeper.class, Creeper::isIgnited, Creeper::setIgnited);
        }

    }

    public static final class EndermanKeys {
        public final OptionalEntityDataKey<Enderman, BlockData> CARRIED_BLOCK;
        public final BooleanEntityDataKey<Enderman> IS_SCREAMING;
        public final BooleanEntityDataKey<Enderman> HAS_BEEN_STARED_AT;

        public EndermanKeys(MetadataKeyRegistry r) {
            CARRIED_BLOCK = r.registerOptional("CARRIED_BLOCK", Enderman.class, BlockData.class, Enderman::getCarriedBlock, Enderman::setCarriedBlock);
            IS_SCREAMING = r.registerBoolean("IS_SCREAMING", Enderman.class, Enderman::isScreaming, Enderman::setScreaming);
            HAS_BEEN_STARED_AT = r.registerBoolean("HAS_BEEN_STARED_AT", Enderman.class, Enderman::hasBeenStaredAt, Enderman::setHasBeenStaredAt);
        }

    }

    public static final class GuardianKeys {
        public final BooleanEntityDataKey<Guardian> IS_RETRACTING_SPIKES;
        public final SimpleEntityDataKey<Guardian, Integer> LASER_TARGET_ENTITY_ID;
        public final OptionalEntityDataKey<Guardian, LivingEntity> LASER_TARGET_ENTITY;

        public GuardianKeys(MetadataKeyRegistry r) {
            IS_RETRACTING_SPIKES = r.registerBoolean("IS_RETRACTING_SPIKES", Guardian.class, Guardian::isMoving, EntityUtils::setMoving);
            LASER_TARGET_ENTITY_ID = r.register("LASER_TARGET_ENTITY_ID", Guardian.class, Integer.class, EntityUtils::getLaserTargetId, EntityUtils::setLaserTargetId, 0);
            LASER_TARGET_ENTITY = r.registerDependantOptionalEntity("LASER_TARGET_ENTITY_ID", LivingEntity.class, LASER_TARGET_ENTITY_ID);
        }

    }

    public static final class SkeletonKeys {
        public final BooleanEntityDataKey<Skeleton> IS_CONVERTING_INTO_STRAY;

        public SkeletonKeys(MetadataKeyRegistry r) {
            IS_CONVERTING_INTO_STRAY = r.registerBoolean("IS_CONVERTING_INTO_STRAY", Skeleton.class, Skeleton::isConverting, (skeleton, aBoolean) -> skeleton.setConversionTime(aBoolean ? 0 : -1));
        }

    }

    public static final class SpiderKeys {
        public final FlagsEntityDataKey<Spider, SpiderFlags> SPIDER_FLAGS;
        public final FlagEntityDataKey<Spider, SpiderFlags, Boolean> IS_CLIMBING;

        public SpiderKeys(MetadataKeyRegistry r) {
            SPIDER_FLAGS = r.registerFlags("SPIDER_FLAGS", Spider.class, SpiderFlags.class);
            IS_CLIMBING = r.registerFlag(SPIDER_FLAGS, SpiderFlags.IS_CLIMBING);
        }

    }

    public static final class VexKeys {
        public final FlagsEntityDataKey<Vex, VexFlags> VEX_FLAGS;
        public final FlagEntityDataKey<Vex, VexFlags, Boolean> IS_ATTACKING;

        public VexKeys(MetadataKeyRegistry r) {
            VEX_FLAGS = r.registerFlags("VEX_FLAGS", Vex.class, VexFlags.class);
            IS_ATTACKING = r.registerFlag(VEX_FLAGS, VexFlags.IS_ATTACKING);
        }

    }

    public static final class WardenKeys {
        public final SimpleEntityDataKey<Warden, Integer> ANGER_LEVEL;

        public WardenKeys(MetadataKeyRegistry r) {
            ANGER_LEVEL = r.register("ANGER_LEVEL", Warden.class, Integer.class, Warden::getAnger, EntityUtils::setClientAngerLevel, 0);
        }

    }

    public static final class WitherKeys {
        public final SimpleEntityDataKey<Wither, Integer> CENTER_HEAD_TARGET_ID, LEFT_HEAD_TARGET_ID, RIGHT_HEAD_TARGET_ID, INVULNERABILITY_TICKS;
        public final OptionalEntityDataKey<Wither, LivingEntity> CENTER_HEAD_TARGET, LEFT_HEAD_TARGET, RIGHT_HEAD_TARGET;

        public WitherKeys(MetadataKeyRegistry r) {
            CENTER_HEAD_TARGET_ID = r.register("CENTER_HEAD_TARGET", Wither.class, Integer.class, wither -> EntityUtils.getWitherHeadTargetId(wither, Wither.Head.CENTER), (wither, integer) -> EntityUtils.setWitherHeadTargetId(wither, Wither.Head.CENTER, integer), 0);
            LEFT_HEAD_TARGET_ID = r.register("LEFT_HEAD_TARGET", Wither.class, Integer.class, wither -> EntityUtils.getWitherHeadTargetId(wither, Wither.Head.LEFT), (wither, integer) -> EntityUtils.setWitherHeadTargetId(wither, Wither.Head.LEFT, integer), 0);
            RIGHT_HEAD_TARGET_ID = r.register("RIGHT_HEAD_TARGET", Wither.class, Integer.class, wither -> EntityUtils.getWitherHeadTargetId(wither, Wither.Head.RIGHT), (wither, integer) -> EntityUtils.setWitherHeadTargetId(wither, Wither.Head.RIGHT, integer), 0);
            INVULNERABILITY_TICKS = r.register("INVULNERABILITY_TICKS", Wither.class, Integer.class, Wither::getInvulnerableTicks, Wither::setInvulnerableTicks, 0);
            CENTER_HEAD_TARGET = r.registerDependantOptionalEntity("CENTER_HEAD_TARGET_ID", LivingEntity.class, CENTER_HEAD_TARGET_ID);
            LEFT_HEAD_TARGET = r.registerDependantOptionalEntity("LEFT_HEAD_TARGET_ID", LivingEntity.class, LEFT_HEAD_TARGET_ID);
            RIGHT_HEAD_TARGET = r.registerDependantOptionalEntity("RIGHT_HEAD_TARGET_ID", LivingEntity.class, RIGHT_HEAD_TARGET_ID);
        }

    }

    public static final class ZoglinKeys {
        public final BooleanEntityDataKey<Zoglin> IS_BABY_ZOGLIN;

        public ZoglinKeys(MetadataKeyRegistry r) {
            IS_BABY_ZOGLIN = r.registerInvertedBoolean("IS_BABY_ZOGLIN", Zoglin.class, Zoglin::isAdult, (zoglin, aBoolean) -> {
                if (!aBoolean) zoglin.setAdult();
                else zoglin.setBaby();
            });
        }

    }

    public static final class ZombieKeys {
        public final BooleanEntityDataKey<Zombie> IS_BABY_ZOMBIE;
        public final SimpleEntityDataKey<Zombie, Integer> UNUSED_ZOMBIE;
        public final BooleanEntityDataKey<Zombie> IS_CONVERTING_INTO_DROWNED;

        public ZombieKeys(MetadataKeyRegistry r) {
            IS_BABY_ZOMBIE = r.registerInvertedBoolean("IS_BABY_ZOMBIE", Zombie.class, Zombie::isAdult, (zombie, aBoolean) -> {
                if (!aBoolean) zombie.setAdult();
                else zombie.setBaby();
            });
            UNUSED_ZOMBIE = r.register("UNUSED_ZOMBIE", Zombie.class, Integer.class, _ -> 0, (_, _) -> {}, 0);
            IS_CONVERTING_INTO_DROWNED = r.registerBoolean("IS_CONVERTING_INTO_DROWNED", Zombie.class, Zombie::isConverting, (zombie, aBoolean) -> zombie.setConversionTime(aBoolean ? 0 : -1));
        }

    }

    public static final class ZombieVillagerKeys {
        public final BooleanEntityDataKey<ZombieVillager> IS_CONVERTING_INTO_VILLAGER;
        public final SimpleEntityDataKey<ZombieVillager, VillagerData> ZOMBIE_VILLAGER_DATA;

        public ZombieVillagerKeys(MetadataKeyRegistry r) {
            IS_CONVERTING_INTO_VILLAGER = r.registerBoolean("IS_CONVERTING_INTO_VILLAGER", ZombieVillager.class, ZombieVillager::isConverting, (zombieVillager, aBoolean) -> zombieVillager.setConversionTime(aBoolean ? 0 : -1));
            ZOMBIE_VILLAGER_DATA = r.register("ZOMBIE_VILLAGER_DATA", ZombieVillager.class, VillagerData.class, VillagerData::new, (villager, villagerData) -> villagerData.apply(villager), new VillagerData());
        }

    }

    public static final class PiglinAbstractKeys {
        public final BooleanEntityDataKey<PiglinAbstract> IS_IMMUNE_TO_ZOMBIFICATION;

        public PiglinAbstractKeys(MetadataKeyRegistry r) {
            IS_IMMUNE_TO_ZOMBIFICATION = r.registerBoolean("IS_IMMUNE_TO_ZOMBIFICATION", PiglinAbstract.class, PiglinAbstract::isImmuneToZombification, PiglinAbstract::setImmuneToZombification);
        }

    }

    public static final class PiglinKeys {
        public final BooleanEntityDataKey<Piglin> IS_BABY_PIGLIN, IS_CHARGING_CROSSBOW, IS_DANCING;

        public PiglinKeys(MetadataKeyRegistry r) {
            IS_BABY_PIGLIN = r.registerInvertedBoolean("IS_BABY_PIGLIN", Piglin.class, Piglin::isAdult, (piglin, aBoolean) -> {
                if (!aBoolean) piglin.setAdult();
                else piglin.setBaby();
            });
            IS_CHARGING_CROSSBOW = r.registerBoolean("IS_CHARGING_CROSSBOW", Piglin.class, Piglin::isChargingCrossbow, Piglin::setChargingCrossbow);
            IS_DANCING = r.registerBoolean("IS_DANCING", Piglin.class, Piglin::isDancing, Piglin::setDancing);
        }

    }

    public static final class RaiderKeys {
        public final BooleanEntityDataKey<Raider> IS_CELEBRATING;

        public RaiderKeys(MetadataKeyRegistry r) {
            IS_CELEBRATING = r.registerBoolean("IS_CELEBRATING", Raider.class, Raider::isCelebrating, Raider::setCelebrating);
        }

    }

    public static final class PillagerKeys {
        public final BooleanEntityDataKey<Pillager> IS_CHARGING;

        public PillagerKeys(MetadataKeyRegistry r) {
            IS_CHARGING = r.registerBoolean("IS_CHARGING", Pillager.class, EntityUtils::isChargingCrossbow, EntityUtils::setChargingCrossbow);
        }

    }

    public static final class WitchKeys {
        public final BooleanEntityDataKey<Witch> IS_DRINKING_POTION;

        public WitchKeys(MetadataKeyRegistry r) {
            IS_DRINKING_POTION = r.registerBoolean("IS_DRINKING_POTION", Witch.class, Witch::isDrinkingPotion, EntityUtils::setWitchUsingItem);
        }

    }

    public static final class SpellcasterKeys {
        public final EnumByteEntityDataKey<Spellcaster, Spellcaster.Spell> SPELL;

        public SpellcasterKeys(MetadataKeyRegistry r) {
            SPELL = r.registerEnumByte("SPELL", Spellcaster.class, Spellcaster.Spell.class, Spellcaster::getSpell, Spellcaster::setSpell);
        }

    }

    public static final class ThrowableProjectileKeys {
        public final SimpleEntityDataKey<ThrowableProjectile, ItemStack> THROWN_ITEM;

        public ThrowableProjectileKeys(MetadataKeyRegistry r) {
            THROWN_ITEM = r.register("THROWN_ITEM", ThrowableProjectile.class, ItemStack.class, ThrowableProjectile::getItem, ThrowableProjectile::setItem, ItemStack.empty());
        }

    }

    public static final class VehicleKeys {
        public final SimpleEntityDataKey<Vehicle, Integer> SHAKING_POWER, SHAKING_DIRECTION;
        public final SimpleEntityDataKey<Vehicle, Float> SHAKING_MULTIPLIER;

        public VehicleKeys(MetadataKeyRegistry r) {
            SHAKING_POWER = r.register("SHAKING_POWER", Vehicle.class, Integer.class, EntityUtils::getVehicleShakingPower, EntityUtils::setVehicleShakingPower, 0);
            SHAKING_DIRECTION = r.register("SHAKING_DIRECTION", Vehicle.class, Integer.class, EntityUtils::getVehicleShakingDirection, EntityUtils::setVehicleShakingDirection, 1);
            SHAKING_MULTIPLIER = r.register("SHAKING_MULTIPLIER", Vehicle.class, Float.class, EntityUtils::getVehicleShakingMultiplier, EntityUtils::setVehicleShakingMultiplier, 0f);
        }

    }

    public static final class BoatKeys {
        public final BooleanEntityDataKey<Boat> IS_LEFT_PADDLE_TURNING, IS_RIGHT_PADDLE_TURNING;
        public final SimpleEntityDataKey<Boat, Integer> SPLASH_TICKS;

        public BoatKeys(MetadataKeyRegistry r) {
            IS_LEFT_PADDLE_TURNING = r.registerBoolean("IS_LEFT_PADDLE_TURNING", Boat.class, EntityUtils::isTurningLeftPaddle, EntityUtils::setTurningLeftPaddle);
            IS_RIGHT_PADDLE_TURNING = r.registerBoolean("IS_RIGHT_PADDLE_TURNING", Boat.class, EntityUtils::isTurningRightPaddle, EntityUtils::setTurningRightPaddle);
            SPLASH_TICKS = r.register("SPLASH_TICKS", Boat.class, Integer.class, EntityUtils::getSplashTicks, EntityUtils::setSplashTicks, 0);
        }

    }

    public static final class MinecartKeys {
        public final OptionalEntityDataKey<Minecart, BlockData> MINECART_BLOCK;
        public final SimpleEntityDataKey<Minecart, Integer> MINECART_BLOCK_ALTITUDE;

        public MinecartKeys(MetadataKeyRegistry r) {
            MINECART_BLOCK = r.registerOptional("MINECART_BLOCK", Minecart.class, BlockData.class, Minecart::getDisplayBlockData, Minecart::setDisplayBlockData);
            MINECART_BLOCK_ALTITUDE = r.register("MINECART_BLOCK_ALTITUDE", Minecart.class, Integer.class, Minecart::getDisplayBlockOffset, Minecart::setDisplayBlockOffset, 6);
        }

    }

    public static final class CommandMinecartKeys {
        public final SimpleEntityDataKey<CommandMinecart, String> COMMAND;
        public final SimpleEntityDataKey<CommandMinecart, Component> LAST_OUTPUT;

        public CommandMinecartKeys(MetadataKeyRegistry r) {
            COMMAND = r.register("COMMAND", CommandMinecart.class, String.class, CommandMinecart::getCommand, CommandMinecart::setCommand, "");
            LAST_OUTPUT = r.register("LAST_OUTPUT", CommandMinecart.class, Component.class, CommandBlockHolder::lastOutput, CommandBlockHolder::lastOutput, Component.empty());
        }

    }

    public static final class PoweredMinecartKeys {
        public final BooleanEntityDataKey<PoweredMinecart> HAS_FUEL;

        public PoweredMinecartKeys(MetadataKeyRegistry r) {
            HAS_FUEL = r.registerBoolean("HAS_FUEL", PoweredMinecart.class, EntityUtils::hasFuel, EntityUtils::setHasFuel, false);
        }

    }

}