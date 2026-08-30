package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.tameable;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.EntityUtils;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BooleanFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.Flag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import org.bukkit.entity.Tameable;

public class TameableFlags extends FlagByte<Tameable> implements Copyable<TameableFlags> {
    public static final BooleanFlag<Tameable> IS_SITTING = Flag.booleanFlag("IS_SITTING", EntityUtils::isSitting, EntityUtils::setSitting);
    public static final BooleanFlag<Tameable> UNUSED = Flag.unusedBooleanFlag("UNUSED");
    public static final BooleanFlag<Tameable> IS_TAMED = Flag.booleanFlag("IS_TAMED", Tameable::isTamed, Tameable::setTamed);

    @SuppressWarnings("unchecked")
    private static final Flag<Tameable, ?, ?>[] FLAGS = new Flag[] {IS_SITTING, UNUSED, IS_TAMED};

    public static Flag<Tameable, ?, ?>[] getFlags() {
        return FLAGS.clone();
    }

    @Override
    public Flag<Tameable, ?, ?>[] getConstants() {
        return getFlags();
    }

    public TameableFlags(Tameable entity) {
        super(entity);
    }

    public TameableFlags() {
        super();
    }

    public TameableFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<Tameable> getFlagHolderClass() {
        return Tameable.class;
    }

    public boolean isSitting() {
        return get(IS_SITTING);
    }

    public void setSitting(boolean value) {
        set(IS_SITTING, value);
    }

    public boolean isTamed() {
        return get(IS_TAMED);
    }

    public void setTamed(boolean value) {
        set(IS_TAMED, value);
    }

    @Override
    public TameableFlags copy() {
        return new TameableFlags(flags);
    }
}