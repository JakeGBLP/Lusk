package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.event.client.BlockUpdatePacketEvent;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;

@AllArgsConstructor
@Getter
@Setter
public class BlockUpdatePacket implements BufferSerializableClientboundPacket<BlockUpdatePacketEvent> {

    protected BlockVector position;
    protected BlockData blockData;

    public BlockUpdatePacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeBlockVector(position);
        buffer.writeBlockData(blockData);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        position = buffer.readBlockVector();
        blockData = buffer.readBlockData();
    }

    @Override
    public BlockUpdatePacketEvent createEvent(Player player, boolean async) {
        return new BlockUpdatePacketEvent(this, player, async);
    }

    @Override
    public BlockUpdatePacket copy() {
        return new BlockUpdatePacket(position.clone(), blockData.clone());
    }

}
