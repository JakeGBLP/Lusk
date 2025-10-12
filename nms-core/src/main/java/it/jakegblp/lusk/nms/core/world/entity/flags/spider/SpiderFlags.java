package it.jakegblp.lusk.nms.core.world.entity.flags.spider;

import it.jakegblp.lusk.nms.core.world.entity.FlagByte;
import org.bukkit.entity.Spider;

public class SpiderFlags extends FlagByte<SpiderFlag, Spider, Boolean> {

    public SpiderFlags() {
        super();
    }

    public SpiderFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<SpiderFlag> getBitFlagClass() {
        return SpiderFlag.class;
    }

    @Override
    public Class<Spider> getFlagHolderClass() {
        return Spider.class;
    }

    public boolean isClimbing() {
        return get(SpiderFlag.IS_CLIMBING);
    }

    public void setClimbing(boolean value) {
        set(SpiderFlag.IS_CLIMBING, value);
    }

    @Override
    public SpiderFlags clone() {
        return new SpiderFlags(flags);
    }
}