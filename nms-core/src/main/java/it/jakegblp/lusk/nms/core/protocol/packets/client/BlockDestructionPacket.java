package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
import lombok.*;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.Range;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class BlockDestructionPacket implements ClientboundPacketWithId {
    protected int id;
    protected BlockVector position;
    protected @Range(from = -1, to = 9) int blockDestructionStage;

    public BlockDestructionPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    // todo: skript impl
    public boolean causesRemoval() {
        return blockDestructionStage < 0;
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeVarInt(id);
        buffer.writeBlockVector(position);
        buffer.writeByte(blockDestructionStage);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        id = buffer.readVarInt();
        position = buffer.readBlockVector();
        blockDestructionStage = buffer.readByte();
    }

    @Override
    public BlockDestructionPacket copy() {
        return new BlockDestructionPacket(id, position.clone(), blockDestructionStage);
    }
}
