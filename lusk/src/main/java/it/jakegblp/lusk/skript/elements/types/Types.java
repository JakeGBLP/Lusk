package it.jakegblp.lusk.skript.elements.types;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.Color;
import ch.njol.skript.util.SkriptColor;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import it.jakegblp.lusk.common.URLUtils;
import it.jakegblp.lusk.nms.core.protocol.packets.BundlePacket;
import it.jakegblp.lusk.nms.core.protocol.packets.Packet;
import it.jakegblp.lusk.nms.core.protocol.packets.client.*;
import it.jakegblp.lusk.nms.core.protocol.packets.server.ServerboundPacket;
import it.jakegblp.lusk.nms.core.world.entity.EntityAnimation;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKeys;
import it.jakegblp.lusk.nms.core.world.player.CompletablePlayerProfile;
import it.jakegblp.lusk.nms.core.world.player.MutableProfileProperty;
import it.jakegblp.lusk.nms.core.world.player.PlayerInfo;
import it.jakegblp.lusk.nms.core.world.player.TexturesPayload;
import it.jakegblp.lusk.skript.api.changer.SimplePluralChanger;
import it.jakegblp.lusk.skript.api.classinfo.EnumClassInfoWrapper;
import it.jakegblp.lusk.skript.api.classinfo.EnumLikeClassInfoWrapper;
import it.jakegblp.lusk.skript.api.classinfo.SimpleClassInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.DyeColor;
import org.bukkit.Particle;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Pose;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.skriptlang.skript.lang.converter.Converters;

import java.net.URL;

public class Types {

    public static final Changer<? super RemoveEntitiesPacket> REMOVE_ENTITIES_PACKET_CHANGER = SimplePluralChanger.builder(RemoveEntitiesPacket.class, Integer.class, Integer[].class)
            .set(RemoveEntitiesPacket::set)
            .remove(RemoveEntitiesPacket::remove)
            .add(RemoveEntitiesPacket::add)
            .delete(RemoveEntitiesPacket::clear)
            .build();

    static {
        Classes.registerClass(new ClassInfo<>(Packet.class, "packet")
                .user("packets?")
                .name("Packet")
                .description("A packet.") // add example
                .since("2.0.0"));
        Classes.registerClass(new ClassInfo<>(ClientboundPacket.class, "clientpacket")
                .user("client ?(bound ?)?packets?")
                .name("Clientbound Packet")
                .description("A packet bound to the client. Sent from the server to the client.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(BundlePacket.class, "bundlepacket")
                .toString(bundlePacket -> "Bundle Packet with Packets: "+ Classes.toString(bundlePacket.getPackets().toArray()))
                .user("bundle ?packets?")
                .name("Bundle Packet")
                .description("A packet that wraps around other packets to send them all at once.") // add example
                .changer(SimplePluralChanger.builder(BundlePacket.class, Packet.class, Packet[].class)
                                .add(BundlePacket::add)
                                .remove(BundlePacket::remove)
                                .set(BundlePacket::set)
                                .delete(BundlePacket::clear)
                                .build())
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(ClientBundlePacket.class, "clientbundlepacket")
                .toString(clientBundlePacket -> "Client Bundle Packet with Packets: "+ Classes.toString(clientBundlePacket.getPackets().toArray()))
                .user("client ?bundle ?packets?")
                .name("Client Bundle Packet")
                .description("A packet that wraps around other packets to send them to the client all at once.") // add example
                .changer(SimplePluralChanger.builder(ClientBundlePacket.class, ClientboundPacket.class, ClientboundPacket[].class)
                                .add(ClientBundlePacket::add)
                                .remove(ClientBundlePacket::remove)
                                .set(ClientBundlePacket::set)
                                .delete(ClientBundlePacket::clear)
                                .build())
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(BlockDestructionPacket.class, "blockdestructionpacket")
                .toString(blockDestructionPacket -> "Block Destruction Packet with id "+blockDestructionPacket.getId()+", stage "+blockDestructionPacket.getBlockDestructionStage()+" and location "+blockDestructionPacket.getPosition())
                .user("block ?destruction ?packets?")
                .name("Block Destruction Packet")
                .description("A packet that dictates the destruction stage shown to the client.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(SetEquipmentPacket.class, "equipmentpacket")
                .toString(equipmentPacket -> "Equipment Set Packet with id "+equipmentPacket.getId()+" and "+equipmentPacket.getEquipment().size()+ " equipments")
                .user("(client ?)?(equipment ?(set ?)?|set ?equipment ?)packets?")
                .name("Equipment Set Packet")
                .description("A packet that dictates what the client sees as the equipment of an entity.") // add example
                .since("2.0.0")
                .changer(SimplePluralChanger.builder(SetEquipmentPacket.class, EquipmentSlot.class, EquipmentSlot[].class)
                        .delete(SetEquipmentPacket::clear)
                        .remove(SetEquipmentPacket::remove)
                        .build()));
        Classes.registerClass(new ClassInfo<>(ServerboundPacket.class, "serverpacket")
                .user("server ?(bound ?)?packets?")
                .name("Serverbound Packet")
                .description("A packet bound to the server. Sent from the client to the server.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(AddEntityPacket.class, "entityspawnpacket")
                .toString(entitySpawnPacket -> "Entity Spawn Packet of " + EntityData.toString(entitySpawnPacket.getEntityType().getEntityClass()) + " with id "+entitySpawnPacket.getId())
                .user("entity ?spawn? packets?")
                .name("Entity Spawn Packet")
                .description("An entity spawn packet.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(RemoveEntitiesPacket.class, "entityremovepacket")
                .toString(removeEntitiesPacket -> "Entities Remove Packet with ids " + Classes.toString(removeEntitiesPacket.getEntityIds().toArray()))
                .user("entit(y|ies) ?remove? packets?")
                .name("Entity Remove Packet")
                .description("An entity remove packet, can remove multiple entities at once.") // add example
                .since("2.0.0")
                .changer(REMOVE_ENTITIES_PACKET_CHANGER));
        Classes.registerClass(new SimpleClassInfo<>(PlayerPositionPacket.class, "playerpositionpacket")
                .toString(playerPositionPacket -> "Player Position Packet with teleport id "+ playerPositionPacket.getTeleportId())
                .user("player ?position? packets?")
                .name("Player Position Packet")
                .description("A player position packet.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(PlayerRotationPacket.class, "playerrotationpacket")
                .toString(playerRotationPacket -> "Player Rotation Packet with yaw "+ playerRotationPacket.getYaw() + " and pitch " + playerRotationPacket.getPitch())
                .user("player ?rotation? packets?")
                .name("Player Rotation Packet")
                .description("A player rotation packet.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(EntityMetadataPacket.class, "entitymetadatapacket")
                .toString(entityMetadataPacket -> "Entity Metadata Packet with entity id "+ entityMetadataPacket.getId() + " and " + entityMetadataPacket.getEntityMetadata().size() + " entries")
                .user("entity ?metadata? packets?")
                .name("Entity Metadata Packet")
                .description("An entity metadata packet.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(PlayerInfoUpdatePacket.class, "playerinfopacket")
                .toString(packet -> "Player Info Packet with "+ packet.getPlayerInfos().size() + " entries and " + packet.getActions().size() + " actions")
                .user("entity ?metadata? packets?")
                .name("Entity Metadata Packet")
                .description("An entity metadata packet.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(EntityMetadata.class, "entitymetadata")
                .toString(entityMetadata -> "Entity Metadata with " + entityMetadata.size() + " entries")
                .user("entity ?metadatas?")
                .name("Entity Metadata")
                .description("An entity's metadata") // add example
                .since("2.0.0"));
        EnumClassInfoWrapper<EntityAnimation> ENTITY_ANIMATION_ENUM = new EnumClassInfoWrapper<>(EntityAnimation.class, null, null);
        Classes.registerClass(ENTITY_ANIMATION_ENUM.getClassInfo("entityanimation")
                .user("entity ?animations?")
                .name("Entity - Animation")
                .description("All the entity animations.") // add example
                .since("2.0.0"));
        EnumLikeClassInfoWrapper<MetadataKey> METADATA_KEYS = new EnumLikeClassInfoWrapper<>(MetadataKey.class, MetadataKeys.NAMED_KEYS);
        Classes.registerClass(METADATA_KEYS.getClassInfo("entitymetadatakey")
                .user("(entity ?)?metadata ?keys?")
                .name("Entity - Metadata MetadataKey")
                .description("All the metadata keys.")
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(PlayerInfo.class, "playerinfo")
                .toString(playerInfo -> "Player Info with UUID " + playerInfo.getUUID())
                .user("player ?info(rmation)?s?")
                .name("Player - Info")
                .description("A player's info.\n\nContains the player's UUID, profile (skin, cape, etc), tab visibility, ping, gamemode, display name, hat visibility, tab list order and chat session data.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(TexturesPayload.class, "texturepayload")
                .toString(texturesPayload -> (texturesPayload.getSignature() != null ? "Signed " : "") + "Texture Payload with name " + texturesPayload.getProfileName())
                .user("texture ?payloads?")
                .name("Player - Texture Payload")
                .description("Contains all the texture data of a player.") // add example
                .since("2.0.0"));

        // Async
        Classes.registerClass(new SimpleClassInfo<>(CompletablePlayerProfile.class, "completableplayerprofile")
                .toString(completablePlayerProfile -> switch (completablePlayerProfile.getExecutionMode()) {
                    case ASYNCHRONOUS -> "Asynchronous";
                    case SYNCHRONOUS -> "Synchronous";
                    case INHERITED -> "Dynamical";
                } + "ly Completable Player Profile with name " + completablePlayerProfile.getName() + " and UUID " + completablePlayerProfile.getId())
                .user("completable ?(player ?)?profiles?")
                .name("Player - Profile")
                .description("A player's profile.\n\nContains the player's UUID, name, skin, cape, player model (alex/steve) and profile properties.\n\nThis type differs from a normal `Player Profile` in that it can be completed asynchronously, you don't need to do anything with this information, they are used and converted to and from interchangeably.") // add example
                .since("2.0.0"));

        // Bukkit
        if (Skript.classExists("org.bukkit.entity.Pose") && Classes.getExactClassInfo(Pose.class) == null) {
            EnumClassInfoWrapper<Pose> POSE_ENUM = new EnumClassInfoWrapper<>(Pose.class, null, "pose");
            Classes.registerClass(POSE_ENUM.getClassInfo("pose")
                    .user("poses?")
                    .name("Entity - Pose")
                    .description("All the Poses.") // add example
                    .since("1.0.0")); // todo: fix docs
        }
        if (Classes.getExactClassInfo(BlockState.class) == null) {
            Classes.registerClass(new ClassInfo<>(BlockState.class, "blockstate")
                    .user("block ?states?")
                    .name("BlockState")
                    .description("""
                    Represents a captured state of a block, which will not change automatically.
                    Unlike Block, which only one object can exist per coordinate, BlockState can exist multiple times for any given Block. Note that another plugin may change the state of the block and you will not know, or they may change the block to another type entirely, causing your BlockState to become invalid.
                    """)
                    .since("1.3")
                    .parser(new Parser<>() {
                        @Override
                        public boolean canParse(ParseContext context) {
                            return false;
                        }

                        @Override
                        public @NotNull String toString(BlockState blockState, int flags) {
                            return (blockState.isPlaced() ? "placed " : "") + "BlockState of type '" + blockState.getType() + "' at '" + blockState.getLocation() + '"';
                        }

                        @Override
                        public @NotNull String toVariableNameString(BlockState blockState) {
                            return toString(blockState, 0);
                        }
                    }));
        }
        if (Classes.getExactClassInfo(Particle.class) == null) {
            Classes.registerClass(new ClassInfo<>(Particle.class, "particle")
                    .since("1.3")
                    .parser(new Parser<>() {
                        @Override
                        public boolean canParse(ParseContext context) {
                            return false;
                        }

                        @Override
                        public @NotNull String toString(Particle particle, int flags) {
                            return particle.toString();
                        }

                        @Override
                        public String toVariableNameString(Particle particle) {
                            return toString(particle, 0);
                        }

                    }));
        }


        // Paper
        if (Classes.getExactClassInfo(PlayerProfile.class) == null) {
            Classes.registerClass(new SimpleClassInfo<>(PlayerProfile.class, "playerprofile")
                    .toString(playerProfile -> "Player Profile with name " + playerProfile.getName() + " and UUID " + playerProfile.getId())
                    .user("(player ?)?profiles?")
                    .name("Player - Profile")
                    .description("A player's profile.\n\nContains the player's UUID, name, skin, cape, player model (alex/steve) and profile properties.") // add example
                    .since("2.0.0"));
        }
        if (Classes.getExactClassInfo(ProfileProperty.class) == null) {
            Classes.registerClass(new SimpleClassInfo<>(ProfileProperty.class, "playerprofileproperty")
                    .toString(profileProperty -> "Player Profile Property named " + profileProperty.getName() + " with value " + profileProperty.getValue() + (profileProperty.isSigned() ? " and signature " + profileProperty.getSignature() : ""))
                    .user("(player ?)?profile ?propert(y|ies)")
                    .name("Player Profile - Property")
                    .description("A property of a player profile.\nUsed for skin and cape textures.") // add example
                    .since("2.0.0"));
        }

        // Other
        if (Classes.getExactClassInfo(URL.class) == null) {
            Classes.registerClass(new SimpleClassInfo<>(URL.class, "url")
                    .toString(URL::toString)
                    .user("urls?")
                    .name("URL")
                    .description("An URL, used mainly to fetch skins.") // add example
                    .since("2.0.0"));
        }
        /*
        If another addon registers ProfileProperty as a classinfo,
        instances of MutableProfileProperty can be converted to that type.
         */
        Converters.registerConverter(MutableProfileProperty.class, ProfileProperty.class, MutableProfileProperty::asImmutable);
        /*
        Same as above,
        but allows external ProfileProperty classinfos to be compatible with syntaxes using MutableProfileProperty
         */
        Converters.registerConverter(ProfileProperty.class, MutableProfileProperty.class, MutableProfileProperty::new);

        Converters.registerConverter(PlayerProfile.class, CompletablePlayerProfile.class, CompletablePlayerProfile::new);
        Converters.registerConverter(CompletablePlayerProfile.class, PlayerProfile.class, CompletablePlayerProfile::getPlayerProfile);
        Converters.registerConverter(String.class, URL.class, URLUtils::toURL);
        Converters.registerConverter(String.class, Component.class, string -> MiniMessage.miniMessage().deserialize(string));
        Converters.registerConverter(Color.class, DyeColor.class, Color::asDyeColor);
        Converters.registerConverter(DyeColor.class, Color.class, SkriptColor::fromDyeColor);
        Converters.registerConverter(Packet.class, BundlePacket.class, from -> from instanceof BundlePacket<?> bundlePacket ? bundlePacket : null);
        Converters.registerConverter(Packet.class, ClientboundPacket.class, from -> from instanceof ClientboundPacket clientboundPacket ? clientboundPacket : null);
    }
}
