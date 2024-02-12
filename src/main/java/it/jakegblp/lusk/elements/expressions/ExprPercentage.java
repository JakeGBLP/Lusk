package it.jakegblp.lusk.elements.expressions;

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
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Percentage")
@Description("Returns the percentage x of number y.")
@Examples({"broadcast 250% of 25"})
@Since("1.0.0")
public class ExprPercentage extends SimpleExpression<Number> {
    static {
        Skript.registerExpression(ExprPercentage.class, Number.class, ExpressionType.COMBINED,
                "%number%([ ]\\%| percent) (of|from) %number%");
    }

    private Expression<Number> number;
    private Expression<Number> percentage;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        number = (Expression<Number>) exprs[0];
        percentage = (Expression<Number>) exprs[1];
        return true;
    }

    @Override
    protected Number @NotNull [] get(@NotNull Event e) {
        Number p = percentage.getSingle(e);
        Number n = number.getSingle(e);
        if (n != null) {
            if (p != null) {
                return new Number[]{p.doubleValue() / 100 * n.doubleValue()};
            }
        }
        return new Number[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return percentage + "% of " + number;
    }
}
