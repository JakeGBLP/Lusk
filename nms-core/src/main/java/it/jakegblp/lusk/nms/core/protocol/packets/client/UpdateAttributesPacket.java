package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.event.client.UpdateAttributesPacketEvent;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.world.entity.attribute.AttributeSnapshot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class UpdateAttributesPacket implements ClientboundPacketWithEntityId<UpdateAttributesPacketEvent> {

    protected int entityId;
    protected List<AttributeSnapshot> attributeSnapshots;

    public UpdateAttributesPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    public UpdateAttributesPacket(int entityId, AttributeSnapshot... attributeSnapshot) {
        this(entityId, List.of(attributeSnapshot));
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeVarInt(entityId);
        buffer.writeCollection(attributeSnapshots, SimpleByteBuf::writeAttributeSnapshot);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        entityId = buffer.readVarInt();
        attributeSnapshots = buffer.readList(SimpleByteBuf::readAttributeSnapshot);
    }

    @Override
    public UpdateAttributesPacketEvent createEvent(Player player, boolean async) {
        return new UpdateAttributesPacketEvent(this, player, async);
    }

    @Override
    public UpdateAttributesPacket copy() {
        return new UpdateAttributesPacket(entityId, CommonUtils.map(attributeSnapshots, AttributeSnapshot::copy));
    }
}
