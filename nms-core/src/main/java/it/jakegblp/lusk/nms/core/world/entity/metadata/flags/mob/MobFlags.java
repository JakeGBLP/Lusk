package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.mob;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import org.bukkit.entity.Mob;


public class MobFlags extends FlagByte<MobFlag, Mob, Boolean> implements Copyable<MobFlags> {

    public MobFlags() {
        super();
    }

    public MobFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<MobFlag> getBitFlagClass() {
        return MobFlag.class;
    }

    @Override
    public Class<Mob> getFlagHolderClass() {
        return Mob.class;
    }

    public boolean hasNoAI() {
        return get(MobFlag.NO_AI);
    }

    public void setNoAI(boolean value) {
        set(MobFlag.NO_AI, value);
    }

    public boolean isLeftHanded() {
        return get(MobFlag.IS_LEFT_HANDED);
    }

    public void setLeftHanded(boolean value) {
        set(MobFlag.IS_LEFT_HANDED, value);
    }

    public boolean isAggressive() {
        return get(MobFlag.IS_AGGRESSIVE);
    }

    public void setAggressive(boolean value) {
        set(MobFlag.IS_AGGRESSIVE, value);
    }

    @Override
    public MobFlags copy() {
        return new MobFlags(flags);
    }
}