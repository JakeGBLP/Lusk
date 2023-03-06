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
import org.bukkit.event.Event;
import org.bukkit.event.entity.ArrowBodyCountChangeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Arrow Body Count")
@Description("Returns the past or future count of arrows in the Arrow Body Count Change.")
@Examples({"on arrow count change:\n\tbroadcast future-arrow count"})
@Since("1.0.0")
public class ExprArrowBodyCount extends SimpleExpression<Number> {
    static {
        Skript.registerExpression(ExprArrowBodyCount.class, Number.class, ExpressionType.SIMPLE,
                "[(:(old|past)|(new|future))]( |-)arrow [body] count");
    }
    private boolean old;

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!Skript.classExists("ch.njol.skript.expressions.ExprArrowsStuck")) {
            if (!getParser().isCurrentEvent(ArrowBodyCountChangeEvent.class)) {
                Skript.error("This expression can only be used in the Arrow Body Count Change event!");
                return false;
            }
            old = parseResult.hasTag("old") || parseResult.hasTag("past");
            return true;
        }
        return false;
    }
    @Override
    protected Number @NotNull [] get(@NotNull Event e) {
        Number number;
        if (old) {
            number = ((ArrowBodyCountChangeEvent) e).getOldAmount();
        } else {
            number = ((ArrowBodyCountChangeEvent) e).getNewAmount();
        }

        return new Number[]{number};
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
        return (old ? "Past" : "Future") + " Arrow Body Count";
    }
}
