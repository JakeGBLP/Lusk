package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.common.SimpleList;
import it.jakegblp.lusk.nms.core.event.client.RemoveEntitiesPacketEvent;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.*;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class RemoveEntitiesPacket implements BufferSerializableClientboundPacket<RemoveEntitiesPacketEvent>, SimpleList<Integer> {
    protected IntList entityIds;

    public RemoveEntitiesPacket(List<Integer> entityIds) {
        this(new IntArrayList(entityIds));
    }

    public RemoveEntitiesPacket(int... entityIds) {
        this(new IntArrayList(entityIds));
    }

    public RemoveEntitiesPacket() {
        this(new IntArrayList());
    }
    public RemoveEntitiesPacket(SimpleByteBuf buffer) {
        this(buffer.readIntList());
    }

    @Override
    public List<Integer> get() {
        return entityIds;
    }

    public void add(Integer... entityIds) {
        add(List.of(entityIds));
    }

    public void add(List<Integer> entityIds) {
        this.entityIds.addAll(entityIds);
    }

    public void remove(Integer... entityIds) {
        remove(List.of(entityIds));
    }

    public void remove(List<Integer> entityIds) {
        this.entityIds.removeAll(entityIds);
    }

    public void set(Integer... entityIds) {
        set(List.of(entityIds));
    }

    public void set(List<Integer> entityIds) {
        this.entityIds = new IntArrayList(entityIds);
    }

    public void clear() {
        this.entityIds.clear();
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeIntList(this.entityIds);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        this.entityIds = buffer.readIntList();
    }

    @Override
    public RemoveEntitiesPacketEvent createEvent(Player player, boolean async) {
        return new RemoveEntitiesPacketEvent(this, player, async);
    }

    @Override
    public RemoveEntitiesPacket copy() {
        return new RemoveEntitiesPacket(entityIds);
    }
}
