package it.jakegblp.lusk.nms.impl.allversions;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.datafixers.util.Pair;
import io.netty.channel.Channel;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.datacomponent.item.PaperResolvableProfile;
import io.papermc.paper.world.WeatheringCopperState;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.adapters.SharedBehaviorAdapter;
import it.jakegblp.lusk.nms.core.protocol.packets.client.*;
import it.jakegblp.lusk.nms.core.util.*;
import it.jakegblp.lusk.nms.core.world.entity.ServerEntityReference;
import it.jakegblp.lusk.nms.core.world.entity.attribute.AttributeSnapshot;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataItem;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import it.jakegblp.lusk.nms.core.world.entity.serialization.DataHolderType;
import it.jakegblp.lusk.nms.core.world.level.particles.ParticleWrapper;
import it.jakegblp.lusk.nms.core.world.player.ChatSessionData;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.util.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraft.network.protocol.PacketType;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.ChickenVariant;
import net.minecraft.world.entity.animal.CowVariant;
import net.minecraft.world.entity.animal.PigVariant;
import net.minecraft.world.entity.animal.coppergolem.CopperGolemState;
import net.minecraft.world.entity.animal.frog.FrogVariant;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariant;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerData;
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
import org.bukkit.Registry;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.*;
import org.bukkit.craftbukkit.attribute.CraftAttribute;
import org.bukkit.craftbukkit.attribute.CraftAttributeInstance;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.entity.*;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

import static it.jakegblp.lusk.common.StructureTranslation.fromMapToPairList;
import static it.jakegblp.lusk.common.StructureTranslation.fromPairListToMap;
import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;
import static net.minecraft.network.codec.ByteBufCodecs.ROTATION_BYTE;

@Getter
public class AllVersions implements
        SharedBehaviorAdapter<
                EntityDataSerializer,
                ServerPlayer,
                Packet,
                ServerGamePacketListenerImpl,
                Connection,
                DedicatedServer,
                ClientboundBundlePacket
                > {

    @SuppressWarnings("rawtypes")
    public final BiMap<it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer<?>, EntityDataSerializer> entityDataSerializers = HashBiMap.create();

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

    @SuppressWarnings("rawtypes")
    public final Codec<EntityDataSerializer, it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer, ?, ?> ENTITY_DATA_SERIALIZER_CODEC =
            Codec.codec(entityDataSerializers::get, decoded -> entityDataSerializers.inverse().get(decoded));

    public final SimpleBufferCodec<ResourceLocation, NamespacedKey> RESOURCE_LOCATION_256_CODEC = registerCodec(ResourceLocation.class, NamespacedKey.class,
            (buffer, rl) -> buffer.writeString(rl.toString()),
            buffer -> ResourceLocation.parse(buffer.readString(256)),
            (buffer, key) -> buffer.writeString(key.toString()),
            buffer -> Preconditions.checkNotNull(NamespacedKey.fromString(buffer.readString(256)), "Invalid namespaced key codec conversion")
    );

    public final SimpleBufferCodec<ResourceLocation, NamespacedKey> RESOURCE_LOCATION_CODEC = registerCodec(ResourceLocation.class, NamespacedKey.class,
            (buffer, rl) -> buffer.writeString(rl.toString()),
            buffer -> ResourceLocation.parse(buffer.readString(32767)),
            (buffer, key) -> buffer.writeString(key.toString()),
            buffer -> Preconditions.checkNotNull(NamespacedKey.fromString(buffer.readString(32767)), "Invalid namespaced key codec conversion")
    );

    public final SimpleBufferCodec<Pose, org.bukkit.entity.Pose> POSE_CODEC = registerCodec(BufferCodec.leftSided(
            Pose.class, org.bukkit.entity.Pose.class,
            nms -> Pose.CROUCHING.equals(nms) ? org.bukkit.entity.Pose.SNEAKING : org.bukkit.entity.Pose.valueOf(nms.name()),
            bukkit -> org.bukkit.entity.Pose.SNEAKING.equals(bukkit) ? Pose.CROUCHING : Pose.valueOf(bukkit.name()),
            (buffer, value) -> Pose.STREAM_CODEC.encode(buffer.unwrap(), value),
            buffer -> Pose.STREAM_CODEC.decode(buffer.unwrap()))
    );

    public final SimpleBufferCodec<ChatFormatting, NamedTextColor> NAMED_TEXT_COLOR_CODEC = registerCodec(BufferCodec.leftSided(
            ChatFormatting.class, NamedTextColor.class,
            nms -> NamedTextColor.NAMES.value(nms.getName()),
            kyori -> ChatFormatting.getByName(NamedTextColor.NAMES.key(kyori)),
            SimpleByteBuf::writeEnum,
            buffer -> buffer.readEnum(ChatFormatting.class)
    ));

    public final SimpleBufferCodec<DyeColor, org.bukkit.DyeColor> DYE_COLOR_CODEC = registerCodec(BufferCodec.leftSided(
            DyeColor.class, org.bukkit.DyeColor.class,
            nms -> org.bukkit.DyeColor.getByWoolData((byte) nms.getId()),
            bukkit -> DyeColor.byId(bukkit.getWoolData()),
            (buffer, value) -> DyeColor.STREAM_CODEC.encode(buffer.unwrap(), value),
            buffer -> DyeColor.STREAM_CODEC.decode(buffer.unwrap())
    ));
    public final SimpleBufferCodec<Component, net.kyori.adventure.text.Component> COMPONENT_CODEC = registerCodec(BufferCodec.leftSided(
            Component.class, net.kyori.adventure.text.Component.class,
            PaperAdventure::asAdventure,
            PaperAdventure::asVanilla,
            (buffer, value) -> ComponentSerialization.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), value),
            buffer -> ComponentSerialization.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)))
    );

    @SuppressWarnings("rawtypes")
    public final SimpleBufferCodec<SynchedEntityData.DataValue, MetadataItem> METADATA_ITEM_CODEC = registerCodec(
            SynchedEntityData.DataValue.class, MetadataItem.class,
            (buffer, nms) -> nms.write(toRegistryFriendlyByteBuf(buffer)),
            buffer -> SynchedEntityData.DataValue.read(toRegistryFriendlyByteBuf(buffer), buffer.readByte()),
            (buffer, api) -> api.write(buffer),
            MetadataItem::new
    );

    public final SimpleBufferCodec<EquipmentSlot, org.bukkit.inventory.EquipmentSlot> EQUIPMENT_SLOT_CODEC = registerCodec(BufferCodec.leftSided(
            EquipmentSlot.class, org.bukkit.inventory.EquipmentSlot.class,
            CraftEquipmentSlot::getSlot,
            CraftEquipmentSlot::getNMS,
            (buffer, value) -> EquipmentSlot.STREAM_CODEC.encode(buffer.unwrap(), value),
            buffer -> EquipmentSlot.STREAM_CODEC.decode(buffer.unwrap()))
    );

    @SuppressWarnings("rawtypes")
    public final SimpleBufferCodec<EntityType, org.bukkit.entity.EntityType> ENTITY_TYPE_CODEC = registerCodec(BufferCodec.leftSided(
            EntityType.class, org.bukkit.entity.EntityType.class,
            entityType -> Registry.ENTITY_TYPE.getOrThrow(CraftNamespacedKey.fromMinecraft(BuiltInRegistries.ENTITY_TYPE.getKey(entityType))),
            entityType -> BuiltInRegistries.ENTITY_TYPE.getValue(CraftNamespacedKey.toMinecraft(entityType.getKey())),
            (buffer, entityType) -> EntityType.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), entityType),
            buffer -> EntityType.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer))
    ));
    public final SimpleBufferCodec<ProfilePublicKey, it.jakegblp.lusk.nms.core.world.player.ProfilePublicKey> PROFILE_PUBLIC_KEY_CODEC = registerCodec(
            ProfilePublicKey.class, it.jakegblp.lusk.nms.core.world.player.ProfilePublicKey.class,
            (buffer, value) -> value.data().write(toFriendlyByteBuf(buffer)),
            buffer -> new ProfilePublicKey(new ProfilePublicKey.Data(toFriendlyByteBuf(buffer))),
            SimpleByteBuf::writeProfilePublicKey,
            SimpleByteBuf::readProfilePublicKey
    );
    public final SimpleBufferCodec<RemoteChatSession, ChatSessionData> CHAT_SESSION_DATA_CODEC = registerCodec(
            RemoteChatSession.class, ChatSessionData.class,
            (buffer, value) -> RemoteChatSession.Data.write(toFriendlyByteBuf(buffer), value.asData()),
            buffer -> {
                var data = RemoteChatSession.Data.read(toFriendlyByteBuf(buffer));
                return new RemoteChatSession(data.sessionId(), new ProfilePublicKey(data.profilePublicKey())); // potentially hidden side effects
            },
            SimpleByteBuf::writeChatSessionData,
            SimpleByteBuf::readChatSessionData
    );
    public final SimpleBufferCodec<GameProfile, PlayerProfile> PLAYER_PROFILE_CODEC = registerCodec(BufferCodec.leftSided(
            GameProfile.class, PlayerProfile.class,
            CraftPlayerProfile::asBukkitCopy,
            CraftPlayerProfile::asAuthlibCopy,
            (buffer, profile) ->  ByteBufCodecs.GAME_PROFILE.encode(buffer.unwrap(), profile),
            buffer -> ByteBufCodecs.GAME_PROFILE.decode(buffer.unwrap())
    ));
    public final SimpleBufferCodec<BlockState, BlockData> BLOCK_DATA_CODEC = registerCodec(BufferCodec.leftSided(
            BlockState.class, BlockData.class,
            CraftBlockData::fromData,
            bukkit -> ((CraftBlockData) bukkit).getState(),
            (buffer, value) ->  ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY).encode(buffer.unwrap(), value),
            buffer -> ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY).decode(buffer.unwrap())
    ));
    public final SimpleBufferCodec<ItemStack, org.bukkit.inventory.ItemStack>
            ITEMSTACK_CODEC = registerCodec(BufferCodec.leftSided(ItemStack.class, org.bukkit.inventory.ItemStack.class,
            CraftItemStack::asBukkitCopy, CraftItemStack::asNMSCopy,
            (buffer, value) -> ItemStack.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), value),
            buffer -> ItemStack.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)))
    ),
            OPTIONAL_ITEMSTACK_CODEC = registerCodec(BufferCodec.leftSided(ItemStack.class, org.bukkit.inventory.ItemStack.class,
                    CraftItemStack::asBukkitCopy, CraftItemStack::asNMSCopy,
                    (buffer, value) -> ItemStack.OPTIONAL_STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), value),
                    buffer -> ItemStack.OPTIONAL_STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)))
            );
    public final SimpleBufferCodec<GameType, GameMode> GAMEMODE_CODEC = registerCodec(BufferCodec.leftSided(GameType.class, GameMode.class,
            gameType -> GameMode.getByValue(gameType.getId()), gameMode -> GameType.byId(gameMode.getValue()),
            (buffer, value) -> GameType.STREAM_CODEC.encode(buffer.unwrap(), value),
            buffer -> GameType.STREAM_CODEC.decode(buffer.unwrap()))
    );
    public final SimpleBufferCodec<PropertyMap, ProfilePropertySet> PROFILE_PROPERTIES_CODEC = registerCodec(
            PropertyMap.class, ProfilePropertySet.class,
            (buffer, propertyMap) -> ByteBufCodecs.GAME_PROFILE_PROPERTIES.encode(buffer.unwrap(), propertyMap),
            buffer -> ByteBufCodecs.GAME_PROFILE_PROPERTIES.decode(buffer.unwrap()),
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
                for(int i = 0; i < count; ++i) {
                    String string = buffer.readString(64);
                    String string1 = buffer.readString(32767);
                    String string2 = buffer.readNullable((buffer1) -> buffer1.readString(1024));
                    propertySet.add(new ProfileProperty(string, string1, string2));
                }
                return propertySet;
            }
    );

    public final SimpleBufferCodec<ClientboundSetEntityDataPacket, EntityMetadataPacket> ENTITY_METADATA_PACKET = registerCodec(BufferCodec.leftSided(
            ClientboundSetEntityDataPacket.class, EntityMetadataPacket.class,
            packet -> {
                List<SynchedEntityData.DataValue<?>> dataValueList = packet.packedItems();
                EntityMetadata entityMetadata = new EntityMetadata();
                for (SynchedEntityData.DataValue<?> dataValue : dataValueList) {
                    int id = dataValue.id();
                    try {
                        entityMetadata.setInternal(id, new MetadataItem<>(id, dataValue.value(), (it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer<Object>) ENTITY_DATA_SERIALIZER_CODEC.encode(dataValue.serializer())));
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
                return new EntityMetadataPacket(packet.id(), entityMetadata);
            },
            packet -> new ClientboundSetEntityDataPacket(
                    packet.getId(),
                    CommonUtils.map(packet.getEntityMetadata().items(),
                            item -> {
                                Object nmsValue = NMS.toNMSObject(item.value());
                                try {
                                    return new SynchedEntityData.DataValue<>(
                                            item.id(),
                                            ENTITY_DATA_SERIALIZER_CODEC.decode(item.serializer()),
                                            nmsValue);
                                } catch (Throwable e) {
                                    throw new RuntimeException(e);
                                }
                            })
            ),
            (buffer, nms) -> ClientboundSetEntityDataPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), nms),
            (buffer) -> ClientboundSetEntityDataPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer))
    ));

    public final SimpleBufferCodec<ClientboundPlayerInfoUpdatePacket, PlayerInfoUpdatePacket> PLAYER_INFO_UPDATE_PACKET = registerCodec(BufferCodec.of(
            ClientboundPlayerInfoUpdatePacket.class, PlayerInfoUpdatePacket.class,
            (buffer, packet) -> ClientboundPlayerInfoUpdatePacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), packet),
            buffer -> ClientboundPlayerInfoUpdatePacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
            (buffer, packet) -> packet.write(buffer),
            PlayerInfoUpdatePacket::new
    ));

    public final SimpleBufferCodec<ClientboundSetCameraPacket, SetCameraPacket> SET_CAMERA_PACKET = registerCodec(BufferCodec.of(
            ClientboundSetCameraPacket.class, SetCameraPacket.class,
            (buffer, packet) -> ClientboundSetCameraPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), packet),
            buffer -> ClientboundSetCameraPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
            (buffer, packet) -> packet.write(buffer),
            SetCameraPacket::new
    ));

    public final SimpleBufferCodec<ClientboundSystemChatPacket, SystemChatPacket> SYSTEM_CHAT_PACKET = registerCodec(BufferCodec.of(
            ClientboundSystemChatPacket.class, SystemChatPacket.class,
            (buffer, packet) -> ClientboundSystemChatPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), packet),
            buffer -> ClientboundSystemChatPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
            (buffer, packet) -> packet.write(buffer),
            SystemChatPacket::new
    ));

    public final SimpleBufferCodec<ClientboundEntityEventPacket, EntityEventPacket> ENTITY_EVENT_PACKET_CODEC = registerCodec(BufferCodec.of(
            ClientboundEntityEventPacket.class, EntityEventPacket.class,
            (buffer, packet) -> ClientboundEntityEventPacket.STREAM_CODEC.encode(toFriendlyByteBuf(buffer), packet),
            buffer -> ClientboundEntityEventPacket.STREAM_CODEC.decode(toFriendlyByteBuf(buffer)),
            (buffer, packet) -> packet.write(buffer),
            EntityEventPacket::new
    ));

    public final SimpleBufferCodec<ClientboundSetEquipmentPacket, SetEquipmentPacket> SET_EQUIPMENT_PACKET = registerCodec(BufferCodec.leftSided(
            ClientboundSetEquipmentPacket.class, SetEquipmentPacket.class,
            clientboundSetEquipmentPacket -> new SetEquipmentPacket(clientboundSetEquipmentPacket.getEntity(), fromPairListToMap(clientboundSetEquipmentPacket.getSlots(), EQUIPMENT_SLOT_CODEC::encode, ITEMSTACK_CODEC::encode)),
            setEquipmentPacket -> {
                List<Pair<EquipmentSlot, ItemStack>> list = fromMapToPairList(setEquipmentPacket.getEquipment(), EQUIPMENT_SLOT_CODEC::decode, ITEMSTACK_CODEC::decode);
                return new ClientboundSetEquipmentPacket(setEquipmentPacket.getId(), list);
            },
            (buffer, packet) -> ClientboundSetEquipmentPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), packet),
            buffer -> ClientboundSetEquipmentPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer))
    ));

    public final IdentityBufferCodec<Integer> UNSIGNED_BYTE_CODEC = registerCodec(BufferCodec.identity(Integer.class, SimpleByteBuf::writeUnsignedByte, SimpleByteBuf::readUnsignedByte));

    public final IdentityBufferCodec<Boolean> BOOLEAN_CODEC = registerCodec(BufferCodec.identity(Boolean.class, SimpleByteBuf::writeBoolean, SimpleByteBuf::readBoolean));
    public final IdentityBufferCodec<String> STRING_CODEC = registerCodec(BufferCodec.identity(String.class, SimpleByteBuf::writeString, SimpleByteBuf::readString));
    public final IdentityBufferCodec<Byte> BYTE_CODEC = registerCodec(BufferCodec.identity(Byte.class, (buffer, value) -> buffer.writeByte(value), SimpleByteBuf::readByte));
    public final IdentityBufferCodec<Integer> VAR_INT_CODEC = registerCodec(BufferCodec.identity(Integer.class, SimpleByteBuf::writeVarInt, SimpleByteBuf::readVarInt));
    public final IdentityBufferCodec<Long> LONG_CODEC = registerCodec(BufferCodec.identity(Long.class, SimpleByteBuf::writeLong, SimpleByteBuf::readLong));
    public final IdentityBufferCodec<Double> DOUBLE_CODEC = registerCodec(BufferCodec.identity(Double.class, SimpleByteBuf::writeDouble, SimpleByteBuf::readDouble));
    public final IdentityBufferCodec<UUID> UUID_CODEC = registerCodec(BufferCodec.identity(UUID.class, SimpleByteBuf::writeUUID, SimpleByteBuf::readUUID));

    //public final MultiBufferCodec VECTOR_MULTI_CODEC = registerCodec(MultiBufferCodec.builder()
    //                .add(Vec3.class, (buffer, vector) -> FriendlyByteBuf.writeVec3(buffer.unwrap(), vector), buffer -> FriendlyByteBuf.readVec3(buffer.unwrap()))
    //                .add(Vector.class, SimpleByteBuf::writeVector, SimpleByteBuf::readVector)
    //                .add(Vector3f.class, (buffer, vector) -> VECTOR3F.encode(buffer.unwrap(), vector), buffer -> VECTOR3F.decode(buffer.unwrap()))
    //                .build());
    public final SimpleBufferCodec<Vector3f, Vector> VECTOR3F_CODEC = registerCodec(Vector3f.class, Vector.class,
            (buffer, vector) -> FriendlyByteBuf.writeVector3f(buffer.unwrap(), vector),
            buffer -> FriendlyByteBuf.readVector3f(buffer.unwrap()),
            SimpleByteBuf::writeVector,
            SimpleByteBuf::readVector);
    public final SimpleBufferCodec<Rotations, io.papermc.paper.math.Rotations> ROTATIONS_CODEC = registerCodec(Rotations.class, io.papermc.paper.math.Rotations.class,
            (buffer, rotations) -> Rotations.STREAM_CODEC.encode(buffer.unwrap(), rotations),
            buffer -> Rotations.STREAM_CODEC.decode(buffer.unwrap()),
            (buffer, value) -> {
                buffer.writeFloat((float) value.x());
                buffer.writeFloat((float) value.y());
                buffer.writeFloat((float) value.z());
            },
            buffer -> io.papermc.paper.math.Rotations.ofDegrees(buffer.readFloat(), buffer.readFloat(), buffer.readFloat()));

    public final SimpleBufferCodec<Vec3, Vector> VEC3_CODEC = registerCodec(Vec3.class, Vector.class,
            (buffer, vector) -> FriendlyByteBuf.writeVec3(buffer.unwrap(), vector),
            buffer -> FriendlyByteBuf.readVec3(buffer.unwrap()),
            SimpleByteBuf::writeVector,
            SimpleByteBuf::readVector);

    public final SimpleBufferCodec<Direction, BlockFace> BLOCK_FACE_CODEC = registerCodec(BufferCodec.leftSided(Direction.class, BlockFace.class,
            value -> BlockFace.valueOf(value.name()),
            value -> Direction.valueOf(value.name()),
            (buffer, direction) -> Direction.STREAM_CODEC.encode(buffer.unwrap(), direction),
            buffer -> Direction.STREAM_CODEC.decode(buffer.unwrap())));

    public final SimpleBufferCodec<BlockPos, BlockVector> BLOCK_VECTOR_CODEC = registerCodec(BlockPos.class, BlockVector.class,
            (buffer, vector) -> FriendlyByteBuf.writeBlockPos(buffer.unwrap(), vector),
            buffer -> FriendlyByteBuf.readBlockPos(buffer.unwrap()),
            SimpleByteBuf::writeBlockVector,
            SimpleByteBuf::readBlockVector);

    public final IdentityBufferCodec<Float> DEGREES_CODEC = registerCodec(BufferCodec.identity(Float.class, (buffer, value) -> ROTATION_BYTE.encode(buffer.unwrap(), value), buffer -> ROTATION_BYTE.decode(buffer.unwrap())));
    public final IdentityBufferCodec<IntList> INT_LIST_CODEC = registerCodec(BufferCodec.identity(IntList.class, SimpleByteBuf::writeIntList, SimpleByteBuf::readIntList));

    public final SimpleBufferCodec<ClientboundAddEntityPacket, AddEntityPacket> ADD_ENTITY_PACKET_CODEC = registerCodec(
            ClientboundAddEntityPacket.class, AddEntityPacket.class,
            (buffer, packet) -> ClientboundAddEntityPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), packet),
            buffer -> ClientboundAddEntityPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
            (buffer, packet) -> packet.write(buffer),
            AddEntityPacket::new
    );
    public final SimpleBufferCodec<ClientboundSetPlayerTeamPacket, TeamPacket> TEAM_PACKET_CODEC = registerCodec(
            ClientboundSetPlayerTeamPacket.class, TeamPacket.class,
            (buffer, packet) -> ClientboundSetPlayerTeamPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), packet),
            buffer -> ClientboundSetPlayerTeamPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
            (buffer, packet) -> packet.write(buffer),
            TeamPacket::new
    );
    public final SimpleBufferCodec<ClientboundBlockUpdatePacket, BlockUpdatePacket> BLOCK_UPDATE_PACKET = registerCodec(
            ClientboundBlockUpdatePacket.class, BlockUpdatePacket.class,
            (buffer, packet) -> ClientboundBlockUpdatePacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), packet),
            buffer -> ClientboundBlockUpdatePacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
            (buffer, packet) -> packet.write(buffer),
            BlockUpdatePacket::new
    );
    public final SimpleBufferCodec<ClientboundBlockDestructionPacket, BlockDestructionPacket> BLOCK_DESTRUCTION_PACKET_CODEC = registerCodec(
            ClientboundBlockDestructionPacket.class, BlockDestructionPacket.class,
            (buffer, value) -> ClientboundBlockDestructionPacket.STREAM_CODEC.encode(toFriendlyByteBuf(buffer), value),
            buffer -> ClientboundBlockDestructionPacket.STREAM_CODEC.decode(toFriendlyByteBuf(buffer)),
            (buffer, value) -> value.write(buffer),
            BlockDestructionPacket::new
    );
    public final SimpleBufferCodec<ClientboundAnimatePacket, EntityAnimationPacket> ENTITY_ANIMATION_PACKET_CODEC = registerCodec(
            ClientboundAnimatePacket.class, EntityAnimationPacket.class,
            (buffer, value) -> ClientboundAnimatePacket.STREAM_CODEC.encode(toFriendlyByteBuf(buffer), value),
            buffer -> ClientboundAnimatePacket.STREAM_CODEC.decode(toFriendlyByteBuf(buffer)),
            (buffer, value) -> value.write(buffer),
            EntityAnimationPacket::new
    );
    public final SimpleBufferCodec<ClientboundRemoveEntitiesPacket, RemoveEntitiesPacket> REMOVE_ENTITIES_PACKET_CODEC = registerCodec(
            ClientboundRemoveEntitiesPacket.class, RemoveEntitiesPacket.class,
            (buffer, value) -> ClientboundRemoveEntitiesPacket.STREAM_CODEC.encode(toFriendlyByteBuf(buffer), value),
            buffer -> ClientboundRemoveEntitiesPacket.STREAM_CODEC.decode(toFriendlyByteBuf(buffer)),
            (buffer, value) -> value.write(buffer),
            RemoveEntitiesPacket::new
    );
    public final SimpleBufferCodec<ClientboundPlayerPositionPacket, PlayerPositionPacket> PLAYER_POSITION_PACKET_CODEC = registerCodec(BufferCodec.of(
            ClientboundPlayerPositionPacket.class, PlayerPositionPacket.class,
            (buffer, packet) -> ClientboundPlayerPositionPacket.STREAM_CODEC.encode(new FriendlyByteBuf(buffer.unwrap()), packet),
            buffer -> ClientboundPlayerPositionPacket.STREAM_CODEC.decode(new FriendlyByteBuf(buffer.unwrap())),
            (buffer, packet) -> packet.write(buffer),
            PlayerPositionPacket::new
    ));
    public final SimpleBufferCodec<SoundEvent, Sound> SOUND_CODEC = registerCodec(BufferCodec.leftSided(
            SoundEvent.class, Sound.class,
            CraftSound::minecraftToBukkit,
            CraftSound::bukkitToMinecraft,
            (buffer, value) -> SoundEvent.DIRECT_STREAM_CODEC.encode(buffer.unwrap(), value),
            buffer -> SoundEvent.DIRECT_STREAM_CODEC.decode(buffer.unwrap())
    ));
    public final SimpleBufferCodec<SoundSource, SoundCategory> SOUND_CATEGORY_CODEC = registerCodec(BufferCodec.ofEnum(
            SoundSource.class, SoundCategory.class
    ));
    public final SimpleBufferCodec<AttributeModifier, org.bukkit.attribute.AttributeModifier> ATTRIBUTE_MODIFIER_CODEC = registerCodec(BufferCodec.leftSided(
            AttributeModifier.class, org.bukkit.attribute.AttributeModifier.class,
            CraftAttributeInstance::convert,
            CraftAttributeInstance::convert,
            (buffer, value) -> AttributeModifier.STREAM_CODEC.encode(buffer.unwrap(), value),
            buffer -> AttributeModifier.STREAM_CODEC.decode(buffer.unwrap())
    ));
    public final SimpleBufferCodec<ClientboundUpdateAttributesPacket.AttributeSnapshot, AttributeSnapshot> ATTRIBUTE_SNAPSHOT_CODEC = registerCodec(
            ClientboundUpdateAttributesPacket.AttributeSnapshot.class, AttributeSnapshot.class,
            (buffer, value) -> ClientboundUpdateAttributesPacket.AttributeSnapshot.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), value),
            buffer -> ClientboundUpdateAttributesPacket.AttributeSnapshot.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
            SimpleByteBuf::writeAttributeSnapshot,
            AttributeSnapshot::new
    );
    private final StreamCodec<RegistryFriendlyByteBuf, Attribute> ATTRIBUTE_NMS_STREAM_CODEC = ByteBufCodecs.registry(Registries.ATTRIBUTE);
    public final SimpleBufferCodec<Attribute, org.bukkit.attribute.Attribute> ATTRIBUTE_CODEC = registerCodec(BufferCodec.leftSided(
            Attribute.class, org.bukkit.attribute.Attribute.class,
            CraftAttribute::minecraftToBukkit,
            CraftAttribute::bukkitToMinecraft,
            (buffer, value) -> ATTRIBUTE_NMS_STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), value),
            buffer -> ATTRIBUTE_NMS_STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer))
    ));
    public final SimpleBufferCodec<ClientboundUpdateAttributesPacket, UpdateAttributesPacket> UPDATE_ATTRIBUTES_PACKET_CODEC = registerCodec(
            ClientboundUpdateAttributesPacket.class, UpdateAttributesPacket.class,
            (buffer, value) -> ClientboundUpdateAttributesPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), value),
            buffer -> ClientboundUpdateAttributesPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
            (buffer, value) -> value.write(buffer),
            UpdateAttributesPacket::new
    );
    public final SimpleBufferCodec<PacketFlow, it.jakegblp.lusk.nms.core.protocol.PacketFlow> PACKET_FLOW_CODEC = registerCodec(BufferCodec.ofEnum(
            PacketFlow.class, it.jakegblp.lusk.nms.core.protocol.PacketFlow.class)
    );
    public final SimpleBufferCodec<PacketType, it.jakegblp.lusk.nms.core.protocol.PacketType> PACKET_TYPE_CODEC = registerCodec(
            PacketType.class, it.jakegblp.lusk.nms.core.protocol.PacketType.class,
            (buffer, value) -> {
                PACKET_FLOW_CODEC.writeFrom(value.flow(), buffer);
                RESOURCE_LOCATION_256_CODEC.writeFrom(value.id(), buffer);
            },
            buffer -> new PacketType<>(PACKET_FLOW_CODEC.readFrom(buffer), RESOURCE_LOCATION_256_CODEC.readFrom(buffer)),
            (buffer, value) -> {
                PACKET_FLOW_CODEC.writeTo(value.packetFlow(), buffer);
                RESOURCE_LOCATION_256_CODEC.writeTo(value.key(), buffer);
            },
            buffer -> new it.jakegblp.lusk.nms.core.protocol.PacketType(PACKET_FLOW_CODEC.readTo(buffer), RESOURCE_LOCATION_256_CODEC.readTo(buffer))
    );
    public final SimpleBufferCodec<ClientboundSoundPacket, SoundPacket> SOUND_PACKET_CODEC = registerCodec(
            ClientboundSoundPacket.class, SoundPacket.class,
            (buffer, value) -> ClientboundSoundPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), value),
            buffer -> ClientboundSoundPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
            (buffer, value) -> value.write(buffer),
            SoundPacket::new
    );
    public final SimpleBufferCodec<ClientboundSoundEntityPacket, SoundEntityPacket> SOUND_ENTITY_PACKET_CODEC = registerCodec(
            ClientboundSoundEntityPacket.class, SoundEntityPacket.class,
            (buffer, value) -> ClientboundSoundEntityPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), value),
            buffer -> ClientboundSoundEntityPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
            (buffer, value) -> value.write(buffer),
            SoundEntityPacket::new
    );
    public final SimpleBufferCodec<ParticleOptions, ParticleWrapper> PARTICLE_CODEC =
            registerCodec(BufferCodec.leftSided(
                    ParticleOptions.class,
                    ParticleWrapper.class,
                    options -> new ParticleWrapper(CraftParticle.minecraftToBukkit(options.getType()), processParticleOptions(options)),
                    wrapper -> CraftParticle.createParticleParam(wrapper.particle(), wrapper.data()),
                    (buffer, options) -> ParticleTypes.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), options),
                    buffer -> ParticleTypes.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer))
            ));
    public final SimpleBufferCodec<ClientboundLevelParticlesPacket, LevelParticlesPacket> LEVEL_PARTICLES_PACKET_CODEC = registerCodec(
            ClientboundLevelParticlesPacket.class, LevelParticlesPacket.class,
            (buffer, value) -> ClientboundLevelParticlesPacket.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), value),
            buffer -> ClientboundLevelParticlesPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
            (buffer, value) -> value.write(buffer),
            LevelParticlesPacket::new
    );
    public final SimpleBufferCodec<EntityReference, ServerEntityReference> SERVER_ENTITY_REFERENCE_CODEC = registerCodec(
            EntityReference.class, ServerEntityReference.class,
            (buffer, value) -> EntityReference.streamCodec().encode(toRegistryFriendlyByteBuf(buffer), value),
            buffer -> EntityReference.streamCodec().decode(toRegistryFriendlyByteBuf(buffer)),
            (buffer, value) -> buffer.writeUUID(value.getUUID()),
            buffer -> ServerEntityReference.of(buffer.readUUID())
    );
    public final SimpleBufferCodec<GlobalPos, Location> LOCATION_CODEC = registerCodec(BufferCodec.leftSided(
            GlobalPos.class, Location.class,
            globalPos -> BLOCK_VECTOR_CODEC.encode(globalPos.pos()).toLocation(Bukkit.getWorld(RESOURCE_LOCATION_CODEC.encode(globalPos.dimension().location()))),
            location -> new GlobalPos(ResourceKey.create(Registries.DIMENSION, RESOURCE_LOCATION_CODEC.decode(location.getWorld().getKey())), BLOCK_VECTOR_CODEC.decode(location.toVector().toBlockVector())),
            (buffer, value) -> GlobalPos.STREAM_CODEC.encode(buffer.unwrap(), value),
            buffer -> GlobalPos.STREAM_CODEC.decode(buffer.unwrap())
    ));
    public final SimpleBufferCodec<VillagerData, it.jakegblp.lusk.nms.core.world.entity.villager.VillagerData> VILLAGER_DATA_CODEC = registerCodec(BufferCodec.leftSided(
            VillagerData.class, it.jakegblp.lusk.nms.core.world.entity.villager.VillagerData.class,
            villagerData -> new it.jakegblp.lusk.nms.core.world.entity.villager.VillagerData(
                    Registry.VILLAGER_TYPE.getOrThrow(RESOURCE_LOCATION_CODEC.encode(villagerData.type().unwrapKey().orElseThrow().location())),
                    Registry.VILLAGER_PROFESSION.getOrThrow(RESOURCE_LOCATION_CODEC.encode(villagerData.profession().unwrapKey().orElseThrow().location())),
                    villagerData.level()
            ), villagerData -> new VillagerData(
                    registryAccess().lookupOrThrow(Registries.VILLAGER_TYPE).getOrThrow(ResourceKey.create(
                            Registries.VILLAGER_TYPE, RESOURCE_LOCATION_CODEC.decode(villagerData.type().getKey()))),
                    registryAccess().lookupOrThrow(Registries.VILLAGER_PROFESSION).getOrThrow(ResourceKey.create(
                            Registries.VILLAGER_PROFESSION, RESOURCE_LOCATION_CODEC.decode(villagerData.profession().getKey()))),
                    villagerData.level()
            ), (buffer, value) -> VillagerData.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), value),
            buffer -> VillagerData.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer))
    ));
    public final SimpleBufferCodec<CatVariant, Cat.Type> CAT_VARIANT_CODEC = registerCodec(leftSidedHolderCodec(
            CatVariant.class, Cat.Type.class,
            (buffer, value) -> CatVariant.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), value),
            buffer -> CatVariant.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
            CraftCat.CraftType::minecraftToBukkit,
            CraftCat.CraftType::bukkitToMinecraft,
            Registries.CAT_VARIANT
    ));
    // todo: reflection or module for pre animal variant versions
    public final SimpleBufferCodec<ChickenVariant, Chicken.Variant> CHICKEN_VARIANT_CODEC = registerCodec(leftSidedHolderCodec(
            ChickenVariant.class, Chicken.Variant.class,
            (buffer, value) -> ChickenVariant.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), value),
            buffer -> ChickenVariant.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
            CraftChicken.CraftVariant::minecraftToBukkit,
            CraftChicken.CraftVariant::bukkitToMinecraft,
            Registries.CHICKEN_VARIANT
    ));
    public final SimpleBufferCodec<CowVariant, Cow.Variant> COW_VARIANT_CODEC = registerCodec(leftSidedHolderCodec(
            CowVariant.class, Cow.Variant.class,
            (buffer, value) -> CowVariant.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), value),
            buffer -> CowVariant.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
            CraftCow.CraftVariant::minecraftToBukkit,
            CraftCow.CraftVariant::bukkitToMinecraft,
            Registries.COW_VARIANT
    ));
    public final SimpleBufferCodec<WolfVariant, Wolf.Variant> WOLF_VARIANT_CODEC = registerCodec(leftSidedHolderCodec(
            WolfVariant.class, Wolf.Variant.class,
            (buffer, value) -> WolfVariant.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), value),
            buffer -> WolfVariant.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
            CraftWolf.CraftVariant::minecraftToBukkit,
            CraftWolf.CraftVariant::bukkitToMinecraft,
            Registries.WOLF_VARIANT
    ));
    public final SimpleBufferCodec<WolfSoundVariant, Wolf.SoundVariant> WOLF_SOUND_VARIANT_CODEC = registerCodec(leftSidedHolderCodec(
            WolfSoundVariant.class, Wolf.SoundVariant.class,
            (buffer, value) -> WolfSoundVariant.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), value),
            buffer -> WolfSoundVariant.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
            CraftWolf.CraftSoundVariant::minecraftToBukkit,
            CraftWolf.CraftSoundVariant::bukkitToMinecraft,
            Registries.WOLF_SOUND_VARIANT
    ));
    public final SimpleBufferCodec<FrogVariant, Frog.Variant> FROG_VARIANT_CODEC = registerCodec(leftSidedHolderCodec(
            FrogVariant.class, Frog.Variant.class,
            (buffer, value) -> FrogVariant.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), value),
            buffer -> FrogVariant.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
            CraftFrog.CraftVariant::minecraftToBukkit,
            CraftFrog.CraftVariant::bukkitToMinecraft,
            Registries.FROG_VARIANT
    ));
    public final SimpleBufferCodec<PigVariant, Pig.Variant> PIG_VARIANT_CODEC = registerCodec(leftSidedHolderCodec(
            PigVariant.class, Pig.Variant.class,
            (buffer, value) -> PigVariant.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), value),
            buffer -> PigVariant.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
            CraftPig.CraftVariant::minecraftToBukkit,
            CraftPig.CraftVariant::bukkitToMinecraft,
            Registries.PIG_VARIANT
    ));
    public final SimpleBufferCodec<PaintingVariant, Art> PAINTING_VARIANT_CODEC = registerCodec(leftSidedHolderCodec(
            PaintingVariant.class, Art.class,
            (buffer, value) -> PaintingVariant.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), value),
            buffer -> PaintingVariant.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)),
            CraftArt::minecraftToBukkit,
            CraftArt::bukkitToMinecraft,
            Registries.PAINTING_VARIANT
    ));
    public final SimpleBufferCodec<Sniffer.State, org.bukkit.entity.Sniffer.State> SNIFFER_STATE_CODEC = registerCodec(BufferCodec.leftSided(
            Sniffer.State.class, org.bukkit.entity.Sniffer.State.class,
            state -> org.bukkit.entity.Sniffer.State.values()[state.id()],
            state -> Sniffer.State.BY_ID.apply(state.ordinal()),
            (buffer, value) -> Sniffer.State.STREAM_CODEC.encode(buffer.unwrap(), value),
            buffer -> Sniffer.State.STREAM_CODEC.decode(buffer.unwrap())
    ));
    public final SimpleBufferCodec<WeatheringCopper.WeatherState, WeatheringCopperState> WEATHERING_COPPER_STATE_CODEC = registerCodec(BufferCodec.leftSided(
            WeatheringCopper.WeatherState.class, WeatheringCopperState.class,
            state -> WeatheringCopperState.values()[state.ordinal()],
            state -> WeatheringCopper.WeatherState.BY_ID.apply(state.ordinal()),
            (buffer, value) -> WeatheringCopper.WeatherState.STREAM_CODEC.encode(buffer.unwrap(), value),
            buffer -> WeatheringCopper.WeatherState.STREAM_CODEC.decode(buffer.unwrap())
    ));
    public final SimpleBufferCodec<CopperGolemState, it.jakegblp.lusk.nms.core.world.entity.coppergolem.CopperGolemState> COPPER_GOLEM_STATE_CODEC = registerCodec(BufferCodec.leftSided(
            CopperGolemState.class, it.jakegblp.lusk.nms.core.world.entity.coppergolem.CopperGolemState.class,
            state -> it.jakegblp.lusk.nms.core.world.entity.coppergolem.CopperGolemState.values()[state.ordinal()],
            state -> CopperGolemState.values()[state.ordinal()],
            (buffer, value) -> CopperGolemState.STREAM_CODEC.encode(buffer.unwrap(), value),
            buffer -> CopperGolemState.STREAM_CODEC.decode(buffer.unwrap())
    ));
    public final IdentityBufferCodec<Quaternionf> QUATERNIONF_CODEC = registerCodec(BufferCodec.identity(Quaternionf.class, SimpleByteBuf::writeQuaternionf, SimpleByteBuf::readQuaternionf));
    public final SimpleBufferCodec<ResolvableProfile, io.papermc.paper.datacomponent.item.ResolvableProfile> RESOLVABLE_PROFILE_CODEC = registerCodec(BufferCodec.leftSided(
            ResolvableProfile.class, io.papermc.paper.datacomponent.item.ResolvableProfile.class,
            PaperResolvableProfile::new,
            profile -> ((PaperResolvableProfile)profile).getHandle(),
            (buffer, value) -> ResolvableProfile.STREAM_CODEC.encode(buffer.unwrap(), value),
            buffer -> ResolvableProfile.STREAM_CODEC.decode(buffer.unwrap())
    ));

    public <F> Holder<F> wrapAsHolder(ResourceKey<net.minecraft.core.Registry<F>> resourceKey, F f) {
        return registryAccess().lookupOrThrow(resourceKey).wrapAsHolder(f);
    }

    @NullMarked
    public <F, T> IntermediarySimpleBufferCodec<F, Holder<F>, T> leftSidedHolderCodec(
            Class<F> fromClass,
            Class<T> toClass,
            BufferCodec.Writer<Holder<F>> writeFrom,
            BufferCodec.Reader<Holder<F>> readFrom,
            Function<F, T> toFrom,
            Function<T, F> fromFrom,
            ResourceKey<net.minecraft.core.Registry<F>> registryResourceKey
    ) {
        Function<F, Holder<F>> toHolder = f -> wrapAsHolder(registryResourceKey, f);
        return holderCodec(
                fromClass,
                toClass,
                writeFrom,
                readFrom,
                (buffer, to) -> writeFrom.write(buffer, toHolder.apply(fromFrom.apply(to))),
                buffer -> toFrom.apply(readFrom.read(buffer).value()),
                toHolder
        );
    }

    @NullMarked
    public <F, T> IntermediarySimpleBufferCodec<F, Holder<F>, T> holderCodec(
            Class<F> fromClass,
            Class<T> toClass,
            BufferCodec.Writer<Holder<F>> writeFrom,
            BufferCodec.Reader<Holder<F>> readFrom,
            BufferCodec.Writer<T> writeTo,
            BufferCodec.Reader<T> readTo,
            Function<F, Holder<F>> toHolder
    ) {
        return new IntermediarySimpleBufferCodec<>() {
            @Override
            public Class<F> getFromClass() {
                return fromClass;
            }

            @Override
            public Class<T> getToClass() {
                return toClass;
            }

            @Override
            public void writeFrom(F f, SimpleByteBuf buffer) throws IllegalArgumentException {
                writeFrom.write(buffer, toHolder.apply(f));
            }

            @Override
            public F readFrom(SimpleByteBuf buffer) throws IllegalArgumentException {
                return readFrom.read(buffer).value();
            }

            @Override
            public void writeTo(T t, SimpleByteBuf buffer) throws IllegalArgumentException {
                writeTo.write(buffer, t);
            }

            @Override
            public T readTo(SimpleByteBuf buffer) throws IllegalArgumentException {
                return readTo.read(buffer);
            }

            @Override
            public DataHolderType getHolderType() {
                return DataHolderType.HOLDER;
            }

            @Override
            public Holder<F> wrap(F f) {
                return toHolder.apply(f);
            }

            @Override
            public F unwrap(Holder<F> fHolder) {
                return fHolder.value();
            }
        };
    }


    public RegistryFriendlyByteBuf toRegistryFriendlyByteBuf(@NotNull SimpleByteBuf simpleByteBuf) {
        return new RegistryFriendlyByteBuf(simpleByteBuf.unwrap(), registryAccess());
    }

    private @Nullable Object processParticleOptions(@NotNull ParticleOptions options) {
        return switch (options) {
            case BlockParticleOption block -> CraftBlockData.fromData(block.getState());
            case ItemParticleOption item -> CraftItemStack.asBukkitCopy(item.getItem());
            case DustParticleOptions dust -> {
                var c = dust.getColor();
                yield new Particle.DustOptions(Color.fromRGB((int) (c.x() * 255), (int) (c.y() * 255), (int) (c.z() * 255)), dust.getScale());
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
            case TrailParticleOption trail ->
                    new Particle.Trail(new Location(null, trail.target().x, trail.target().y, trail.target().z), Color.fromRGB(trail.color()), trail.duration());
            case VibrationParticleOption vib -> // lossy
                    new Vibration(null, vib.getArrivalInTicks());
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

    @SneakyThrows
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <F, T> SimpleBufferCodec<F, T> getNMSSerializerCodec(EntityDataSerializer entityDataSerializer) {
        return ENTITY_DATA_SERIALIZER_CODEC.encode(entityDataSerializer).codec();
    }

    @Override
    public Class<?> getSerializableClass(Class<?> clazz) {
        if (FlagByte.class.isAssignableFrom(clazz) || clazz == Spellcaster.Spell.class || clazz == Llama.Color.class || clazz == Rabbit.Type.class || clazz == Panda.Gene.class || clazz == Display.Billboard.class || clazz == org.bukkit.DyeColor.class || clazz == ItemDisplay.ItemDisplayTransform.class) return Byte.class;
        else if (clazz == Display.Brightness.class || clazz == TropicalFish.Pattern.class || clazz == Salmon.Variant.class || clazz == Parrot.Variant.class || clazz == MushroomCow.Variant.class || clazz == Fox.Type.class || clazz == Armadillo.State.class || clazz == Axolotl.Variant.class || clazz == EnderDragon.Phase.class) return Integer.class;
        else if (isCodecFromClass(clazz)) return clazz;
        else return getFirstCodec(clazz).getFromClass();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class<Packet> getNMSPacketClass() {
        return Packet.class;
    }

    @Override
    public Player asPlayer(@NotNull ServerPlayer serverPlayer) {
        return serverPlayer.getBukkitEntity();
    }

    @Override
    @Nullable
    public Entity getEntityFromId(int id, World world) {
        final net.minecraft.world.entity.Entity entity = ((CraftWorld) world).getHandle().moonrise$getEntityLookup().get(id);
        return entity == null ? null : entity.getBukkitEntity();
    }

    @Override
    public Object rewriteMetadataPacketForGlow(Object metadataPacket) {
        ClientboundSetEntityDataPacket packet = (ClientboundSetEntityDataPacket) metadataPacket;

        final ArrayList<SynchedEntityData.DataValue<?>> dataValues = new ArrayList<>(packet.packedItems());
        if (dataValues.stream()
                .map(SynchedEntityData.DataValue::value)
                .filter(Byte.class::isInstance)
                .map(Byte.class::cast)
                .noneMatch(aByte -> aByte == (byte) 0x40))

            dataValues.add(new SynchedEntityData.DataValue<>(
                    0, EntityDataSerializers.BYTE, (byte) 0x40
            ));

        return new ClientboundSetEntityDataPacket(packet.id(), dataValues);
    }

    @Override
    public void setPlayerSpinAttack(Player player, int ticks) {
        ((CraftPlayer) player).getHandle().startAutoSpinAttack(ticks, 0, ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.TRIDENT)));
    }

    @Override
    public void setCubeFast(Location corner1, Location corner2, BlockData blockData) {

        if (corner1 == null || corner2 == null || blockData == null) return;
        if (corner1.getWorld() == null || corner2.getWorld() == null) return;
        if (!corner1.getWorld().equals(corner2.getWorld())) return;

        final Map<LevelChunk, List<BlockPos>> chunkMap = getChunkBlockPositions(corner1, corner2);
        if (chunkMap.isEmpty()) return;

        final BlockState state = ((CraftBlockData) blockData).getState();
        final World bukkitWorld = corner1.getWorld();

        final BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        final ArrayList<int[]> touched = new ArrayList<>(chunkMap.size());

        for (Map.Entry<LevelChunk, List<BlockPos>> entry : chunkMap.entrySet()) {
            final LevelChunk chunk = entry.getKey();
            final List<BlockPos> blockPositions = entry.getValue();
            if (blockPositions == null || blockPositions.isEmpty()) continue;

            final int cx = chunk.locX;
            final int cz = chunk.locZ;

            for (int i = 0, size = blockPositions.size(); i < size; i++) {
                final BlockPos p = blockPositions.get(i);
                mutable.set(p.getX(), p.getY(), p.getZ());
                chunk.setBlockState(mutable, state, 206);
            }

            touched.add(new int[]{cx, cz});
        }

        for (int i = 0, size = touched.size(); i < size; i++) {
            final int[] c = touched.get(i);
            bukkitWorld.refreshChunk(c[0], c[1]);
        }
    }

    @Override
    public void replaceFast(Location corner1, Location corner2, BlockData oldBlockData, BlockData newBlockData) {
        if (corner1 == null || corner2 == null || oldBlockData == null || newBlockData == null) return;
        if (corner1.getWorld() == null || corner2.getWorld() == null) return;
        if (!corner1.getWorld().equals(corner2.getWorld())) return;

        final Map<LevelChunk, List<BlockPos>> chunkMap = getChunkBlockPositions(corner1, corner2);
        if (chunkMap.isEmpty()) return;

        final BlockState fromState = ((CraftBlockData) oldBlockData).getState();
        final BlockState toState   = ((CraftBlockData) newBlockData).getState();
        final World bukkitWorld = corner1.getWorld();

        final BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (Map.Entry<LevelChunk, List<BlockPos>> entry : chunkMap.entrySet()) {
            final LevelChunk chunk = entry.getKey();
            final List<BlockPos> positions = entry.getValue();
            if (positions == null || positions.isEmpty()) continue;

            final int cx = chunk.locX;
            final int cz = chunk.locZ;

            for (int i = 0, size = positions.size(); i < size; i++) {
                final BlockPos p = positions.get(i);

                if (chunk.getBlockState(p) != fromState) continue;

                mutable.set(p.getX(), p.getY(), p.getZ());

                chunk.setBlockState(mutable, toState, 260);
            }
            bukkitWorld.refreshChunk(cx, cz);
        }

    }

    @Override
    public ServerPlayer asServerPlayer(Player player) {
        return ((CraftPlayer)player).getHandle();
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
