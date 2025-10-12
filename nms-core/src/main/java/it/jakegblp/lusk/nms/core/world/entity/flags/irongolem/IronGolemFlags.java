package it.jakegblp.lusk.nms.core.world.entity.flags.irongolem;

import it.jakegblp.lusk.nms.core.world.entity.FlagByte;
import org.bukkit.entity.IronGolem;

import java.util.Collection;

public class IronGolemFlags extends FlagByte<IronGolemFlag, IronGolem, Boolean> {

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
    public IronGolemFlags clone() {
        return new IronGolemFlags(flags);
    }
}