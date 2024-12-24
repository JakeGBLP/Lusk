package it.jakegblp.lusk.elements.minecraft.blocks.sign.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.BlockWrapper;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.Constants.MINECRAFT_1_20_1;

@Name("Sign - is Waxed")
@Description("Checks if a the provided signs are waxed.")
@Examples({"if target block is waxed:"})
@Since("1.3")
@RequiredPlugins("1.20.1+")
public class CondSignIsWaxed extends PropertyCondition<Object> {

    static {
        if (MINECRAFT_1_20_1)
            register(CondSignIsWaxed.class, "waxed", "blocks/blockstates/itemtypes");
    }

    @Override
    public boolean check(Object o) {
        return new BlockWrapper(o).isSignWaxed();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "waxed";
    }

}