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
import it.jakegblp.lusk.api.wrappers.AnvilGuiWrapper;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import static it.jakegblp.lusk.utils.Constants.ANVIL_GUI_PREFIX;

@Name("Anvil GUI - Close")
@Description("Closes an anvil gui.\n*NOTES*:\n- Won't do anything if the anvil gui is not open.\n- Must be used before opening another anvil gui for a player.")
@Examples({"close anvil the anvil gui"})
@Since("1.3")
public class EffAnvilGuiClose extends Effect {
    static {
        Skript.registerEffect(EffAnvilGuiClose.class,
                "close " + ANVIL_GUI_PREFIX + " %anvilguiinventories%");
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
        return "close " + ANVIL_GUI_PREFIX + " " + anvilGuiWrapperExpression.toString(event, debug);
    }

    @Override
    protected void execute(@NotNull Event event) {
        AnvilGuiWrapper wrapper = anvilGuiWrapperExpression.getSingle(event);
        if (wrapper == null) return;
        wrapper.close();
    }
}