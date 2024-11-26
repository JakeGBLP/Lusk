package it.jakegblp.lusk.elements.anvilgui.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.api.wrappers.AnvilGuiWrapper;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.ANVIL_GUI_PREFIX;

@Name("Anvil GUI - All Open Anvil Gui")
@Description("Gets all the open anvil guis.")
@Examples("broadcast open all anvil guis")
@Since("1.3")
public class ExprOpenAnvilGuis extends SimpleExpression<AnvilGuiWrapper> {
    static {
        Skript.registerExpression(ExprOpenAnvilGuis.class, AnvilGuiWrapper.class, ExpressionType.SIMPLE,
                "[all [[of] the]|the] open "+ ANVIL_GUI_PREFIX+ "s");
    }

    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected AnvilGuiWrapper @NotNull [] get(@NotNull Event e) {
        return AnvilGuiWrapper.getOpenGuis().keySet().toArray(AnvilGuiWrapper[]::new);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends AnvilGuiWrapper> getReturnType() {
        return AnvilGuiWrapper.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "all of the open anvil guis";
    }
}
