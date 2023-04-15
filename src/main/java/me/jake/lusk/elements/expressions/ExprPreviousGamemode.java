package me.jake.lusk.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Previous Gamemode")
@Description("Returns the previous gamemode of a player.")
@Examples({"broadcast previous gamemode of player"})
@Since("1.0.2")
public class ExprPreviousGamemode extends SimplePropertyExpression<Player, GameMode> {
    static {
        register(ExprPreviousGamemode.class, GameMode.class, "previous gamemode", "player");
    }

    @Override
    public @NotNull Class<? extends GameMode> getReturnType() {
        return GameMode.class;
    }

    @Override
    @Nullable
    public GameMode convert(Player player) {
        if (player != null) {
            return player.getPreviousGameMode();
        }
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "previous gamemode";
    }
}