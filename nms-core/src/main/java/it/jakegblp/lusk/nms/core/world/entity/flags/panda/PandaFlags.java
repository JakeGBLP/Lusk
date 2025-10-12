package it.jakegblp.lusk.nms.core.world.entity.flags.panda;

import it.jakegblp.lusk.nms.core.world.entity.FlagByte;
import org.bukkit.entity.Panda;

import java.util.Collection;

public class PandaFlags extends FlagByte<PandaFlag, Panda, Boolean> {

    public PandaFlags() {
        super();
    }

    public PandaFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<PandaFlag> getBitFlagClass() {
        return PandaFlag.class;
    }

    @Override
    public Class<Panda> getFlagHolderClass() {
        return Panda.class;
    }

    public boolean hasUnused() {
        return get(PandaFlag.UNUSED);
    }

    public void setUnused(boolean value) {
        set(PandaFlag.UNUSED, value);
    }

    public boolean isSneezing() {
        return get(PandaFlag.IS_SNEEZING);
    }

    public void setSneezing(boolean value) {
        set(PandaFlag.IS_SNEEZING, value);
    }

    public boolean isRolling() {
        return get(PandaFlag.IS_ROLLING);
    }

    public void setRolling(boolean value) {
        set(PandaFlag.IS_ROLLING, value);
    }

    public boolean isSitting() {
        return get(PandaFlag.IS_SITTING);
    }

    public void setSitting(boolean value) {
        set(PandaFlag.IS_SITTING, value);
    }

    public boolean isOnBack() {
        return get(PandaFlag.IS_ON_BACK);
    }

    public void setOnBack(boolean value) {
        set(PandaFlag.IS_ON_BACK, value);
    }

    @Override
    public PandaFlags clone() {
        return new PandaFlags(flags);
    }
}