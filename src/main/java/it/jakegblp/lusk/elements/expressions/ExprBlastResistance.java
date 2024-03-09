package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Block - Blast Resistance")
@Description("""
        Obtains the blast resistance value (also known as block "durability").
        This value is used in explosions to calculate whether a block should be broken or not.
                
        Only works for placeable item/blocks.
        """)
@Examples({"broadcast blast resistance of obsidian"})
@Since("1.0.0")
public class ExprBlastResistance extends SimplePropertyExpression<ItemType, Float> {
    static {
        register(ExprBlastResistance.class, Float.class, "[block] blast resistance", "itemtypes");
    }

    @Override
    public @NotNull Class<? extends Float> getReturnType() {
        return Float.class;
    }

    @Override
    @Nullable
    public Float convert(ItemType itemType) {
        Material material = itemType.getMaterial();
        return material.isBlock() ? material.getBlastResistance() : null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "block blast resistance";
    }
}