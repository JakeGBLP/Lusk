package me.jake.lusk.elements.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Block - Slipperiness")
@Description("""
        Returns a value that represents how 'slippery' the block is. Blocks with higher slipperiness, like ice can be slid on further by the player and other entities.
        Most blocks have a default slipperiness of 0.6.
        
        Only works for placeable item/blocks.
        """)
@Examples({"broadcast slipperiness of packed ice"})
@Since("1.0.0")
public class ExprSlipperiness extends SimplePropertyExpression<ItemType, Float> {
    static {
        register(ExprSlipperiness.class, Float.class, "slipperiness", "itemtype");
    }

    @Override
    public @NotNull Class<? extends Float> getReturnType() {
        return Float.class;
    }

    @Override
    @Nullable
    public Float convert(ItemType itemType) {
        Material material = itemType.getMaterial();
        return material.isBlock() ? material.getSlipperiness() : null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "slipperiness";
    }
}