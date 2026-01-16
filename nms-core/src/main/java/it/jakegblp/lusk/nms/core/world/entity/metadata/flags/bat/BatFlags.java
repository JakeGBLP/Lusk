package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.bat;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import org.bukkit.entity.Bat;

public class BatFlags extends FlagByte<BatFlag, Bat, Boolean> implements Copyable<BatFlags> {

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
    public BatFlags copy() {
        return new BatFlags(flags);
    }
}