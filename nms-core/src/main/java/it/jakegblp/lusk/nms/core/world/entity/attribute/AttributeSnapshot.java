package it.jakegblp.lusk.nms.core.world.entity.attribute;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.util.BufferSerializable;
import it.jakegblp.lusk.nms.core.util.PureNMSObject;
import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class AttributeSnapshot implements BufferSerializable<AttributeSnapshot>, Copyable<AttributeSnapshot>, PureNMSObject<Object> {
    protected Attribute attribute;
    protected double base;
    protected List<AttributeModifier> modifiers;

    public AttributeSnapshot(SimpleByteBuf buffer) {
        read(buffer);
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeAttribute(attribute);
        buffer.writeDouble(base);
        buffer.writeCollection(modifiers, SimpleByteBuf::writeAttributeModifier);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        attribute = buffer.readAttribute();
        base = buffer.readDouble();
        modifiers = buffer.readList(SimpleByteBuf::readAttributeModifier);
    }

    @Override
    public AttributeSnapshot copy() {
        return new AttributeSnapshot(attribute, base, new ArrayList<>(modifiers));
    }
}
