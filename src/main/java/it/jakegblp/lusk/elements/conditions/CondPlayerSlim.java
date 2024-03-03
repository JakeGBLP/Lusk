package it.jakegblp.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;

@Name("Player - is Slim")
@Description("Checks if a player is slim.")
@Examples({"if player is slim:"})
@Since("1.0.2")
public class CondPlayerSlim extends PropertyCondition<Player> {
    static {
        register(CondPlayerSlim.class, "slim", "players");
    }

    @Override
    public boolean check(Player player) {
        return player != null && player.getPlayerProfile().getTextures().getSkinModel().equals(PlayerTextures.SkinModel.SLIM);
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "slim";
    }
}