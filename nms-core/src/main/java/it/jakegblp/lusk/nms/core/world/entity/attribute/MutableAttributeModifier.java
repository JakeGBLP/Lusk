package it.jakegblp.lusk.nms.core.world.entity.attribute;

import it.jakegblp.lusk.common.Mutable;
import lombok.*;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jspecify.annotations.NullMarked;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NullMarked
public class MutableAttributeModifier implements Keyed, Mutable<AttributeModifier> {
    private NamespacedKey key;
    private double amount;
    private AttributeModifier.Operation operation;
    private EquipmentSlotGroup slotGroup;
    private boolean isTransient;

    @NullMarked
    public MutableAttributeModifier(AttributeModifier modifier, boolean isTransient) {
        this(modifier.getKey(), modifier.getAmount(), modifier.getOperation(), modifier.getSlotGroup(), isTransient);
    }

    @NullMarked
    public MutableAttributeModifier(AttributeModifier modifier) {
        this(modifier, false);
    }

    @Override
    public AttributeModifier immutable() {
        return new AttributeModifier(key, amount, operation, slotGroup);
    }

}
