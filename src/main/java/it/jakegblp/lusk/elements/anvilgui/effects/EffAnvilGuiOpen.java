package it.jakegblp.lusk.elements.anvilgui.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.api.AnvilGuiWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.ANVIL_GUI_PREFIX;

@Name("Anvil GUI - Open")
@Description(
        """
                Opens an anvil GUI to one or more players.
                *NOTES*:
                - Anvil GUIs are always opened 1 tick later.
                - You can only open an anvil GUI for a player if the player is not already viewing another (or this) anvil GUI.
                - Using `force` will forcibly close the anvil GUI the player is currently viewing to open a new one. Be aware that, due to how anvil GUIs work, closing an anvil GUI is global, meaning it will be closed for all players viewing it. To avoid this, ensure that you create per-player anvil GUIs.
                """)
@Examples({"open a new anvil gui to the player"})
@Since("1.3")
public class EffAnvilGuiOpen extends Effect {
    static {
        Skript.registerEffect(EffAnvilGuiOpen.class,
                "[:force] open " + ANVIL_GUI_PREFIX + " %anvilguiinventory% to %players%");
    }

    private Expression<AnvilGuiWrapper> anvilGuiWrapperExpression;
    private Expression<Player> playerExpression;
    private boolean force;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        anvilGuiWrapperExpression = (Expression<AnvilGuiWrapper>) expressions[0];
        playerExpression = (Expression<Player>) expressions[1];
        force = parser.hasTag("force");
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (force ? "force " : "")+"open anvil gui "+
                anvilGuiWrapperExpression.toString(event,debug)+" to "+playerExpression.toString(event,debug);
    }

    @Override
    protected void execute(@NotNull Event event) {
        AnvilGuiWrapper wrapper = anvilGuiWrapperExpression.getSingle(event);
        if (wrapper == null) return;
        Player[] players = playerExpression.getAll(event);
        for (Player player : players) {
            if (force) {
                wrapper.closeAndOpen(player);
                return;
            }
            wrapper.open(player);
        }
    }
}