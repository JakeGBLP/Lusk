package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.irongolem;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import org.bukkit.entity.IronGolem;

public class IronGolemFlags extends FlagByte<IronGolemFlag, IronGolem, Boolean> implements Copyable<IronGolemFlags> {

    public IronGolemFlags() {
        super();
    }

    public IronGolemFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<IronGolemFlag> getBitFlagClass() {
        return IronGolemFlag.class;
    }

    @Override
    public Class<IronGolem> getFlagHolderClass() {
        return IronGolem.class;
    }

    public boolean isPlayerCreated() {
        return get(IronGolemFlag.IS_PLAYER_CREATED);
    }

    public void setPlayerCreated(boolean value) {
        set(IronGolemFlag.IS_PLAYER_CREATED, value);
    }

    @Override
    public IronGolemFlags copy() {
        return new IronGolemFlags(flags);
    }
}