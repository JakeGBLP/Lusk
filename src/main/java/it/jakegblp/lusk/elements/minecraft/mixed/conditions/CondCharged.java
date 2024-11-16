package it.jakegblp.lusk.elements.minecraft.mixed.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.jetbrains.annotations.NotNull;

@Name("Crossbow / Creeper / Wither / Wither Skull - is Charged")
@Description("Checks if a crossbow, creeper, wither or wither skull is charged.")
@Examples({"if tool of player is charged:"})
@Since("1.0.1, 1.0.3 (Wither/Wither Skull)")
@SuppressWarnings("unused")
public class CondCharged extends PropertyCondition<Object> {
    //todo: test in 2.10
    static {
        register(CondCharged.class, "charged", "itemstacks/entities");
    }

    @Override
    public boolean check(Object o) {
        if (o instanceof ItemStack itemStack && itemStack.getItemMeta() instanceof CrossbowMeta crossbowMeta) {
            return crossbowMeta.hasChargedProjectiles();
        } else if (o instanceof CrossbowMeta crossbowMeta)
            return crossbowMeta.hasChargedProjectiles();
        else if (o instanceof Wither wither)
            return wither.isCharged();
        else if (o instanceof WitherSkull witherSkull)
            return witherSkull.isCharged();
        else if (o instanceof Creeper creeper) {
            return creeper.isPowered();
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "charged";
    }

}