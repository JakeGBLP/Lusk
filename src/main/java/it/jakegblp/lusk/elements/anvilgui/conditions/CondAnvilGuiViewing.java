package it.jakegblp.lusk.elements.anvilgui.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.api.wrappers.AnvilGuiWrapper;
import it.jakegblp.lusk.utils.Constants;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Anvil GUI - is Viewing")
@Description("Checks if a player is currently viewing a specific anvil GUI or any at all.")
@Examples({"if player is viewing any anvil guis:\n\tbroadcast \"%player% is viewing an anvil gui!\""})
@Since("1.3")
public class CondAnvilGuiViewing extends Condition {
    static {
        Skript.registerCondition(CondAnvilGuiViewing.class,
                "%players% (is[not:(n't| not)]|are[not:(n't| not)]) viewing (any:an[y] anvil gui[s]|" + Constants.ANVIL_GUI_PREFIX + " %-anvilguiinventory%)");
    }

    private boolean any;
    private Expression<Player> playerExpression;
    private Expression<AnvilGuiWrapper> anvilGuiWrapperExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parser) {
        any = parser.hasTag("any");
        if (!any) {
            anvilGuiWrapperExpression = (Expression<AnvilGuiWrapper>) expressions[1];
        }
        playerExpression = (Expression<Player>) expressions[0];
        setNegated(parser.hasTag("not"));
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return playerExpression.toString(event, debug) + " are " + (isNegated() ? "not " : "") + "viewing " + (any ? "any anvil gui" : "anvil gui " + anvilGuiWrapperExpression.toString(event, debug));
    }

    @Override
    public boolean check(@NotNull Event event) {
        Player[] players = playerExpression.getAll(event);
        if (any) {
            return AnvilGuiWrapper.isViewingAnyAnvilGui(players);
        }
        AnvilGuiWrapper anvilGuiWrapper = anvilGuiWrapperExpression.getSingle(event);
        if (anvilGuiWrapper == null) {
            return false;
        }
        return anvilGuiWrapper.isOpenTo(players);
    }
}