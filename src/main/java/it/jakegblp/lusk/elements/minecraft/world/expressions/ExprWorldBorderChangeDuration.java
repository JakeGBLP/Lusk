package it.jakegblp.lusk.elements.minecraft.world.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import io.papermc.paper.event.world.border.WorldBorderBoundsChangeEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.EVENT_OR_SIMPLE;
import static it.jakegblp.lusk.utils.Constants.PAPER_HAS_WORLD_BORDER_EVENT;
import static it.jakegblp.lusk.utils.SkriptUtils.getMilliseconds;

@Name("WorldBorder - Change Duration")
@Description("Returns the duration of the change in the World Border Start Change event.\nCan be set.\nRequires Paper.")
@Examples({""})
@Since("1.0.2, 1.3 (Milliseconds)")
@RequiredPlugins("Paper")
@SuppressWarnings("unused")
public class ExprWorldBorderChangeDuration extends SimpleExpression<Object> {
    static {
        if (PAPER_HAS_WORLD_BORDER_EVENT) {
            Skript.registerExpression(ExprWorldBorderChangeDuration.class, Object.class, EVENT_OR_SIMPLE,
                    "[the] world[ |-]border (shift|change) duration [millis:in milliseconds]");
        }
    }

    private boolean usesMilliseconds;

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(WorldBorderBoundsChangeEvent.class)) {
            Skript.error("This expression can only be used in the World Border Change events.!");
            return false;
        }
        usesMilliseconds = parseResult.hasTag("millis");
        return true;
    }

    @Override
    protected Object @NotNull [] get(@NotNull Event e) {
        WorldBorderBoundsChangeEvent event = (WorldBorderBoundsChangeEvent) e;
        return new Object[]{usesMilliseconds ? event.getDuration() : new Timespan(event.getDuration())};
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{usesMilliseconds ? Long.class : Timespan.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        WorldBorderBoundsChangeEvent event = (WorldBorderBoundsChangeEvent) e;
        if (usesMilliseconds && delta[0] instanceof Long aLong)
            event.setDuration(aLong);
        else if (delta[0] instanceof Timespan timespan)
            event.setDuration(getMilliseconds(timespan));
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the world border change duration" + (usesMilliseconds ? " in milliseconds" : "");
    }
}
