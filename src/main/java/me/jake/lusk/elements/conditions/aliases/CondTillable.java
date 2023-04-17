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

@Name("is Tillable")
@Description("Checks if an item or block is tillable with an hoe.")
@Examples("if tool of player is tillable:")
@Since("1.0.1")
public class CondTillable extends PropertyCondition<Object> {

    static {
        register(CondTillable.class, "tillable", "itemtype/block");
    }

    @Override
    public boolean check(Object o) {
        if (o != null) {
            if (o instanceof Block) {
                return Utils.isTillable(((Block)o).getType());
            } else if (o instanceof ItemType) {
                return Utils.isTillable(((ItemType)o).getMaterial());
            }
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "tillable";
    }
}
