package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.fox;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BooleanFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.Flag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Sittable;

public class FoxFlags extends FlagByte<Fox> implements Copyable<FoxFlags> {
    public static final BooleanFlag<Fox> IS_SITTING = Flag.booleanFlag("IS_SITTING", Sittable::isSitting, Sittable::setSitting);
    public static final BooleanFlag<Fox> UNUSED = Flag.unusedBooleanFlag("UNUSED");
    public static final BooleanFlag<Fox> IS_CROUCHING = Flag.booleanFlag("IS_CROUCHING", Fox::isCrouching, Fox::setCrouching);
    public static final BooleanFlag<Fox> IS_INTERESTED = Flag.booleanFlag("IS_INTERESTED", Fox::isInterested, Fox::setInterested);
    public static final BooleanFlag<Fox> IS_POUNCING = Flag.booleanFlag("IS_POUNCING", Fox::isLeaping, Fox::setLeaping);
    public static final BooleanFlag<Fox> IS_SLEEPING = Flag.booleanFlag("IS_SLEEPING", Fox::isSleeping, Fox::setSleeping);
    public static final BooleanFlag<Fox> IS_FACEPLANTED = Flag.booleanFlag("IS_FACEPLANTED", Fox::isFaceplanted, Fox::setFaceplanted);
    public static final BooleanFlag<Fox> IS_DEFENDING = Flag.booleanFlag("IS_DEFENDING", Fox::isDefending, Fox::setDefending);

    @SuppressWarnings("unchecked")
    private static final Flag<Fox, ?, ?>[] FLAGS = new Flag[] {IS_SITTING, UNUSED, IS_CROUCHING, IS_INTERESTED, IS_POUNCING, IS_SLEEPING, IS_FACEPLANTED, IS_DEFENDING};

    public static Flag<Fox, ?, ?>[] getFlags() {
        return FLAGS.clone();
    }

    @Override
    public Flag<Fox, ?, ?>[] getConstants() {
        return getFlags();
    }

    public FoxFlags() {
        super();
    }

    public FoxFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<Fox> getFlagHolderClass() {
        return Fox.class;
    }

    public boolean isSitting() {
        return get(IS_SITTING);
    }

    public void setSitting(boolean value) {
        set(IS_SITTING, value);
    }

    public boolean isCrouching() {
        return get(IS_CROUCHING);
    }

    public void setCrouching(boolean value) {
        set(IS_CROUCHING, value);
    }

    public boolean isInterested() {
        return get(IS_INTERESTED);
    }

    public void setInterested(boolean value) {
        set(IS_INTERESTED, value);
    }

    public boolean isPouncing() {
        return get(IS_POUNCING);
    }

    public void setPouncing(boolean value) {
        set(IS_POUNCING, value);
    }

    public boolean isSleeping() {
        return get(IS_SLEEPING);
    }

    public void setSleeping(boolean value) {
        set(IS_SLEEPING, value);
    }

    public boolean isFaceplanted() {
        return get(IS_FACEPLANTED);
    }

    public void setFaceplanted(boolean value) {
        set(IS_FACEPLANTED, value);
    }

    public boolean isDefending() {
        return get(IS_DEFENDING);
    }

    public void setDefending(boolean value) {
        set(IS_DEFENDING, value);
    }

    @Override
    public FoxFlags copy() {
        return new FoxFlags(flags);
    }
}