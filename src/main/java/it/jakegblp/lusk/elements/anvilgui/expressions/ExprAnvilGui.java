package it.jakegblp.lusk.elements.anvilgui.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.api.events.AnvilGuiEvent;
import it.jakegblp.lusk.api.wrappers.AnvilGuiWrapper;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.EVENT_OR_SIMPLE;

@Name("Anvil GUI - The Anvil Gui")
@Description("Gets the anvil gui involved in an event.")
@Examples({"set anvil title of anvil gui to \"hello\"\nopen anvil anvil gui to player"})
@Since("1.3")
public class ExprAnvilGui extends SimpleExpression<AnvilGuiWrapper> {
    static {
        Skript.registerExpression(ExprAnvilGui.class, AnvilGuiWrapper.class, EVENT_OR_SIMPLE,
                "[the] anvil gui");
    }

    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        return getParser().isCurrentEvent(AnvilGuiEvent.class);
    }

    @Override
    protected AnvilGuiWrapper @NotNull [] get(@NotNull Event e) {
        if (e instanceof AnvilGuiEvent anvilGuiEvent) {
            return new AnvilGuiWrapper[]{anvilGuiEvent.getAnvil()};
        }
        return new AnvilGuiWrapper[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends AnvilGuiWrapper> getReturnType() {
        return AnvilGuiWrapper.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the anvil gui";
    }
}
