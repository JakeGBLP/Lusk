package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.sheep;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import org.bukkit.DyeColor;
import org.bukkit.entity.Sheep;

public class SheepFlags extends FlagByte<SheepFlag, Sheep, Object> implements Copyable<SheepFlags> {

    public SheepFlags() {
        super();
    }

    public SheepFlags(byte value) {
        super(value);
    }

    @Override
    public Class<SheepFlag> getBitFlagClass() {
        return SheepFlag.class;
    }

    @Override
    public Class<Sheep> getFlagHolderClass() {
        return Sheep.class;
    }

    public DyeColor getColor() {
        return (DyeColor) get(SheepFlag.WOOL_COLOR);
    }

    public void setColor(DyeColor color) {
        set(SheepFlag.WOOL_COLOR, color);
    }

    public boolean isSheared() {
        return (boolean) get(SheepFlag.IS_SHEARED);
    }

    public void setSheared(boolean value) {
        set(SheepFlag.IS_SHEARED, value);
    }

    @Override
    public SheepFlags copy() {
        return new SheepFlags(flags);
    }
}
