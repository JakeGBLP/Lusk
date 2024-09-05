package it.jakegblp.lusk.elements.minecraft.blocks.block.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.BlockUtils.isFullBlock;

@Name("Block - is Full Block")
@Description("Checks if a block is a full block (like dirt, stone, endstone).\nEssentially checks if a block is 1x1x1 and only has one mesh.")
@Examples({"if event-block is a full block:"})
@Since("1.2.1-beta1")
public class CondFullBlock extends PropertyCondition<Block> {
    static {
        register(CondFullBlock.class, "[a] full block[s]", "blocks");
    }

    @Override
    public boolean check(Block block) {
        return isFullBlock(block);
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "full block";
    }
}
