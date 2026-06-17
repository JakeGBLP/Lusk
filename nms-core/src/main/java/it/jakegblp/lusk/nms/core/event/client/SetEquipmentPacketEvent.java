package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.SetEquipmentPacket;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;

@NullMarked
@Getter
public class SetEquipmentPacketEvent extends ClientPacketEvent<SetEquipmentPacket> {

    private static final HandlerList handlers = new HandlerList();

    protected int entityId;
    protected Map<EquipmentSlot, ItemStack> equipment;

    public SetEquipmentPacketEvent(SetEquipmentPacket packet, Player player, boolean async) {
        super(player, async);
        this.entityId = packet.getEntityId();
        this.equipment = new HashMap<>(packet.getEquipment());
    }

    public void setEntityId(int entityId) {
        markModified();
        this.entityId = entityId;
    }

    public void setEquipment(Map<EquipmentSlot, ItemStack> equipment) {
        markModified();
        this.equipment = new HashMap<>(equipment);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public SetEquipmentPacket createPacket() {
        return new SetEquipmentPacket(getEntityId(), getEquipment());
    }
}
