package it.jakegblp.lusk.nms.core.world.entity.flags.vex;

import it.jakegblp.lusk.nms.core.world.entity.FlagByte;
import org.bukkit.entity.Vex;

public class VexFlags extends FlagByte<VexFlag, Vex, Boolean> {

    public VexFlags() {
        super();
    }

    public VexFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<VexFlag> getBitFlagClass() {
        return VexFlag.class;
    }

    @Override
    public Class<Vex> getFlagHolderClass() {
        return Vex.class;
    }

    public boolean isAttacking() {
        return get(VexFlag.IS_ATTACKING);
    }

    public void setAttacking(boolean value) {
        set(VexFlag.IS_ATTACKING, value);
    }

    @Override
    public VexFlags clone() {
        return new VexFlags(flags);
    }
}