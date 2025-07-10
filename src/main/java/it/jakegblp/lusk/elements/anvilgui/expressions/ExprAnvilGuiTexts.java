package it.jakegblp.lusk.elements.anvilgui.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.api.AnvilGuiWrapper;
import it.jakegblp.lusk.api.events.AnvilGuiSnapshotEvent;
import it.jakegblp.lusk.api.skript.PropertyExpression;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.ANVIL_GUI_PREFIX;

@Name("Anvil GUI - Text Input/Title")
@Description("Gets the title and the text input of the provided anvil guis.\n*NOTES*:\n- These can be set.\n- To apply the changes you need to reopen the anvil gui to the player.")
@Examples({"set anvil gui text input of {_anvil} to \"Hello!!\""})
@Since("1.3")
public class ExprAnvilGuiTexts extends PropertyExpression<AnvilGuiWrapper, String> {

    static {
        register(ExprAnvilGuiTexts.class, String.class, ANVIL_GUI_PREFIX + " ((rename|text) input|text|:title)", "anvilguiinventories");
    }

    private boolean title;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr((Expression<? extends AnvilGuiWrapper>) expressions[0]);
        title = parseResult.hasTag("title");
        return true;
    }

    @Override
    protected String @NotNull [] get(@NotNull Event event, AnvilGuiWrapper @NotNull [] source) {
        return get(source, anvilGuiWrapper -> {
            if (event instanceof AnvilGuiSnapshotEvent snapshotEvent && !title)
                return snapshotEvent.getText();
            return title ? anvilGuiWrapper.getTitle() : anvilGuiWrapper.getText();
        });
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return new Class[]{String.class};
        }
        return null;
    }

    @Override
    public void change(@NotNull Event event, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET && delta[0] instanceof String string) {
            getExpr().stream(event).forEach(anvilGuiWrapper -> {
                if (title) {
                    anvilGuiWrapper.setTitle(string);
                } else {
                    anvilGuiWrapper.setText(string);
                }
            });
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "lusk anvil gui " + (title ? "title" : "text") + " of " + getExpr().toString(event, debug);
    }
}