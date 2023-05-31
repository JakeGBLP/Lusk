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
import io.papermc.paper.event.world.border.WorldBorderBoundsChangeEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("WorldBorder - Change Duration")
@Description("Returns the duration of the change in the World Border Start Change event.\nCan be set.\nRequires Paper.")
@Examples({""})
@Since("1.0.2")
public class ExprWorldBorderChangeDuration extends SimpleExpression<Timespan> {
    static {
        if (Skript.classExists("io.papermc.paper.event.world.border.WorldBorderEvent")) {
            Skript.registerExpression(ExprWorldBorderChangeDuration.class, Timespan.class, ExpressionType.SIMPLE,
                    "[the] (shift|change) duration");
        }
    }

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(WorldBorderBoundsChangeEvent.class)) {
            Skript.error("This expression can only be used in the World Border Change events.!");
            return false;
        }
        return true;
    }

    @Override
    protected Timespan @NotNull [] get(@NotNull Event e) {
        WorldBorderBoundsChangeEvent event = (WorldBorderBoundsChangeEvent) e;
        return new Timespan[]{new Timespan(event.getDuration())};
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Timespan[].class);
        }
        return new Class[0];
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Timespan timespan = delta instanceof Timespan[] ? ((Timespan[]) delta)[0] : null;
        if (timespan == null) return;
        ((WorldBorderBoundsChangeEvent) e).setDuration(timespan.getMilliSeconds());
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
        return "the change duration";
    }
}
