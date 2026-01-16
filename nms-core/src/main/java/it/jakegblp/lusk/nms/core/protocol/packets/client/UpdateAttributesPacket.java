package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.world.entity.attribute.AttributeSnapshot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class UpdateAttributesPacket implements ClientboundPacketWithId {

    protected int id;
    protected List<AttributeSnapshot> attributeSnapshots;

    public UpdateAttributesPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeVarInt(id);
        buffer.writeCollection(attributeSnapshots, SimpleByteBuf::writeAttributeSnapshot);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        id = buffer.readVarInt();
        attributeSnapshots = buffer.readList(SimpleByteBuf::readAttributeSnapshot);
    }

    @Override
    public UpdateAttributesPacket copy() {
        return new UpdateAttributesPacket(id, CommonUtils.map(attributeSnapshots, AttributeSnapshot::copy));
    }
}
