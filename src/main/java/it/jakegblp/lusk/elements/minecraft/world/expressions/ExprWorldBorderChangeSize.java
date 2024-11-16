package it.jakegblp.lusk.elements.minecraft.world.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import io.papermc.paper.event.world.border.WorldBorderBoundsChangeEvent;
import io.papermc.paper.event.world.border.WorldBorderBoundsChangeFinishEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("WorldBorder - New/Old Size")
@Description("Returns the new/old anger of the warden in the World Border Change events.\nCan be set only within the Start variant of the event.")
@Examples({""})
@Since("1.0.2")
@RequiredPlugins("Paper")
@SuppressWarnings("unused")
public class ExprWorldBorderChangeSize extends SimpleExpression<Double> {
    static {
        if (Skript.classExists("io.papermc.paper.event.world.border.WorldBorderEvent")) {
            Skript.registerExpression(ExprWorldBorderChangeSize.class, Double.class, ExpressionType.EVENT,
                    "[the] [future|:past] world[ ]border size");
        }
    }

    private Boolean past;
    private boolean set;

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        past = parseResult.hasTag("past");
        if (getParser().isCurrentEvent(WorldBorderBoundsChangeEvent.class)) {
            set = true;
            return true;
        } else if (getParser().isCurrentEvent(WorldBorderBoundsChangeFinishEvent.class)) {
            set = false;
            return true;
        }
        Skript.error("This expression can only be used in the World Border Change events.!");
        return false;
    }

    @Override
    protected Double @NotNull [] get(@NotNull Event e) {
        double i;
        if (e instanceof WorldBorderBoundsChangeEvent event) {
            i = past ? event.getOldSize() : event.getNewSize();
            return new Double[]{i};
        } else if (e instanceof WorldBorderBoundsChangeFinishEvent event) {
            i = past ? event.getOldSize() : event.getNewSize();
            return new Double[]{i};
        }
        return new Double[0];
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return set && mode == Changer.ChangeMode.SET ? new Class[]{Double.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Double aDouble)
            ((WorldBorderBoundsChangeEvent) e).setNewSize(aDouble);
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Double> getReturnType() {
        return Double.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the" + (past == null ? "" : (past ? " past" : " future")) + " worldborder size";
    }
}
