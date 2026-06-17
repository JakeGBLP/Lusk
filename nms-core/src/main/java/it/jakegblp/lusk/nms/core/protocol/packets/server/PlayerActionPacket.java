package it.jakegblp.lusk.nms.core.protocol.packets.server;

import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.BlockFace;
import org.bukkit.util.BlockVector;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class PlayerActionPacket implements BufferSerializableServerboundPacket {

    protected BlockVector position;
    protected BlockFace blockFace;
    protected Action action;
    protected int sequence;

    public PlayerActionPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    @Override
    public PlayerActionPacket copy() {
        return new PlayerActionPacket(position, blockFace, action, sequence);
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeEnum(this.action);
        buffer.writeBlockVector(this.position);
        buffer.writeBlockFace(this.blockFace);
        buffer.writeVarInt(this.sequence);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        this.action = buffer.readEnum(Action.class);
        this.position = buffer.readBlockVector();
        this.blockFace = buffer.readBlockFace();
        this.sequence = buffer.readVarInt();
    }

    public enum Action {
        START_DESTROY_BLOCK,
        ABORT_DESTROY_BLOCK,
        STOP_DESTROY_BLOCK,
        DROP_ALL_ITEMS,
        DROP_ITEM,
        RELEASE_USE_ITEM,
        SWAP_ITEM_WITH_OFFHAND;
    }
}
