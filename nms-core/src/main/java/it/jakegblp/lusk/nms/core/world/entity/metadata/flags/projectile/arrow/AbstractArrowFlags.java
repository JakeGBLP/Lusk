package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.projectile.arrow;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import org.bukkit.entity.AbstractArrow;

public class AbstractArrowFlags extends FlagByte<AbstractArrowFlag, AbstractArrow, Boolean> implements Copyable<AbstractArrowFlags> {

    public AbstractArrowFlags() {
        super();
    }

    public AbstractArrowFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<AbstractArrowFlag> getBitFlagClass() {
        return AbstractArrowFlag.class;
    }

    @Override
    public Class<AbstractArrow> getFlagHolderClass() {
        return AbstractArrow.class;
    }

    public boolean isCritical() {
        return get(AbstractArrowFlag.IS_CRITICAL);
    }

    public void setCritical(boolean value) {
        set(AbstractArrowFlag.IS_CRITICAL, value);
    }

    public boolean isNoClip() {
        return get(AbstractArrowFlag.NO_CLIP);
    }

    public void setNoClip(boolean value) {
        set(AbstractArrowFlag.NO_CLIP, value);
    }

    @Override
    public AbstractArrowFlags copy() {
        return new AbstractArrowFlags(flags);
    }
}