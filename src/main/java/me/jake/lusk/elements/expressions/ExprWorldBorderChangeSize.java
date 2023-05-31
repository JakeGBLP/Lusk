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
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import io.papermc.paper.event.world.border.WorldBorderBoundsChangeEvent;
import io.papermc.paper.event.world.border.WorldBorderBoundsChangeFinishEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("WorldBorder - New/Old Size")
@Description("Returns the new/old anger of the warden in the World Border Change events.\nCan be set only within the Start variant of the event.")
@Examples({""})
@Since("1.0.2")
public class ExprWorldBorderChangeSize extends SimpleExpression<Double> {
    static {
        if (Skript.classExists("io.papermc.paper.event.world.border.WorldBorderEvent")) {
            Skript.registerExpression(ExprWorldBorderChangeSize.class, Double.class, ExpressionType.SIMPLE,
                    "[the] old size",
                    "[the] new size",
                    "[the] size");
        }
    }

    private Boolean past;
    private boolean set;

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (getParser().isCurrentEvent(WorldBorderBoundsChangeEvent.class)) {
            if (matchedPattern == 0) {
                past = true;
            } else if (matchedPattern == 1) {
                past = false;
            }
            set = true;
            return true;
        } else if (getParser().isCurrentEvent(WorldBorderBoundsChangeFinishEvent.class)) {
            if (matchedPattern == 0) {
                past = true;
            } else if (matchedPattern == 1) {
                past = false;
            }
            set = false;
            return true;
        }

        Skript.error("This expression can only be used in the World Border Change events.!");
        return false;
    }

    @Override
    protected Double @NotNull [] get(@NotNull Event e) {
        Double i = null;
        if (e instanceof WorldBorderBoundsChangeEvent event) {
            if (past) {
                i = event.getOldSize();
            } else {
                i = event.getNewSize();
            }
        } else if (e instanceof WorldBorderBoundsChangeFinishEvent event) {
            if (past) {
                i = event.getOldSize();
            } else {
                i = event.getNewSize();
            }
        }
        if (i != null) {
            return new Double[]{i};
        }
        return new Double[0];
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (set) {
            if (mode == Changer.ChangeMode.SET) {
                return CollectionUtils.array(Double[].class);
            }
        }
        return new Class[0];
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Double aDouble = delta instanceof Double[] ? ((Double[]) delta)[0] : null;
        if (aDouble == null) return;
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
        return "the" + (past == null ? "" : (past ? " past" : " future")) + " size";
    }
}
