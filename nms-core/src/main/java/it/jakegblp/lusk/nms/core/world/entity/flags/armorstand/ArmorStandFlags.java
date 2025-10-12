package it.jakegblp.lusk.nms.core.world.entity.flags.armorstand;

import it.jakegblp.lusk.nms.core.world.entity.FlagByte;
import org.bukkit.entity.ArmorStand;

import java.util.Collection;

public class ArmorStandFlags extends FlagByte<ArmorStandFlag, ArmorStand, Boolean> implements Cloneable {

    public ArmorStandFlags() {
        super();
    }

    public ArmorStandFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<ArmorStandFlag> getBitFlagClass() {
        return ArmorStandFlag.class;
    }

    @Override
    public Class<ArmorStand> getFlagHolderClass() {
        return ArmorStand.class;
    }

    public boolean isSmall() {
        return get(ArmorStandFlag.IS_SMALL);
    }

    public void setSmall(boolean value) {
        set(ArmorStandFlag.IS_SMALL, value);
    }

    public boolean hasUnused() {
        return get(ArmorStandFlag.UNUSED);
    }

    public void setUnused(boolean value) {
        set(ArmorStandFlag.UNUSED, value);
    }

    public boolean hasArms() {
        return get(ArmorStandFlag.HAS_ARMS);
    }

    public void setArms(boolean value) {
        set(ArmorStandFlag.HAS_ARMS, value);
    }

    public boolean hasNoBasePlate() {
        return get(ArmorStandFlag.HAS_NO_BASEPLATE);
    }

    public void setNoBasePlate(boolean value) {
        set(ArmorStandFlag.HAS_NO_BASEPLATE, value);
    }

    public boolean isMarker() {
        return get(ArmorStandFlag.IS_MARKER);
    }

    public void setMarker(boolean value) {
        set(ArmorStandFlag.IS_MARKER, value);
    }

    @Override
    public ArmorStandFlags clone() {
        return new ArmorStandFlags(this.flags);
    }
}
