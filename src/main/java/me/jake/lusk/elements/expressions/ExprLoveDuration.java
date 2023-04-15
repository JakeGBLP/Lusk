package me.jake.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityEnterLoveModeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Love Duration")
@Description("Returns the love duration of the animal in the Love event.\n Can be set.")
@Examples({"on love:\n\tbroadcast the love duration"})
@Since("1.0.2")
public class ExprLoveDuration extends SimpleExpression<Timespan> {
    static {
        Skript.registerExpression(ExprLoveDuration.class, Timespan.class, ExpressionType.SIMPLE,
                "[the] love duration");
    }

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(EntityEnterLoveModeEvent.class)) {
            Skript.error("This expression can only be used in the Love event!");
            return false;
        }
        return true;
    }
    @Override
    protected Timespan @NotNull [] get(@NotNull Event e) {
        return new Timespan[]{Timespan.fromTicks_i(((EntityEnterLoveModeEvent)e).getTicksInLove())};
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Timespan[].class);
        } else {
            return new Class[0];
        }
    }
    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Timespan timespan = delta instanceof Timespan[] ? ((Timespan[]) delta)[0] : null;
        if (timespan == null) return;
        ((EntityEnterLoveModeEvent)e).setTicksInLove((int) timespan.getTicks_i());
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Timespan> getReturnType() {
        return Timespan.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the love duration";
    }
}
