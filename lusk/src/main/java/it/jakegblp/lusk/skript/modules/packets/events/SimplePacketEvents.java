package it.jakegblp.lusk.skript.modules.packets.events;

import ch.njol.skript.bukkitutil.EntityUtils;
import ch.njol.skript.entity.EntityData;
import it.jakegblp.lusk.nms.core.event.client.*;
import it.jakegblp.lusk.nms.core.event.server.PacketReceiveEvent;
import it.jakegblp.lusk.nms.core.event.server.PlayerActionPacketEvent;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.server.PlayerActionPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.server.ServerboundPacket;
import it.jakegblp.lusk.nms.core.world.entity.ProtocolEntityReference;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.UUID;

public class SimplePacketEvents {

    // todo: add and remove for vector and location event values
    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.event(PacketReceiveEvent.class)
                .patterns("packet receive[d]")
                .name("Packet Receive")
                .description("Called when the server receives a packet from the client.")
                .since("2.0.0")
                .examples("on packet received:")
                .keywords("packets", "packet", "packet listener")
                .register(syntaxRegistry);
        Registrations.event(PacketSendEvent.class)
                .patterns("packet sen(d|t)")
                .name("Packet Send")
                .description("Called when the server send a packet to the client.")
                .since("2.0.0")
                .examples("on packet send:")
                .keywords("packets", "packet", "packet listener")
                .register(syntaxRegistry);
        Registrations.registerEventValue(PacketReceiveEvent.class, ServerboundPacket.class, PacketReceiveEvent::getPacket, PacketReceiveEvent::setPacket);
        Registrations.registerEventValue(PacketSendEvent.class, ClientboundPacket.class, PacketSendEvent::getPacket, PacketSendEvent::setPacket);

        Registrations.event(BlockUpdatePacketEvent.class)
                .patterns("block (send|change|update) [packet]")
                .name("Block Update (Packet)")
                .description("Called when the server updates a block for the client.")
                .since("2.0.0")
                .examples("on block update:")
                .keywords("packets", "packet", "block", "block update")
                .register(syntaxRegistry);
        Registrations.registerEventValue(BlockUpdatePacketEvent.class, BlockData.class, BlockUpdatePacketEvent::getBlockData, BlockUpdatePacketEvent::setBlockData);
        Registrations.registerEventValue(BlockUpdatePacketEvent.class, Vector.class, BlockUpdatePacketEvent::getPosition, BlockUpdatePacketEvent::setPosition);
        Registrations.registerEventValue(BlockUpdatePacketEvent.class, Location.class, BlockUpdatePacketEvent::getLocation);

        Registrations.event(BlockDestructionPacketEvent.class)
                .patterns("block (destruction|mining) [packet]")
                .name("Block Destruction (Packet)")
                .description("Called when the server updates the destruction stage of a block for the client.")
                .since("2.0.0")
                .examples("on block destruction packet:")
                .keywords("packets", "packet", "block", "block destruction", "block damage", "block breaking", "breaking stage")
                .register(syntaxRegistry);
        Registrations.registerEventValue(BlockDestructionPacketEvent.class, Integer.class, BlockDestructionPacketEvent::getEntityId, BlockDestructionPacketEvent::setEntityId);
        Registrations.registerEventValue(BlockDestructionPacketEvent.class, Byte.class, BlockDestructionPacketEvent::getBlockDestructionStage, BlockDestructionPacketEvent::setBlockDestructionStage);
        Registrations.registerEventValue(BlockDestructionPacketEvent.class, Location.class, BlockDestructionPacketEvent::getLocation, BlockDestructionPacketEvent::setLocation);
        Registrations.registerEventValue(BlockDestructionPacketEvent.class, Vector.class, BlockDestructionPacketEvent::getPosition, (event, vector) -> event.setPosition(vector.toBlockVector()));

        Registrations.event(ParticleSendPacketEvent.class)
                .patterns("particle send [packet]")
                .name("Particle Send (Packet)")
                .description("Called when the server sends a particle to the client.")
                .since("2.0.0")
                .examples("on particle send:")
                .keywords("particle", "packet", "packets")
                .register(syntaxRegistry);
        Registrations.registerEventValue(ParticleSendPacketEvent.class, Particle.class, ParticleSendPacketEvent::getBukkitParticle);
        Registrations.registerEventValue(ParticleSendPacketEvent.class, Integer.class, ParticleSendPacketEvent::getCount, ParticleSendPacketEvent::setCount);
        Registrations.registerEventValue(ParticleSendPacketEvent.class, Float.class, ParticleSendPacketEvent::getMaxSpeed, ParticleSendPacketEvent::setMaxSpeed, Float::sum, (aFloat, aFloat2) -> aFloat - aFloat2);
        Registrations.registerEventValue(ParticleSendPacketEvent.class, Vector.class, ParticleSendPacketEvent::getPosition, ParticleSendPacketEvent::setPosition);
        Registrations.registerEventValue(ParticleSendPacketEvent.class, Location.class, ParticleSendPacketEvent::getLocation);

        Registrations.event(PlayerActionPacketEvent.class)
                .patterns("player action [packet]")
                .name("Player Action (Packet)")
                .description("Called when the player executes a valid action.")
                .since("2.0.0")
                .examples("on player action:")
                .keywords("action", "packet", "packets")
                .register(syntaxRegistry);
        Registrations.registerEventValue(PlayerActionPacketEvent.class, Location.class, PlayerActionPacketEvent::getLocation);
        Registrations.registerEventValue(PlayerActionPacketEvent.class, Vector.class, PlayerActionPacketEvent::getPosition, PlayerActionPacketEvent::setPosition);
        Registrations.registerEventValue(PlayerActionPacketEvent.class, PlayerActionPacket.Action.class, PlayerActionPacketEvent::getAction, PlayerActionPacketEvent::setAction);
        Registrations.registerEventValue(PlayerActionPacketEvent.class, Integer.class, PlayerActionPacketEvent::getSequence, PlayerActionPacketEvent::setSequence);
        Registrations.registerEventValue(PlayerActionPacketEvent.class, BlockFace.class, PlayerActionPacketEvent::getBlockFace, PlayerActionPacketEvent::setBlockFace);

        Registrations.event(PlayerActionPacketEvent.class)
                .patterns("entity metadata [update] [packet]")
                .name("Entity Metadata (Packet)")
                .description("Called when the server sends an entity metadata packet to the client.")
                .since("2.0.0")
                .examples("on entity metadata update:")
                .keywords("metadata", "packet", "packets")
                .register(syntaxRegistry);
        Registrations.registerEventValue(EntityMetadataPacketEvent.class, World.class, EntityMetadataPacketEvent::getWorld);
        Registrations.registerEventValue(EntityMetadataPacketEvent.class, ProtocolEntityReference.class, event -> ProtocolEntityReference.of(event.getEntityId()));
        Registrations.registerEventValue(EntityMetadataPacketEvent.class, Integer.class, EntityMetadataPacketEvent::getEntityId, EntityMetadataPacketEvent::setEntityId);
        Registrations.registerEventValue(EntityMetadataPacketEvent.class, EntityMetadata.class, EntityMetadataPacketEvent::getEntityMetadata, EntityMetadataPacketEvent::setEntityMetadata);

        Registrations.event(PlayerActionPacketEvent.class)
                .patterns("(entity (spawn|add)|(spawn|add) entity) [packet]")
                .name("Entity Spawn (Packet)")
                .description("Called when the server sends an entity spawn packet to the client.")
                .since("2.0.0")
                .examples("on entity spawn packet:")
                .keywords("spawn", "summon", "packet", "packets", "entity")
                .register(syntaxRegistry);
        Registrations.registerEventValue(AddEntityPacketEvent.class, World.class, AddEntityPacketEvent::getWorld);
        Registrations.registerEventValue(AddEntityPacketEvent.class, ProtocolEntityReference.class, event -> ProtocolEntityReference.of(event.getEntityId()));
        Registrations.registerEventValue(AddEntityPacketEvent.class, Integer.class, AddEntityPacketEvent::getEntityId, AddEntityPacketEvent::setEntityId);
        Registrations.registerEventValue(AddEntityPacketEvent.class, UUID.class, AddEntityPacketEvent::getEntityUUID, AddEntityPacketEvent::setEntityUUID);
        Registrations.registerEventValue(AddEntityPacketEvent.class, EntityData.class,
                event -> EntityUtils.toSkriptEntityData(event.getEntityType()),
                (addEntityEvent, entityData) -> addEntityEvent.setEntityType(EntityUtils.toBukkitEntityType(entityData))
        );
        Registrations.registerEventValue(AddEntityPacketEvent.class, Vector.class, AddEntityPacketEvent::getVelocity, AddEntityPacketEvent::setVelocity);
        Registrations.registerEventValue(AddEntityPacketEvent.class, Location.class, AddEntityPacketEvent::getLocation, AddEntityPacketEvent::setLocation);
        Registrations.registerEventValue(AddEntityPacketEvent.class, Float.class, AddEntityPacketEvent::getHeadYaw, AddEntityPacketEvent::setHeadYaw);

        Registrations.event(EntityMovePacketEvent.class)
                .patterns("entity movement [packet]")
                .name("Entity Move (Packet)")
                .description("Called when the server sends an entity move packet to the client.\nBoth movement and rotation are included.")
                .since("2.0.0")
                .examples("on entity move packet:")
                .keywords("movement", "move", "rotate", "rotation", "packet", "packets", "entity")
                .register(syntaxRegistry);
        Registrations.registerEventValue(EntityMovePacketEvent.class, Vector.class, EntityMovePacketEvent::getMovement, EntityMovePacketEvent::setMovement);
        Registrations.registerEventValue(EntityMovePacketEvent.class, Boolean.class, EntityMovePacketEvent::isOnGround, EntityMovePacketEvent::setOnGround);
    }

}
