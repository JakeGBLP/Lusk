package it.jakegblp.lusk.nms.core.protocol.packets.client.tobemoved;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

import java.util.Collection;

@Getter
@Setter
public class AttributeSnapshot {

    Attribute attribute;
    double base;
    Collection<AttributeModifier> modifiers;




}
