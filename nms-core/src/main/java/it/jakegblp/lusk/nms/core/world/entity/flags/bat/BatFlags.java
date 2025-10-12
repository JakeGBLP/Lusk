package it.jakegblp.lusk.nms.core.world.entity.flags.bat;

import it.jakegblp.lusk.nms.core.world.entity.FlagByte;
import org.bukkit.entity.Bat;

public class BatFlags extends FlagByte<BatFlag, Bat, Boolean> {

    public BatFlags() {
        super();
    }

    public BatFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<BatFlag> getBitFlagClass() {
        return BatFlag.class;
    }

    @Override
    public Class<Bat> getFlagHolderClass() {
        return Bat.class;
    }

    public boolean isHanging() {
        return get(BatFlag.IS_HANGING);
    }

    public void setHanging(boolean value) {
        set(BatFlag.IS_HANGING, value);
    }

    @Override
    public BatFlags clone() {
        return new BatFlags(flags);
    }
}