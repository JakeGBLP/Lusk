package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.blaze;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import org.bukkit.entity.Blaze;

public class BlazeFlags extends FlagByte<BlazeFlag, Blaze, Boolean> implements Copyable<BlazeFlags> {

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
    public BlazeFlags copy() {
        return new BlazeFlags(flags);
    }
}