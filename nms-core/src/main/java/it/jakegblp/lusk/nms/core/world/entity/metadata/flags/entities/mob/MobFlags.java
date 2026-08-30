package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.mob;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BooleanFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.Flag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;


public class MobFlags extends FlagByte<Mob> implements Copyable<MobFlags> {
    public static final BooleanFlag<Mob> NO_AI = Flag.invertedBooleanFlag("NO_AI", LivingEntity::hasAI, LivingEntity::setAI);
    public static final BooleanFlag<Mob> IS_LEFT_HANDED = Flag.invertedBooleanFlag("IS_LEFT_HANDED", Mob::isLeftHanded, Mob::setLeftHanded);
    public static final BooleanFlag<Mob> IS_AGGRESSIVE = Flag.invertedBooleanFlag("IS_AGGRESSIVE", Mob::isAggressive, Mob::setAggressive);

    @SuppressWarnings("unchecked")
    private static final Flag<Mob, ?, ?>[] FLAGS = new Flag[] {NO_AI, IS_LEFT_HANDED, IS_AGGRESSIVE};

    public static Flag<Mob, ?, ?>[] getFlags() {
        return FLAGS.clone();
    }

    @Override
    public Flag<Mob, ?, ?>[] getConstants() {
        return getFlags();
    }

    public MobFlags(Mob entity) {
        super(entity);
    }

    public MobFlags() {
        super();
    }

    public MobFlags(byte flags) {
        super(flags);
    }
    @Override
    public Class<Mob> getFlagHolderClass() {
        return Mob.class;
    }

    public boolean hasNoAI() {
        return get(NO_AI);
    }

    public void setNoAI(boolean value) {
        set(NO_AI, value);
    }

    public boolean isLeftHanded() {
        return get(IS_LEFT_HANDED);
    }

    public void setLeftHanded(boolean value) {
        set(IS_LEFT_HANDED, value);
    }

    public boolean isAggressive() {
        return get(IS_AGGRESSIVE);
    }

    public void setAggressive(boolean value) {
        set(IS_AGGRESSIVE, value);
    }

    @Override
    public MobFlags copy() {
        return new MobFlags(flags);
    }
}