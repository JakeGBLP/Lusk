package it.jakegblp.lusk.elements.minecraft.item.conditions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;

import static it.jakegblp.lusk.utils.ItemUtils.hasStoredEnchantments;

@Name("Enchanted Book - has Stored Enchantments")
@Description("Checks if one or more enchanted books have any stored enchantments at all.")
@Examples({"if tool of player has stored enchantments:"})
@Since("1.3")
@SuppressWarnings("unused")
public class CondHasStoredEnchantments extends PropertyCondition<ItemType> {

    static {
        register(CondHasStoredEnchantments.class, PropertyType.HAVE, "stored enchant[ment]s", "itemtypes");
    }

    @Override
    public boolean check(ItemType value) {
        return hasStoredEnchantments(value);
    }

    @Override
    protected String getPropertyName() {
        return "stored enchantments";
    }
}