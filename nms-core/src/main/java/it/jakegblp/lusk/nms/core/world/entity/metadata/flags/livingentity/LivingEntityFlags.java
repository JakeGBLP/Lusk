package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.livingentity;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;

public class LivingEntityFlags extends FlagByte<LivingEntityFlag, LivingEntity, Object> implements Copyable<LivingEntityFlags> {

    public LivingEntityFlags() {
        super();
    }

    public LivingEntityFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<LivingEntityFlag> getBitFlagClass() {
        return LivingEntityFlag.class;
    }

    @Override
    public Class<LivingEntity> getFlagHolderClass() {
        return LivingEntity.class;
    }

    public boolean isHandActive() {
        return (boolean) get(LivingEntityFlag.HAND_ACTIVE);
    }

    public void setHandActive(boolean value) {
        set(LivingEntityFlag.HAND_ACTIVE, value);
    }

    public EquipmentSlot getHand() {
        return (EquipmentSlot) get(LivingEntityFlag.HAND);
    }

    public void setHand(EquipmentSlot hand) {
        set(LivingEntityFlag.HAND, hand);
    }

    public boolean isRiptiding() {
        return (boolean) get(LivingEntityFlag.RIPTIDE_ATTACK);
    }

    public void setRiptiding(boolean value) {
        set(LivingEntityFlag.RIPTIDE_ATTACK, value);
    }

    @Override
    public LivingEntityFlags copy() {
        return new LivingEntityFlags(flags);
    }
}