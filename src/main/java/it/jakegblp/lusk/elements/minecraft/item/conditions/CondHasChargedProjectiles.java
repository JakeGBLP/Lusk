package it.jakegblp.lusk.elements.minecraft.item.conditions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;

import static it.jakegblp.lusk.utils.ItemUtils.hasChargedProjectiles;

@Name("Crossbow - has Charged Projectiles")
@Description("Checks if one or more crossbows have any charged projectiles at all.\nThis behaves the same way the `is charged` condition behaves for crossbows.")
@Examples({"if tool of player has charged projectiles:"})
@Since("1.3")
@SuppressWarnings("unused")
public class CondHasChargedProjectiles extends PropertyCondition<ItemType> {

    static {
        register(CondHasChargedProjectiles.class, PropertyType.HAVE, "charged projectiles", "itemtypes");
    }

    @Override
    public boolean check(ItemType value) {
        return hasChargedProjectiles(value);
    }

    @Override
    protected String getPropertyName() {
        return "charged projectiles";
    }
}