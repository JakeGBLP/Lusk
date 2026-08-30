package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.spider;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.EntityUtils;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BooleanFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.Flag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import org.bukkit.entity.Spider;

public class SpiderFlags extends FlagByte<Spider> implements Copyable<SpiderFlags> {
    public static final BooleanFlag<Spider> IS_CLIMBING = Flag.booleanFlag("IS_CLIMBING", Spider::isClimbing, EntityUtils::setClimbing);

    @SuppressWarnings("unchecked")
    private static final Flag<Spider, ?, ?>[] FLAGS = new Flag[] {IS_CLIMBING};

    public static Flag<Spider, ?, ?>[] getFlags() {
        return FLAGS.clone();
    }

    @Override
    public Flag<Spider, ?, ?>[] getConstants() {
        return getFlags();
    }

    public SpiderFlags(Spider entity) {
        super(entity);
    }

    public SpiderFlags() {
        super();
    }

    public SpiderFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<Spider> getFlagHolderClass() {
        return Spider.class;
    }

    public boolean isClimbing() {
        return get(SpiderFlags.IS_CLIMBING);
    }

    public void setClimbing(boolean value) {
        set(SpiderFlags.IS_CLIMBING, value);
    }

    @Override
    public SpiderFlags copy() {
        return new SpiderFlags(flags);
    }
}