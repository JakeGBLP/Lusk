package it.jakegblp.lusk.elements.minecraft.blocks.sign.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.BlockWrapper;
import org.jetbrains.annotations.NotNull;

@Name("Sign - is Editable")
@Description("Checks if the provided signs can be edited.")
@Examples({"if target block is editable:"})
@Since("1.0.3, 1.3 (Plural, Blockstate)")
@DocumentationId("9124")
public class CondSignIsEditable extends PropertyCondition<Object> {

    static {
        register(CondSignIsEditable.class, "editable", "blocks/blockstates/itemtypes");
    }

    @Override
    public boolean check(Object o) {
        return new BlockWrapper(o).isSignEditable();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "editable";
    }

}