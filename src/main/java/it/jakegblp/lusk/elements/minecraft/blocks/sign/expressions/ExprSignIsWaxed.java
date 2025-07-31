package it.jakegblp.lusk.elements.minecraft.blocks.sign.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.BlockWrapper;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.MINECRAFT_1_20_1;

@Name("Sign - is Waxed (Property)")
@Description("Returns whether or not the provided signs are waxed.\nCan be set and reset (which makes it not waxed).")
@Examples({"broadcast editable state of target block"})
@Since("1.3")
@RequiredPlugins("1.20.1+")
@SuppressWarnings("unused")
public class ExprSignIsWaxed extends SimpleBooleanPropertyExpression<Object> {

    static {
        if (MINECRAFT_1_20_1)
            register(ExprSignIsWaxed.class,Boolean.class,"[sign]","[is] waxed", "blocks/blockstates/itemtypes");
    }

    @Override
    public @Nullable Boolean convert(Object from) {
        return new BlockWrapper(from).isSignWaxed();
    }

    @Override
    public void set(Object from, Boolean to) {
        new BlockWrapper(from, true).setIsSignWaxed(to);
    }

    @Override
    public void reset(Object from) {
        set(from,true);
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public boolean allowReset() {
        return true;
    }

    @Override
    protected String getPropertyName() {
        return "is waxed";
    }
}