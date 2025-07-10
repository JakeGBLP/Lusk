package it.jakegblp.lusk.elements.minecraft.blocks.block.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.BlockWrapper;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.jetbrains.annotations.Nullable;

@Name("Block - is Waterlogged (Property)")
@Description("Returns whether or not one or more blocks, blockstates or blockdatas are waterlogged.\nCan be set.")
@Examples({"broadcast waterlogged property of block"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprBlockIsWaterLogged extends SimpleBooleanPropertyExpression<Object> {

    static {
        register(ExprBlockIsWaterLogged.class, Boolean.class, "[block]","[is] water[ |-]log[ged]","blocks/blockstates/blockdatas/itemtypes");
    }

    @Override
    public @Nullable Boolean convert(Object from) {
        return new BlockWrapper(from).isWaterLogged();
    }

    @Override
    public void set(Object from, Boolean to) {
        new BlockWrapper(from).setWaterLogged(to);
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    protected String getPropertyName() {
        return "is waterlogged";
    }
}