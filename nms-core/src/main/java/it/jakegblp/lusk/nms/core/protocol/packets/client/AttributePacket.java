package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.world.entity.attribute.AttributeSnapshot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@AllArgsConstructor
@Getter
@Setter
public class AttributePacket implements ClientboundPacket, Cloneable {

    // todo: finish wrapper

    int id;
    List<AttributeSnapshot> attributes;

    @Override
    public Object asNMS() {
        return NMS.toNMSAttributePacket(this);
    }

    @Override
    public AttributePacket clone() {
        try {
            AttributePacket clone = (AttributePacket) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
