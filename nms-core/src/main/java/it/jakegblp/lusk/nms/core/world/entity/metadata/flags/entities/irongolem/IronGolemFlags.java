package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.irongolem;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BooleanFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.Flag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import org.bukkit.entity.IronGolem;

public class IronGolemFlags extends FlagByte<IronGolem> implements Copyable<IronGolemFlags> {
    public static final BooleanFlag<IronGolem> IS_PLAYER_CREATED = Flag.booleanFlag("IS_PLAYER_CREATED", IronGolem::isPlayerCreated, IronGolem::setPlayerCreated);

    @SuppressWarnings("unchecked")
    private static final Flag<IronGolem, ?, ?>[] FLAGS = new Flag[] {IS_PLAYER_CREATED};

    public static Flag<IronGolem, ?, ?>[] getFlags() {
        return FLAGS.clone();
    }

    @Override
    public Flag<IronGolem, ?, ?>[] getConstants() {
        return getFlags();
    }

    public IronGolemFlags(IronGolem entity) {
        super(entity);
    }

    public IronGolemFlags() {
        super();
    }

    public IronGolemFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<IronGolem> getFlagHolderClass() {
        return IronGolem.class;
    }

    public boolean isPlayerCreated() {
        return get(IS_PLAYER_CREATED);
    }

    public void setPlayerCreated(boolean value) {
        set(IS_PLAYER_CREATED, value);
    }

    @Override
    public IronGolemFlags copy() {
        return new IronGolemFlags(flags);
    }
}