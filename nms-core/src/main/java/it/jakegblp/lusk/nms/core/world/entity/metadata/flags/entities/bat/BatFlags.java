package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.bat;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BooleanFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.Flag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import org.bukkit.entity.Bat;

public class BatFlags extends FlagByte<Bat> implements Copyable<BatFlags> {
    public static final BooleanFlag<Bat> IS_HANGING = Flag.invertedBooleanFlag("IS_HANGING", Bat::isAwake, Bat::setAwake);

    @SuppressWarnings("unchecked")
    private static final Flag<Bat, ?, ?>[] FLAGS = new Flag[] {IS_HANGING};

    public static Flag<Bat, ?, ?>[] getFlags() {
        return FLAGS.clone();
    }

    @Override
    public Flag<Bat, ?, ?>[] getConstants() {
        return getFlags();
    }

    public BatFlags(Bat entity) {
        super(entity);
    }

    public BatFlags() {
        super();
    }

    public BatFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<Bat> getFlagHolderClass() {
        return Bat.class;
    }

    public boolean isHanging() {
        return get(IS_HANGING);
    }

    public void setHanging(boolean value) {
        set(IS_HANGING, value);
    }

    @Override
    public BatFlags copy() {
        return new BatFlags(flags);
    }
}