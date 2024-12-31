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
import it.jakegblp.lusk.api.AnvilGuiWrapper;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Anvil GUI - New Anvil Gui")
@Description("Creates a new anvil gui. Allows you to copy slots and text data from another anvil gui, this doesn't copy the \"prevent closing\" property, this is intentional.")
@Examples({"set {_anvil} to a new anvil gui"})
@Since("1.3")
public class ExprNewAnvilGui extends SimpleExpression<AnvilGuiWrapper> {
    static {
        Skript.registerExpression(ExprNewAnvilGui.class, AnvilGuiWrapper.class, ExpressionType.COMBINED,
                "[a] new anvil gui [clone:(copying|cloning|from) %-anvilguiinventory%]");
    }

    private Expression<AnvilGuiWrapper> anvilGuiWrapperExpression;
    private boolean clone;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        anvilGuiWrapperExpression = (Expression<AnvilGuiWrapper>) expressions[0];
        clone = parseResult.hasTag("clone");
        return true;
    }

    @Override
    protected AnvilGuiWrapper @NotNull [] get(@NotNull Event e) {
        if (clone && anvilGuiWrapperExpression != null) {
            AnvilGuiWrapper anvilGuiWrapper = anvilGuiWrapperExpression.getSingle(e);
            if (anvilGuiWrapper != null)
                return new AnvilGuiWrapper[]{new AnvilGuiWrapper(anvilGuiWrapper)};
        }
        return new AnvilGuiWrapper[]{new AnvilGuiWrapper()};
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
        return "a new anvil gui" + (clone ? " copying " + anvilGuiWrapperExpression.toString(e, debug) : "");
    }
}
