package it.jakegblp.lusk.skript.modules.packets.events;

import ch.njol.skript.bukkitutil.EntityUtils;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import it.jakegblp.lusk.nms.core.event.client.*;
import it.jakegblp.lusk.nms.core.event.server.PacketReceiveEvent;
import it.jakegblp.lusk.nms.core.event.server.PlayerActionPacketEvent;
import it.jakegblp.lusk.nms.core.logger.events.ConsoleLogEvent;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.server.PlayerActionPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.server.ServerboundPacket;
import it.jakegblp.lusk.nms.core.world.entity.ProtocolEntityReference;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.skript.api.syntax.event.SimpleEventValues;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.UUID;

public class SimplePacketEvents {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerEvent(syntaxRegistry, SimpleEvent.class, PacketReceiveEvent.class,
                "Packet Receive", "Called when the server receives a packet from the client.", "on packet received:",
                "2.0.0", new String[]{"packets", "packet", "packet listener"}, "packet receive[d]");
        AddonUtils.registerEvent(syntaxRegistry, SimpleEvent.class, PacketSendEvent.class,
                "Packet Send", "Called when the server send a packet to the client.", "on packet send:",
                "2.0.0", new String[]{"packets", "packet", "packet listener"}, "packet sen(d|t)");
        SimpleEventValues.registerEventValue(PacketReceiveEvent.class, ServerboundPacket.class, PacketReceiveEvent::getPacket, PacketReceiveEvent::setPacket);
        SimpleEventValues.registerEventValue(PacketSendEvent.class, ClientboundPacket.class, PacketSendEvent::getPacket, PacketSendEvent::setPacket);

        AddonUtils.registerEvent(syntaxRegistry, SimpleEvent.class, BlockUpdatePacketEvent.class,
                "Block Update (Packet)", "Called when the server updates a block for the client.", "on block update:",
                "2.0.0", new String[]{"packets", "packet", "block", "block update"}, "block (send|change|update) [packet]");
        SimpleEventValues.registerEventValue(BlockUpdatePacketEvent.class, BlockData.class, BlockUpdatePacketEvent::getBlockData, BlockUpdatePacketEvent::setBlockData);
        SimpleEventValues.registerEventValue(BlockUpdatePacketEvent.class, Vector.class, BlockUpdatePacketEvent::getPosition, BlockUpdatePacketEvent::setPosition);
        EventValues.registerEventValue(BlockUpdatePacketEvent.class, Location.class, BlockUpdatePacketEvent::getLocation, EventValues.TIME_NOW);

        AddonUtils.registerEvent(syntaxRegistry, SimpleEvent.class, BlockDestructionPacketEvent.class,
                "Block Destruction (Packet)", "Called when the server updates the destruction stage of a block for the client.", "on block destruction packet:",
                "2.0.0", new String[]{"packets", "packet", "block", "block destruction", "block damage", "block breaking", "breaking stage"}, "block (destruction|mining) [packet]");
        SimpleEventValues.registerEventValue(BlockDestructionPacketEvent.class, Integer.class, BlockDestructionPacketEvent::getEntityId, BlockDestructionPacketEvent::setEntityId);
        SimpleEventValues.registerEventValue(BlockDestructionPacketEvent.class, Byte.class, BlockDestructionPacketEvent::getBlockDestructionStage, BlockDestructionPacketEvent::setBlockDestructionStage);
        SimpleEventValues.registerEventValue(BlockDestructionPacketEvent.class, Location.class, BlockDestructionPacketEvent::getLocation, BlockDestructionPacketEvent::setLocation);
        SimpleEventValues.registerEventValue(BlockDestructionPacketEvent.class, Vector.class, BlockDestructionPacketEvent::getPosition, (event, vector) -> event.setPosition(vector.toBlockVector()));

        AddonUtils.registerEvent(syntaxRegistry, SimpleEvent.class, ConsoleLogEvent.class,
                "Console Log", "Called when the server's console is used to log something.", "on console log:",
                "2.0.0", new String[]{"log", "logging", "console"}, "(console|server) log");
        EventValues.registerEventValue(ConsoleLogEvent.class, String.class, ConsoleLogEvent::getMessage, EventValues.TIME_NOW);

        AddonUtils.registerEvent(syntaxRegistry, SimpleEvent.class, ParticleSendPacketEvent.class,
                "Particle Send (Packet)", "Called when the server sends a particle to the client.", "on console log:",
                "2.0.0", new String[]{"particle", "packet", "packets"}, "particle send [packet]");
        EventValues.registerEventValue(ParticleSendPacketEvent.class, Particle.class, ParticleSendPacketEvent::getBukkitParticle, EventValues.TIME_NOW);
        SimpleEventValues.registerEventValue(ParticleSendPacketEvent.class, Integer.class, ParticleSendPacketEvent::getCount, ParticleSendPacketEvent::setCount);
        SimpleEventValues.registerEventValue(ParticleSendPacketEvent.class, Float.class, ParticleSendPacketEvent::getMaxSpeed, ParticleSendPacketEvent::setMaxSpeed);
        SimpleEventValues.registerEventValue(ParticleSendPacketEvent.class, Vector.class, ParticleSendPacketEvent::getPosition, ParticleSendPacketEvent::setPosition);
        EventValues.registerEventValue(ParticleSendPacketEvent.class, Location.class, ParticleSendPacketEvent::getLocation, EventValues.TIME_NOW);

        AddonUtils.registerEvent(syntaxRegistry, SimpleEvent.class, PlayerActionPacketEvent.class,
                "Particle Action (Packet)", "Called when the server sends a particle to the client.", "on console log:",
                "2.0.0", new String[]{"action", "packet", "packets"}, "particle send [packet]");
        EventValues.registerEventValue(PlayerActionPacketEvent.class, Location.class, PlayerActionPacketEvent::getLocation, EventValues.TIME_NOW);
        SimpleEventValues.registerEventValue(PlayerActionPacketEvent.class, Vector.class, PlayerActionPacketEvent::getPosition, PlayerActionPacketEvent::setPosition);
        SimpleEventValues.registerEventValue(PlayerActionPacketEvent.class, PlayerActionPacket.Action.class, PlayerActionPacketEvent::getAction, PlayerActionPacketEvent::setAction);
        SimpleEventValues.registerEventValue(PlayerActionPacketEvent.class, Integer.class, PlayerActionPacketEvent::getSequence, PlayerActionPacketEvent::setSequence);
        SimpleEventValues.registerEventValue(PlayerActionPacketEvent.class, BlockFace.class, PlayerActionPacketEvent::getBlockFace, PlayerActionPacketEvent::setBlockFace);

        AddonUtils.registerEvent(syntaxRegistry, SimpleEvent.class, EntityMetadataPacketEvent.class,
                "Entity Metadata (Packet)", "Called when the server sends an entity metadata packet to the client.", "on entity metadata update:",
                "2.0.0", new String[]{"metadata", "packet", "packets"}, "entity metadata [update] [packet]");
        EventValues.registerEventValue(EntityMetadataPacketEvent.class, World.class, EntityMetadataPacketEvent::getWorld, EventValues.TIME_NOW);
        EventValues.registerEventValue(EntityMetadataPacketEvent.class, ProtocolEntityReference.class, event -> ProtocolEntityReference.of(event.getEntityId()), EventValues.TIME_NOW);
        SimpleEventValues.registerEventValue(EntityMetadataPacketEvent.class, Integer.class, EntityMetadataPacketEvent::getEntityId, EntityMetadataPacketEvent::setEntityId);
        SimpleEventValues.registerEventValue(EntityMetadataPacketEvent.class, EntityMetadata.class, EntityMetadataPacketEvent::getEntityMetadata, EntityMetadataPacketEvent::setEntityMetadata);

        AddonUtils.registerEvent(syntaxRegistry, SimpleEvent.class, AddEntityPacketEvent.class,
                "Entity Spawn (Packet)", "Called when the server sends an entity spawn packet to the client.", "on entity spawn packet:",
                "2.0.0", new String[]{"spawn", "summon", "packet", "packets", "entity"}, "(entity (spawn|add)|(spawn|add) entity) [packet]");
        EventValues.registerEventValue(AddEntityPacketEvent.class, World.class, AddEntityPacketEvent::getWorld, EventValues.TIME_NOW);
        EventValues.registerEventValue(AddEntityPacketEvent.class, ProtocolEntityReference.class, event -> ProtocolEntityReference.of(event.getEntityId()), EventValues.TIME_NOW);
        SimpleEventValues.registerEventValue(AddEntityPacketEvent.class, Integer.class, AddEntityPacketEvent::getEntityId, AddEntityPacketEvent::setEntityId);
        SimpleEventValues.registerEventValue(AddEntityPacketEvent.class, UUID.class, AddEntityPacketEvent::getEntityUUID, AddEntityPacketEvent::setEntityUUID);
        SimpleEventValues.registerEventValue(AddEntityPacketEvent.class, EntityData.class,
                event -> EntityUtils.toSkriptEntityData(event.getEntityType()),
                (addEntityEvent, entityData) -> addEntityEvent.setEntityType(EntityUtils.toBukkitEntityType(entityData))
        );
        SimpleEventValues.registerEventValue(AddEntityPacketEvent.class, Vector.class, AddEntityPacketEvent::getVelocity, AddEntityPacketEvent::setVelocity);
        SimpleEventValues.registerEventValue(AddEntityPacketEvent.class, Location.class, AddEntityPacketEvent::getLocation, AddEntityPacketEvent::setLocation);
        SimpleEventValues.registerEventValue(AddEntityPacketEvent.class, Float.class, AddEntityPacketEvent::getHeadYaw, AddEntityPacketEvent::setHeadYaw);
        SimpleEventValues.registerEventValue(AddEntityPacketEvent.class, Integer.class, AddEntityPacketEvent::getData, AddEntityPacketEvent::setData);

        AddonUtils.registerEvent(syntaxRegistry, SimpleEvent.class, EntityMovePacketEvent.class,
                "Entity Move (Packet)", "Called when the server sends an entity move packet to the client.\nBoth movement and rotation are included.", "on entity move packet:",
                "2.0.0", new String[]{"movement", "move", "rotate", "rotation", "packet", "packets", "entity"}, "entity movement [packet]");
        SimpleEventValues.registerEventValue(EntityMovePacketEvent.class, Vector.class, EntityMovePacketEvent::getMovement, EntityMovePacketEvent::setMovement);
        SimpleEventValues.registerEventValue(EntityMovePacketEvent.class, Boolean.class, EntityMovePacketEvent::isOnGround, EntityMovePacketEvent::setOnGround);
    }

}
