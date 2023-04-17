package me.jake.lusk.elements.conditions.aliases;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import me.jake.lusk.utils.Utils;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

@Name("is Waxable")
@Description("Checks if an item or block is waxable with an axe.")
@Examples("if tool of player is waxable:")
@Since("1.0.1")
public class CondWaxable extends PropertyCondition<Object> {

    static {
        register(CondWaxable.class, "waxable", "itemtype/block");
    }

    @Override
    public boolean check(Object o) {
        if (o != null) {
            if (o instanceof Block) {
                return Utils.isWaxable(((Block)o).getType());
            } else if (o instanceof ItemType) {
                return Utils.isWaxable(((ItemType)o).getMaterial());
            }
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "waxable";
    }
}
