package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@Getter
@Setter
public class SetEquipmentPacket implements ClientboundPacketWithId {

    protected int id;
    protected boolean sanitized;
    protected Map<EquipmentSlot, ItemStack> equipment;

    public SetEquipmentPacket(int entityId, boolean sanitized, Map<EquipmentSlot, ItemStack> equipment) {
        this.id = entityId;
        this.sanitized = sanitized;
        this.equipment = equipment;
    }

    public SetEquipmentPacket(int id, boolean sanitized) {
        this(id, sanitized, Map.of());
    }

    public SetEquipmentPacket(int id, Map<EquipmentSlot, ItemStack> equipment) {
        this(id, false, equipment);
    }

    public SetEquipmentPacket(int id) {
        this(id, false, Map.of());
    }

    public void set(EquipmentSlot slot, ItemStack item) {
        equipment.put(slot, item);
    }

    public void remove(EquipmentSlot slot) {
        equipment.remove(slot);
    }

    public void remove(EquipmentSlot... slots) {
        equipment.keySet().retainAll(Arrays.asList(slots));
    }

    public void clear() {
        equipment.clear();
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        NMS.write(this, buffer);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        var read = NMS.read(getClass(), buffer);
        this.id = read.getId();
        this.sanitized = read.isSanitized();
        this.equipment = read.getEquipment();
    }

    @Override
    public SetEquipmentPacket copy() {
        Map<EquipmentSlot, ItemStack> map = new HashMap<>(this.equipment.size());
        equipment.forEach((key, value) -> map.put(key, value.clone()));
        return new SetEquipmentPacket(id, sanitized, map);
    }
}
