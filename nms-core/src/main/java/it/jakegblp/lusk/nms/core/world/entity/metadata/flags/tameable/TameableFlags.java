package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.tameable;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import org.bukkit.entity.Tameable;

public class TameableFlags extends FlagByte<TameableFlag, Tameable, Boolean> implements Copyable<TameableFlags> {

    public TameableFlags() {
        super();
    }

    public TameableFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<TameableFlag> getBitFlagClass() {
        return TameableFlag.class;
    }

    @Override
    public Class<Tameable> getFlagHolderClass() {
        return Tameable.class;
    }

    public boolean isSitting() {
        return get(TameableFlag.IS_SITTING);
    }

    public void setSitting(boolean value) {
        set(TameableFlag.IS_SITTING, value);
    }

    public boolean hasUnused() {
        return get(TameableFlag.UNUSED);
    }

    public void setUnused(boolean value) {
        set(TameableFlag.UNUSED, value);
    }

    public boolean isTamed() {
        return get(TameableFlag.IS_TAMED);
    }

    public void setTamed(boolean value) {
        set(TameableFlag.IS_TAMED, value);
    }

    @Override
    public TameableFlags copy() {
        return new TameableFlags(flags);
    }
}