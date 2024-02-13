package it.jakegblp.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Name("Has Seen Credits")
@Description("Checks if a player has seen the end credits.")
@Examples({"if target has seen the credits:"})
@Since("1.0.2")
public class CondPlayerSeenCredits extends PropertyCondition<Player> {

    static {
        register(CondPlayerSeenCredits.class, PropertyType.HAVE, "seen credits", "players");
    }

    @Override
    public boolean check(Player player) {
        return player.hasSeenWinScreen();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "seen credits";
    }

}