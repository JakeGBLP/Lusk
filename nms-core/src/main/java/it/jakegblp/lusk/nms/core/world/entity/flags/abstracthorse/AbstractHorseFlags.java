package it.jakegblp.lusk.nms.core.world.entity.flags.abstracthorse;

import it.jakegblp.lusk.nms.core.world.entity.FlagByte;
import org.bukkit.entity.AbstractHorse;

import java.util.Collection;

public class AbstractHorseFlags extends FlagByte<AbstractHorseFlag, AbstractHorse, Boolean> implements Cloneable {

    public AbstractHorseFlags() {
        super();
    }

    public AbstractHorseFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<AbstractHorseFlag> getBitFlagClass() {
        return AbstractHorseFlag.class;
    }

    @Override
    public Class<AbstractHorse> getFlagHolderClass() {
        return AbstractHorse.class;
    }

    public boolean hasUnused1() {
        return get(AbstractHorseFlag.UNUSED_1);
    }

    public void setUnused1(boolean value) {
        set(AbstractHorseFlag.UNUSED_1, value);
    }

    public boolean isTame() {
        return get(AbstractHorseFlag.IS_TAME);
    }

    public void setTame(boolean value) {
        set(AbstractHorseFlag.IS_TAME, value);
    }

    public boolean hasUnused2() {
        return get(AbstractHorseFlag.UNUSED_2);
    }

    public void setUnused2(boolean value) {
        set(AbstractHorseFlag.UNUSED_2, value);
    }

    public boolean hasBred() {
        return get(AbstractHorseFlag.HAS_BRED);
    }

    public void setBred(boolean value) {
        set(AbstractHorseFlag.HAS_BRED, value);
    }

    public boolean isEating() {
        return get(AbstractHorseFlag.IS_EATING);
    }

    public void setEating(boolean value) {
        set(AbstractHorseFlag.IS_EATING, value);
    }

    public boolean isRearing() {
        return get(AbstractHorseFlag.IS_REARING);
    }

    public void setRearing(boolean value) {
        set(AbstractHorseFlag.IS_REARING, value);
    }

    public boolean hasMouthOpen() {
        return get(AbstractHorseFlag.HAS_MOUTH_OPEN);
    }

    public void setMouthOpen(boolean value) {
        set(AbstractHorseFlag.HAS_MOUTH_OPEN, value);
    }

    @Override
    public AbstractHorseFlags clone() {
        return new AbstractHorseFlags(this.flags);
    }
}
