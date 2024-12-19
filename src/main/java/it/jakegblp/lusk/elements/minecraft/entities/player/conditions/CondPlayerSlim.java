package it.jakegblp.lusk.elements.minecraft.entities.player.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.PlayerUtils.isSlim;

@Name("Player - is Slim")
@Description("Checks if a player is slim.")
@Examples({"if player is slim:"})
@Since("1.0.2, 1.3 (OfflinePlayers)")
public class CondPlayerSlim extends PropertyCondition<OfflinePlayer> {
    static { // todo: paper only???
        register(CondPlayerSlim.class, "slim", "offlineplayers");
    }

    @Override
    public boolean check(OfflinePlayer player) {
        return isSlim(player);
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "slim";
    }
}