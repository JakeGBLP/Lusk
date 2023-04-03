package me.jake.lusk.elements.conditions;

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
public class CondSlim extends PropertyCondition<Player> {

    static {
        register(CondSlim.class, "slim", "player");
    }

    @Override
    public boolean check(Player player) {
        if (player != null) {
            return player.getPlayerProfile().getTextures().getSkinModel().equals(PlayerTextures.SkinModel.SLIM);
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "slim";
    }
}