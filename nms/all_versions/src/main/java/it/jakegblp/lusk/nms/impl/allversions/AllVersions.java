package it.jakegblp.lusk.nms.impl.allversions;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.datacomponent.item.PaperResolvableProfile;
import io.papermc.paper.world.WeatheringCopperState;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.common.reflection.SimpleClass;
import it.jakegblp.lusk.nms.core.adapter.SharedBehaviorAdapter;
import it.jakegblp.lusk.nms.core.event.PacketWrapperEvent;
import it.jakegblp.lusk.nms.core.event.client.PacketPreSendEvent;
import it.jakegblp.lusk.nms.core.event.client.PacketSendEvent;
import it.jakegblp.lusk.nms.core.event.server.PacketPreReceiveEvent;
import it.jakegblp.lusk.nms.core.event.server.PacketReceiveEvent;
import it.jakegblp.lusk.nms.core.event.server.PlayerActionPacketEvent;
import it.jakegblp.lusk.nms.core.protocol.packets.client.*;
import it.jakegblp.lusk.nms.core.protocol.packets.server.PlayerActionPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.server.ServerboundPacket;
import it.jakegblp.lusk.nms.core.serialization.BufferCodec;
import it.jakegblp.lusk.nms.core.serialization.Mapper;
import it.jakegblp.lusk.nms.core.serialization.RegistryMapper;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.world.entity.ServerEntityReference;
import it.jakegblp.lusk.nms.core.world.entity.attribute.AttributeSnapshot;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataItem;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import it.jakegblp.lusk.nms.core.world.level.particles.ParticleWrapper;
import it.jakegblp.lusk.nms.core.world.player.chat.ChatSessionData;
import it.jakegblp.lusk.nms.core.world.player.profile.ProfilePropertySet;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.ChatFormatting;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.RemoteChatSession;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.animal.feline.CatVariant;
import net.minecraft.world.entity.animal.frog.FrogVariant;
import net.minecraft.world.entity.animal.golem.CopperGolemState;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariant;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.*;
import org.bukkit.craftbukkit.attribute.CraftAttributeInstance;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftEntityType;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftBlockVector;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.MainHand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.util.*;
import java.util.function.Function;

import static net.minecraft.network.codec.ByteBufCodecs.ROTATION_BYTE;

public class AllVersions implements
        SharedBehaviorAdapter<
                EntityDataSerializer,
                ServerPlayer,
                Packet,
                ServerGamePacketListenerImpl,
                Connection
                > {

    public final Class<Identifier> NMS_NAMESPACED_KEY_CLASS =
            SimpleClass.firstInPackage("net.minecraft.resources", "Identifier", "ResourceLocation");
    @SuppressWarnings("rawtypes")
    @Getter
    public final BiMap<it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer<?>, EntityDataSerializer> entityDataSerializers = HashBiMap.create();

    @Getter
    private final MappingsImpl mappings = new MappingsImpl(this);
    @Getter
    private final BufferCodecsImpl codecs = new BufferCodecsImpl(this);

    public BufferCodec<NamespacedKey> NAMESPACED_KEY_CODEC;
    public BufferCodec<NamespacedKey> NAMESPACED_KEY_256_CODEC;
    public Mapper<Identifier, NamespacedKey> NAMESPACED_KEY_MAPPER;
    public BufferCodec<org.bukkit.entity.Pose> POSE_CODEC;
    public Mapper<Pose, org.bukkit.entity.Pose> POSE_MAPPER;
    public BufferCodec<NamedTextColor> NAMED_TEXT_COLOR_CODEC;
    public Mapper<ChatFormatting, NamedTextColor> NAMED_TEXT_COLOR_MAPPER;
    public BufferCodec<org.bukkit.DyeColor> DYE_COLOR_CODEC;
    public Mapper<DyeColor, org.bukkit.DyeColor> DYE_COLOR_MAPPER;
    public BufferCodec<net.kyori.adventure.text.Component> COMPONENT_CODEC;
    public Mapper<Component, net.kyori.adventure.text.Component> COMPONENT_MAPPER;
    public BufferCodec<Optional<net.kyori.adventure.text.Component>> OPTIONAL_COMPONENT_CODEC;
    @SuppressWarnings("rawtypes")
    public BufferCodec<MetadataItem> METADATA_ITEM_CODEC;
    @SuppressWarnings("rawtypes")
    public Mapper<SynchedEntityData.DataValue, MetadataItem> METADATA_ITEM_MAPPER;
    public BufferCodec<org.bukkit.inventory.EquipmentSlot> EQUIPMENT_SLOT_CODEC;
    public Mapper<EquipmentSlot, org.bukkit.inventory.EquipmentSlot> EQUIPMENT_SLOT_MAPPER;
    public BufferCodec<org.bukkit.inventory.EquipmentSlotGroup> EQUIPMENT_SLOT_GROUP_CODEC;
    public Mapper<EquipmentSlotGroup, org.bukkit.inventory.EquipmentSlotGroup> EQUIPMENT_SLOT_GROUP_MAPPER;
    public BufferCodec<org.bukkit.entity.EntityType> ENTITY_TYPE_CODEC;
    @SuppressWarnings("rawtypes")
    public Mapper<EntityType, org.bukkit.entity.EntityType> ENTITY_TYPE_MAPPER;
    public BufferCodec<it.jakegblp.lusk.nms.core.world.player.chat.ProfilePublicKey> PROFILE_PUBLIC_KEY_CODEC;
    public Mapper<ProfilePublicKey, it.jakegblp.lusk.nms.core.world.player.chat.ProfilePublicKey> PROFILE_PUBLIC_KEY_MAPPER;
    public Mapper<RemoteChatSession, ChatSessionData> CHAT_SESSION_DATA_MAPPER;
    public BufferCodec<ChatSessionData> CHAT_SESSION_DATA_CODEC;
    public Mapper<GameProfile, PlayerProfile> PLAYER_PROFILE_MAPPER;
    public BufferCodec<PlayerProfile> PLAYER_PROFILE_CODEC;
    public Mapper<BlockState, BlockData> BLOCK_DATA_MAPPER;
    public BufferCodec<BlockData> BLOCK_DATA_CODEC;
    public BufferCodec<Optional<BlockData>> OPTIONAL_BLOCK_DATA_CODEC;
    public Mapper<ItemStack, org.bukkit.inventory.ItemStack> ITEMSTACK_MAPPER;
    public BufferCodec<org.bukkit.inventory.ItemStack> ITEMSTACK_CODEC, OPTIONAL_ITEMSTACK_CODEC;
    public Mapper<GameType, GameMode> GAMEMODE_MAPPER;
    public BufferCodec<GameMode> GAMEMODE_CODEC;
    public Mapper<PropertyMap, ProfilePropertySet> PROFILE_PROPERTIES_MAPPER;
    public BufferCodec<ProfilePropertySet> PROFILE_PROPERTIES_CODEC;
    public Mapper<ClientboundSetEntityDataPacket, EntityMetadataPacket> ENTITY_METADATA_PACKET_MAPPER;
    public BufferCodec<EntityMetadataPacket> ENTITY_METADATA_PACKET_CODEC;
    public Mapper<ClientboundPlayerInfoUpdatePacket, PlayerInfoUpdatePacket> PLAYER_INFO_UPDATE_PACKET_MAPPER;
    public BufferCodec<PlayerInfoUpdatePacket> PLAYER_INFO_UPDATE_PACKET_CODEC;
    public Mapper<ClientboundSetCameraPacket, SetCameraPacket> SET_CAMERA_PACKET_MAPPER;
    public BufferCodec<SetCameraPacket> SET_CAMERA_PACKET_CODEC;
    public Mapper<ClientboundSetPassengersPacket, SetPassengersPacket> SET_PASSENGER_PACKET_MAPPER;
    public BufferCodec<SetPassengersPacket> SET_PASSENGER_PACKET_CODEC;
    public Mapper<ServerboundPlayerActionPacket, PlayerActionPacket> PLAYER_ACTION_PACKET_MAPPER;
    public BufferCodec<PlayerActionPacket> PLAYER_ACTION_PACKET_CODEC;
    public Mapper<ClientboundSystemChatPacket, SystemChatPacket> SYSTEM_CHAT_PACKET_MAPPER;
    public BufferCodec<SystemChatPacket> SYSTEM_CHAT_PACKET_CODEC;
    public Mapper<ClientboundEntityEventPacket, EntityEventPacket> ENTITY_EVENT_PACKET_MAPPER;
    public BufferCodec<EntityEventPacket> ENTITY_EVENT_PACKET_CODEC;
    public Mapper<ClientboundSetEquipmentPacket, SetEquipmentPacket> SET_EQUIPMENT_PACKET_MAPPER;
    public BufferCodec<SetEquipmentPacket> SET_EQUIPMENT_PACKET_CODEC;
    public BufferCodec<Integer> UNSIGNED_BYTE_CODEC;
    public BufferCodec<Boolean> BOOLEAN_CODEC;
    public BufferCodec<String> STRING_CODEC;
    public BufferCodec<Byte> BYTE_CODEC;
    public BufferCodec<Integer> VAR_INT_CODEC;
    public BufferCodec<Optional<Integer>> OPTIONAL_VAR_INT_CODEC;
    public BufferCodec<Long> LONG_CODEC;
    public BufferCodec<Double> DOUBLE_CODEC;
    public BufferCodec<UUID> UUID_CODEC;
    public Mapper<Vector3f, Vector> VECTOR3F_MAPPER;
    public BufferCodec<Vector> VECTOR3F_CODEC;
    public Mapper<Rotations, io.papermc.paper.math.Rotations> ROTATIONS_MAPPER;
    public BufferCodec<io.papermc.paper.math.Rotations> ROTATIONS_CODEC;
    public Mapper<Vec3, Vector> VEC3_MAPPER;
    public BufferCodec<Vector> VEC3_CODEC;
    public Mapper<Direction, BlockFace> BLOCK_FACE_MAPPER;
    public BufferCodec<BlockFace> BLOCK_FACE_CODEC;
    public Mapper<BlockPos, BlockVector> BLOCK_VECTOR_MAPPER;
    public BufferCodec<BlockVector> BLOCK_VECTOR_CODEC;
    public BufferCodec<Optional<BlockVector>> OPTIONAL_BLOCK_VECTOR_CODEC;
    public BufferCodec<Float> DEGREES_CODEC;
    public BufferCodec<IntList> INT_LIST_CODEC;
    public Mapper<ClientboundAddEntityPacket, AddEntityPacket> ADD_ENTITY_PACKET_MAPPER;
    public BufferCodec<AddEntityPacket> ADD_ENTITY_PACKET_CODEC;
    public Mapper<ClientboundSetPlayerTeamPacket, TeamPacket> TEAM_PACKET_MAPPER;
    public BufferCodec<TeamPacket> TEAM_PACKET_CODEC;
    public Mapper<ClientboundBlockUpdatePacket, BlockUpdatePacket> BLOCK_UPDATE_PACKET_MAPPER;
    public BufferCodec<BlockUpdatePacket> BLOCK_UPDATE_PACKET_CODEC;
    public Mapper<ClientboundBlockDestructionPacket, BlockDestructionPacket> BLOCK_DESTRUCTION_PACKET_MAPPER;
    public BufferCodec<BlockDestructionPacket> BLOCK_DESTRUCTION_PACKET_CODEC;
    public Mapper<ClientboundAnimatePacket, EntityAnimationPacket> ENTITY_ANIMATION_PACKET_MAPPER;
    public BufferCodec<EntityAnimationPacket> ENTITY_ANIMATION_PACKET_CODEC;
    public Mapper<ClientboundRemoveEntitiesPacket, RemoveEntitiesPacket> REMOVE_ENTITIES_PACKET_MAPPER;
    public BufferCodec<RemoveEntitiesPacket> REMOVE_ENTITIES_PACKET_CODEC;
    public Mapper<ClientboundPlayerPositionPacket, PlayerPositionPacket> PLAYER_POSITION_PACKET_MAPPER;
    public BufferCodec<PlayerPositionPacket> PLAYER_POSITION_PACKET_CODEC;
    public Mapper<SoundEvent, Sound> SOUND_MAPPER;
    public BufferCodec<Sound> SOUND_CODEC;
    public Mapper<SoundSource, SoundCategory> SOUND_CATEGORY_MAPPER;
    public BufferCodec<SoundCategory> SOUND_CATEGORY_CODEC;
    public Mapper<AttributeModifier, org.bukkit.attribute.AttributeModifier> ATTRIBUTE_MODIFIER_MAPPER;
    public BufferCodec<org.bukkit.attribute.AttributeModifier> ATTRIBUTE_MODIFIER_CODEC;
    public Mapper<ClientboundUpdateAttributesPacket.AttributeSnapshot, AttributeSnapshot> ATTRIBUTE_SNAPSHOT_MAPPER;
    public BufferCodec<AttributeSnapshot> ATTRIBUTE_SNAPSHOT_CODEC;
    public RegistryMapper<Attribute, org.bukkit.attribute.Attribute> ATTRIBUTE_MAPPER;
    public BufferCodec<org.bukkit.attribute.Attribute> ATTRIBUTE_CODEC;
    public Mapper<ClientboundUpdateAttributesPacket, UpdateAttributesPacket> UPDATE_ATTRIBUTES_PACKET_MAPPER;
    public BufferCodec<UpdateAttributesPacket> UPDATE_ATTRIBUTES_PACKET_CODEC;
    public Mapper<ClientboundSoundPacket, SoundPacket> SOUND_PACKET_MAPPER;
    public BufferCodec<SoundPacket> SOUND_PACKET_CODEC;
    public Mapper<ClientboundSoundEntityPacket, SoundEntityPacket> SOUND_ENTITY_PACKET_MAPPER;
    public BufferCodec<SoundEntityPacket> SOUND_ENTITY_PACKET_CODEC;
    public Mapper<ParticleOptions, ParticleWrapper> PARTICLE_MAPPER;
    public BufferCodec<ParticleWrapper> PARTICLE_CODEC;
    public BufferCodec<Optional<ParticleWrapper>> OPTIONAL_PARTICLE_CODEC;
    public BufferCodec<List<ParticleWrapper>> LIST_PARTICLE_CODEC;
    public Mapper<ClientboundLevelParticlesPacket, LevelParticlesPacket> LEVEL_PARTICLES_PACKET_MAPPER;
    public BufferCodec<LevelParticlesPacket> LEVEL_PARTICLES_PACKET_CODEC;
    public Mapper<EntityReference, ServerEntityReference> SERVER_ENTITY_REFERENCE_MAPPER;
    public BufferCodec<ServerEntityReference> SERVER_ENTITY_REFERENCE_CODEC;
    public BufferCodec<Optional<ServerEntityReference>> OPTIONAL_SERVER_ENTITY_REFERENCE_CODEC;
    public RegistryMapper<WolfVariant, Wolf.Variant> WOLF_VARIANT_MAPPER;
    public BufferCodec<Wolf.Variant> WOLF_VARIANT_CODEC;
    public RegistryMapper<WolfSoundVariant, Wolf.SoundVariant> WOLF_SOUND_VARIANT_MAPPER;
    public BufferCodec<Wolf.SoundVariant> WOLF_SOUND_VARIANT_CODEC;
    public RegistryMapper<FrogVariant, Frog.Variant> FROG_VARIANT_MAPPER;
    public BufferCodec<Frog.Variant> FROG_VARIANT_CODEC;
    public Mapper<Sniffer.State, org.bukkit.entity.Sniffer.State> SNIFFER_STATE_MAPPER;
    public BufferCodec<org.bukkit.entity.Sniffer.State> SNIFFER_STATE_CODEC;
    public Mapper<WeatheringCopper.WeatherState, WeatheringCopperState> WEATHERING_COPPER_STATE_MAPPER;
    public BufferCodec<WeatheringCopperState> WEATHERING_COPPER_STATE_CODEC;
    public BufferCodec<Quaternionf> QUATERNIONF_CODEC;
    public BufferCodec<Quaternionfc> QUATERNIONFC_CODEC;
    public Mapper<ResolvableProfile, io.papermc.paper.datacomponent.item.ResolvableProfile> RESOLVABLE_PROFILE_MAPPER;
    public BufferCodec<io.papermc.paper.datacomponent.item.ResolvableProfile> RESOLVABLE_PROFILE_CODEC;
    public Mapper<net.minecraft.world.entity.animal.armadillo.Armadillo.ArmadilloState, Armadillo.State> ARMADILLO_STATE_MAPPER;
    public BufferCodec<Armadillo.State> ARMADILLO_STATE_CODEC;
    public Mapper<HumanoidArm, MainHand> HUMANOID_ARM_MAPPER;
    public BufferCodec<MainHand> HUMANOID_ARM_CODEC;
    public Mapper<ClientboundBundlePacket, ClientBundlePacket> CLIENT_BUNDLE_PACKET_MAPPER; // no codec, we don't serialize this one
    public Mapper<ClientboundMoveEntityPacket.Pos, MoveEntityPacket.Position> MOVE_ENTITY_POSITION_PACKET_MAPPER;
    public BufferCodec<MoveEntityPacket.Position> MOVE_ENTITY_POSITION_PACKET_CODEC;
    public Mapper<ClientboundMoveEntityPacket.Rot, MoveEntityPacket.Rotation> MOVE_ENTITY_ROTATION_PACKET_MAPPER;
    public BufferCodec<MoveEntityPacket.Rotation> MOVE_ENTITY_ROTATION_PACKET_CODEC;
    public Mapper<ClientboundMoveEntityPacket.PosRot, MoveEntityPacket.PositionRotation> MOVE_ENTITY_POSITION_ROTATION_PACKET_MAPPER;
    public BufferCodec<MoveEntityPacket.PositionRotation> MOVE_ENTITY_POSITION_ROTATION_PACKET_CODEC;
    public Mapper<GlobalPos, Location> LOCATION_MAPPER;
    public BufferCodec<Location> LOCATION_CODEC;
    public BufferCodec<Optional<Location>> OPTIONAL_LOCATION__CODEC;

    private Version serverVersion;

    @Override
    public void init(Version serverVersion) {
        this.serverVersion = serverVersion;

        NAMESPACED_KEY_CODEC = codecs.register(
                NamespacedKey.class,
                (buf, namespacedKey) -> buf.writeString(namespacedKey.asString()),
                buf -> NamespacedKey.fromString(buf.readString())
        );
        NAMESPACED_KEY_256_CODEC = codecs.register(
                NamespacedKey.class,
                (buf, namespacedKey) -> buf.writeString(namespacedKey.asString(), 256),
                buf -> NamespacedKey.fromString(buf.readString(256))
        );
        NAMESPACED_KEY_MAPPER = mappings.register(
                NMS_NAMESPACED_KEY_CLASS, NamespacedKey.class,
                CraftNamespacedKey::fromMinecraft,
                CraftNamespacedKey::toMinecraft
        );

        POSE_CODEC = codecs.register(org.bukkit.entity.Pose.class);
        POSE_MAPPER = mappings.register(Pose.class, org.bukkit.entity.Pose.class);

        NAMED_TEXT_COLOR_MAPPER = mappings.register(
                ChatFormatting.class, NamedTextColor.class,
                color -> (NamedTextColor) PaperAdventure.asAdventure(color),
                PaperAdventure::asVanilla
        );
        NAMED_TEXT_COLOR_CODEC = codecs.register(
                NamedTextColor.class,
                (buf, namedTextColor) -> buf.writeEnum(NAMED_TEXT_COLOR_MAPPER.from(namedTextColor)),
                buf -> NAMED_TEXT_COLOR_MAPPER.to(buf.readEnum(ChatFormatting.class))
        );

        DYE_COLOR_CODEC = codecs.register(
                org.bukkit.DyeColor.class,
                (buf, color) -> buf.writeConstant(color, org.bukkit.DyeColor::getWoolData),
                buf -> buf.readConstant(org.bukkit.DyeColor.values())
        );
        DYE_COLOR_MAPPER = mappings.register(
                DyeColor.class, org.bukkit.DyeColor.class,
                color -> org.bukkit.DyeColor.getByWoolData((byte) color.getId()),
                color -> DyeColor.byId(color.getWoolData())
        );

        COMPONENT_MAPPER = mappings.register(
                Component.class, net.kyori.adventure.text.Component.class,
                PaperAdventure::asAdventure,
                PaperAdventure::asVanilla
        );
        COMPONENT_CODEC = codecs.register(
                COMPONENT_MAPPER,
                (buf, component) -> ComponentSerialization.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buf), component),
                buf -> ComponentSerialization.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buf))
        );
        OPTIONAL_COMPONENT_CODEC = codecs.register(COMPONENT_CODEC.optional());

        METADATA_ITEM_MAPPER = mappings.register(
                SynchedEntityData.DataValue.class,
                MetadataItem.class,
                dataValue -> new MetadataItem<>(dataValue.id(), mappings.map(dataValue.value()), getEntityDataSerializers().inverse().get(dataValue.serializer())),
                item -> new SynchedEntityData.DataValue<>(item.id(), getEntityDataSerializers().get(item.serializer()), mappings.mapBack(item.value()))
        );
        METADATA_ITEM_CODEC = codecs.register(BufferCodec.of(MetadataItem.class, buf -> new MetadataItem(buf)));
        //METADATA_ITEM_CODEC = codecs.register(BufferCodec.of(MetadataItem.class, buf -> new MetadataItem(buf)));
        //METADATA_ITEM_MAPPER = mappings.register(
        //        SynchedEntityData.DataValue.class,
        //        (buffer, nms) -> nms.write(toRegistryFriendlyByteBuf(buffer)),
        //        buffer -> SynchedEntityData.DataValue.read(toRegistryFriendlyByteBuf(buffer), buffer.readByte()),
        //        METADATA_ITEM_CODEC
        //);

        EQUIPMENT_SLOT_CODEC = codecs.register(org.bukkit.inventory.EquipmentSlot.class);
        EQUIPMENT_SLOT_MAPPER = mappings.register(EquipmentSlot.class, org.bukkit.inventory.EquipmentSlot.class);

        EQUIPMENT_SLOT_GROUP_MAPPER = mappings.register(
                EquipmentSlotGroup.class,
                org.bukkit.inventory.EquipmentSlotGroup.class,
                CraftEquipmentSlot::getSlotGroup,
                CraftEquipmentSlot::getNMSGroup
        );
        EQUIPMENT_SLOT_GROUP_CODEC = codecs.register(
                EQUIPMENT_SLOT_GROUP_MAPPER,
                (buffer, value) -> EquipmentSlotGroup.STREAM_CODEC.encode(buffer.unwrap(), value),
                buffer -> EquipmentSlotGroup.STREAM_CODEC.decode(buffer.unwrap())
        );

        ENTITY_TYPE_MAPPER = mappings.register(
                EntityType.class,
                org.bukkit.entity.EntityType.class,
                CraftEntityType::minecraftToBukkit,
                CraftEntityType::bukkitToMinecraft
        );
        final var entityTypeStreamCodec = ByteBufCodecs.registry(Registries.ENTITY_TYPE); // the standard one is not present in all supported versions
        ENTITY_TYPE_CODEC = codecs.register(
                ENTITY_TYPE_MAPPER,
                (buffer, entityType) -> entityTypeStreamCodec.encode(toRegistryFriendlyByteBuf(buffer), entityType),
                buffer -> entityTypeStreamCodec.decode(toRegistryFriendlyByteBuf(buffer))
        );

        PROFILE_PUBLIC_KEY_CODEC = codecs.register(
                it.jakegblp.lusk.nms.core.world.player.chat.ProfilePublicKey.class,
                SimpleByteBuf::writeProfilePublicKey,
                SimpleByteBuf::readProfilePublicKey
        );
        PROFILE_PUBLIC_KEY_MAPPER = mappings.register(
                ProfilePublicKey.class,
                (buffer, value) -> value.data().write(toFriendlyByteBuf(buffer)),
                buffer -> new ProfilePublicKey(new ProfilePublicKey.Data(toFriendlyByteBuf(buffer))),
                PROFILE_PUBLIC_KEY_CODEC
        );

        CHAT_SESSION_DATA_CODEC = codecs.register(
                ChatSessionData.class,
                SimpleByteBuf::writeChatSessionData,
                SimpleByteBuf::readChatSessionData
        );
        CHAT_SESSION_DATA_MAPPER = mappings.register(
                RemoteChatSession.class,
                ChatSessionData.class,
                remoteChatSession -> new ChatSessionData(remoteChatSession.sessionId(), PROFILE_PUBLIC_KEY_MAPPER.to(remoteChatSession.profilePublicKey())),
                chatSessionData -> new RemoteChatSession(chatSessionData.getSessionId(), PROFILE_PUBLIC_KEY_MAPPER.from(chatSessionData.getProfilePublicKey()))
        );

        PLAYER_PROFILE_MAPPER = mappings.register(
                GameProfile.class,
                PlayerProfile.class,
                CraftPlayerProfile::asBukkitCopy,
                CraftPlayerProfile::asAuthlibCopy
        );
        PLAYER_PROFILE_CODEC = codecs.register(
                PLAYER_PROFILE_MAPPER,
                (buffer, profile) -> ByteBufCodecs.GAME_PROFILE.encode(buffer.unwrap(), profile),
                buffer -> ByteBufCodecs.GAME_PROFILE.decode(buffer.unwrap())
        );

        BLOCK_DATA_MAPPER = mappings.register(
                BlockState.class,
                BlockData.class,
                CraftBlockData::fromData,
                bukkit -> ((CraftBlockData) bukkit).getState()
        );
        var blockDataCodec = ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY);
        BLOCK_DATA_CODEC = codecs.register(
                BLOCK_DATA_MAPPER,
                (buffer, value) -> blockDataCodec.encode(buffer.unwrap(), value),
                buffer -> blockDataCodec.decode(buffer.unwrap())
        );

        OPTIONAL_BLOCK_DATA_CODEC = codecs.register(BLOCK_DATA_CODEC.optional());

        ITEMSTACK_MAPPER = mappings.register(
                ItemStack.class,
                org.bukkit.inventory.ItemStack.class,
                CraftItemStack::asBukkitCopy,
                CraftItemStack::asNMSCopy
        );
        ITEMSTACK_CODEC = codecs.register(
                ITEMSTACK_MAPPER,
                (buffer, value) -> ItemStack.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), value),
                buffer -> ItemStack.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer))
        );
        OPTIONAL_ITEMSTACK_CODEC = codecs.register(
                ITEMSTACK_MAPPER,
                (buffer, value) -> ItemStack.OPTIONAL_STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), value),
                buffer -> ItemStack.OPTIONAL_STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer))
        );

        GAMEMODE_MAPPER = mappings.register(
                GameType.class,
                GameMode.class,
                gameType -> GameMode.getByValue(gameType.getId()),
                gameMode -> GameType.byId(gameMode.getValue())
        );
        GAMEMODE_CODEC = codecs.register(
                GAMEMODE_MAPPER,
                (buffer, value) -> GameType.STREAM_CODEC.encode(buffer.unwrap(), value),
                buffer -> GameType.STREAM_CODEC.decode(buffer.unwrap())
        );

        PROFILE_PROPERTIES_CODEC = codecs.register(
                ProfilePropertySet.class,
                (buffer, propertySet) -> {
                    buffer.writeCount(propertySet.size(), 16);
                    for (ProfileProperty property : propertySet) {
                        buffer.writeString(property.getName(), 64);
                        buffer.writeString(property.getValue(), 32767);
                        buffer.writeNullable(property.getSignature(), (simpleByteBuf, string) -> simpleByteBuf.writeString(string, 1024));
                    }
                },
                buffer -> {
                    int count = buffer.readCount(16);
                    ProfilePropertySet propertySet = new ProfilePropertySet();
                    for (int i = 0; i < count; ++i) {
                        String string = buffer.readString(64);
                        String string1 = buffer.readString(32767);
                        String string2 = buffer.readNullable((buffer1) -> buffer1.readString(1024));
                        propertySet.add(new ProfileProperty(string, string1, string2));
                    }
                    return propertySet;
                }
        );
        PROFILE_PROPERTIES_MAPPER = mappings.register(
                PropertyMap.class,
                (buf, propertyMap) -> ByteBufCodecs.GAME_PROFILE_PROPERTIES.encode(buf.unwrap(), propertyMap),
                buf -> ByteBufCodecs.GAME_PROFILE_PROPERTIES.decode(buf.unwrap()),
                PROFILE_PROPERTIES_CODEC
        );

        ENTITY_METADATA_PACKET_MAPPER = mappings.register(
                ClientboundSetEntityDataPacket.class,
                EntityMetadataPacket.class,
                packet -> {
                    List<SynchedEntityData.DataValue<?>> dataValueList = packet.packedItems();
                    EntityMetadata entityMetadata = new EntityMetadata();
                    for (SynchedEntityData.DataValue<?> dataValue : dataValueList)
                        entityMetadata.setInternal(dataValue.id(), METADATA_ITEM_MAPPER.to(dataValue));
                    return new EntityMetadataPacket(packet.id(), entityMetadata);
                },
                packet -> new ClientboundSetEntityDataPacket(
                        packet.getEntityId(),
                        CommonUtils.map(packet.getEntityMetadata().items(),
                                item -> METADATA_ITEM_MAPPER.from(item))
                )

        );
        ENTITY_METADATA_PACKET_CODEC = codecs.register(
                EntityMetadataPacket.class,
                (buf, entityMetadataPacket) -> entityMetadataPacket.write(buf),
                EntityMetadataPacket::new
        );
        //ENTITY_METADATA_PACKET_CODEC = codecs.register(
        //        EntityMetadataPacket.class,
        //        (buf, entityMetadataPacket) -> entityMetadataPacket.write(buf),
        //        EntityMetadataPacket::new
        //);
        //ENTITY_METADATA_PACKET_MAPPER = mappings.register(
        //        ClientboundSetEntityDataPacket.class,
        //        (buffer, nms) -> ClientboundSetEntityDataPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), nms),
        //        (buffer) -> ClientboundSetEntityDataPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
        //        ENTITY_METADATA_PACKET_CODEC
        //);

        PLAYER_INFO_UPDATE_PACKET_CODEC = codecs.register(
                PlayerInfoUpdatePacket.class,
                (buf, playerInfoUpdatePacket) -> playerInfoUpdatePacket.write(buf),
                PlayerInfoUpdatePacket::new
        );
        PLAYER_INFO_UPDATE_PACKET_MAPPER = mappings.register(
                ClientboundPlayerInfoUpdatePacket.class,
                (buffer, packet) -> ClientboundPlayerInfoUpdatePacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), packet),
                buffer -> ClientboundPlayerInfoUpdatePacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
                PLAYER_INFO_UPDATE_PACKET_CODEC
        );

        SET_CAMERA_PACKET_CODEC = codecs.register(
                SetCameraPacket.class,
                (buf, packet) -> packet.write(buf),
                SetCameraPacket::new
        );
        SET_CAMERA_PACKET_MAPPER = mappings.register(
                ClientboundSetCameraPacket.class,
                (buffer, packet) -> ClientboundSetCameraPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), packet),
                buffer -> ClientboundSetCameraPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
                SET_CAMERA_PACKET_CODEC
        );

        SET_PASSENGER_PACKET_CODEC = codecs.register(
                SetPassengersPacket.class,
                (buf, packet) -> packet.write(buf),
                SetPassengersPacket::new
        );
        SET_PASSENGER_PACKET_MAPPER = mappings.register(
                ClientboundSetPassengersPacket.class,
                (buffer, packet) -> ClientboundSetPassengersPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), packet),
                buffer -> ClientboundSetPassengersPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
                SET_PASSENGER_PACKET_CODEC
        );

        PLAYER_ACTION_PACKET_CODEC = codecs.register(
                PlayerActionPacket.class,
                (buf, packet) -> packet.write(buf),
                PlayerActionPacket::new
        );
        PLAYER_ACTION_PACKET_MAPPER = mappings.register(
                ServerboundPlayerActionPacket.class,
                (buffer, packet) -> ServerboundPlayerActionPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), packet),
                buffer -> ServerboundPlayerActionPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
                PLAYER_ACTION_PACKET_CODEC
        );

        SYSTEM_CHAT_PACKET_CODEC = codecs.register(
                SystemChatPacket.class,
                (buf, packet) -> packet.write(buf),
                SystemChatPacket::new
        );
        SYSTEM_CHAT_PACKET_MAPPER = mappings.register(
                ClientboundSystemChatPacket.class,
                (buffer, packet) -> ClientboundSystemChatPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), packet),
                buffer -> ClientboundSystemChatPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
                SYSTEM_CHAT_PACKET_CODEC
        );

        ENTITY_EVENT_PACKET_CODEC = codecs.register(
                EntityEventPacket.class,
                (buf, packet) -> packet.write(buf),
                EntityEventPacket::new
        );
        ENTITY_EVENT_PACKET_MAPPER = mappings.register(
                ClientboundEntityEventPacket.class,
                (buffer, packet) -> ClientboundEntityEventPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), packet),
                buffer -> ClientboundEntityEventPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
                ENTITY_EVENT_PACKET_CODEC
        );

        SET_EQUIPMENT_PACKET_CODEC = codecs.register(
                SetEquipmentPacket.class,
                (buf, packet) -> packet.write(buf),
                SetEquipmentPacket::new
        );
        SET_EQUIPMENT_PACKET_MAPPER = mappings.register(
                ClientboundSetEquipmentPacket.class,
                (buffer, packet) -> ClientboundSetEquipmentPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), packet),
                buffer -> ClientboundSetEquipmentPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
                SET_EQUIPMENT_PACKET_CODEC
        );

        UNSIGNED_BYTE_CODEC = codecs.register(Integer.class, SimpleByteBuf::writeUnsignedByte, SimpleByteBuf::readUnsignedByte);
        BOOLEAN_CODEC = codecs.register(Boolean.class, SimpleByteBuf::writeBoolean, SimpleByteBuf::readBoolean);
        STRING_CODEC = codecs.register(String.class, SimpleByteBuf::writeString, SimpleByteBuf::readString);
        BYTE_CODEC = codecs.register(Byte.class, (buffer, value) -> buffer.writeByte(value), SimpleByteBuf::readByte);
        VAR_INT_CODEC = codecs.register(Integer.class, SimpleByteBuf::writeVarInt, SimpleByteBuf::readVarInt);
        OPTIONAL_VAR_INT_CODEC = codecs.register(VAR_INT_CODEC.optional());
        LONG_CODEC = codecs.register(Long.class, SimpleByteBuf::writeLong, SimpleByteBuf::readLong);
        DOUBLE_CODEC = codecs.register(Double.class, SimpleByteBuf::writeDouble, SimpleByteBuf::readDouble);
        UUID_CODEC = codecs.register(UUID.class, SimpleByteBuf::writeUUID, SimpleByteBuf::readUUID);

        VECTOR3F_CODEC = codecs.register(Vector.class, SimpleByteBuf::writeVector3f, SimpleByteBuf::readVector3f);
        VECTOR3F_MAPPER = mappings.register(
                Vector3f.class, Vector.class,
                vector3f -> new Vector(vector3f.x(), vector3f.y(), vector3f.z()),
                Vector::toVector3f
        );

        ROTATIONS_CODEC = codecs.register(
                io.papermc.paper.math.Rotations.class,
                SimpleByteBuf::writeRotations,
                SimpleByteBuf::readRotations
        );
        ROTATIONS_MAPPER = mappings.register(
                Rotations.class, io.papermc.paper.math.Rotations.class,
                rotations -> io.papermc.paper.math.Rotations.ofDegrees(rotations.x(), rotations.y(), rotations.z()),
                rotations -> new Rotations((float) rotations.x(), (float) rotations.y(), (float) rotations.z())
        );

        VEC3_CODEC = codecs.register(
                Vector.class,
                SimpleByteBuf::writeVector,
                SimpleByteBuf::readVector
        );
        VEC3_MAPPER = mappings.register(
                Vec3.class, Vector.class,
                vec3 -> new Vector(vec3.x(), vec3.y(), vec3.z()),
                vector -> new Vec3(vector.getX(), vector.getY(), vector.getZ())
        );

        BLOCK_FACE_MAPPER = mappings.register(
                Direction.class, BlockFace.class,
                CraftBlock::notchToBlockFace,
                CraftBlock::blockFaceToNotch
        );
        BLOCK_FACE_CODEC = codecs.register(
                BLOCK_FACE_MAPPER,
                (buffer, direction) -> Direction.STREAM_CODEC.encode(buffer.unwrap(), direction),
                buffer -> Direction.STREAM_CODEC.decode(buffer.unwrap())
        );

        BLOCK_VECTOR_CODEC = codecs.register(
                BlockVector.class,
                SimpleByteBuf::writeBlockVector,
                SimpleByteBuf::readBlockVector
        );
        BLOCK_VECTOR_MAPPER = mappings.register(
                BlockPos.class, BlockVector.class,
                CraftBlockVector::toBukkit,
                CraftBlockVector::toBlockPosition
        );

        OPTIONAL_BLOCK_VECTOR_CODEC = codecs.register(BLOCK_VECTOR_CODEC.optional());

        DEGREES_CODEC = codecs.register(
                Float.class, (buffer, value) -> ROTATION_BYTE.encode(buffer.unwrap(), value),
                buffer -> ROTATION_BYTE.decode(buffer.unwrap())
        );

        INT_LIST_CODEC = codecs.register(IntList.class, SimpleByteBuf::writeIntList, SimpleByteBuf::readIntList);

        ADD_ENTITY_PACKET_CODEC = codecs.register(
                AddEntityPacket.class,
                (buffer, packet) -> packet.write(buffer),
                AddEntityPacket::new
        );
        ADD_ENTITY_PACKET_MAPPER = mappings.register(
                ClientboundAddEntityPacket.class,
                (buf, packet) -> ClientboundAddEntityPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buf), packet),
                buf -> ClientboundAddEntityPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buf)),
                ADD_ENTITY_PACKET_CODEC
        );

        TEAM_PACKET_CODEC = codecs.register(
                TeamPacket.class,
                (buffer, packet) -> packet.write(buffer),
                TeamPacket::new
        );
        TEAM_PACKET_MAPPER = mappings.register(
                ClientboundSetPlayerTeamPacket.class,
                (buf, packet) -> ClientboundSetPlayerTeamPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buf), packet),
                buf -> ClientboundSetPlayerTeamPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buf)),
                TEAM_PACKET_CODEC
        );

        BLOCK_UPDATE_PACKET_CODEC = codecs.register(
                BlockUpdatePacket.class,
                (buffer, packet) -> packet.write(buffer),
                BlockUpdatePacket::new
        );
        BLOCK_UPDATE_PACKET_MAPPER = mappings.register(
                ClientboundBlockUpdatePacket.class,
                (buf, packet) -> ClientboundBlockUpdatePacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buf), packet),
                buf -> ClientboundBlockUpdatePacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buf)),
                BLOCK_UPDATE_PACKET_CODEC
        );

        BLOCK_DESTRUCTION_PACKET_CODEC = codecs.register(
                BlockDestructionPacket.class,
                (buffer, packet) -> packet.write(buffer),
                BlockDestructionPacket::new
        );
        BLOCK_DESTRUCTION_PACKET_MAPPER = mappings.register(
                ClientboundBlockDestructionPacket.class,
                (buf, packet) -> ClientboundBlockDestructionPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buf), packet),
                buf -> ClientboundBlockDestructionPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buf)),
                BLOCK_DESTRUCTION_PACKET_CODEC
        );

        ENTITY_ANIMATION_PACKET_CODEC = codecs.register(
                EntityAnimationPacket.class,
                (buffer, packet) -> packet.write(buffer),
                EntityAnimationPacket::new
        );
        ENTITY_ANIMATION_PACKET_MAPPER = mappings.register(
                ClientboundAnimatePacket.class,
                (buf, packet) -> ClientboundAnimatePacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buf), packet),
                buf -> ClientboundAnimatePacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buf)),
                ENTITY_ANIMATION_PACKET_CODEC
        );

        REMOVE_ENTITIES_PACKET_CODEC = codecs.register(
                RemoveEntitiesPacket.class,
                (buffer, packet) -> packet.write(buffer),
                RemoveEntitiesPacket::new
        );
        REMOVE_ENTITIES_PACKET_MAPPER = mappings.register(
                ClientboundRemoveEntitiesPacket.class,
                (buf, packet) -> ClientboundRemoveEntitiesPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buf), packet),
                buf -> ClientboundRemoveEntitiesPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buf)),
                REMOVE_ENTITIES_PACKET_CODEC
        );

        PLAYER_POSITION_PACKET_CODEC = codecs.register(
                PlayerPositionPacket.class,
                (buffer, packet) -> packet.write(buffer),
                PlayerPositionPacket::new
        );
        PLAYER_POSITION_PACKET_MAPPER = mappings.register(
                ClientboundPlayerPositionPacket.class,
                (buf, packet) -> ClientboundPlayerPositionPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buf), packet),
                buf -> ClientboundPlayerPositionPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buf)),
                PLAYER_POSITION_PACKET_CODEC
        );

        SOUND_MAPPER = mappings.register(
                SoundEvent.class, Sound.class,
                CraftSound::minecraftToBukkit,
                CraftSound::bukkitToMinecraft
        );
        SOUND_CODEC = codecs.register(
                SOUND_MAPPER,
                (buffer, value) -> SoundEvent.DIRECT_STREAM_CODEC.encode(buffer.unwrap(), value),
                buffer -> SoundEvent.DIRECT_STREAM_CODEC.decode(buffer.unwrap())
        );

        SOUND_CATEGORY_MAPPER = mappings.register(SoundSource.class, SoundCategory.class);
        SOUND_CATEGORY_CODEC = codecs.register(SoundCategory.class);

        ATTRIBUTE_MODIFIER_MAPPER = mappings.register(
                AttributeModifier.class, org.bukkit.attribute.AttributeModifier.class,
                CraftAttributeInstance::convert,
                CraftAttributeInstance::convert
        );
        ATTRIBUTE_MODIFIER_CODEC = codecs.register(
                ATTRIBUTE_MODIFIER_MAPPER,
                (buffer, value) -> AttributeModifier.STREAM_CODEC.encode(buffer.unwrap(), value),
                buffer -> AttributeModifier.STREAM_CODEC.decode(buffer.unwrap())
        );

        ATTRIBUTE_SNAPSHOT_CODEC = codecs.register(
                AttributeSnapshot.class,
                (buffer, packet) -> packet.write(buffer),
                AttributeSnapshot::new
        );
        ATTRIBUTE_SNAPSHOT_MAPPER = mappings.register(
                ClientboundUpdateAttributesPacket.AttributeSnapshot.class,
                (buf, packet) -> ClientboundUpdateAttributesPacket.AttributeSnapshot.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buf), packet),
                buf -> ClientboundUpdateAttributesPacket.AttributeSnapshot.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buf)),
                ATTRIBUTE_SNAPSHOT_CODEC
        );

        ATTRIBUTE_MAPPER = mappings.register(
                Attribute.class, org.bukkit.attribute.Attribute.class,
                Registries.ATTRIBUTE
        );
        ATTRIBUTE_CODEC = codecs.register(
                ATTRIBUTE_MAPPER,
                ByteBufCodecs.holderRegistry(Registries.ATTRIBUTE)
        );

        UPDATE_ATTRIBUTES_PACKET_CODEC = codecs.register(
                UpdateAttributesPacket.class,
                (buffer, packet) -> packet.write(buffer),
                UpdateAttributesPacket::new
        );
        UPDATE_ATTRIBUTES_PACKET_MAPPER = mappings.register(
                ClientboundUpdateAttributesPacket.class,
                (buf, packet) -> ClientboundUpdateAttributesPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buf), packet),
                buf -> ClientboundUpdateAttributesPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buf)),
                UPDATE_ATTRIBUTES_PACKET_CODEC
        );
        //PACKET_FLOW_CODEC = getNMS().registerCodec(BufferCodecOld.ofEnum(
        //        PacketFlow.class, it.jakegblp.lusk.nms.core.protocol.PacketFlow.class)
        //);
        //PACKET_TYPE_CODEC = getNMS().registerCodec(
        //        PacketType.class, it.jakegblp.lusk.nms.core.protocol.PacketType.class,
        //        (buffer, value) -> {
        //            PACKET_FLOW_CODEC.writeFrom(value.flow(), buffer);
        //            NAMESPACED_KEY_256_CODEC.writeFrom(value.id(), buffer);
        //        },
        //        buffer -> new PacketType<>(PACKET_FLOW_CODEC.readFrom(buffer), NAMESPACED_KEY_256_CODEC.readFrom(buffer)),
        //        (buffer, value) -> {
        //            PACKET_FLOW_CODEC.writeTo(value.packetFlow(), buffer);
        //            NAMESPACED_KEY_256_CODEC.writeTo(value.key(), buffer);
        //        },
        //        buffer -> new it.jakegblp.lusk.nms.core.protocol.PacketType(PACKET_FLOW_CODEC.readTo(buffer), NAMESPACED_KEY_256_CODEC.readTo(buffer))
        //);

        SOUND_PACKET_CODEC = codecs.register(
                SoundPacket.class,
                (buffer, packet) -> packet.write(buffer),
                SoundPacket::new
        );
        SOUND_PACKET_MAPPER = mappings.register(
                ClientboundSoundPacket.class,
                (buf, packet) -> ClientboundSoundPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buf), packet),
                buf -> ClientboundSoundPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buf)),
                SOUND_PACKET_CODEC
        );

        SOUND_ENTITY_PACKET_CODEC = codecs.register(
                SoundEntityPacket.class,
                (buffer, packet) -> packet.write(buffer),
                SoundEntityPacket::new
        );
        SOUND_ENTITY_PACKET_MAPPER = mappings.register(
                ClientboundSoundEntityPacket.class,
                (buf, packet) -> ClientboundSoundEntityPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buf), packet),
                buf -> ClientboundSoundEntityPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buf)),
                SOUND_ENTITY_PACKET_CODEC
        );

        PARTICLE_MAPPER = mappings.register(
                ParticleOptions.class,
                ParticleWrapper.class,
                options -> new ParticleWrapper(CraftParticle.minecraftToBukkit(options.getType()), processParticleOptions(options)),
                wrapper -> CraftParticle.createParticleParam(wrapper.particle(), wrapper.data())
        );
        PARTICLE_CODEC = codecs.register(
                PARTICLE_MAPPER,
                (buffer, options) -> ParticleTypes.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), options),
                buffer -> ParticleTypes.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer))
        );
        OPTIONAL_PARTICLE_CODEC = codecs.register(PARTICLE_CODEC.optional());
        LIST_PARTICLE_CODEC = codecs.register(PARTICLE_CODEC.list());

        LEVEL_PARTICLES_PACKET_CODEC = codecs.register(
                LevelParticlesPacket.class,
                (buffer, packet) -> packet.write(buffer),
                LevelParticlesPacket::new
        );
        LEVEL_PARTICLES_PACKET_MAPPER = mappings.register(
                ClientboundLevelParticlesPacket.class,
                (buf, packet) -> ClientboundLevelParticlesPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buf), packet),
                buf -> ClientboundLevelParticlesPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buf)),
                LEVEL_PARTICLES_PACKET_CODEC
        );

        SERVER_ENTITY_REFERENCE_MAPPER = mappings.register(
                EntityReference.class, ServerEntityReference.class,
                entityReference -> ServerEntityReference.of(entityReference.getUUID()),
                serverEntityReference -> EntityReference.of(serverEntityReference.getUUID())
        );
        SERVER_ENTITY_REFERENCE_CODEC = codecs.register(
                ServerEntityReference.class,
                (buf, serverEntityReference) -> buf.writeUUID(serverEntityReference.getUUID()),
                buf -> ServerEntityReference.of(buf.readUUID())
        );
        OPTIONAL_SERVER_ENTITY_REFERENCE_CODEC = codecs.register(SERVER_ENTITY_REFERENCE_CODEC.optional());

        WOLF_VARIANT_MAPPER = mappings.register(WolfVariant.class, Wolf.Variant.class, Registries.WOLF_VARIANT);
        WOLF_VARIANT_CODEC = codecs.register(WOLF_VARIANT_MAPPER, WolfVariant.STREAM_CODEC);

        WOLF_SOUND_VARIANT_MAPPER = mappings.register(WolfSoundVariant.class, Wolf.SoundVariant.class, Registries.WOLF_SOUND_VARIANT);
        WOLF_SOUND_VARIANT_CODEC = codecs.register(WOLF_SOUND_VARIANT_MAPPER, WolfSoundVariant.STREAM_CODEC);

        FROG_VARIANT_MAPPER = mappings.register(FrogVariant.class, Frog.Variant.class, Registries.FROG_VARIANT);
        FROG_VARIANT_CODEC = codecs.register(FROG_VARIANT_MAPPER, FrogVariant.STREAM_CODEC);

        SNIFFER_STATE_CODEC = codecs.register(org.bukkit.entity.Sniffer.State.class);
        SNIFFER_STATE_MAPPER = mappings.register(Sniffer.State.class, org.bukkit.entity.Sniffer.State.class);

        WEATHERING_COPPER_STATE_CODEC = codecs.register(WeatheringCopperState.class);
        WEATHERING_COPPER_STATE_MAPPER = mappings.register(WeatheringCopper.WeatherState.class, WeatheringCopperState.class);

        QUATERNIONF_CODEC = codecs.register(Quaternionf.class, SimpleByteBuf::writeQuaternion, SimpleByteBuf::readQuaternion);
        QUATERNIONFC_CODEC = codecs.register(Quaternionfc.class, SimpleByteBuf::writeQuaternion, SimpleByteBuf::readQuaternion);

        if (serverVersion.isGreaterOrEqual(Version.of(1,21,3))) {
            RESOLVABLE_PROFILE_MAPPER = mappings.register(
                    ResolvableProfile.class, io.papermc.paper.datacomponent.item.ResolvableProfile.class,
                    PaperResolvableProfile::new,
                    profile -> ((PaperResolvableProfile) profile).getHandle()
            );
            RESOLVABLE_PROFILE_CODEC = codecs.register(RESOLVABLE_PROFILE_MAPPER, ResolvableProfile.STREAM_CODEC);
        } else {
            RESOLVABLE_PROFILE_MAPPER = null;
            RESOLVABLE_PROFILE_CODEC = null;
        }

        ARMADILLO_STATE_MAPPER = mappings.register(net.minecraft.world.entity.animal.armadillo.Armadillo.ArmadilloState.class, Armadillo.State.class);
        ARMADILLO_STATE_CODEC = codecs.register(Armadillo.State.class); // stream codec?

        HUMANOID_ARM_MAPPER = mappings.register(HumanoidArm.class, MainHand.class);
        HUMANOID_ARM_CODEC = codecs.register(MainHand.class);

        CLIENT_BUNDLE_PACKET_MAPPER = mappings.register(
                ClientboundBundlePacket.class, ClientBundlePacket.class,
                packet -> {
                    var bundle = new ClientBundlePacket();
                    for (Packet<? super ClientGamePacketListener> subPacket : packet.subPackets())
                        bundle.add((ClientboundPacket<?>) mappings.map(subPacket));
                    return bundle;
                },
                packet -> new ClientboundBundlePacket(CommonUtils.map(packet.getPackets(), mappings::map))
        );

        MOVE_ENTITY_POSITION_PACKET_CODEC = codecs.register(
                MoveEntityPacket.Position.class,
                (buf, packet) -> packet.write(buf),
                MoveEntityPacket.Position::new
        );

        MOVE_ENTITY_POSITION_PACKET_MAPPER = mappings.register(
                ClientboundMoveEntityPacket.Pos.class,
                ClientboundMoveEntityPacket.Pos.STREAM_CODEC,
                MOVE_ENTITY_POSITION_PACKET_CODEC
        );

        MOVE_ENTITY_ROTATION_PACKET_CODEC = codecs.register(
                MoveEntityPacket.Rotation.class,
                (buf, packet) -> packet.write(buf),
                MoveEntityPacket.Rotation::new
        );

        MOVE_ENTITY_ROTATION_PACKET_MAPPER = mappings.register(
                ClientboundMoveEntityPacket.Rot.class,
                ClientboundMoveEntityPacket.Rot.STREAM_CODEC,
                MOVE_ENTITY_ROTATION_PACKET_CODEC
        );

        MOVE_ENTITY_POSITION_ROTATION_PACKET_CODEC = codecs.register(
                MoveEntityPacket.PositionRotation.class,
                (buf, packet) -> packet.write(buf),
                MoveEntityPacket.PositionRotation::new
        );

        MOVE_ENTITY_POSITION_ROTATION_PACKET_MAPPER = mappings.register(
                ClientboundMoveEntityPacket.PosRot.class,
                ClientboundMoveEntityPacket.PosRot.STREAM_CODEC,
                MOVE_ENTITY_POSITION_ROTATION_PACKET_CODEC
        );

        LOCATION_MAPPER = mappings.register(
                GlobalPos.class, Location.class,
                CraftLocation::fromGlobalPos,
                CraftLocation::toGlobalPos
        );
        LOCATION_CODEC = codecs.register(
                LOCATION_MAPPER,
                (buffer, value) -> GlobalPos.STREAM_CODEC.encode(buffer.unwrap(), value),
                buffer -> GlobalPos.STREAM_CODEC.decode(buffer.unwrap())
        );
        OPTIONAL_LOCATION__CODEC = codecs.register(LOCATION_CODEC.optional());

        if (serverVersion.isGreaterOrEqual(Version.of(1,21,9))) {
            var copperGolemStateMapper = mappings.register(CopperGolemState.class, it.jakegblp.lusk.nms.core.world.entity.coppergolem.CopperGolemState.class);
            codecs.register(
                    copperGolemStateMapper,
                    (buffer, value) -> CopperGolemState.STREAM_CODEC.encode(buffer.unwrap(), value),
                    buffer -> CopperGolemState.STREAM_CODEC.decode(buffer.unwrap())
            );
        }

        var catVariantMapper = mappings.registerRegistryUnsafe(CatVariant.class, Cat.Type.class, Registries.CAT_VARIANT);
        codecs.registerUnsafe(catVariantMapper, CatVariant.STREAM_CODEC);

        var paintingVariantMapper = mappings.registerRegistryUnsafe(PaintingVariant.class, Art.class, Registries.PAINTING_VARIANT);
        codecs.registerUnsafe(paintingVariantMapper, PaintingVariant.STREAM_CODEC);

    }

    private static Map<LevelChunk, List<BlockPos>> getChunkBlockPositions(Location loc1, Location loc2) {
        final Map<LevelChunk, List<BlockPos>> chunkMap = new HashMap<>();

        final int x1 = loc1.getBlockX();
        final int y1 = loc1.getBlockY();
        final int z1 = loc1.getBlockZ();

        final int x2 = loc2.getBlockX();
        final int y2 = loc2.getBlockY();
        final int z2 = loc2.getBlockZ();

        final int minX = Math.min(x1, x2);
        final int minY = Math.min(y1, y2);
        final int minZ = Math.min(z1, z2);

        final int maxX = Math.max(x1, x2);
        final int maxY = Math.max(y1, y2);
        final int maxZ = Math.max(z1, z2);

        final World bw = loc1.getWorld();
        final Level nmsLevel = ((CraftWorld) bw).getHandle();

        final int minChunkX = minX >> 4;
        final int maxChunkX = maxX >> 4;
        final int minChunkZ = minZ >> 4;
        final int maxChunkZ = maxZ >> 4;

        for (int cx = minChunkX; cx <= maxChunkX; cx++) {
            final int wx0 = Math.max(minX, cx << 4);
            final int wx1 = Math.min(maxX, (cx << 4) + 15);

            for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {
                final int wz0 = Math.max(minZ, cz << 4);
                final int wz1 = Math.min(maxZ, (cz << 4) + 15);

                final LevelChunk chunk =
                        nmsLevel.getChunk(cx, cz);

                final int cap = Math.max(16, (wx1 - wx0 + 1) * (maxY - minY + 1) * (wz1 - wz0 + 1));
                final ArrayList<BlockPos> list = new ArrayList<>(cap);
                chunkMap.put(chunk, list);

                for (int x = wx0; x <= wx1; x++) {
                    for (int y = minY; y <= maxY; y++) {
                        for (int z = wz0; z <= wz1; z++) {
                            list.add(new BlockPos(x, y, z));
                        }
                    }
                }
            }
        }
        return chunkMap;
    }

    public <T> StreamCodec<SimpleByteBuf, T> registryCodec(final ResourceKey<? extends net.minecraft.core.Registry<T>> registryKey) {
        return registryCodec(registryKey, (registry) -> registry);
    }

    public <T, R> StreamCodec<SimpleByteBuf, R> registryCodec(final ResourceKey<? extends net.minecraft.core.Registry<T>> registryKey, final Function<net.minecraft.core.Registry<T>, IdMap<R>> idGetter) {
        return new StreamCodec<>() {
            private IdMap<R> getRegistryOrThrow() {
                return idGetter.apply(AllVersions.this.getDedicatedServer().registryAccess().lookupOrThrow(registryKey));
            }

            public @NotNull R decode(@NotNull SimpleByteBuf buffer) {
                return this.getRegistryOrThrow().byIdOrThrow(VarInt.read(buffer.unwrap()));
            }

            public void encode(@NotNull SimpleByteBuf buffer, @NotNull R value) {
                VarInt.write(buffer.unwrap(), this.getRegistryOrThrow().getIdOrThrow(value));
            }
        };
    }

    public RegistryFriendlyByteBuf toRegistryFriendlyByteBuf(@NotNull SimpleByteBuf simpleByteBuf) {
        return new RegistryFriendlyByteBuf(simpleByteBuf.unwrap(), registryAccess());
    }

    private @Nullable Object processParticleOptions(@NotNull ParticleOptions options) {
        return switch (options) {
            case SimpleParticleType ignored -> null; // no extra data

            case BlockParticleOption block -> CraftBlockData.fromData(block.getState());
            case ItemParticleOption item -> CraftItemStack.asBukkitCopy(item.getItem());

            case DustParticleOptions dust -> {
                var c = dust.getColor();
                yield new Particle.DustOptions(
                        Color.fromRGB((int) (c.x() * 255), (int) (c.y() * 255), (int) (c.z() * 255)),
                        dust.getScale()
                );
            }

            case DustColorTransitionOptions dust -> {
                var from = dust.getFromColor();
                var to = dust.getToColor();
                yield new Particle.DustTransition(
                        Color.fromRGB((int) (from.x() * 255), (int) (from.y() * 255), (int) (from.z() * 255)),
                        Color.fromRGB((int) (to.x() * 255), (int) (to.y() * 255), (int) (to.z() * 255)),
                        dust.getScale()
                );
            }

            case ColorParticleOption c -> Color.fromRGB((int) c.getRed(), (int) c.getGreen(), (int) c.getBlue());
            case PowerParticleOption power -> power.getPower();
            case SculkChargeParticleOptions sculk -> sculk.roll();
            case ShriekParticleOption shriek -> shriek.getDelay();

            case TrailParticleOption trail -> new Particle.Trail(
                    new Location(null, trail.target().x, trail.target().y, trail.target().z),
                    Color.fromRGB(trail.color()),
                    trail.duration()
            );

            case VibrationParticleOption vib -> new Vibration(null, vib.getArrivalInTicks());

            default -> throw new IllegalStateException("Unexpected particle options instance: " + options);
        };
    }

    public RegistryFriendlyByteBuf toRegistryFriendlyByteBuf(@NotNull FriendlyByteBuf simpleByteBuf) {
        return new RegistryFriendlyByteBuf(simpleByteBuf.unwrap(), registryAccess());
    }

    public FriendlyByteBuf toFriendlyByteBuf(@NotNull SimpleByteBuf simpleByteBuf) {
        return new FriendlyByteBuf(simpleByteBuf.unwrap());
    }

    @NullMarked
    @SuppressWarnings("rawtypes")
    public void registerEntityDataSerializer(it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer<?> entityDataSerializer, EntityDataSerializer nmsEntityDataSerializer) {
        entityDataSerializers.put(entityDataSerializer, nmsEntityDataSerializer);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public int getNMSSerializerId(EntityDataSerializer entityDataSerializer) {
        return EntityDataSerializers.getSerializedId(entityDataSerializer);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public EntityDataSerializer getNMSSerializer(int id) {
        return EntityDataSerializers.getSerializer(id);
    }

    public boolean isSimpleType(Class<?> clazz) {
        return clazz.isPrimitive() || Number.class.isAssignableFrom(clazz) || clazz == String.class || clazz == Boolean.class || Quaternionfc.class.isAssignableFrom(clazz);
    }

    public boolean isSerializable(Class<?> clazz) {
        return isSimpleType(clazz) || mappings.canConvertForward(clazz);
    }

    @Override
    public Class<?> getSerializableClass(Class<?> clazz) {
        Class<?> a;
        if (FlagByte.class.isAssignableFrom(clazz) || clazz == Spellcaster.Spell.class || clazz == Llama.Color.class || clazz == Rabbit.Type.class || clazz == Panda.Gene.class || clazz == Display.Billboard.class || clazz == org.bukkit.DyeColor.class || clazz == ItemDisplay.ItemDisplayTransform.class)
            a = Byte.class;
        else if (clazz == Display.Brightness.class || clazz == TropicalFish.Pattern.class || clazz == Salmon.Variant.class || clazz == Parrot.Variant.class || clazz == MushroomCow.Variant.class || clazz == Fox.Type.class || clazz == Armadillo.State.class || clazz == Axolotl.Variant.class || clazz == EnderDragon.Phase.class)
            a = Integer.class;
        else if (mappings.canConvertForward(clazz) || Number.class.isAssignableFrom(clazz) || clazz == String.class || clazz == Boolean.class || Quaternionfc.class.isAssignableFrom(clazz)) a = clazz;
        else a =  mappings.getMapperFromForwardClass(clazz).fromClass();
        return a;
    }

    @Override
    public Class<?> getFrontEndClass(Class<?> clazz) {
        if (mappings.canConvertBackward(clazz) || Number.class.isAssignableFrom(clazz) || clazz == String.class || clazz == Boolean.class || Quaternionfc.class.isAssignableFrom(clazz)) return clazz;
        else return mappings.getMapperFromBackwardClass(clazz).toClass();
    }

    @Override
    @Nullable
    public Entity getEntityFromId(int id, World world) {
        final net.minecraft.world.entity.Entity entity = ((CraftWorld) world).getHandle().moonrise$getEntityLookup().get(id);
        return entity == null ? null : entity.getBukkitEntity();
    }

    @Override
    public void setCubeFast(Location corner1, Location corner2, BlockData blockData) {
        if (corner1 == null || corner2 == null || blockData == null) return;
        if (corner1.getWorld() == null || corner2.getWorld() == null) return;
        if (!corner1.getWorld().equals(corner2.getWorld())) return;

        World bukkitWorld = corner1.getWorld();
        Level nmsWorld = ((CraftWorld) bukkitWorld).getHandle();

        BlockState state = ((CraftBlockData) blockData).getState();

        int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
        int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());

        int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
        int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

        int y1 = corner1.getBlockY();
        int y2 = corner2.getBlockY();

        int minY = Math.clamp(Math.min(y1, y2), nmsWorld.getMinY(), nmsWorld.getMaxY());
        int maxY = Math.clamp(Math.max(y1, y2), nmsWorld.getMinY(), nmsWorld.getMaxY());

        if (minY > maxY) return;

        int minChunkX = minX >> 4;
        int maxChunkX = maxX >> 4;
        int minChunkZ = minZ >> 4;
        int maxChunkZ = maxZ >> 4;

        final BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {

                final LevelChunk chunk = nmsWorld.getChunk(chunkX, chunkZ);

                int chunkMinX = chunkX << 4;
                int chunkMinZ = chunkZ << 4;

                for (int y = minY; y <= maxY; y++)
                    for (int x = Math.max(minX, chunkMinX); x <= Math.min(maxX, chunkMinX + 15); x++)
                        for (int z = Math.max(minZ, chunkMinZ); z <= Math.min(maxZ, chunkMinZ + 15); z++)
                            chunk.setBlockState(mutable.set(x, y, z), state, 16);
                bukkitWorld.refreshChunk(chunkX, chunkZ);
            }
        }
    }

    @Override
    public void replaceFast(Location corner1, Location corner2, BlockData oldBlockData, BlockData newBlockData) {
        if (corner1 == null || corner2 == null || oldBlockData == null || newBlockData == null) return;
        if (corner1.getWorld() == null || corner2.getWorld() == null) return;
        if (!corner1.getWorld().equals(corner2.getWorld())) return;

        final Map<LevelChunk, List<BlockPos>> chunkMap = getChunkBlockPositions(corner1, corner2);
        if (chunkMap.isEmpty()) return;

        BlockState fromState = ((CraftBlockData) oldBlockData).getState();
        BlockState toState = ((CraftBlockData) newBlockData).getState();
        World bukkitWorld = corner1.getWorld();

        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (Map.Entry<LevelChunk, List<BlockPos>> entry : chunkMap.entrySet()) {
            LevelChunk chunk = entry.getKey();
            List<BlockPos> positions = entry.getValue();
            if (positions == null || positions.isEmpty()) continue;

            int cx = chunk.locX;
            int cz = chunk.locZ;

            for (final BlockPos p : positions) {
                if (chunk.getBlockState(p) != fromState) continue;

                mutable.set(p.getX(), p.getY(), p.getZ());

                chunk.setBlockState(mutable, toState, 260);
            }
            bukkitWorld.refreshChunk(cx, cz);
        }

    }

    @Override
    public ServerPlayer asServerPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    @Override
    public ServerGamePacketListenerImpl getPlayerConnection(ServerPlayer serverPlayer) {
        return serverPlayer.connection;
    }

    @Override
    @SuppressWarnings("rawtypes")
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
    public void injectPlayer(Player player, JavaPlugin plugin) {
        var serverPlayer = ((CraftPlayer)player).getHandle();
        var pipeline = serverPlayer.connection.connection.channel.pipeline();
        if (pipeline.get("packet_interceptor") != null) return;

        pipeline.addBefore("packet_handler", "packet_interceptor", new ChannelDuplexHandler() {

            private final PluginManager pluginManager = Bukkit.getPluginManager();


            private boolean inactive(ChannelHandlerContext ctx) {
                return !ctx.channel().isActive() || Bukkit.isStopping();
            }


            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                if (inactive(ctx)) return;
                if (!(msg instanceof Packet<?> nmsPacket && nmsPacket.type().flow().equals(PacketFlow.SERVERBOUND))) {
                    super.channelRead(ctx, msg);
                    return;
                }

                if (inactive(ctx)) return;

                if (PacketPreReceiveEvent.getHandlerList().getRegisteredListeners().length > 0) {
                    PacketPreReceiveEvent pre = new PacketPreReceiveEvent(player, true);
                    pluginManager.callEvent(pre);
                    if (pre.isCancelled()) return;
                }

                var newNmsPacket = nmsPacket;
                if (PacketReceiveEvent.getHandlerList().getRegisteredListeners().length > 0) {
                    ServerboundPacket packet = mappings.map(msg);
                    PacketReceiveEvent<ServerboundPacket> event = new PacketReceiveEvent<>(packet.copy(), player, true);
                    pluginManager.callEvent(event);
                    if (event.isCancelled()) return;
                    else if (!packet.equals(event.getPacket())) newNmsPacket = (Packet<?>) event.getPacket().asNMS();
                }

                if (msg instanceof ServerboundPlayerActionPacket) {
                    PlayerActionPacket packet = mappings.map(msg);
                    PlayerActionPacketEvent event = new PlayerActionPacketEvent(packet.copy(), player, true);
                    pluginManager.callEvent(event);
                    if (event.isCancelled()) return;
                    else if (event.isModified()) newNmsPacket = (Packet<?>) event.createPacket().asNMS();
                }

                try {
                    super.channelRead(ctx, newNmsPacket);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                if (inactive(ctx)) {
                    promise.setSuccess();
                    return;
                }
                if (!(msg instanceof Packet<?> nmsPacket && nmsPacket.type().flow().equals(PacketFlow.CLIENTBOUND))) {
                    super.channelRead(ctx, msg);
                    return;
                }
                if (inactive(ctx)) {
                    promise.setSuccess();
                    return;
                }

                if (PacketPreSendEvent.getHandlerList().getRegisteredListeners().length > 0 && msg instanceof ClientboundBundlePacket) {
                    PacketPreSendEvent pre = new PacketPreSendEvent(player, true);
                    pluginManager.callEvent(pre);
                    if (pre.isCancelled()) {
                        promise.setSuccess();
                        return;
                    }
                }

                var newNmsPacket = nmsPacket;
                if (PacketSendEvent.getHandlerList().getRegisteredListeners().length > 0) {
                    ClientboundPacket<?> packet = mappings.map(msg);
                    PacketSendEvent<?> event = new PacketSendEvent<>(packet.copy(), player, true);
                    pluginManager.callEvent(event);
                    if (event.isCancelled()) {
                        promise.setSuccess();
                        return;
                    } else if (!packet.equals(event.getPacket())) newNmsPacket = (Packet<?>) event.getPacket().asNMS();
                }

                for (Mapper<?, ?> mapper : mappings.getMappers()) {
                    if (mapper.fromClass().isInstance(msg)) {
                        if (msg instanceof ClientboundSoundPacket || msg instanceof ClientboundSoundEntityPacket)
                            continue;
                        Mapper<Packet<?>, ClientboundPacket<?>> packetMapper = (Mapper<Packet<?>, ClientboundPacket<?>>) mapper;
                        var packet = packetMapper.to(nmsPacket);
                        PacketWrapperEvent<?> event = packet.createEvent(player, true);
                        pluginManager.callEvent(event);
                        if (event.isCancelled()) {
                            promise.setSuccess();
                            return;
                        } else if (event.isModified()) {
                            newNmsPacket = (Packet<?>) event.createPacket().asNMS();
                            System.out.println(newNmsPacket);
                            break;
                        }

                    }
                }
                    /*
                    int targetId = packet.getId();

                    final Entity entity = LevelUtil.getEntityFromID(targetId, player.getWorld());
                    if (entity != null && entity.isGlowing()) {
                        super.write(ctx, msg, promise);
                        return;
                    }

                    final Set<Integer> list = GlowMap.glowMap.get(player);
                    if (list == null || !list.contains(targetId)) {
                        super.write(ctx, msg, promise);
                        return;
                    }

                    super.write(ctx, rewriteMetadataPacketForGlow(msg), promise);
                    return;
                    */
                //} else if (nms.isSerializableInstanceOf(msg, SoundPacket.class)) {
                //    SoundPacket soundPacket = nms.fromNMS(msg, SoundPacket.class);
                //    SoundAtLocationEvent event = new SoundAtLocationEvent(soundPacket, player, true);
//
                //    pluginManager.callEvent(event);
                //    if (event.isCancelled())
                //        return;
                //} else if (nms.isSerializableInstanceOf(msg, SoundEntityPacket.class)) {
                //    SoundEntityPacket soundEntityPacket = nms.fromNMS(msg, SoundEntityPacket.class);
                //    SoundAtEntityEvent event = new SoundAtEntityEvent(soundEntityPacket, player, true);
//
                //    pluginManager.callEvent(event);
                //    if (event.isCancelled())
                //        return;

                try {
                    super.write(ctx, newNmsPacket, promise);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                if (cause instanceof ClosedChannelException) return;
                cause.printStackTrace();
                super.exceptionCaught(ctx, cause);
            }
        });
    }

    public DedicatedServer getDedicatedServer() {
        return ((CraftServer) Bukkit.getServer()).getServer();
    }

    public RegistryAccess registryAccess() {
        return getDedicatedServer().registryAccess();
    }

    @Override
    public void playEntityEffect(Entity entity, byte data) {
        var handle = ((CraftEntity) entity).getHandle();
        Preconditions.checkState(!handle.generation, "Cannot play effect during world generation");
        try (Level level = handle.level()) {
            level.broadcastEntityEvent(handle, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
