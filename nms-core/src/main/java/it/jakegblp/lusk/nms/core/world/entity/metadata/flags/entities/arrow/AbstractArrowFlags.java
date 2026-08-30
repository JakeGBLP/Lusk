package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.arrow;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BooleanFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.Flag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import lombok.NoArgsConstructor;
import org.bukkit.entity.AbstractArrow;
import org.jspecify.annotations.NullMarked;

@NullMarked
@NoArgsConstructor
public class AbstractArrowFlags extends FlagByte<AbstractArrow> implements Copyable<AbstractArrowFlags> {

    public static final BooleanFlag<AbstractArrow> IS_CRITICAL = Flag.booleanFlag("IS_CRITICAL", AbstractArrow::isCritical, AbstractArrow::setCritical);
    public static final BooleanFlag<AbstractArrow> NO_CLIP = Flag.booleanFlag("NO_CLIP", AbstractArrow::hasNoPhysics, AbstractArrow::setNoPhysics);

    @SuppressWarnings("unchecked")
    private static final Flag<AbstractArrow, ?, ?>[] FLAGS = new Flag[] {IS_CRITICAL, NO_CLIP};

    public static Flag<AbstractArrow, ?, ?>[] getFlags() {
        return FLAGS.clone();
    }

    public AbstractArrowFlags(AbstractArrow entity) {
        super(entity);
    }

    public AbstractArrowFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<AbstractArrow> getFlagHolderClass() {
        return AbstractArrow.class;
    }

    @Override
    public Flag<AbstractArrow, ?, ?>[] getConstants() {
        return getFlags();
    }

    public boolean isCritical() {
        return get(IS_CRITICAL);
    }

    public void setCritical(boolean value) {
        set(IS_CRITICAL, value);
    }

    public boolean isNoClip() {
        return get(NO_CLIP);
    }

    public void setNoClip(boolean value) {
        set(NO_CLIP, value);
    }

    @Override
    public AbstractArrowFlags copy() {
        return new AbstractArrowFlags(flags);
    }
}