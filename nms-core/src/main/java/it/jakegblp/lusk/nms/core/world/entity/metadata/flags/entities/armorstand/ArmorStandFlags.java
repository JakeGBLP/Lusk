package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.armorstand;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BooleanFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.Flag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import org.bukkit.entity.ArmorStand;

public class ArmorStandFlags extends FlagByte<ArmorStand> implements Copyable<ArmorStandFlags> {
    public static final BooleanFlag<ArmorStand> IS_SMALL = Flag.booleanFlag("IS_SMALL", ArmorStand::isSmall, ArmorStand::setSmall);
    public static final BooleanFlag<ArmorStand> UNUSED = Flag.unusedBooleanFlag("UNUSED");
    public static final BooleanFlag<ArmorStand> HAS_ARMS = Flag.booleanFlag("HAS_ARMS", ArmorStand::hasArms, ArmorStand::setArms);
    public static final BooleanFlag<ArmorStand> HAS_NO_BASEPLATE = Flag.invertedBooleanFlag("HAS_NO_BASEPLATE", ArmorStand::hasBasePlate, ArmorStand::setBasePlate);
    public static final BooleanFlag<ArmorStand> IS_MARKER = Flag.booleanFlag("IS_MARKER", ArmorStand::isMarker, ArmorStand::setMarker);

    @SuppressWarnings("unchecked")
    private static final Flag<ArmorStand, ?, ?>[] FLAGS = new Flag[] {IS_SMALL, UNUSED, HAS_ARMS, HAS_NO_BASEPLATE, IS_MARKER};

    public static Flag<ArmorStand, ?, ?>[] getFlags() {
        return FLAGS.clone();
    }

    @Override
    public Flag<ArmorStand, ?, ?>[] getConstants() {
        return getFlags();
    }

    public ArmorStandFlags(ArmorStand entity) {
        super(entity);
    }

    public ArmorStandFlags() {
        super();
    }

    public ArmorStandFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<ArmorStand> getFlagHolderClass() {
        return ArmorStand.class;
    }

    public boolean isSmall() {
        return get(IS_SMALL);
    }

    public void setSmall(boolean value) {
        set(IS_SMALL, value);
    }

    public boolean hasArms() {
        return get(HAS_ARMS);
    }

    public void setArms(boolean value) {
        set(HAS_ARMS, value);
    }

    public boolean hasNoBasePlate() {
        return get(HAS_NO_BASEPLATE);
    }

    public void setNoBasePlate(boolean value) {
        set(HAS_NO_BASEPLATE, value);
    }

    public boolean isMarker() {
        return get(IS_MARKER);
    }

    public void setMarker(boolean value) {
        set(IS_MARKER, value);
    }

    @Override
    public ArmorStandFlags copy() {
        return new ArmorStandFlags(this.flags);
    }
}
