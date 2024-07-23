package it.jakegblp.lusk.elements.minecraft.blockface.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

@Name("Blockface - is Cartesian")
@Description("Returns true if the provided blockfaces are aligned with one of the unit axes in 3D Cartesian space (NORTH, SOUTH, EAST, WEST, UP, DOWN).")
@Examples("if {_blockface} is cartesian")
@Since("1.2")
public class CondBlockFaceCartesian extends PropertyCondition<BlockFace> {
    static {
        register(CondBlockFaceCartesian.class, "cartesian", "blockfaces");
    }

    @Override
    public boolean check(BlockFace blockFace) {
        return blockFace.isCartesian();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "cartesian";
    }
}