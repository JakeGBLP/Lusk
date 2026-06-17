package it.jakegblp.lusk.skript.modules.packets;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.registrations.Classes;
import com.google.common.collect.BiMap;
import it.jakegblp.lusk.nms.core.protocol.packets.*;
import it.jakegblp.lusk.nms.core.protocol.packets.client.*;
import it.jakegblp.lusk.nms.core.protocol.packets.server.PlayerActionPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.server.ServerboundPacket;
import it.jakegblp.lusk.nms.core.util.Displayable;
import it.jakegblp.lusk.nms.core.world.entity.EntityAnimation;
import it.jakegblp.lusk.nms.core.world.entity.effect.InternalEntityEffect;
import it.jakegblp.lusk.nms.core.world.entity.guardian.GuardianBeam;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataItem;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.nms.core.world.player.PlayerInfo;
import it.jakegblp.lusk.skript.api.changer.SimplePluralChanger;
import it.jakegblp.lusk.skript.api.classinfo.EnumClassInfoWrapper;
import it.jakegblp.lusk.skript.api.classinfo.EnumLikeClassInfoWrapper;
import it.jakegblp.lusk.skript.api.classinfo.SimpleClassInfo;
import it.jakegblp.lusk.skript.api.module.LuskModule;
import it.jakegblp.lusk.skript.modules.packets.effects.*;
import it.jakegblp.lusk.skript.modules.packets.effects.entityeffects.EffDeath;
import it.jakegblp.lusk.skript.modules.packets.effects.metadata.blockdisplay.EffMetadataBlockDisplayBlockData;
import it.jakegblp.lusk.skript.modules.packets.effects.metadata.display.*;
import it.jakegblp.lusk.skript.modules.packets.effects.metadata.entity.*;
import it.jakegblp.lusk.skript.modules.packets.effects.metadata.itemdisplay.EffMetadataItemDisplayItem;
import it.jakegblp.lusk.skript.modules.packets.effects.metadata.textdisplay.EffMetadataTextDisplayLineWidth;
import it.jakegblp.lusk.skript.modules.packets.effects.metadata.textdisplay.EffMetadataTextDisplayText;
import it.jakegblp.lusk.skript.modules.packets.events.SimplePacketEvents;
import it.jakegblp.lusk.skript.modules.packets.expressions.ExprEntityFromIdInWorld;
import it.jakegblp.lusk.skript.modules.packets.expressions.constructors.*;
import it.jakegblp.lusk.skript.modules.packets.expressions.properties.*;
import org.bukkit.inventory.EquipmentSlot;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.lang.comparator.Comparators;
import org.skriptlang.skript.lang.comparator.Relation;
import org.skriptlang.skript.lang.converter.Converters;
import org.skriptlang.skript.registration.SyntaxRegistry;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

public class PacketsModule extends LuskModule {

    @Override
    public boolean canLoad(SkriptAddon addon) {
        return NMS != null;
    }

    @Override
    protected void registration(SyntaxRegistry registry) {
        ExprEntityMetadataPacket.register(registry);

        EffDeath.register(registry);

        EffMetadataBlockDisplayBlockData.register(registry);
        EffMetadataDisplayBillboard.register(registry);

        EffMetadataDisplayHeight.register(registry);
        EffMetadataDisplayScale.register(registry);
        EffMetadataDisplayTranslation.register(registry);
        EffMetadataDisplayWidth.register(registry);

        EffMetadataEntityAirTime.register(registry);
        EffMetadataEntityBurning.register(registry);
        EffMetadataEntityCustomName.register(registry);
        EffMetadataEntityCustomNameVisibility.register(registry);
        EffMetadataEntityFreezeTime.register(registry);
        EffMetadataEntityGliding.register(registry);
        EffMetadataEntityGlowing.register(registry);
        EffMetadataEntityGravity.register(registry);
        EffMetadataEntityInvisible.register(registry);
        EffMetadataEntityPose.register(registry);
        EffMetadataEntitySilent.register(registry);
        EffMetadataEntitySneaking.register(registry);
        EffMetadataEntitySprinting.register(registry);
        EffMetadataEntitySwimming.register(registry);

        EffMetadataItemDisplayItem.register(registry);

        EffMetadataTextDisplayText.register(registry);
        EffMetadataTextDisplayLineWidth.register(registry);

        EffDispatchPacket.register(registry);
        EffEntityEffectPacket.register(registry);
        EffRemoveEntities.register(registry);
        EffSecSpawnFakeEntity.register(registry);
        EffSecSpawnFakePlayer.register(registry);
        EffQuicklySetBlocks.register(registry);
        EffSetPassengerPacket.register(registry);
        EffShowRemoveDisplayable.register(registry);

        SimplePacketEvents.register(registry);

        ExprEntityMetadataPacket.register(registry);
        ExprPlayerInfoUpdatePacket.register(registry);
        ExprPlayerRotationPacket.register(registry);
        ExprRemoveEntitiesPacket.register(registry);
        ExprSecAddEntityPacket.register(registry);
        ExprSecAttributePacket.register(registry);
        ExprSecBlockDestructionPacket.register(registry);
        ExprClientBundlePacket.register(registry);
        ExprSecEntityMetadataPacket.register(registry);
        ExprSecEquipmentPacket.register(registry);
        ExprSecPlayerPositionPacket.register(registry);
        ExprSecPlayerRotationPacket.register(registry);
        ExprSecTeamCreatePacket.register(registry);
        ExprTeamDeletePacket.register(registry);
        ExprSecTeamMembersAddPacket.register(registry);
        ExprSecTeamMembersRemovePacket.register(registry);
        ExprSecTeamUpdateInfoPacket.register(registry);
        ExprSetPassengersPacket.register(registry);

        ExprBundlePackets.register(registry);
        ExprEntityMetadataEntry.register(registry);
        ExprEntityMetadataEntries.register(registry);
        ExprEntityRemovePacketIds.register(registry);
        ExprEquipmentOfPacket.register(registry);
        ExprPlayerInfoPacketEntry.register(registry);
        ExprPlayerInfos.register(registry);
        ExprPositionPacketComponents.register(registry);
        ExprRotationPacketComponents.register(registry);
        ExprTeleportId.register(registry);
        ExprEntityFromIdInWorld.register(registry);
    }

    public static final Changer<? super RemoveEntitiesPacket> REMOVE_ENTITIES_PACKET_CHANGER = SimplePluralChanger.simpleListChanger(RemoveEntitiesPacket.class, Integer.class, Integer[].class);

    @Override
    public void init(SkriptAddon addon) {
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
                .toString(bundlePacket -> "Bundle Packet with Packets: " + Classes.toString(bundlePacket.getPackets().toArray()))
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
                .toString(clientBundlePacket -> "Client Bundle Packet with Packets: " + Classes.toString(clientBundlePacket.getPackets().toArray()))
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
                .toString(blockDestructionPacket -> "Block Destruction Packet with id " + blockDestructionPacket.getEntityId() + ", stage " + blockDestructionPacket.getBlockDestructionStage() + " and location " + blockDestructionPacket.getPosition())
                .user("block ?destruction ?packets?")
                .name("Block Destruction Packet")
                .description("A packet that dictates the destruction stage shown to the client.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(SetEquipmentPacket.class, "equipmentpacket")
                .toString(equipmentPacket -> "Equipment Set Packet with id " + equipmentPacket.getEntityId() + " and " + equipmentPacket.getEquipment().size() + " equipments")
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
                .toString(entitySpawnPacket -> "Entity Spawn Packet of " + EntityData.toString(entitySpawnPacket.getEntityType().getEntityClass()) + " with id " + entitySpawnPacket.getEntityId())
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
        Classes.registerClass(new SimpleClassInfo<>(GenericRotationPacket.class, "genericrotationpacket")
                .toString(packet -> "Generic Rotation Packet with yaw " + packet.getYaw() + " and pitch " + packet.getPitch())
                .user("generic ?rotation? packets?")
                .name("Generic Rotation Packet")
                .description("Any packet that includes yaw and pitch as rotation of an entity or player.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(GenericPositionPacket.class, "genericpositionpacket")
                .toString(packet -> "Generic Position Packet with x " + packet.getPositionX() + ", y " + packet.getPositionY() + " and z " + packet.getPositionZ())
                .user("generic ?position? packets?")
                .name("Generic Position Packet")
                .description("Any packet that includes x, y and z as position of an entity or player.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(GenericVelocityPacket.class, "genericvelocitypacket")
                .toString(packet -> "Generic Velocity Packet with x " + packet.getVelocityX() + ", y " + packet.getVelocityY() + " and z " + packet.getVelocityZ())
                .user("generic ?velocity? packets?")
                .name("Generic Velocity Packet")
                .description("Any packet that includes x, y and z as velocity of an entity or player.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(GenericMovementPacket.class, "genericmovementpacket")
                .toString(packet -> "Generic Movement Packet with x " + packet.getMovementX() + ", y " + packet.getMovementY() + " and z " + packet.getMovementZ())
                .user("generic ?movement? packets?")
                .name("Generic Movement Packet")
                .description("Any packet that includes x, y and z as movement of an entity or player.\n\nThis is not the same thing as the generic velocity packet.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(EntityTeleportPacket.class, "entityteleportpacket")
                .toString(packet -> "Generic Teleport Packet with x " + packet.getVelocityX() + ", y " + packet.getVelocityY() + " and z " + packet.getVelocityZ())
                .user("entity ?teleport? packets?")
                .name("Entity Teleport Packet")
                .description("Any packet that includes x, y and z as velocity of an entity or player.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(PlayerPositionPacket.class, "playerpositionpacket")
                .toString(playerPositionPacket -> "Player Position Packet with teleport id " + playerPositionPacket.getTeleportId())
                .user("player ?position? packets?")
                .name("Player Position Packet")
                .description("A player position packet.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(PlayerRotationPacket.class, "playerrotationpacket")
                .toString(playerRotationPacket -> "Player Rotation Packet with yaw " + playerRotationPacket.getYaw() + " and pitch " + playerRotationPacket.getPitch())
                .user("player ?rotation? packets?")
                .name("Player Rotation Packet")
                .description("A player rotation packet.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(EntityMetadataPacket.class, "entitymetadatapacket")
                .toString(entityMetadataPacket -> "Entity Metadata Packet with entity id " + entityMetadataPacket.getEntityId() + " and " + entityMetadataPacket.getEntityMetadata().size() + " entries")
                .user("entity ?metadata? packets?")
                .name("Entity Metadata Packet")
                .description("An entity metadata packet.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(SetPassengersPacket.class, "setpassengerspacket")
                .toString(setPassengersPacket -> "Set Passengers Packet with vehicle " + Classes.toString(setPassengersPacket.getVehicle()) + (setPassengersPacket.getPassengers().isEmpty() ? " and no passengers" : " and passengers " + Classes.toString(setPassengersPacket.getPassengers())))
                .user("set ?passenger[s]? packets?")
                .name("Set Passengers Packet")
                .description("A packet that makes entities ride other entities for the client.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(PlayerInfoUpdatePacket.class, "playerinfopacket")
                .toString(packet -> "Player Info Packet with " + packet.getPlayerInfos().size() + " entries and " + packet.getActions().size() + " actions")
                .user("entity ?metadata? packets?")
                .name("Entity Metadata Packet")
                .description("An entity metadata packet.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(EntityMetadata.class, "entitymetadata")
                .toString(entityMetadata -> "Entity Metadata with " + entityMetadata.size() + " entries")
                .defaultExpression(new EventValueExpression<>(EntityMetadata.class))
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
        EnumLikeClassInfoWrapper<MetadataKey> METADATA_KEYS = new EnumLikeClassInfoWrapper<>(MetadataKey.class, (BiMap)NMS.getMetadataKeyRegistry().namedKeys());
        Classes.registerClass(METADATA_KEYS.getClassInfo("entitymetadatakey")
                .defaultExpression(new EventValueExpression<>(MetadataKey.class))
                .user("(entity ?)?metadata ?keys?")
                .name("Entity - Metadata Key")
                .description("All the metadata keys.")
                .since("2.0.0"));
        //Classes.registerClass(new SimpleClassInfo<>(MetadataKeyReference.class, "entitymetadatakeyreference")
        //        .toString(reference -> Classes.toString(Classes.getExactClassInfo(reference.serializer().codec().getToClass())) + " metadata key reference id " + reference.id())
        //        .user("(entity ?)?metadata ?key ?references?")
        //        .name("Entity - Metadata Key Reference")
        //        .description("The parent type of both entity metadata keys and entity metadata entries.") // add example
        //        .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(MetadataItem.class, "entitymetadataentry")
                .toString(metadataItem -> Classes.toString(Classes.getExactClassInfo(metadataItem.type())) + " metadata entry with " + (metadataItem.value() == null ? "no value" : "value " + Classes.toString(metadataItem.value())) + " and id " + metadataItem.id())
                .user("(entity ?)?metadata ?entr(y|ies)")
                .name("Entity - Metadata Entry")
                .description("A snapshot of a metadata value containing it's accessor id, value and extra info about it.\nInternally known as a metadata item.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(Displayable.class, "displayable")
                .toString(displayable -> "displayable object")
                .user("displayables?")
                .name("Player - Displayable Object")
                .description("An object that can be displayed and removed for players.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(GuardianBeam.class, "guardianbeam")
                .toString(guardianBeam -> "guardian beam from " + Classes.toString(guardianBeam.getStart()) + " to " + Classes.toString(guardianBeam.getEnd()))
                .user("guardian ?beams?")
                .name("Guardian - Beam")
                .description("A guardian beam, can be displayed and removed for players.\nNote: the developer is responsible for specifying to whom a guardian beam is displayed.") // add example
                .since("2.0.0"));

        Classes.registerClass(new SimpleClassInfo<>(PlayerInfo.class, "playerinfo")
                .toString(playerInfo -> "Player Info with UUID " + playerInfo.getUuid())
                .user("player ?info(rmation)?s?")
                .name("Player - Info")
                .description("A player's info.\n\nContains the player's UUID, profile (skin, cape, etc), tab visibility, ping, gamemode, display name, hat visibility, tab list order and chat session data.") // add example
                .since("2.0.0"));

        EnumClassInfoWrapper<PlayerActionPacket.Action> PLAYER_ACTION_ENUM = new EnumClassInfoWrapper<>(PlayerActionPacket.Action.class);
        Classes.registerClass(PLAYER_ACTION_ENUM.getClassInfo("playeraction")
                .user("player ?actions?")
                .name("Player Action")
                .description("Actions the player can execute and trigger the player action packet with.") // add example
                .since("2.0.0"));

        EnumClassInfoWrapper<InternalEntityEffect> INTERNAL_ENTITY_EFFECT_ENUM = new EnumClassInfoWrapper<>(InternalEntityEffect.class);
        Classes.registerClass(INTERNAL_ENTITY_EFFECT_ENUM.getClassInfo("internalentityeffect")
                .user("internal ?entity ?effects?")
                .name("Internal Entity Effect")
                .description("Entity effects not normally available.") // add example
                .since("2.0.0"));
        Converters.registerConverter(MetadataItem.class, MetadataKey.class, MetadataItem::asKey);
        Converters.registerConverter(EntityMetadataPacket.class, EntityMetadata.class, EntityMetadataPacket::getEntityMetadata);
        Converters.registerConverter(Packet.class, BundlePacket.class, from -> from instanceof BundlePacket<?> bundlePacket ? bundlePacket : null);
        Converters.registerConverter(Packet.class, ClientboundPacket.class, from -> from instanceof ClientboundPacket clientboundPacket ? clientboundPacket : null);
        Comparators.registerComparator(MetadataItem.class, MetadataKey.class, (item, key) -> Relation.get(key.matches(item)));
    }

    @Override
    public String name() {
        return "packets";
    }
}
