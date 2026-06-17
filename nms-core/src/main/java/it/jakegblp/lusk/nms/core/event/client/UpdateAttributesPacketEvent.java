package it.jakegblp.lusk.nms.core.event.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.UpdateAttributesPacket;
import it.jakegblp.lusk.nms.core.world.entity.attribute.AttributeSnapshot;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
@Getter
public class UpdateAttributesPacketEvent extends ClientPacketEvent<UpdateAttributesPacket> {

    private static final HandlerList handlers = new HandlerList();

    protected int entityId;
    protected List<AttributeSnapshot> attributeSnapshots;

    public UpdateAttributesPacketEvent(UpdateAttributesPacket packet, Player player, boolean async) {
        super(player, async);
        this.entityId = packet.getEntityId();
        this.attributeSnapshots = new ArrayList<>(packet.getAttributeSnapshots());
    }

    public void setAttributeSnapshots(List<AttributeSnapshot> attributeSnapshots) {
        markModified();
        this.attributeSnapshots = new ArrayList<>(attributeSnapshots);
    }

    public List<AttributeSnapshot> getAttributeSnapshots() {
        return new ArrayList<>(attributeSnapshots);
    }

    public void setEntityId(int entityId) {
        markModified();
        this.entityId = entityId;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public UpdateAttributesPacket createPacket() {
        return new UpdateAttributesPacket(getEntityId(), getAttributeSnapshots());
    }
}
