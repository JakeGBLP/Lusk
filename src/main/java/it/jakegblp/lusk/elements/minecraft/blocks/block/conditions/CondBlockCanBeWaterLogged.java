package it.jakegblp.lusk.elements.minecraft.blocks.block.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.utils.BlockUtils;
import org.jetbrains.annotations.NotNull;

@Name("Block - can Be Waterlogged")
@Description("Checks if a block can be waterlogged.")
@Examples({"if event-block can be waterlogged:\n\twaterlog event-block"})
@Since("1.3")
@SuppressWarnings("unused")
public class CondBlockCanBeWaterLogged extends PropertyCondition<Object> {
    static {
        register(CondBlockCanBeWaterLogged.class, PropertyType.CAN,"be water[ |-]logged", "blocks/blockdatas");
    }

    @Override
    public boolean check(Object o) {
        return BlockUtils.canBeWaterlogged(o);
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "be waterlogged";
    }
}