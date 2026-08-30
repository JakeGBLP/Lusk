package it.jakegblp.lusk.nms.core.adapter;

import com.destroystokyo.paper.SkinParts;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.papermc.paper.registry.RegistryKey;
import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.nms.core.serialization.*;
import it.jakegblp.lusk.nms.core.world.entity.effect.InternalEntityEffect;
import it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer;
import it.jakegblp.lusk.nms.core.world.entity.villager.VillagerData;
import it.jakegblp.lusk.nms.core.world.level.particles.ParticleWrapper;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.MainHand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

public interface SharedBehaviorAdapter<
        NMSEntityDataSerializer,
        NMSServerPlayer,
        NMSPacket,
        NMSServerGamePacketListenerImpl,
        NMSConnection
        > {

    void init(Version serverVersion);

    Mappers getMappings();

    BufferCodecs getCodecs();

    @NullMarked
    void registerEntityDataSerializer(EntityDataSerializer<?, ?> entityDataSerializer, NMSEntityDataSerializer nmsEntityDataSerializer);

    @NullMarked
    default void registerEntityDataSerializer(BufferCodecs codecs, Class<?> fromClass, NMSEntityDataSerializer nmsEntityDataSerializer) {
        registerEntityDataSerializer(EntityDataSerializer.simple(codecs, getFrontEndClass(fromClass)), nmsEntityDataSerializer);
    }

    @NullMarked
    default void registerOptionalEntityDataSerializer(BufferCodecs codecs, Class<?> fromClass, NMSEntityDataSerializer nmsEntityDataSerializer) {
        registerEntityDataSerializer(EntityDataSerializer.optional(codecs, getFrontEndClass(fromClass)), nmsEntityDataSerializer);
    }

    @NullMarked
    default void registerListEntityDataSerializer(BufferCodecs codecs, Class<?> fromClass, NMSEntityDataSerializer nmsEntityDataSerializer) {
        registerEntityDataSerializer(EntityDataSerializer.list(codecs, getFrontEndClass(fromClass)), nmsEntityDataSerializer);
    }

    @NullMarked
    default void registerHolderEntityDataSerializer(BufferCodecs codecs, Class<?> fromClass, NMSEntityDataSerializer nmsEntityDataSerializer) {
        registerEntityDataSerializer(EntityDataSerializer.holder(codecs, getFrontEndClass(fromClass)), nmsEntityDataSerializer);
    }

    int getNMSSerializerId(NMSEntityDataSerializer nmsEntityDataSerializer);

    NMSEntityDataSerializer getNMSSerializer(int id);

    default EntityDataSerializer<?, ?> getSerializer(int id) {
        return getEntityDataSerializers().inverse().get(getNMSSerializer(id));
    }

    default int getSerializerId(EntityDataSerializer<?, ?> entityDataSerializer) {
        return getNMSSerializerId(getEntityDataSerializers().get(entityDataSerializer));
    }

    BiMap<EntityDataSerializer<?, ?>, NMSEntityDataSerializer> getEntityDataSerializers();

    @SuppressWarnings("unchecked")
    default <T> EntityDataSerializer<T, ?> getEntityDataSerializer(Class<T> toClass) {
        for (EntityDataSerializer<?, ?> entityDataSerializer : getEntityDataSerializers().keySet())
            if (entityDataSerializer.codec().test(toClass))
                return (EntityDataSerializer<T, ?>) entityDataSerializer;
        throw new IllegalArgumentException("No EntityDataSerializer found for " + toClass);
    }

    @SuppressWarnings("unchecked")
    default <T, K extends TypeKey<?>> EntityDataSerializer<T, K> getEntityDataSerializer(K typeKey) {
        for (EntityDataSerializer<?, ?> entityDataSerializer : getEntityDataSerializers().keySet())
            if (entityDataSerializer.codec().key().equals(typeKey))
                return (EntityDataSerializer<T, K>) entityDataSerializer;
        throw new IllegalArgumentException("No EntityDataSerializer found for " + typeKey);
    }

    default <T> EntityDataSerializer.Simple<T> getEntityDataSerializerSimple(Class<T> type) {
        return getEntityDataSerializerSimple(TypeKey.simple(type));
    }

    @SuppressWarnings("all")
    default <T> EntityDataSerializer.Simple<T> getEntityDataSerializerSimple(SimpleKey<T> key) {
        return (EntityDataSerializer.Simple<T>) (EntityDataSerializer) getEntityDataSerializer(key);
    }

    default <T> EntityDataSerializer.Optional<T> getEntityDataSerializerOptional(Class<T> type) {
        return getEntityDataSerializerOptional(TypeKey.optional(type));
    }

    @SuppressWarnings("all")
    default <T> EntityDataSerializer.Optional<T> getEntityDataSerializerOptional(OptionalKey<T> key) {
        return (EntityDataSerializer.Optional<T>) (EntityDataSerializer) getEntityDataSerializer(key);
    }

    default <T> EntityDataSerializer.List<T> getEntityDataSerializerList(Class<T> type) {
        return getEntityDataSerializerList(TypeKey.list(type));
    }

    @SuppressWarnings("all")
    default <T> EntityDataSerializer.List<T> getEntityDataSerializerList(ListKey<T> key) {
        return (EntityDataSerializer.List<T>) (EntityDataSerializer) getEntityDataSerializer(key);
    }

    default <T> EntityDataSerializer.Holder<T> getEntityDataSerializerHolder(Class<T> type) {
        return getEntityDataSerializerHolder(TypeKey.holder(type));
    }

    @SuppressWarnings("all")
    default <T> EntityDataSerializer.Holder<T> getEntityDataSerializerHolder(HolderKey<T> key) {
        return (EntityDataSerializer.Holder<T>) (EntityDataSerializer) getEntityDataSerializer(key);
    }

    Class<?> getSerializableClass(Class<?> clazz);

    Class<?> getFrontEndClass(Class<?> clazz);

    Entity getEntityFromId(int id, World world);

    boolean isSprinting(Entity entity);

    void setSprinting(Entity entity, boolean sprinting);

    boolean isSwimming(Entity entity);

    void setSwimming(Entity entity, boolean swimming);

    boolean hasBred(AbstractHorse abstractHorse);

    void setBred(AbstractHorse abstractHorse, boolean bred);

    boolean isBurning(Blaze blaze);

    void setBurning(Blaze blaze, boolean burning);

    void setHandActive(LivingEntity livingEntity, boolean active);

    void setActiveItemHand(LivingEntity livingEntity, EquipmentSlot slot);

    void setSkinParts(LivingEntity livingEntity, SkinParts skinParts);

    int getAirTicks(Entity entity);

    void setAirTicks(Entity entity, int ticks);

    boolean isSinglePoint(AreaEffectCloud areaEffectCloud);

    void setSinglePoint(AreaEffectCloud areaEffectCloud, boolean singlePoint);

    ParticleWrapper getParticleWrapper(AreaEffectCloud areaEffectCloud);

    void setParticleWrapper(AreaEffectCloud areaEffectCloud, ParticleWrapper particleWrapper);

    BlockVector getStartingPosition(FallingBlock fallingBlock);

    void setStartingPosition(FallingBlock fallingBlock, BlockVector startingPosition);

    @Nullable Integer getBooster(Firework firework);

    void setBooster(Firework firework, Integer booster);

    boolean isBiting(FishHook fishHook);

    void setBiting(FishHook fishHook, boolean biting);

    void setRolling(Bee bee, boolean rolling);

    void setClimbing(Spider spider, boolean climbing);

    boolean isSitting(Tameable tameable);

    void setSitting(Tameable tameable, boolean sitting);

    ParticleWrapper getAreaEffectCloudDefaultParticle();

    ParticleWrapper getParticleOptions(PotionEffect potionEffect);

    @Nullable BlockVector getSleepingPosition(LivingEntity livingEntity);

    void setSleepingPosition(LivingEntity livingEntity, @Nullable BlockVector sleepingPosition);

    MainHand getMainHand(LivingEntity livingEntity);

    void setMainHand(LivingEntity livingEntity, MainHand mainHand);

    void setArmadilloState(Armadillo armadillo, Armadillo.State state);

    long getBeePersistentAngerEndTime(Bee bee);

    void setBeePersistentAngerEndTime(Bee bee, long time);

    boolean isLeashHolder(HappyGhast happyGhast);

    void setLeashHolder(HappyGhast happyGhast, boolean leashHolder);

    boolean staysStill(HappyGhast happyGhast);

    void setStaysStill(HappyGhast happyGhast, boolean staysStill);

    int getDropSeedAtTick(Sniffer sniffer);

    void setDropSeedAtTick(Sniffer sniffer, int ticks);

    void setLayingEgg(Turtle turtle, boolean layingEgg);

    long getLastPoseChangeTick(Camel camel);

    void setLastPoseChangeTick(Camel camel, long tick);

    int getVariant(Horse horse);

    void setVariant(Horse horse, int variant);

    long getAngerEndTime(Wolf wolf);

    void setAngerEndTime(Wolf wolf, long time);

    boolean isDashing(AbstractNautilus nautilus);

    void setDashing(AbstractNautilus nautilus, boolean dashing);

    int getHeadShakeTicks(AbstractVillager villager);

    void setHeadShakeTicks(AbstractVillager villager, int ticks);

    boolean canMove(Creaking creaking);

    void setCanMove(Creaking creaking, boolean canMove);

    void setActive(Creaking creaking, boolean active);

    void setHome(Creaking creaking, @Nullable BlockVector home);

    boolean isTearingDown(Creaking creaking);

    void setTearingDown(Creaking creaking, boolean tearingDown);

    int getState(Creeper creeper);

    void setState(Creeper creeper, int state);

    void setMoving(Guardian guardian, boolean moving);

    int getLaserTargetId(Guardian guardian);

    void setLaserTargetId(Guardian guardian, int targetId);

    void setClientAngerLevel(Warden warden, int angerLevel);

    int getWitherHeadTargetId(Wither wither, Wither.Head head);

    void setWitherHeadTargetId(Wither wither, Wither.Head head, int targetId);

    void setVillagerData(ZombieVillager zombieVillager, VillagerData villagerData);

    boolean isChargingCrossbow(Pillager pillager);

    void setChargingCrossbow(Pillager pillager, boolean charging);

    void setWitchUsingItem(Witch witch, boolean usingItem);

    int getVehicleShakingPower(Vehicle vehicle);

    void setVehicleShakingPower(Vehicle vehicle, int shakingPower);

    int getVehicleShakingDirection(Vehicle vehicle);

    void setVehicleShakingDirection(Vehicle vehicle, int shakingDirection);

    float getVehicleShakingMultiplier(Vehicle vehicle);

    void setVehicleShakingMultiplier(Vehicle vehicle, float shakingMultiplier);

    boolean isTurningLeftPaddle(Boat boat);

    void setTurningLeftPaddle(Boat boat, boolean turning);

    boolean isTurningRightPaddle(Boat boat);

    void setTurningRightPaddle(Boat boat, boolean turning);

    int getSplashTicks(Boat boat);

    void setSplashTicks(Boat boat, int splashTimer);

    boolean hasFuel(PoweredMinecart poweredMinecart);

    void setHasFuel(PoweredMinecart poweredMinecart, boolean hasFuel);

    @Nullable Integer getLeftShoulderParrotVariantId(Player player);

    void setLeftShoulderParrotVariantId(Player player, @Nullable Integer variantId);

    @Nullable Integer getRightShoulderParrotVariantId(Player player);

    void setRightShoulderParrotVariantId(Player player, @Nullable Integer variantId);

    @NullMarked
    <T extends Keyed> int getKeyedId(RegistryKey<T> registryKey, T value);

    @NullMarked
    Keyed getKeyedById(RegistryKey<? extends Keyed> registryKey, int id);

    void setCubeFast(Location corner1, Location corner2, BlockData blockData);

    void replaceFast(Location corner1, Location corner2, BlockData oldBlockData, BlockData newBlockData);

    NMSServerPlayer asServerPlayer(Player player);

    NMSServerGamePacketListenerImpl getPlayerConnection(NMSServerPlayer serverPlayer);

    default void sendPacket(Player player, NMSPacket packet) {
        sendPacket(asServerPlayer(player), packet);
    }

    default void sendPacket(NMSServerPlayer player, NMSPacket packet) {
        sendPacketInternal(getPlayerConnection(player), packet);
    }

    void sendPacketInternal(NMSServerGamePacketListenerImpl packetListener, NMSPacket packet);

    NMSConnection getConnection(NMSServerGamePacketListenerImpl packetListener);

    Channel getChannel(NMSConnection connection);

    default void uninjectPlayer(Player player) {
        uninjectPlayer(asServerPlayer(player));
    }

    default void uninjectPlayer(NMSServerPlayer serverPlayer) {
        ChannelPipeline pipeline = getChannel(getConnection(getPlayerConnection(serverPlayer))).pipeline();
        if (pipeline.get("packet_interceptor") != null) {
            pipeline.remove("packet_interceptor");
        }
    }

    void injectPlayer(Player player, JavaPlugin plugin);

    default void playInternalEntityEffect(Entity entity, InternalEntityEffect internalEntityEffect) {
        Preconditions.checkArgument(internalEntityEffect != null, "Internal Entity effect cannot be null");
        Preconditions.checkArgument(internalEntityEffect.isApplicableTo(entity), "Internal Entity effect cannot be applied to this entity");
        playEntityEffect(entity, internalEntityEffect.getData());
    }

    void playEntityEffect(Entity entity, byte data);

}