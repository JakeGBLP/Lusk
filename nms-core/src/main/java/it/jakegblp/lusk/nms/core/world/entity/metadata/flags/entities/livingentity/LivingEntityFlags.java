package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.livingentity;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.EntityUtils;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BooleanFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.Flag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.MappedBooleanFlag;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;

public class LivingEntityFlags extends FlagByte<LivingEntity> implements Copyable<LivingEntityFlags> {
    public static final BooleanFlag<LivingEntity> HAND_ACTIVE = Flag.booleanFlag("HAND_ACTIVE", LivingEntity::hasActiveItem, EntityUtils::setHandActive);
    public static final MappedBooleanFlag<LivingEntity, EquipmentSlot> HAND = Flag.mappedBooleanFlag(
            "HAND",
            EquipmentSlot.class,
            LivingEntity::getActiveItemHand,
            EntityUtils::setActiveItemHand,
            slot -> slot == EquipmentSlot.OFF_HAND,
            offHand -> offHand ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND,
            EquipmentSlot.HAND);
    public static final BooleanFlag<LivingEntity> RIPTIDE_ATTACK = Flag.booleanFlag("RIPTIDE_ATTACK", LivingEntity::isRiptiding, LivingEntity::setRiptiding);

    @SuppressWarnings("unchecked")
    private static final Flag<LivingEntity, ?, ?>[] FLAGS = new Flag[] {HAND_ACTIVE, HAND, RIPTIDE_ATTACK};

    public static Flag<LivingEntity, ?, ?>[] getFlags() {
        return FLAGS.clone();
    }

    @Override
    public Flag<LivingEntity, ?, ?>[] getConstants() {
        return getFlags();
    }

    public LivingEntityFlags(LivingEntity entity) {
        super(entity);
    }

    public LivingEntityFlags() {
        super();
    }

    public LivingEntityFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<LivingEntity> getFlagHolderClass() {
        return LivingEntity.class;
    }

    public boolean isHandActive() {
        return (boolean) get(HAND_ACTIVE);
    }

    public void setHandActive(boolean value) {
        set(HAND_ACTIVE, value);
    }

    public EquipmentSlot getHand() {
        return (EquipmentSlot) get(HAND);
    }

    public void setHand(EquipmentSlot hand) {
        set(HAND, hand);
    }

    public boolean isRiptiding() {
        return (boolean) get(RIPTIDE_ATTACK);
    }

    public void setRiptiding(boolean value) {
        set(RIPTIDE_ATTACK, value);
    }

    @Override
    public LivingEntityFlags copy() {
        return new LivingEntityFlags(flags);
    }
}