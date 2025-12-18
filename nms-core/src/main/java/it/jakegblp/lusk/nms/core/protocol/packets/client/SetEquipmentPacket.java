package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.common.annotations.Availability;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Map;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@Getter
@Setter
public class SetEquipmentPacket implements ClientboundPacketWithId {

    private int id;
    @Availability(addedIn = "1.20.5")
    private boolean sanitized;
    private Map<EquipmentSlot, ItemStack> equipment;

    @Availability(addedIn = "1.20.5")
    public SetEquipmentPacket(int id, @Availability(addedIn = "1.20.5") boolean sanitized, Map<EquipmentSlot, ItemStack> equipment) {
        this.id = id;
        this.sanitized = sanitized;
        this.equipment = equipment;
    }

    @Availability(addedIn = "1.20.5")
    public boolean isSanitized() {
        return sanitized;
    }

    @Availability(addedIn = "1.20.5")
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
    public Object asNMS() {
        return NMS.toNMSSetEquipmentPacket(this);
    }
}
