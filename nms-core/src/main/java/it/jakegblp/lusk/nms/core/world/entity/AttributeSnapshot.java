package it.jakegblp.lusk.nms.core.world.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

import java.util.Collection;

@AllArgsConstructor
@Getter
@Setter
public class AttributeSnapshot {

    Attribute attribute;
    double base;
    Collection<AttributeModifier> modifiers;

}
