package it.jakegblp.lusk.elements.minecraft.blocks.block.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.wrappers.BlockDataWrapper;
import org.jetbrains.annotations.NotNull;

@Name("Block - can Be Waterlogged")
@Description("Checks if a block can be waterlogged.")
@Examples({"if event-block can be waterlogged:\n\twaterlog event-block"})
@Since("1.3")
@SuppressWarnings("unused")
public class CondBlockCanBeWaterLogged extends PropertyCondition<Object> {
    static {
        register(CondBlockCanBeWaterLogged.class, PropertyType.CAN,"be water[ |-]logged", "blocks/blockstates");
    }

    @Override
    public boolean check(Object o) {
        BlockDataWrapper blockDataWrapper = BlockDataWrapper.create(o);
        if (blockDataWrapper != null) {
            return blockDataWrapper.canBeWaterlogged();
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "be waterlogged";
    }
}