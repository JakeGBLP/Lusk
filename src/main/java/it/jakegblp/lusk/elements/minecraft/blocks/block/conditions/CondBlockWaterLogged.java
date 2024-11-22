package it.jakegblp.lusk.elements.minecraft.blocks.block.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.wrappers.BlockDataWrapper;
import org.jetbrains.annotations.NotNull;

@Name("Block - is Waterlogged")
@Description("Checks if a block is waterlogged.")
@Examples({"if event-block is waterlogged:\n\tbroadcast \"%event-block% is waterlogged!\""})
@Since("1.3")
@SuppressWarnings("unused")
public class CondBlockWaterLogged extends PropertyCondition<Object> {
    static {
        register(CondBlockWaterLogged.class, "water[ |-]logged", "blocks/blockstates");
    }

    @Override
    public boolean check(Object o) {
        BlockDataWrapper blockDataWrapper = BlockDataWrapper.create(o);
        if (blockDataWrapper != null) {
            return blockDataWrapper.isWaterLogged();
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "waterlogged";
    }
}