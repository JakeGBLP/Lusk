package it.jakegblp.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.utils.Utils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Name("Player - Can Critical Damage")
@Description("Checks if a player is in position to inflict a critical hit.\n\nRead https://minecraft.fandom.com/wiki/Damage#Critical_hit for more info.")
@Examples({"if player can critical damage:"})
@Since("1.0.2")
public class CondCanCriticalDamage extends PropertyCondition<Player> {

    static {
        register(CondCanCriticalDamage.class, PropertyType.CAN, "crit[ical[ly] (damage|hit|attack)]", "players");
    }

    @Override
    public boolean check(Player player) {
        return Utils.canCriticalDamage(player);
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "critical damage";
    }

}