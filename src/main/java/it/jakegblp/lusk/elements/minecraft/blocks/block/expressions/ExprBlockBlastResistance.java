package it.jakegblp.lusk.elements.minecraft.blocks.block.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import it.jakegblp.lusk.api.BlockWrapper;
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
public class ExprBlockBlastResistance extends SimplePropertyExpression<Object, Float> {
    static {
        register(ExprBlockBlastResistance.class, Float.class, "[block] blast resistance", "itemtypes/blocks/blockdatas/blockstates");
    }

    @Override
    public @NotNull Class<? extends Float> getReturnType() {
        return Float.class;
    }

    @Override
    @Nullable
    public Float convert(Object o) {
        return new BlockWrapper(o).getBlastResistance();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "block blast resistance";
    }
}