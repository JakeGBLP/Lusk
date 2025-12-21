package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.api.NMSApi;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.BlockVector;

@AllArgsConstructor
@Getter
@Setter
public class BlockUpdatePacket implements ClientboundPacket {

    BlockVector blockPos;
    BlockData blockState;

    @Override
    public Object asNMS() {
        return null;
    }
}
