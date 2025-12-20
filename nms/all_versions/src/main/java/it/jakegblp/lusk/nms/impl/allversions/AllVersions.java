package it.jakegblp.lusk.nms.impl.allversions;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.papermc.paper.adventure.PaperAdventure;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.adapters.SharedBehaviorAdapter;
import it.jakegblp.lusk.nms.core.protocol.packets.client.*;
import it.jakegblp.lusk.nms.core.world.entity.attribute.AttributeSnapshot;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataItem;
import it.jakegblp.lusk.nms.core.world.entity.serialization.EntitySerializerKey;
import it.jakegblp.lusk.nms.core.world.player.ChatSessionData;
import it.jakegblp.lusk.nms.core.world.player.PlayerInfo;
import it.jakegblp.lusk.nms.core.world.player.TeamParameters;
import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.RemoteChatSession;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftParticle;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.attribute.CraftAttribute;
import org.bukkit.craftbukkit.attribute.CraftAttributeInstance;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;
import static it.jakegblp.lusk.nms.core.util.NullabilityUtils.convertIfNotNull;
import static it.jakegblp.lusk.nms.core.world.entity.serialization.EntitySerializerKey.Type.OPTIONAL;

@Getter
public class AllVersions implements
        SharedBehaviorAdapter<
                EquipmentSlot,
                ItemStack,
                Vec3,
                BlockPos,
                ServerPlayer,
                Pose,
                Component,
                Packet,
                ResourceLocation,
                GameType,
                GameProfile,
                RemoteChatSession.Data,
                EntityType,
                ServerGamePacketListenerImpl,
                Connection,
                Team.Visibility,
                Team.CollisionRule,
                DedicatedServer,
                ClientboundBundlePacket,
                ClientboundBlockDestructionPacket,
                ClientboundAnimatePacket,
                ClientboundAddEntityPacket,
                ClientboundRemoveEntitiesPacket,
                ClientboundSetEntityDataPacket,
                ClientboundPlayerInfoUpdatePacket,
                ClientboundPlayerInfoUpdatePacket.Action,
                ClientboundSystemChatPacket,
                ClientboundLevelParticlesPacket,
                ClientboundUpdateAttributesPacket,
                ClientboundSetCameraPacket,
                ClientboundSetPlayerTeamPacket,
                ClientboundSetPlayerTeamPacket.Parameters,
                ClientboundEntityEventPacket
                > {

    private final BiMap<org.bukkit.entity.Pose, Pose> poseMap;

    public AllVersions() {
        poseMap = ImmutableBiMap.of(org.bukkit.entity.Pose.SNEAKING, Pose.CROUCHING);
    }

    @Override
    public Class<Packet> getNMSPacketClass() {
        return Packet.class;
    }

    @Override
    public Class<ItemStack> getNMSItemStackClass() {
        return ItemStack.class;
    }

    @Override
    public Class<EquipmentSlot> getNMSEquipmentSlotClass() {
        return EquipmentSlot.class;
    }

    @Override
    public Vector asVector(Vec3 vec3) {
        return new Vector(vec3.x, vec3.y, vec3.z);
    }

    @Override
    public Vec3 asNMSVector(Vector vector) {
        return new Vec3(vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    public Class<Vec3> getNMSVectorClass() {
        return Vec3.class;
    }

    @Override
    public BlockVector asBlockVector(BlockPos blockPosition) {
        return new BlockVector(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
    }

    @Override
    public BlockPos asNMSBlockVector(BlockVector blockVector) {
        return new BlockPos(blockVector.getBlockX(), blockVector.getBlockY(), blockVector.getBlockZ());
    }

    @Override
    public Class<BlockPos> getNMSBlockVectorClass() {
        return BlockPos.class;
    }

    @Override
    public org.bukkit.entity.Pose asPose(Pose pose) {
        return getPoseMap().inverse().getOrDefault(pose, org.bukkit.entity.Pose.valueOf(pose.toString()));
    }

    @Override
    public Pose asNMSPose(org.bukkit.entity.Pose pose) {
        return getPoseMap().getOrDefault(pose, Pose.valueOf(pose.toString()));
    }

    @Override
    public Class<Pose> getNMSPoseClass() {
        return Pose.class;
    }

    @Override
    public Player asPlayer(ServerPlayer serverPlayer) {
        return serverPlayer.getBukkitEntity();
    }

    @Override
    public Class<ServerPlayer> getNMSServerPlayerClass() {
        return ServerPlayer.class;
    }

    @Override
    public ServerGamePacketListenerImpl getPlayerConnection(ServerPlayer serverPlayer) {
        return serverPlayer.connection;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClientboundBundlePacket toNMSClientBundlePacket(ClientBundlePacket from) {
        List<Packet<? super ClientGamePacketListener>> nmsPackets = new ArrayList<>();
        for (ClientboundPacket packet : from.getPackets())
            nmsPackets.add((Packet<ClientGamePacketListener>) packet.asNMS());
        return new ClientboundBundlePacket(nmsPackets);
    }

    @Override
    public ClientBundlePacket fromNMSClientBundlePacket(ClientboundBundlePacket from) {
        List<ClientboundPacket> packetList = new ArrayList<>();
        for (Packet<? super ClientGamePacketListener> subPacket : from.subPackets()) {
            it.jakegblp.lusk.nms.core.protocol.packets.Packet packet = NMS.fromNMSPacket(subPacket);
            if (packet instanceof ClientboundPacket clientboundPacket)
                packetList.add(clientboundPacket);
        }
        return new ClientBundlePacket(packetList);
    }

    @Override
    public Class<ClientboundBundlePacket> getNMSClientBundlePacketClass() {
        return ClientboundBundlePacket.class;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public ClientboundAddEntityPacket toNMSAddEntityPacket(AddEntityPacket from) {
        return new ClientboundAddEntityPacket(
                from.getId(),
                from.getEntityUUID(),
                from.getX(),
                from.getY(),
                from.getZ(),
                from.getPitch(),
                from.getYaw(),
                toNMSEntityType(from.getEntityType()),
                from.getData(),
                asNMSVector(from.getVelocity()),
                from.getHeadYaw());
    }

    @Override
    public AddEntityPacket fromNMSAddEntityPacket(ClientboundAddEntityPacket from) {
        return new AddEntityPacket(
                from.getId(),
                from.getUUID(),
                from.getX(),
                from.getY(),
                from.getZ(),
                from.getXRot(),
                from.getYRot(),
                from.getYHeadRot(),
                from.getX(),
                from.getY(),
                from.getZ(),
                fromNMSEntityType(from.getType()),
                from.getData()
        );
    }

    @Override
    public Class<ClientboundAddEntityPacket> getNMSAddEntityPacketClass() {
        return ClientboundAddEntityPacket.class;
    }

    @Override
    public Class<Component> getNMSComponentClass() {
        return Component.class;
    }

    @Override
    public void sendPacketInternal(ServerGamePacketListenerImpl serverGamePacketListener, Packet packet) {
        serverGamePacketListener.send(packet);
    }

    @Override
    public Connection getConnection(ServerGamePacketListenerImpl serverGamePacketListener) {
        return serverGamePacketListener.connection;
    }

    @Override
    public Channel getChannel(Connection connection) {
        return connection.channel;
    }

    @Override
    public Team.Visibility toNMSTeamVisibility(org.bukkit.scoreboard.Team.OptionStatus optionStatus) {
        FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
        friendlyByteBuf.writeByte(optionStatus.ordinal());
        return Team.Visibility.STREAM_CODEC.decode(friendlyByteBuf);
    }

    @Override
    public Team.CollisionRule toNMSTeamCollisionRule(org.bukkit.scoreboard.Team.OptionStatus optionStatus) {
        FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
        friendlyByteBuf.writeByte(optionStatus.ordinal());
        return Team.CollisionRule.STREAM_CODEC.decode(friendlyByteBuf);
    }

    @Override
    public org.bukkit.scoreboard.Team.OptionStatus fromNMSTeamVisibility(Team.Visibility visibility) {
        FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
        Team.Visibility.STREAM_CODEC.encode(friendlyByteBuf, visibility);
        return org.bukkit.scoreboard.Team.OptionStatus.values()[friendlyByteBuf.readInt()];
    }

    @Override
    public org.bukkit.scoreboard.Team.OptionStatus fromNMSTeamCollisionRule(Team.CollisionRule collisionRule) {
        FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
        Team.CollisionRule.STREAM_CODEC.encode(friendlyByteBuf, collisionRule);
        return org.bukkit.scoreboard.Team.OptionStatus.values()[friendlyByteBuf.readInt()];
    }

    @Override
    public DedicatedServer getDedicatedServer() {
        return ((CraftServer)Bukkit.getServer()).getServer();
    }

    @Override
    public ClientboundBlockDestructionPacket toNMSBlockDestructionPacket(BlockDestructionPacket from) {
        return new ClientboundBlockDestructionPacket(from.getId(), asNMSBlockVector(from.getPosition()), from.getBlockDestructionStage());
    }

    @Override
    public BlockDestructionPacket fromNMSBlockDestructionPacket(ClientboundBlockDestructionPacket from) {
        return new BlockDestructionPacket(from.getId(), asBlockVector(from.getPos()), from.getProgress());
    }

    @Override
    public Class<ClientboundBlockDestructionPacket> getNMSBlockDestructionPacketClass() {
        return ClientboundBlockDestructionPacket.class;
    }

    @Override
    public ClientboundAnimatePacket toNMSEntityAnimationPacket(EntityAnimationPacket from) {
        FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
        friendlyByteBuf.writeVarInt(from.getId());
        friendlyByteBuf.writeByte(from.getEntityAnimationId());
        return ClientboundAnimatePacket.STREAM_CODEC.decode(friendlyByteBuf);
    }

    @Override
    public EntityAnimationPacket fromNMSEntityAnimationPacket(ClientboundAnimatePacket from) {
        return new EntityAnimationPacket(from.getId(), from.getAction());
    }

    @Override
    public Class<ClientboundAnimatePacket> getNMSEntityAnimationPacketClass() {
        return ClientboundAnimatePacket.class;
    }

    @Override
    public ClientboundRemoveEntitiesPacket toNMSRemoveEntitiesPacket(RemoveEntitiesPacket from) {
        return new ClientboundRemoveEntitiesPacket(from.getEntityIds());
    }

    @Override
    public RemoveEntitiesPacket fromNMSRemoveEntitiesPacket(ClientboundRemoveEntitiesPacket from) {
        return new RemoveEntitiesPacket(from.getEntityIds());
    }

    @Override
    public Class<ClientboundRemoveEntitiesPacket> getNMSRemoveEntitiesPacketClass() {
        return ClientboundRemoveEntitiesPacket.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClientboundSetEntityDataPacket toNMSEntityMetadataPacket(EntityMetadataPacket from) {
        return new ClientboundSetEntityDataPacket(
                from.getId(),
                CommonUtils.map(from.getEntityMetadata().items(),
                        item -> {
                            Object nmsValue = NMS.toNMSObject(item.value());
                            EntitySerializerKey.Type type = item.serializerType();
                            return new SynchedEntityData.DataValue<>(
                                    item.id(),
                                    (EntityDataSerializer<Object>) NMS.getEntityDataSerializer(
                                            NMS.getSerializableClass(item.valueClass()), type
                                    ), type == OPTIONAL ? Optional.ofNullable(nmsValue) : nmsValue);
                        })
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public EntityMetadataPacket fromNMSEntityMetadataPacket(ClientboundSetEntityDataPacket from) {
        List<SynchedEntityData.DataValue<?>> dataValueList = from.packedItems();
        EntityMetadata entityMetadata = new EntityMetadata();
        for (SynchedEntityData.DataValue<?> dataValue : dataValueList) {
            int id = dataValue.id();
            entityMetadata.setInternal(id, new MetadataItem<>(id, dataValue.value(), (EntitySerializerKey<Object>) NMS.getEntityDataSerializerKey(dataValue.serializer())));
        }
        return new EntityMetadataPacket(from.id(), entityMetadata);
    }

    @Override
    public Class<ClientboundSetEntityDataPacket> getNMSEntityMetadataPacketClass() {
        return ClientboundSetEntityDataPacket.class;
    }

    @Override
    public ClientboundPlayerInfoUpdatePacket toNMSPlayerInfoUpdatePacket(PlayerInfoUpdatePacket from) {
        EnumSet<ClientboundPlayerInfoUpdatePacket.Action> actions = CommonUtils.copyOrEmptyEnumSet(CommonUtils.map(from.getActions(), this::toNMSPlayerInfoUpdatePacketAction), ClientboundPlayerInfoUpdatePacket.Action.class);
        return new ClientboundPlayerInfoUpdatePacket(actions,
                (List<ClientboundPlayerInfoUpdatePacket.Entry>) CommonUtils.map(from.getPlayerInfos(), playerInfo -> new ClientboundPlayerInfoUpdatePacket.Entry(
                        playerInfo.getUUID(),
                        convertIfNotNull(playerInfo.getPlayerProfile().getPlayerProfile(), this::toNMSPlayerProfile),
                        playerInfo.isListed(),
                        playerInfo.getLatency(),
                        convertIfNotNull(playerInfo.getGameMode(), this::toNMSGameMode),
                        convertIfNotNull(playerInfo.getDisplayName(), this::asNMSComponent),
                        playerInfo.isShowHat(),
                        playerInfo.getListOrder(),
                        convertIfNotNull(playerInfo.getChatSession(), this::toNMSChatSessionData)
                ))
        );
    }

    @Override
    public PlayerInfoUpdatePacket fromNMSPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket from) {
        var actions = CommonUtils.map(from.actions(), this::fromNMSPlayerInfoUpdatePacketAction);
        return new PlayerInfoUpdatePacket(Set.copyOf(actions), CommonUtils.map(from.entries(), entry -> new PlayerInfo(
                entry.profileId(),
                convertIfNotNull(entry.profile(), this::fromNMSPlayerProfile),
                entry.listed(),
                entry.latency(),
                convertIfNotNull(entry.gameMode(), this::fromNMSGameMode),
                convertIfNotNull(entry.displayName(), this::asComponent),
                entry.showHat(),
                entry.listOrder(),
                convertIfNotNull(entry.chatSession(), this::fromNMSChatSessionData)
        )));
    }

    @Override
    public Class<ClientboundPlayerInfoUpdatePacket> getNMSPlayerInfoUpdatePacketClass() {
        return ClientboundPlayerInfoUpdatePacket.class;
    }

    @Override
    public ClientboundPlayerInfoUpdatePacket.Action toNMSPlayerInfoUpdatePacketAction(PlayerInfoUpdatePacket.Action<?> from) {
        return ClientboundPlayerInfoUpdatePacket.Action.valueOf(from.name());
    }

    @Override
    public PlayerInfoUpdatePacket.Action<?> fromNMSPlayerInfoUpdatePacketAction(ClientboundPlayerInfoUpdatePacket.Action from) {
        return PlayerInfoUpdatePacket.Action.valueOf(from.name());
    }

    @Override
    public Class<ClientboundPlayerInfoUpdatePacket.Action> getNMSPlayerInfoUpdatePacketActionClass() {
        return ClientboundPlayerInfoUpdatePacket.Action.class;
    }

    @Override
    public ClientboundSystemChatPacket toNMSSystemChatPacket(SystemChatPacket from) {
        return null;
    }

    @Override
    public SystemChatPacket fromNMSSystemChatPacket(ClientboundSystemChatPacket from) {
        return null;
    }

    @Override
    public TeamPacket fromNMSSetPlayerTeamPacket(ClientboundSetPlayerTeamPacket from) {
        ClientboundSetPlayerTeamPacket.Action
                teamAction = from.getTeamAction(),
                playerAction = from.getPlayerAction();
        int method;
        if (teamAction != null) {
            method = switch (teamAction) {
                case ADD -> 0;
                case REMOVE -> 1;
            };
        } else if (playerAction != null) {
            method = switch (playerAction) {
                case ADD -> 3;
                case REMOVE -> 4;
            };
        } else
            method = 0;
        return TeamPacket.fromMethod(method, from.getName(), fromNMSTeamParameters(from.getParameters().orElse(null)), new HashSet<>(from.getPlayers()));
    }

    @Override
    public Class<ClientboundSetPlayerTeamPacket> getNMSSetPlayerTeamPacketClass() {
        return ClientboundSetPlayerTeamPacket.class;
    }
    @Override
    @Contract("null -> null")
    public TeamParameters fromNMSTeamParameters(@Nullable ClientboundSetPlayerTeamPacket.Parameters from) {
        return from == null ? null : new TeamParameters(
                asComponent(from.getDisplayName()),
                asComponent(from.getPlayerPrefix()),
                asComponent(from.getPlayerSuffix()),
                fromNMSTeamVisibility(from.getNametagVisibility()),
                fromNMSTeamCollisionRule(from.getCollisionRule()),
                NamedTextColor.NAMES.value(from.getColor().getName()),
                from.getOptions());
    }

    @Override
    public ClientboundSetCameraPacket toNMSSetCameraPacket(SetCameraPacket from) {
        FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
        friendlyByteBuf.writeVarInt(from.getCameraId());
        return ClientboundSetCameraPacket.STREAM_CODEC.decode(friendlyByteBuf);
    }

    @Override
    public SetCameraPacket fromNMSSetCameraPacket(ClientboundSetCameraPacket from) {
        FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
        ClientboundSetCameraPacket.STREAM_CODEC.encode(friendlyByteBuf, from);
        return new SetCameraPacket(friendlyByteBuf.readInt());
    }

    @Override
    public Class<ClientboundSetCameraPacket> getNMSSetCameraPacketClass() {
        return ClientboundSetCameraPacket.class;
    }


    // leave space here for Poa to read


    @Override
    public ClientboundSetPlayerTeamPacket toNMSSetPlayerTeamPacket(TeamPacket from) {
        var registryFriendlyByteBuf = new RegistryFriendlyByteBuf(Unpooled.buffer(), getDedicatedServer().registryAccess());
        registryFriendlyByteBuf.writeUtf(from.getName());
        registryFriendlyByteBuf.writeByte(from.getMethod());
        if (from instanceof TeamPacket.ChangeTeamInfo changeTeamInfo)
            toNMSTeamParameters(changeTeamInfo.getParameters()).write(registryFriendlyByteBuf);
        if (from instanceof TeamPacket.WithMembers withMembers)
            registryFriendlyByteBuf.writeCollection(withMembers.getMembers(), FriendlyByteBuf::writeUtf);
        return ClientboundSetPlayerTeamPacket.STREAM_CODEC.decode(registryFriendlyByteBuf);
    }


    @Override
    public Class<ClientboundSystemChatPacket> getNMSSystemChatPacketClass() {
        return ClientboundSystemChatPacket.class;
    }


    @Override
    public ClientboundUpdateAttributesPacket toNMSAttributePacket(AttributePacket from) {
        final List<AttributeInstance> instances = new ArrayList<>();


        for (AttributeSnapshot attribute : from.getAttributes()) {
            final Holder<Attribute> holder = CraftAttribute.bukkitToMinecraftHolder(attribute.getAttribute()); //todo test on 1.19.4
            final AttributeInstance attributeInstance = new AttributeInstance(holder, a -> {
            });

            Collection<net.minecraft.world.entity.ai.attributes.AttributeModifier> modifiers =
                    attribute.getModifiers()
                            .stream()
                            .map(CraftAttributeInstance::convert)
                            .toList();
            attributeInstance.addPermanentModifiers(modifiers);
            attributeInstance.setBaseValue(attribute.getBase());
            instances.add(attributeInstance);
        }


        return new ClientboundUpdateAttributesPacket(from.getId(), instances);
    }
    @Override
    public ClientboundSetPlayerTeamPacket.Parameters toNMSTeamParameters(TeamParameters from) {
        var registryFriendlyByteBuf = new RegistryFriendlyByteBuf(Unpooled.buffer(), getDedicatedServer().registryAccess());
        ComponentSerialization.TRUSTED_STREAM_CODEC.encode(registryFriendlyByteBuf, asNMSComponent(from.getDisplayName()));
        registryFriendlyByteBuf.writeByte(from.getOptions());
        Team.Visibility.STREAM_CODEC.encode(registryFriendlyByteBuf, toNMSTeamVisibility(from.getNametagVisibility()));
        Team.CollisionRule.STREAM_CODEC.encode(registryFriendlyByteBuf, toNMSTeamCollisionRule(from.getCollisionRule()));
        registryFriendlyByteBuf.writeVarInt(ChatFormatting.getByName(NamedTextColor.NAMES.key(from.getColor())).getId());
        ComponentSerialization.TRUSTED_STREAM_CODEC.encode(registryFriendlyByteBuf, asNMSComponent(from.getPlayerPrefix()));
        ComponentSerialization.TRUSTED_STREAM_CODEC.encode(registryFriendlyByteBuf, asNMSComponent(from.getPlayerSuffix()));
        return new ClientboundSetPlayerTeamPacket.Parameters(registryFriendlyByteBuf);
    }


    @SneakyThrows
    @Override
    public AttributePacket fromNMSAttributePacket(ClientboundUpdateAttributesPacket from) {
        List<AttributeSnapshot> list = new ArrayList<>();
        for (ClientboundUpdateAttributesPacket.AttributeSnapshot value : from.getValues()) {
            final org.bukkit.attribute.Attribute attribute = CraftAttribute.minecraftHolderToBukkit(value.attribute());

            List<org.bukkit.attribute.AttributeModifier> modifiers = value.modifiers().stream().map(CraftAttributeInstance::convert).toList();

            list.add(new AttributeSnapshot(attribute, value.base(), modifiers));
        }
        return new AttributePacket(from.getEntityId(), list);
    }

    @Override
    public Class<ClientboundUpdateAttributesPacket> getNMSAttributePacketClass() {
        return ClientboundUpdateAttributesPacket.class;
    }
    public Class<ClientboundSetPlayerTeamPacket.Parameters> getNMSTeamParametersClass() {
        return ClientboundSetPlayerTeamPacket.Parameters.class;
    }



    @Override
    public ClientboundEntityEventPacket toNMSEntityEventPacket(EntityEventPacket from) {
        final FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
        friendlyByteBuf.writeInt(from.getEntityID());
        friendlyByteBuf.writeByte(from.getEventID());

        return ClientboundEntityEventPacket.STREAM_CODEC.decode(friendlyByteBuf);
    }

    @Override
    public EntityEventPacket fromNMSEntityEventPacket(ClientboundEntityEventPacket from) {
        final FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
        ClientboundEntityEventPacket.STREAM_CODEC.encode(friendlyByteBuf, from);

        return new EntityEventPacket(friendlyByteBuf.readInt(), from.getEventId());
    }

    @Override
    public Class<ClientboundEntityEventPacket> getNMSEntityEventPacketClass() {
        return ClientboundEntityEventPacket.class;
    }


    @Override
    public ClientboundLevelParticlesPacket toNMSLevelParticle(LevelParticlePacket from) {
        return new ClientboundLevelParticlesPacket((ParticleOptions) from.getParticle(), from.isOverrideLimiter(), from.isAlwaysShow(), from.getX(), from.getY(), from.getZ(), from.getXDist(), from.getYDist(), from.getZDist(), from.getMaxSpeed(), from.getCount());
    }

    @Override
    public LevelParticlePacket fromNMSLevelParticle(ClientboundLevelParticlesPacket from) {
        final ParticleType<?> particle = from.getParticle().getType();
        final Particle bukkitParticle = CraftParticle.minecraftToBukkit(particle); //todo test 1.19.4

        return new LevelParticlePacket(from.getX(), from.getY(), from.getZ(), from.getXDist(), from.getYDist(), from.getZDist(), from.getMaxSpeed(), from.getCount(), from.isOverrideLimiter(), from.alwaysShow(), new it.jakegblp.lusk.nms.core.world.level.ParticleOptions(bukkitParticle));
    }

    @Override
    public Class<ClientboundLevelParticlesPacket> getNMSLevelParticleClass() {
        return ClientboundLevelParticlesPacket.class;
    }

    @Override
    public EntityType<?> toNMSEntityType(org.bukkit.entity.EntityType from) {
        return BuiltInRegistries.ENTITY_TYPE.getOptional(asNMSNamespacedKey(from.getKey())).orElseThrow();
    }

    @Override
    public org.bukkit.entity.EntityType fromNMSEntityType(EntityType to) {
        return Registry.ENTITY_TYPE.get(asNamespacedKey(BuiltInRegistries.ENTITY_TYPE.getKey(to)));
    }

    @Override
    public Class<EntityType> getNMSEntityTypeClass() {
        return EntityType.class;
    }

    @Override
    public ResourceLocation asNMSNamespacedKey(NamespacedKey key) {
        return PaperAdventure.asVanillaNullable(key);
    }

    @Override
    public NamespacedKey asNamespacedKey(ResourceLocation resourceLocation) {
        Key key = PaperAdventure.asAdventure(resourceLocation);
        return new NamespacedKey(key.namespace(), key.value());
    }

    @Override
    public Class<ResourceLocation> getNMSNamespacedKeyClass() {
        return ResourceLocation.class;
    }

    @Override
    public GameType toNMSGameMode(GameMode from) {
        return GameType.valueOf(from.name());
    }

    @Override
    public GameMode fromNMSGameMode(GameType from) {
        return GameMode.valueOf(from.name());
    }

    @Override
    public Class<GameType> getNMSGameModeClass() {
        return GameType.class;
    }

    @Override
    public GameProfile toNMSPlayerProfile(PlayerProfile from) {
        return CraftPlayerProfile.asAuthlibCopy(from);
    }

    @Override
    public PlayerProfile fromNMSPlayerProfile(GameProfile from) {
        return CraftPlayerProfile.asBukkitCopy(from);
    }

    @Override
    public Class<GameProfile> getNMSPlayerProfileClass() {
        return GameProfile.class;
    }

    @Override
    public RemoteChatSession.Data toNMSChatSessionData(ChatSessionData from) {
        var key = from.profilePublicKey();
        return new RemoteChatSession.Data(from.sessionId(), new ProfilePublicKey.Data(key.timestamp(), key.publicKey(), key.signature()));
    }

    @Override
    public ChatSessionData fromNMSChatSessionData(RemoteChatSession.Data from) {
        var data = from.profilePublicKey();
        return new ChatSessionData(from.sessionId(), new it.jakegblp.lusk.nms.core.world.player.ProfilePublicKey(data.expiresAt(), data.key(), data.keySignature()));
    }

    @Override
    public Class<RemoteChatSession.Data> getNMSChatSessionDataClass() {
        return RemoteChatSession.Data.class;
    }

    @Override
    public Component asNMSComponent(net.kyori.adventure.text.Component component) {
        return PaperAdventure.asVanilla(component);
    }

    @Override
    public net.kyori.adventure.text.Component asComponent(Component component) {
        return PaperAdventure.asAdventure(component);
    }
}
