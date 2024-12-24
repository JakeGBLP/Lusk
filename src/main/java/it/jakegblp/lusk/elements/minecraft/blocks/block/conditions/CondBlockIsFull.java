package it.jakegblp.lusk.elements.minecraft.blocks.block.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.BlockWrapper;
import org.jetbrains.annotations.NotNull;

@Name("Block - is Full Block")
@Description("Checks if a block is a full block (like dirt, stone, endstone).\nEssentially checks if a block is 1x1x1 and only has one mesh.")
@Examples({"if event-block is a full block:"})
@Since("1.2.1, 1.3 (BlockStates)")
public class CondBlockIsFull extends PropertyCondition<Object> {
    static {
        register(CondBlockIsFull.class, "[a] full block[s]", "blocks/blockstates");
    }

    @Override
    public boolean check(Object o) {
        return new BlockWrapper(o).isFull();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "full block";
    }
}
