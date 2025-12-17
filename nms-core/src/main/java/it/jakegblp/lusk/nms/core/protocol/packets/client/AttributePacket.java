package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.protocol.packets.client.tobemoved.AttributeSnapshot;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.attribute.Attribute;

import java.util.List;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;


@Getter
@Setter
public class AttributePacket implements ClientboundPacket{

    int id;
    Attribute attribute;
    double base;

    public AttributePacket(int id, Attribute attribute, double base){
        this.id = id;
        this.attribute = attribute;
        this.base = base;
    }

    @Override
    public Object asNMS() {
        return NMS.toNMSAttributePacket(this);
    }
}
