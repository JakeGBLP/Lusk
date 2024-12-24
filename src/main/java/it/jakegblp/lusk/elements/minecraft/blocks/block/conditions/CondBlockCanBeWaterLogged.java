package it.jakegblp.lusk.elements.minecraft.blocks.block.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.BlockWrapper;
import org.jetbrains.annotations.NotNull;

@Name("Block - can Be Waterlogged")
@Description("Checks whether or not one or more blocks, blockstates or blockdatas can be waterlogged.")
@Examples({"if event-block can be waterlogged:\n\tbroadcast \"%event-block% can be waterlogged!\""})
@Since("1.3")
@SuppressWarnings("unused")
public class CondBlockCanBeWaterLogged extends PropertyCondition<Object> {
    static {
        register(CondBlockCanBeWaterLogged.class, PropertyType.CAN,"be water[ |-]logged", "blocks/blockstates/blockdatas/itemtypes");
    }

    @Override
    public boolean check(Object o) {
        return new BlockWrapper(o).canBeWaterlogged();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "be waterlogged";
    }
}