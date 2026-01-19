package it.jakegblp.lusk.nms.core.protocol.packets.server;

import io.papermc.paper.math.BlockPosition;
import it.jakegblp.lusk.nms.core.protocol.packets.Packet;
import it.jakegblp.lusk.nms.core.util.BlockFace3D;
import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.BlockFace;


@AllArgsConstructor
@Getter
@Setter
public class PlayerActionPacket implements BufferSerializableServerboundPacket{

    private BlockPosition pos;
    private BlockFace direction;
    private Action action;
    private int sequence;

    public PlayerActionPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    @Override
    public Packet copy() {
        return new PlayerActionPacket(pos, direction, action, sequence);
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeEnum(this.action);
        buffer.writeBlockPos(this.pos);
        buffer.writeByte(BlockFace3D.faceTo3D(this.direction));
        buffer.writeVarInt(this.sequence);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        this.action = buffer.readEnum(Action.class);
        this.pos = buffer.readBlockPos();
        this.direction = BlockFace3D.faceFrom3D(buffer.readUnsignedByte());
        this.sequence = buffer.readVarInt();
    }


    public static enum Action {
        START_DESTROY_BLOCK,
        ABORT_DESTROY_BLOCK,
        STOP_DESTROY_BLOCK,
        DROP_ALL_ITEMS,
        DROP_ITEM,
        RELEASE_USE_ITEM,
        SWAP_ITEM_WITH_OFFHAND;
    }
}
