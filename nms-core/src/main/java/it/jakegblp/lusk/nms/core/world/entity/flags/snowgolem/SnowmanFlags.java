package it.jakegblp.lusk.nms.core.world.entity.flags.snowgolem;

import it.jakegblp.lusk.nms.core.world.entity.FlagByte;
import org.bukkit.entity.Snowman;

public class SnowmanFlags extends FlagByte<SnowmanFlag, Snowman, Boolean> {

    public SnowmanFlags() {
        super();
    }

    public SnowmanFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<SnowmanFlag> getBitFlagClass() {
        return SnowmanFlag.class;
    }

    @Override
    public Class<Snowman> getFlagHolderClass() {
        return Snowman.class;
    }

    public boolean hasUnused1() {
        return get(SnowmanFlag.UNUSED_1);
    }

    public void setUnused1(boolean value) {
        set(SnowmanFlag.UNUSED_1, value);
    }

    public boolean hasUnused2() {
        return get(SnowmanFlag.UNUSED_2);
    }

    public void setUnused2(boolean value) {
        set(SnowmanFlag.UNUSED_2, value);
    }

    public boolean hasUnused3() {
        return get(SnowmanFlag.UNUSED_3);
    }

    public void setUnusedE(boolean value) {
        set(SnowmanFlag.UNUSED_3, value);
    }

    public boolean hasUnused4() {
        return get(SnowmanFlag.UNUSED_4);
    }

    public void setUnused4(boolean value) {
        set(SnowmanFlag.UNUSED_4, value);
    }

    public boolean hasPumpkinHat() {
        return get(SnowmanFlag.HAS_PUMPKIN);
    }

    public void setPumpkinHat(boolean value) {
        set(SnowmanFlag.HAS_PUMPKIN, value);
    }

    @Override
    public SnowmanFlags clone() {
        return new SnowmanFlags(flags);
    }
}