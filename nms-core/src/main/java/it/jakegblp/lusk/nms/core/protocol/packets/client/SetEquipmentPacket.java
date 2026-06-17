package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.event.client.SetEquipmentPacketEvent;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class SetEquipmentPacket implements ClientboundPacketWithEntityId<SetEquipmentPacketEvent> {

    protected int entityId;
    protected Map<EquipmentSlot, ItemStack> equipment;

    public SetEquipmentPacket(SimpleByteBuf buf) {
        read(buf);
    }

    public SetEquipmentPacket(int entityId, Map<EquipmentSlot, ItemStack> equipment) {
        this.entityId = entityId;
        this.equipment = new LinkedHashMap<>(equipment);
    }

    public SetEquipmentPacket(int id) {
        this(id, new LinkedHashMap<>());
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
        buffer.writeVarInt(entityId);
        int index = 0;
        int size = equipment.size();
        for (Map.Entry<EquipmentSlot, ItemStack> entry : equipment.entrySet()) {
            EquipmentSlot slot = entry.getKey();
            ItemStack item = entry.getValue();

            boolean hasNext = index++ != size - 1;

            int slotByte = slot.ordinal();
            if (hasNext)
                slotByte |= 0x80;
            buffer.writeByte(slotByte);
            buffer.writeOptionalItemStack(item);
        }
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        entityId = buffer.readVarInt();
        equipment = new LinkedHashMap<>();
        int slotByte;
        do {
            slotByte = buffer.readUnsignedByte();

            EquipmentSlot slot = EquipmentSlot.values()[slotByte & 0x7F];
            ItemStack item = buffer.readItemStack();

            equipment.put(slot, item);
        } while ((slotByte & 0x80) != 0);
    }

    @Override
    public SetEquipmentPacketEvent createEvent(Player player, boolean async) {
        return new SetEquipmentPacketEvent(this, player, async);
    }

    @Override
    public SetEquipmentPacket copy() {
        Map<EquipmentSlot, ItemStack> map = new LinkedHashMap<>(this.equipment.size());
        equipment.forEach((key, value) -> map.put(key, value.clone()));
        return new SetEquipmentPacket(entityId, map);
    }
}
