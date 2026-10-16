package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Block/BlockData/BlockState/Item - Slipperiness")
@Description("""
        Returns a value that represents how 'slippery' the block is.
        Blocks with higher slipperiness, like ice can be slid on further by the player and other entities.
        Most blocks have a default slipperiness of 0.6.
        
        Can be used with items, blocks, blockstates, and blockdatas.
        """)
@Example("broadcast slipperiness of packed ice")
@Since("1.0.0, 1.3 (Plural, Block, BlockData, BlockState)")
public class ExprBlockSlipperiness extends SimplePropertyExpression<Object, Float> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprBlockSlipperiness.class, ExprBlockSlipperiness::new, Float.class)
                .propertyOf("[block] slipperiness", "itemtypes/blocks/blockdatas/blockstates")
                .register(syntaxRegistry);
    }

    @Override
    public @NotNull Class<? extends Float> getReturnType() {
        return Float.class;
    }

    @Override
    @Nullable
    public Float convert(Object object) {
        return switch (object) {
            case ItemType itemType -> itemType.hasBlockType() ? itemType.getBlockType().getSlipperiness() : null;
            case Block block -> block.getType().getSlipperiness();
            case BlockData blockData -> blockData.getMaterial().getSlipperiness();
            case BlockState blockState -> blockState.getType().getSlipperiness();
            default -> null;
        };
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "block slipperiness";
    }
}