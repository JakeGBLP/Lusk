package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.bee;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.EntityUtils;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BooleanFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.Flag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import org.bukkit.entity.Bee;

public class BeeFlags extends FlagByte<Bee> implements Copyable<BeeFlags> {
    public static final BooleanFlag<Bee> UNUSED = Flag.unusedBooleanFlag("UNUSED");
    public static final BooleanFlag<Bee> IS_ANGRY = Flag.booleanFlag("IS_ANGRY", Bee::isRolling, EntityUtils::setRolling);
    public static final BooleanFlag<Bee> HAS_STUNG = Flag.booleanFlag("HAS_STUNG", Bee::hasStung, Bee::setHasStung);
    public static final BooleanFlag<Bee> HAS_NECTAR = Flag.booleanFlag("HAS_NECTAR", Bee::hasNectar, Bee::setHasNectar);

    @SuppressWarnings("unchecked")
    private static final Flag<Bee, ?, ?>[] FLAGS = new Flag[] {UNUSED, IS_ANGRY, HAS_STUNG, HAS_NECTAR};

    public static Flag<Bee, ?, ?>[] getFlags() {
        return FLAGS.clone();
    }

    @Override
    public Flag<Bee, ?, ?>[] getConstants() {
        return getFlags();
    }

    public BeeFlags(Bee entity) {
        super(entity);
    }

    public BeeFlags() {
        super();
    }

    public BeeFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<Bee> getFlagHolderClass() {
        return Bee.class;
    }

    public boolean isAngry() {
        return get(IS_ANGRY);
    }

    public void setAngry(boolean value) {
        set(IS_ANGRY, value);
    }

    public boolean hasStung() {
        return get(HAS_STUNG);
    }

    public void setStung(boolean value) {
        set(HAS_STUNG, value);
    }

    public boolean hasNectar() {
        return get(HAS_NECTAR);
    }

    public void setNectar(boolean value) {
        set(HAS_NECTAR, value);
    }

    @Override
    public BeeFlags copy() {
        return new BeeFlags(flags);
    }
}