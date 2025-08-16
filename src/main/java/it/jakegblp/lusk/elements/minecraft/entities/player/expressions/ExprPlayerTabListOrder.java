package it.jakegblp.lusk.elements.minecraft.entities.player.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.Constants.MINECRAFT_1_21_2;

@Name("Player - Player Tab List Order")
@Description("Returns he relative order that the player is shown on the player list.\nCan be set.")
@Examples({"broadcast tab list order of player"})
@Since("1.3.9")
@RequiredPlugins("1.21.2")
public class ExprPlayerTabListOrder extends SimplerPropertyExpression<Player, Integer> {
    static {
        if (MINECRAFT_1_21_2)
            register(ExprPlayerTabListOrder.class, Integer.class, "((tab[ ]|player )list|tab) order", "players");
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public Integer convert(Player player) {
        return player.getPlayerListOrder();
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public boolean allowAdd() {
        return true;
    }

    @Override
    public boolean allowRemove() {
        return true;
    }

    @Override
    public void set(Player from, Integer to) {
        from.setPlayerListOrder(to);
    }

    @Override
    public void add(Player from, Integer to) {
        from.setPlayerListOrder(from.getPlayerListOrder() + to);
    }

    @Override
    public void remove(Player from, Integer to) {
        add(from, -to);
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "tab list order";
    }
}