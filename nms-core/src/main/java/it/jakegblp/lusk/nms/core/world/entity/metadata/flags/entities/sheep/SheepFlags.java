package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.sheep;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BooleanFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.Flag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.MappedIntFlag;
import org.bukkit.DyeColor;
import org.bukkit.entity.Sheep;

public class SheepFlags extends FlagByte<Sheep> implements Copyable<SheepFlags> {

    @SuppressWarnings({"ConstantConditions", "UnstableApiUsage"})
    public static final MappedIntFlag<Sheep, DyeColor> WOOL_COLOR = Flag.mappedIntFlag("wool_color", 4, DyeColor.class,
            Sheep::getColor,
            Sheep::setColor,
            color -> (int) color.getWoolData(),
            bits -> DyeColor.getByWoolData((byte) bits),
            DyeColor.WHITE
    );
    public static final BooleanFlag<Sheep> IS_SHEARED = Flag.booleanFlag("IS_SHEARED", Sheep::isSheared, Sheep::setSheared);

    @SuppressWarnings("unchecked")
    private static final Flag<Sheep, ?, ?>[] FLAGS = new Flag[] {WOOL_COLOR, IS_SHEARED};

    public static Flag<Sheep, ?, ?>[] getFlags() {
        return FLAGS.clone();
    }

    @Override
    public Flag<Sheep, ?, ?>[] getConstants() {
        return getFlags();
    }

    public SheepFlags(Sheep entity) {
        super(entity);
    }

    public SheepFlags() {
        super();
    }

    public SheepFlags(byte value) {
        super(value);
    }

    @Override
    public Class<Sheep> getFlagHolderClass() {
        return Sheep.class;
    }

    public DyeColor getColor() {
        return get(WOOL_COLOR);
    }

    public void setColor(DyeColor color) {
        set(WOOL_COLOR, color);
    }

    public boolean isSheared() {
        return get(IS_SHEARED);
    }

    public void setSheared(boolean value) {
        set(IS_SHEARED, value);
    }

    @Override
    public SheepFlags copy() {
        return new SheepFlags(flags);
    }
}
