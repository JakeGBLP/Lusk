package it.jakegblp.lusk.elements.minecraft.item.conditions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;

import static it.jakegblp.lusk.utils.ItemUtils.hasSuspiciousStewPotionEffects;

@Name("Suspicious Stew - has Custom Potion Effects")
@Description("Checks if the provided suspicious stews have any custom potion effects.")
@Examples({"if tool of player has custom potion effects:"})
@Since("1.3.3")
@SuppressWarnings("unused")
public class CondSuspiciousStewHasCustomEffects extends PropertyCondition<ItemType> {

    static {
        register(CondSuspiciousStewHasCustomEffects.class, PropertyType.HAVE, "[custom] potion effects", "itemtypes");
    }

    @Override
    public boolean check(ItemType value) {
        return hasSuspiciousStewPotionEffects(value);
    }

    @Override
    protected String getPropertyName() {
        return "custom potion effects";
    }
}