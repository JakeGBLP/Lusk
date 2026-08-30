package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.panda;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BooleanFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.Flag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import org.bukkit.entity.Panda;

public class PandaFlags extends FlagByte<Panda> implements Copyable<PandaFlags> {
    public static final BooleanFlag<Panda> UNUSED = Flag.unusedBooleanFlag("UNUSED");
    public static final BooleanFlag<Panda> IS_SNEEZING = Flag.booleanFlag("IS_SNEEZING", Panda::isSneezing, Panda::setSneezing);
    public static final BooleanFlag<Panda> IS_ROLLING = Flag.booleanFlag("IS_ROLLING", Panda::isRolling, Panda::setRolling);
    public static final BooleanFlag<Panda> IS_SITTING = Flag.booleanFlag("IS_SITTING", Panda::isSitting, Panda::setSitting);
    public static final BooleanFlag<Panda> IS_ON_BACK = Flag.booleanFlag("IS_ON_BACK", Panda::isOnBack, Panda::setOnBack);

    @SuppressWarnings("unchecked")
    private static final Flag<Panda, ?, ?>[] FLAGS = new Flag[] {UNUSED, IS_SNEEZING, IS_ROLLING, IS_SITTING, IS_ON_BACK};

    public static Flag<Panda, ?, ?>[] getFlags() {
        return FLAGS.clone();
    }

    @Override
    public Flag<Panda, ?, ?>[] getConstants() {
        return getFlags();
    }

    public PandaFlags(Panda entity) {
        super(entity);
    }

    public PandaFlags() {
        super();
    }

    public PandaFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<Panda> getFlagHolderClass() {
        return Panda.class;
    }

    public boolean isSneezing() {
        return get(IS_SNEEZING);
    }

    public void setSneezing(boolean value) {
        set(IS_SNEEZING, value);
    }

    public boolean isRolling() {
        return get(IS_ROLLING);
    }

    public void setRolling(boolean value) {
        set(IS_ROLLING, value);
    }

    public boolean isSitting() {
        return get(IS_SITTING);
    }

    public void setSitting(boolean value) {
        set(IS_SITTING, value);
    }

    public boolean isOnBack() {
        return get(IS_ON_BACK);
    }

    public void setOnBack(boolean value) {
        set(IS_ON_BACK, value);
    }

    @Override
    public PandaFlags copy() {
        return new PandaFlags(flags);
    }
}