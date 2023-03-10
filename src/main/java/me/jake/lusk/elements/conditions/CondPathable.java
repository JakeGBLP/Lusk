package me.jake.lusk.elements.conditions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import me.jake.lusk.utils.Utils;
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
            if (o instanceof Block) {
                return Utils.isPathable(((Block)o).getType());
            } else if (o instanceof ItemType) {
                return Utils.isPathable(((ItemType)o).getMaterial());
            }
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "pathable";
    }
}
