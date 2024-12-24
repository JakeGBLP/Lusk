package it.jakegblp.lusk.elements.minecraft.blocks.block.expressions;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import it.jakegblp.lusk.api.BlockWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Block/BlockData/BlockState/Item - Slipperiness")
@Description("""
        Returns a value that represents how 'slippery' the block is.
        Blocks with higher slipperiness, like ice can be slid on further by the player and other entities.
        Most blocks have a default slipperiness of 0.6.
        
        Can be used with items, blocks, blockstates, and blockdatas.
        """)
@Examples({"broadcast slipperiness of packed ice"})
@Since("1.0.0, 1.3 (Plural, Block, BlockData, BlockState)")
@DocumentationId("8823")
public class ExprBlockSlipperiness extends SimplePropertyExpression<Object, Float> {
    static {
        register(ExprBlockSlipperiness.class, Float.class, "slipperiness", "itemtypes/blocks/blockdatas/blockstates");
    }

    @Override
    public @NotNull Class<? extends Float> getReturnType() {
        return Float.class;
    }

    @Override
    @Nullable
    public Float convert(Object object) {
        return new BlockWrapper(object).getSlipperiness();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "slipperiness";
    }
}