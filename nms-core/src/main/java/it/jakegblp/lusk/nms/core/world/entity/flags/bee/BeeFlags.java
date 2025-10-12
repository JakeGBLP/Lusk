package it.jakegblp.lusk.nms.core.world.entity.flags.bee;

import it.jakegblp.lusk.nms.core.world.entity.FlagByte;
import org.bukkit.entity.Bee;

import java.util.Collection;

public class BeeFlags extends FlagByte<BeeFlag, Bee, Boolean> {

    public BeeFlags() {
        super();
    }

    public BeeFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<BeeFlag> getBitFlagClass() {
        return BeeFlag.class;
    }

    @Override
    public Class<Bee> getFlagHolderClass() {
        return Bee.class;
    }

    public boolean hasUnused() {
        return get(BeeFlag.UNUSED);
    }

    public void setUnused(boolean value) {
        set(BeeFlag.UNUSED, value);
    }

    public boolean isAngry() {
        return get(BeeFlag.IS_ANGRY);
    }

    public void setAngry(boolean value) {
        set(BeeFlag.IS_ANGRY, value);
    }

    public boolean hasStung() {
        return get(BeeFlag.HAS_STUNG);
    }

    public void setStung(boolean value) {
        set(BeeFlag.HAS_STUNG, value);
    }

    public boolean hasNectar() {
        return get(BeeFlag.HAS_NECTAR);
    }

    public void setNectar(boolean value) {
        set(BeeFlag.HAS_NECTAR, value);
    }

    @Override
    public BeeFlags clone() {
        return new BeeFlags(flags);
    }
}