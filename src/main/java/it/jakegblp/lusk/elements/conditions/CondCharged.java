package it.jakegblp.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.jetbrains.annotations.NotNull;

@Name("Crossbow/Wither/Wither Skull - is Charged")
@Description("Checks if a crossbow, wither or wither skull is charged.")
@Examples({"if tool of player is charged:"})
@Since("1.0.1, 1.0.3 (Wither/Wither Skull)")
public class CondCharged extends PropertyCondition<Object> {

    static {
        register(CondCharged.class, "charged", "itemstack/entity");
    }

    @Override
    public boolean check(Object o) {
        if (o instanceof ItemStack itemStack)
            if (itemStack.getItemMeta() instanceof CrossbowMeta crossbowMeta)
                return crossbowMeta.hasChargedProjectiles();
        else if (o instanceof CrossbowMeta crossbowMeta)
            return crossbowMeta.hasChargedProjectiles();
        else if (o instanceof Wither wither)
            return wither.isCharged();
        else if (o instanceof WitherSkull witherSkull)
            return witherSkull.isCharged();
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "charged";
    }

}