package it.jakegblp.lusk.nms.core.world.entity.flags.fox;

import it.jakegblp.lusk.nms.core.world.entity.FlagByte;
import org.bukkit.entity.Fox;

public class FoxFlags extends FlagByte<FoxFlag, Fox, Boolean> {

    public FoxFlags() {
        super();
    }

    public FoxFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<FoxFlag> getBitFlagClass() {
        return FoxFlag.class;
    }

    @Override
    public Class<Fox> getFlagHolderClass() {
        return Fox.class;
    }

    public boolean isSitting() {
        return get(FoxFlag.IS_SITTING);
    }

    public void setSitting(boolean value) {
        set(FoxFlag.IS_SITTING, value);
    }

    public boolean hasUnused() {
        return get(FoxFlag.UNUSED);
    }

    public void setUnused(boolean value) {
        set(FoxFlag.UNUSED, value);
    }

    public boolean isCrouching() {
        return get(FoxFlag.IS_CROUCHING);
    }

    public void setCrouching(boolean value) {
        set(FoxFlag.IS_CROUCHING, value);
    }

    public boolean isInterested() {
        return get(FoxFlag.IS_INTERESTED);
    }

    public void setInterested(boolean value) {
        set(FoxFlag.IS_INTERESTED, value);
    }

    public boolean isPouncing() {
        return get(FoxFlag.IS_POUNCING);
    }

    public void setPouncing(boolean value) {
        set(FoxFlag.IS_POUNCING, value);
    }

    public boolean isSleeping() {
        return get(FoxFlag.IS_SLEEPING);
    }

    public void setSleeping(boolean value) {
        set(FoxFlag.IS_SLEEPING, value);
    }

    public boolean isFaceplanted() {
        return get(FoxFlag.IS_FACEPLANTED);
    }

    public void setFaceplanted(boolean value) {
        set(FoxFlag.IS_FACEPLANTED, value);
    }

    public boolean isDefending() {
        return get(FoxFlag.IS_DEFENDING);
    }

    public void setDefending(boolean value) {
        set(FoxFlag.IS_DEFENDING, value);
    }

    @Override
    public FoxFlags clone() {
        return new FoxFlags(flags);
    }
}