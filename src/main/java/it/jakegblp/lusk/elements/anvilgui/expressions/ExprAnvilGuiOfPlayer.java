package it.jakegblp.lusk.elements.anvilgui.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import it.jakegblp.lusk.api.AnvilGuiWrapper;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.ANVIL_GUI_PREFIX;

@Name("Anvil GUI - Opened Anvil GUI")
@Description("Gets the open anvil GUI of one or more players.")
@Examples({"set {_anvilGui} to the currently open anvil gui of {_player}"})
@Since("1.3")
public class ExprAnvilGuiOfPlayer extends SimplePropertyExpression<Player, AnvilGuiWrapper> {

    static {
        register(ExprAnvilGuiOfPlayer.class, AnvilGuiWrapper.class, "(current[ly open]|open|top) " + ANVIL_GUI_PREFIX, "players");
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "currently open anvil gui";
    }

    @Override
    public @NotNull Class<? extends AnvilGuiWrapper> getReturnType() {
        return AnvilGuiWrapper.class;
    }

    @Override
    public @Nullable AnvilGuiWrapper convert(Player from) {
        return AnvilGuiWrapper.getOpenAnvilGui(from);
    }
}