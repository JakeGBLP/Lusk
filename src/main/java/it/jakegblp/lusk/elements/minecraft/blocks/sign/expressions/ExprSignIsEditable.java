package it.jakegblp.lusk.elements.minecraft.blocks.sign.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.BlockWrapper;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.jetbrains.annotations.Nullable;

@Name("Sign - is Editable (Property)")
@Description("Returns whether or not the provided signs are editable.\nCan be set and reset (which makes it editable).")
@Examples({"broadcast editable property of target block"})
@Since("1.0.3, 1.3 (Plural, Blockstate, Items)")
@DocumentationId("9166")
@SuppressWarnings("unused")
public class ExprSignIsEditable extends SimpleBooleanPropertyExpression<Object> {

    static {
        register(ExprSignIsEditable.class,Boolean.class,"[sign]","[is] editable", "blocks/blockstates/itemtypes");
    }

    @Override
    public @Nullable Boolean convert(Object from) {
        return new BlockWrapper(from).isSignEditable();
    }

    @Override
    public void set(Object from, Boolean to) {
        new BlockWrapper(from).setIsSignEditable(to);
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
        return "is editable";
    }
}