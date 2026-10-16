package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.abstracthorse;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.EntityUtils;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BooleanFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.Flag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Tameable;

public class AbstractHorseFlags extends FlagByte<AbstractHorse> implements Copyable<AbstractHorseFlags> {
    public static final BooleanFlag<AbstractHorse> UNUSED_1 = Flag.unusedBooleanFlag("UNUSED_1");
    public static final BooleanFlag<AbstractHorse> IS_TAME = Flag.booleanFlag("IS_TAME", Tameable::isTamed, Tameable::setTamed);
    public static final BooleanFlag<AbstractHorse> UNUSED_2 = Flag.unusedBooleanFlag("UNUSED_2");
    public static final BooleanFlag<AbstractHorse> HAS_BRED = Flag.booleanFlag("HAS_BRED", EntityUtils::hasBred, EntityUtils::setBred);
    public static final BooleanFlag<AbstractHorse> IS_EATING = Flag.booleanFlag("IS_EATING", AbstractHorse::isEatingGrass, AbstractHorse::setEatingGrass);
    public static final BooleanFlag<AbstractHorse> IS_REARING = Flag.booleanFlag("IS_REARING", AbstractHorse::isRearing, AbstractHorse::setRearing);
    public static final BooleanFlag<AbstractHorse> HAS_MOUTH_OPEN = Flag.booleanFlag("HAS_MOUTH_OPEN", AbstractHorse::isEating, AbstractHorse::setEating);

    @SuppressWarnings("unchecked")
    private static final Flag<AbstractHorse, ?, ?>[] FLAGS = new Flag[]{UNUSED_1, IS_TAME, UNUSED_2, HAS_BRED, IS_EATING, IS_REARING, HAS_MOUTH_OPEN};

    public static Flag<AbstractHorse, ?, ?>[] getFlags() {
        return FLAGS.clone();
    }

    public AbstractHorseFlags(AbstractHorse entity) {
        super(entity);
    }

    public AbstractHorseFlags() {
        super();
    }

    public AbstractHorseFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<AbstractHorse> getFlagHolderClass() {
        return AbstractHorse.class;
    }

    @Override
    public Flag<AbstractHorse, ?, ?>[] getConstants() {
        return getFlags();
    }

    public boolean isTame() {
        return get(IS_TAME);
    }

    public void setTame(boolean value) {
        set(IS_TAME, value);
    }

    public boolean hasBred() {
        return get(HAS_BRED);
    }

    public void setBred(boolean value) {
        set(HAS_BRED, value);
    }

    public boolean isEating() {
        return get(IS_EATING);
    }

    public void setEating(boolean value) {
        set(IS_EATING, value);
    }

    public boolean isRearing() {
        return get(IS_REARING);
    }

    public void setRearing(boolean value) {
        set(IS_REARING, value);
    }

    public boolean hasMouthOpen() {
        return get(HAS_MOUTH_OPEN);
    }

    public void setMouthOpen(boolean value) {
        set(HAS_MOUTH_OPEN, value);
    }

    @Override
    public AbstractHorseFlags copy() {
        return new AbstractHorseFlags(this.flags);
    }
}
