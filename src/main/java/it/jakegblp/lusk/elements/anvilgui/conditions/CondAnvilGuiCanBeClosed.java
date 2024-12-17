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
import it.jakegblp.lusk.api.events.AnvilGuiEvent;
import it.jakegblp.lusk.api.AnvilGuiWrapper;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Anvil GUI - Can Be Closed")
@Description("Checks if an anvil gui can be closed.\nThis is only true if the `Anvil GUI - Prevent Closing` effect is used and cannot be reversed without creating a new gui.")
@Examples({"set anvil gui right item of {_anvil} to barrier named \"<red>Click To Close!\""})
@Since("1.3")
public class CondAnvilGuiCanBeClosed extends Condition {
    static {
        Skript.registerCondition(CondAnvilGuiCanBeClosed.class,
                "(%anvilguiinventory%|event:[the] anvil) can[not:( no|n')t] be closed");
    }

    private boolean isEvent;
    private Expression<AnvilGuiWrapper> anvilGuiWrapperExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parser) {
        isEvent = parser.hasTag("event");
        if (isEvent) {
            if (!(getParser().isCurrentEvent(AnvilGuiEvent.class))) {
                Skript.error("This condition can only be used in anvil gui events!");
                return false;
            }
        } else {
            anvilGuiWrapperExpression = (Expression<AnvilGuiWrapper>) expressions[0];
        }
        setNegated(parser.hasTag("not"));
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (isEvent && anvilGuiWrapperExpression != null ?
                anvilGuiWrapperExpression.toString(event, debug) : " the anvil ")
                + "can" + (isNegated() ? "not" : "") + " be closed";
    }

    @Override
    public boolean check(@NotNull Event event) {
        return (isNegated()) ^ ((AnvilGuiEvent) event).getAnvil().isPreventsClose();
    }
}