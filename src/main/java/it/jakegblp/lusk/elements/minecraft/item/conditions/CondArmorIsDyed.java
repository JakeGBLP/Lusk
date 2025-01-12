package it.jakegblp.lusk.elements.minecraft.item.conditions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;

import static it.jakegblp.lusk.utils.ItemUtils.isArmorDyed;

@Name("Armor - is Dyed")
@Description("""
Checks whether the provided armor items are dyed (they don't have the default color).
This works for leather armor, horse leather armor and wolf armor.

This might not work correctly on Spigot.
""")
@Examples("if chestplate of player is dyed:")
@Since("1.3.3")
public class CondArmorIsDyed extends PropertyCondition<ItemType> {

    static {
        register(CondArmorIsDyed.class, "(dyed|colored)", "itemtypes");
    }

    @Override
    public boolean check(ItemType value) {
        return isArmorDyed(value);
    }

    @Override
    protected String getPropertyName() {
        return "dyed";
    }
}
