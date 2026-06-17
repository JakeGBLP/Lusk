package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.event.client.BlockDestructionPacketEvent;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NullMarked
public class BlockDestructionPacket implements ClientboundPacketWithEntityId<BlockDestructionPacketEvent> {

    protected int entityId;
    @SuppressWarnings("NotNullFieldNotInitialized")
    protected BlockVector position;
    protected @Range(from = -1, to = 9) byte blockDestructionStage;

    public BlockDestructionPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    public BlockDestructionPacket(int entityId, BlockVector position, byte blockDestructionStage) {
        this.entityId = entityId;
        this.position = position.clone();
        this.blockDestructionStage = blockDestructionStage;
    }

    @Contract("-> new")
    public BlockVector getPosition() {
        return position.clone();
    }

    public void setPosition(BlockVector position) {
        this.position = position.clone();
    }

    public boolean causesRemoval() {
        return blockDestructionStage < 0;
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeVarInt(entityId);
        buffer.writeBlockVector(position);
        buffer.writeByte(blockDestructionStage);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        entityId = buffer.readVarInt();
        position = buffer.readBlockVector();
        blockDestructionStage = buffer.readByte();
    }

    @Override
    public BlockDestructionPacketEvent createEvent(Player player, boolean async) {
        return new BlockDestructionPacketEvent(this, player, async);
    }

    @Override
    public BlockDestructionPacket copy() {
        return new BlockDestructionPacket(entityId, position.clone(), blockDestructionStage);
    }
}
