package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.spider;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import org.bukkit.entity.Spider;

public class SpiderFlags extends FlagByte<SpiderFlag, Spider, Boolean> implements Copyable<SpiderFlags> {

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
    public SpiderFlags copy() {
        return new SpiderFlags(flags);
    }
}