package it.jakegblp.lusk.elements.minecraft.blocks.block.conditions.aliases;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.utils.Constants;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

@Name("is Pathable")
@Description("Checks if an item or block is pathable with a shovel.")
@Examples("if tool of player is pathable:")
@Since("1.0.1")
public class CondPathable extends PropertyCondition<Object> {

    static {
        register(CondPathable.class, "pathable", "itemtype/block");
    }

    @Override
    public boolean check(Object o) {
        if (o != null) {
            if (o instanceof Block block) {
                return Constants.pathables.contains(block.getType());
            } else if (o instanceof ItemType itemType) {
                return Constants.pathables.contains(itemType.getMaterial());
            }
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "pathable";
    }
}
