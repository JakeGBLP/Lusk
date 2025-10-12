package it.jakegblp.lusk.skript.elements.types;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.Color;
import ch.njol.skript.util.SkriptColor;
import it.jakegblp.lusk.nms.core.protocol.packets.BundlePacket;
import it.jakegblp.lusk.nms.core.protocol.packets.Packet;
import it.jakegblp.lusk.nms.core.protocol.packets.client.*;
import it.jakegblp.lusk.nms.core.protocol.packets.server.ServerboundPacket;
import it.jakegblp.lusk.nms.core.world.entity.EntityAnimation;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKeys;
import it.jakegblp.lusk.skript.api.*;
import org.bukkit.DyeColor;
import org.bukkit.entity.Pose;
import org.bukkit.inventory.EquipmentSlot;
import org.skriptlang.skript.lang.converter.Converters;

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
                .toString(blockDestructionPacket -> "Block Destruction Packet with id "+blockDestructionPacket.getEntityId()+", stage "+blockDestructionPacket.getBlockDestructionStage()+" and location "+blockDestructionPacket.getPosition())
                .user("block ?destruction ?packets?")
                .name("Block Destruction Packet")
                .description("A packet that dictates the destruction stage shown to the client.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(SetEquipmentPacket.class, "equipmentpacket")
                .toString(equipmentPacket -> "Equipment Set Packet with id "+equipmentPacket.getEntityId()+" and "+equipmentPacket.getEquipment().size()+ " equipments")
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
                .toString(entitySpawnPacket -> "Entity Spawn Packet of " + EntityData.toString(entitySpawnPacket.getEntityType().getEntityClass()) + " with id "+entitySpawnPacket.getEntityId())
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
                .description("An player position packet.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(PlayerRotationPacket.class, "playerrotationpacket")
                .toString(playerRotationPacket -> "Player Rotation Packet with yaw "+ playerRotationPacket.getYaw() + " and pitch " + playerRotationPacket.getPitch())
                .user("player ?rotation? packets?")
                .name("Player Rotation Packet")
                .description("An player rotation packet.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(EntityMetadataPacket.class, "entitymetadatapacket")
                .toString(entityMetadataPacket -> "Entity Metadata Packet with entity id "+ entityMetadataPacket.getEntityId() + " and " + entityMetadataPacket.getEntityMetadata().size() + " entries")
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
        EnumWrapper<EntityAnimation> ENTITY_ANIMATION_ENUM = new ValidatableEnumWrapper<>(EntityAnimation.class, null, null);
        Classes.registerClass(ENTITY_ANIMATION_ENUM.getClassInfo("entityanimation")
                .user("entity ?animations?")
                .name("Entity - Animation")
                .description("All the entity animations.") // add example
                .since("2.0.0"));
        ListWrapper<MetadataKey> METADATA_KEYS = new ListWrapper<>(MetadataKey.class, MetadataKeys.NAMED_KEYS);
        Classes.registerClass(METADATA_KEYS.getClassInfo("entitymetadatakey")
                .user("(entity ?)?metadata ?keys?")
                .name("Entity - Metadata MetadataKey")
                .description("All the metadata keys.")
                .since("2.0.0"));
        // Bukkit
        if (Skript.classExists("org.bukkit.entity.Pose") && Classes.getExactClassInfo(Pose.class) == null) {
            EnumWrapper<Pose> POSE_ENUM = new EnumWrapper<>(Pose.class, null, "pose");
            Classes.registerClass(POSE_ENUM.getClassInfo("pose")
                    .user("poses?")
                    .name("Entity - Pose")
                    .description("All the Poses.") // add example
                    .since("1.0.0")); // todo: fix docs
        }
        Converters.registerConverter(Color.class, DyeColor.class, Color::asDyeColor);
        Converters.registerConverter(DyeColor.class, Color.class, SkriptColor::fromDyeColor);
        Converters.registerConverter(Packet.class, BundlePacket.class, from -> from instanceof BundlePacket<?> bundlePacket ? bundlePacket : null);
        Converters.registerConverter(Packet.class, ClientboundPacket.class, from -> from instanceof ClientboundPacket clientboundPacket ? clientboundPacket : null);
    }
}
