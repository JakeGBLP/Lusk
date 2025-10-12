package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.common.SimpleList;
import it.jakegblp.lusk.nms.core.annotations.Availability;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Availability(addedIn = "1.17.1")
public class RemoveEntitiesPacket implements ClientboundPacket, SimpleList<Integer> {
    protected IntList entityIds;

    public RemoveEntitiesPacket(List<Integer> entityIds) {
        this.entityIds = new IntArrayList(entityIds);
    }

    public RemoveEntitiesPacket() {
        this.entityIds = new IntArrayList();
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
        clear();
        add(entityIds);
    }

    public void clear() {
        this.entityIds.clear();
    }

    @Override
    public Object asNMS() {
        return NMS.toNMSRemoveEntitiesPacket(this);
    }
}
