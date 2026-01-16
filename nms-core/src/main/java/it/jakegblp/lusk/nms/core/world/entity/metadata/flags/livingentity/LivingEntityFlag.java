package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.livingentity;

import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.SemiBooleanFlag;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Objects;

public enum LivingEntityFlag implements SemiBooleanFlag<Object> {
    HAND_ACTIVE,
    HAND { // todo: codecs??
        @Override
        public Object decode(int bits) {
            return Objects.equals(super.decode(bits), true) ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND;
        }

        @Override
        public int encode(Object value) {
            return value == EquipmentSlot.OFF_HAND ? 1 : 0;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Class<Object> getValueClass() {
            return (Class<Object>)(Class<?>)EquipmentSlot.class;
        }
    },
    RIPTIDE_ATTACK
}