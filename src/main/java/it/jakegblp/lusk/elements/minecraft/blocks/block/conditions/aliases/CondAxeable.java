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

@Name("is Axeable")
@Description("Checks if an item or block is axeable.")
@Examples("if tool of player is axeable:")
@Since("1.0.1")
public class CondAxeable extends PropertyCondition<Object> {

    static {
        register(CondAxeable.class, "axeable", "itemtype/block");
    }

    @Override
    public boolean check(Object o) {
        if (o != null) {
            if (o instanceof Block) {
                return Constants.axeables.contains(((Block) o).getType());
            } else if (o instanceof ItemType) {
                return Constants.axeables.contains(((ItemType) o).getMaterial());
            }
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "axeable";
    }
}
