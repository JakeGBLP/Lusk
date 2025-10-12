package it.jakegblp.lusk.nms.core.world.entity.flags.blaze;

import it.jakegblp.lusk.nms.core.world.entity.FlagByte;
import org.bukkit.entity.Blaze;

public class BlazeFlags extends FlagByte<BlazeFlag, Blaze, Boolean> {

    public BlazeFlags() {
        super();
    }

    public BlazeFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<BlazeFlag> getBitFlagClass() {
        return BlazeFlag.class;
    }

    @Override
    public Class<Blaze> getFlagHolderClass() {
        return Blaze.class;
    }

    public boolean isOnFire() {
        return get(BlazeFlag.ON_FIRE);
    }

    public void setOnFire(boolean value) {
        set(BlazeFlag.ON_FIRE, value);
    }

    @Override
    public BlazeFlags clone() {
        return new BlazeFlags(flags);
    }
}