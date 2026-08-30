package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.blaze;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.EntityUtils;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BooleanFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.Flag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import org.bukkit.entity.Blaze;

public class BlazeFlags extends FlagByte<Blaze> implements Copyable<BlazeFlags> {
    public static final BooleanFlag<Blaze> ON_FIRE = Flag.booleanFlag("ON_FIRE", EntityUtils::isBurning, EntityUtils::setBurning);

    @SuppressWarnings("unchecked")
    private static final Flag<Blaze, ?, ?>[] FLAGS = new Flag[] {ON_FIRE};

    public static Flag<Blaze, ?, ?>[] getFlags() {
        return FLAGS.clone();
    }

    @Override
    public Flag<Blaze, ?, ?>[] getConstants() {
        return getFlags();
    }

    public BlazeFlags(Blaze entity) {
        super(entity);
    }

    public BlazeFlags() {
        super();
    }

    public BlazeFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<Blaze> getFlagHolderClass() {
        return Blaze.class;
    }

    public boolean isOnFire() {
        return get(BlazeFlags.ON_FIRE);
    }

    public void setOnFire(boolean value) {
        set(BlazeFlags.ON_FIRE, value);
    }

    @Override
    public BlazeFlags copy() {
        return new BlazeFlags(flags);
    }
}