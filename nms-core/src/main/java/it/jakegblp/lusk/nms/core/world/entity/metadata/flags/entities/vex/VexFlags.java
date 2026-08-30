package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.vex;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BooleanFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.Flag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import org.bukkit.entity.Vex;

public class VexFlags extends FlagByte<Vex> implements Copyable<VexFlags> {
    public static final BooleanFlag<Vex> IS_ATTACKING = Flag.booleanFlag("IS_ATTACKING", Vex::isCharging, Vex::setCharging);

    @SuppressWarnings("unchecked")
    private static final Flag<Vex, ?, ?>[] FLAGS = new Flag[] {IS_ATTACKING};

    public static Flag<Vex, ?, ?>[] getFlags() {
        return FLAGS.clone();
    }

    @Override
    public Flag<Vex, ?, ?>[] getConstants() {
        return getFlags();
    }

    public VexFlags(Vex entity) {
        super(entity);
    }

    public VexFlags() {
        super();
    }

    public VexFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<Vex> getFlagHolderClass() {
        return Vex.class;
    }

    public boolean isAttacking() {
        return get(IS_ATTACKING);
    }

    public void setAttacking(boolean value) {
        set(IS_ATTACKING, value);
    }

    @Override
    public VexFlags copy() {
        return new VexFlags(flags);
    }
}