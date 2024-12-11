package it.jakegblp.lusk.elements.minecraft.entities.player.expressions;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Name("Player - Previous Game Mode")
@Description("Returns the previous gamemode of a player.")
@Examples({"broadcast previous gamemode of player"})
@Since("1.0.2")
@DocumentationId("9082")
public class ExprPlayerPreviousGameMode extends SimplePropertyExpression<Player, GameMode> {
    static {
        register(ExprPlayerPreviousGameMode.class, GameMode.class, "previous game[ ]mode", "player");
    }

    @Override
    public @NotNull Class<? extends GameMode> getReturnType() {
        return GameMode.class;
    }

    @Override
    public GameMode convert(Player player) {
        return player.getPreviousGameMode();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "previous game mode";
    }
}