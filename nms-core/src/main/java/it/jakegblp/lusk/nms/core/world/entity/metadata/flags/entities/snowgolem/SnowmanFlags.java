package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.snowgolem;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BooleanFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.Flag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import org.bukkit.entity.Snowman;

public class SnowmanFlags extends FlagByte<Snowman> implements Copyable<SnowmanFlags> {
    public static final BooleanFlag<Snowman> UNUSED_1 = Flag.unusedBooleanFlag("UNUSED_1");
    public static final BooleanFlag<Snowman> UNUSED_2 = Flag.unusedBooleanFlag("UNUSED_2");
    public static final BooleanFlag<Snowman> UNUSED_3 = Flag.unusedBooleanFlag("UNUSED_3");
    public static final BooleanFlag<Snowman> UNUSED_4 = Flag.unusedBooleanFlag("UNUSED_4");
    public static final BooleanFlag<Snowman> HAS_PUMPKIN = Flag.invertedBooleanFlag("HAS_PUMPKIN", Snowman::isDerp, Snowman::setDerp);

    @SuppressWarnings("unchecked")
    private static final Flag<Snowman, ?, ?>[] FLAGS = new Flag[] {UNUSED_1, UNUSED_2, UNUSED_3, UNUSED_4, HAS_PUMPKIN};

    public static Flag<Snowman, ?, ?>[] getFlags() {
        return FLAGS.clone();
    }

    @Override
    public Flag<Snowman, ?, ?>[] getConstants() {
        return getFlags();
    }

    public SnowmanFlags(Snowman entity) {
        super(entity);
    }

    public SnowmanFlags() {
        super();
    }

    public SnowmanFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<Snowman> getFlagHolderClass() {
        return Snowman.class;
    }

    public boolean hasPumpkinHat() {
        return get(HAS_PUMPKIN);
    }

    public void setPumpkinHat(boolean value) {
        set(HAS_PUMPKIN, value);
    }

    @Override
    public SnowmanFlags copy() {
        return new SnowmanFlags(flags);
    }
}