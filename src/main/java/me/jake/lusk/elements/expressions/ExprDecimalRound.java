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
import java.util.Objects;

@Name("Decimal Round")
@Description("To use this expression to the fullest, the maximum amount of decimals in the Skript config file should be changed to a higher one.\n\nThis expression crops down the amount of decimals by rounding, similar to how PI (3.14159265359) is often rounded as 3.1416 <- 6 instead of 5 cuz it rounded up.\nIf you want to \"crop\" the decimals (remove last x decimals from the number) you need to round down.")
@Examples({"broadcast 1.2345 rounded up at 2"})
@Since("1.0.0")
public class ExprDecimalRound extends SimpleExpression<Number> {
    static {
        Skript.registerExpression(ExprDecimalRound.class, Number.class, ExpressionType.SIMPLE,
                "%number% round[ed] [(down:down|up:up)[ward[s]]] at [index|decimal] %number%");
    }

    private Expression<Number> number;
    private Expression<Number> index;
    private int down;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (parseResult.hasTag("down")) {
            down = -1;
        } else if (parseResult.hasTag("up")) {
            down = 1;
        } else {
            down = 0;
        }
        number = (Expression<Number>) exprs[0];
        index = (Expression<Number>) exprs[1];
        return true;
    }
    @Override
    protected Number @NotNull [] get(@NotNull Event e) {
        double i = Objects.requireNonNull(index.getSingle(e)).doubleValue();
        double n = Objects.requireNonNull(number.getSingle(e)).doubleValue();
        double result = Utils.roundToDecimal(n,i,down);
        return new Number[]{result};
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
        return "Decimal Round";
    }
}
