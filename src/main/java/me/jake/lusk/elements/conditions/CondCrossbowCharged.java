package me.jake.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.jetbrains.annotations.NotNull;

@Name("Crossbow - is Charged")
@Description("Checks if a crossbow is charged.")
@Examples({"if tool of player is changed:"})
@Since("1.0.1")
public class CondCrossbowCharged extends PropertyCondition<ItemStack> {

    static {
        register(CondCrossbowCharged.class, "charged", "itemstack");
    }

    @Override
    public boolean check(ItemStack i) {
        if (i.getType() == Material.CROSSBOW) {
            return ((CrossbowMeta)i.getItemMeta()).hasChargedProjectiles();
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "charged";
    }

}