package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
import lombok.*;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.Range;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

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
        this(buffer.readVarInt(), buffer.readBlockVector(), buffer.readByte());
    }

    // todo: skript impl
    public boolean causesRemoval() {
        return blockDestructionStage < 0;
    }

    @Override
    public Object asNMS() {
        return NMS.toNMS(this);
    }
}
