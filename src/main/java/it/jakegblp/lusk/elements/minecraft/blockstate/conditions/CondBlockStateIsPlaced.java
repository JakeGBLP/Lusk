package it.jakegblp.lusk.elements.minecraft.blockstate.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.block.BlockState;

@Name("BlockState - is Placed")
@Description("""
        Checks whether the provided blockstates are placed in a world, if not, they are 'virtual' (e. g. on an item).
        Some syntaxes will not work if the block state isn't placed in the world.""")
@Examples("if {_blockState} is placed:")
@Since("1.3")
public class CondBlockStateIsPlaced extends PropertyCondition<BlockState> {

    static {
        register(CondBlockStateIsPlaced.class, "placed", "blockstates");
    }

    @Override
    public boolean check(BlockState value) {
        return value.isPlaced();
    }

    @Override
    protected String getPropertyName() {
        return "placed";
    }
}
