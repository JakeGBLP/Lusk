package me.jake.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.jake.lusk.utils.Utils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Small Capital Case")
@Description("Returns the given string with the small font.")
@Examples({"broadcast player's name in small caps"})
@Since("1.0.0")
public class ExprCases extends SimpleExpression<String> {
    static {
        Skript.registerExpression(ExprCases.class, String.class, ExpressionType.SIMPLE,
                "%string% (in|as|using) [:fully] (small|tiny) (font |[upper[ ]]case|cap(s|ital[ case]))",
                        "[:fully] (small|tiny) (font |[upper[ ]]case|cap(s|ital[ case])) %string%");
    }

    private Expression<String> string;

    private boolean fully;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        fully = parseResult.hasTag("fully");
        string = (Expression<String>) exprs[0];
        return true;
    }
    @Override
    protected String @NotNull [] get(@NotNull Event e) {
        return new String[]{Utils.toSmallFont(string.getSingle(e), fully)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        assert e != null;
        return (fully ? "fully " : "") + "small caps " + string.getSingle(e);
    }
}
