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

    private int entityId;
    @Availability(addedIn = "1.20.5")
    private boolean sanitized;
    private Map<EquipmentSlot, ItemStack> equipment;

    @Availability(addedIn = "1.20.5")
    public SetEquipmentPacket(int entityId, @Availability(addedIn = "1.20.5") boolean sanitized, Map<EquipmentSlot, ItemStack> equipment) {
        this.entityId = entityId;
        this.sanitized = sanitized;
        this.equipment = equipment;
    }

    @Availability(addedIn = "1.20.5")
    public boolean isSanitized() {
        return sanitized;
    }

    @Availability(addedIn = "1.20.5")
    public SetEquipmentPacket(int entityId, boolean sanitized) {
        this(entityId, sanitized, Map.of());
    }

    public SetEquipmentPacket(int entityId, Map<EquipmentSlot, ItemStack> equipment) {
        this(entityId, false, equipment);
    }

    public SetEquipmentPacket(int entityId) {
        this(entityId, false, Map.of());
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
