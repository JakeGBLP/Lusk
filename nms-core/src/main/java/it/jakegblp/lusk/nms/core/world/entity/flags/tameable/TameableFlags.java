package it.jakegblp.lusk.nms.core.world.entity.flags.tameable;

import it.jakegblp.lusk.nms.core.world.entity.FlagByte;
import org.bukkit.entity.Tameable;

public class TameableFlags extends FlagByte<TameableFlag, Tameable, Boolean> {

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
    public TameableFlags clone() {
        return new TameableFlags(flags);
    }
}