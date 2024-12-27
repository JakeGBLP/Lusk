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
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.ANVIL_GUI_PREFIX;

@Name("Anvil GUI - Prevent Closing")
@Description("Prevents an anvil gui from closing.\n*NOTES*:\n- This effect must be used *BEFORE* the gui is opened; if you need to use it after it's already opened you'll have to open it again.\n- If this effect is used you'll need to use the `Anvil GUI - Close` effect to close the gui.")
@Examples({"prevent event-anvilguiinventory from closing"})
@Since("1.3")
public class EffAnvilGuiPreventClosing extends Effect {
    static {
        Skript.registerEffect(EffAnvilGuiPreventClosing.class,
                "prevent [" + ANVIL_GUI_PREFIX + "] %anvilguiinventory% from (closing|being closed)");
    }

    private Expression<AnvilGuiWrapper> anvilGuiWrapperExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        anvilGuiWrapperExpression = (Expression<AnvilGuiWrapper>) expressions[0];
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "prevent " + anvilGuiWrapperExpression.toString(event, debug) + " from closing"; //
    }

    @Override
    protected void execute(@NotNull Event event) {
        AnvilGuiWrapper wrapper = anvilGuiWrapperExpression.getSingle(event);
        if (wrapper == null) return;
        wrapper.preventClose();
    }
}