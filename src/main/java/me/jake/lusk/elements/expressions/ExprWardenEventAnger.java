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
import io.papermc.paper.event.entity.WardenAngerChangeEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Warden Anger (Event)")
@Description("Returns the new/old anger of the warden in the Warden Anger Change event.\nCan be set.")
@Examples({""})
@Since("1.0.1")
public class ExprWardenEventAnger extends SimpleExpression<Integer> {
    static {
        if (Skript.classExists("io.papermc.paper.event.entity.WardenAngerChangeEvent")) {
            Skript.registerExpression(ExprWardenEventAnger.class, Integer.class, ExpressionType.SIMPLE,
                    "[the] old anger [level] [of [the] warden]",
                    "[the] new anger [level] [of [the] warden]",
                    "[the] anger [level] [of [the] warden]");
        }
    }

    private Boolean past;

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(WardenAngerChangeEvent.class)) {
            Skript.error("This expression can only be used in the Warden Anger Change Event!");
            return false;
        }
        if (matchedPattern == 0) {
            past = true;
        } else if (matchedPattern == 1) {
            past = false;
        }
        return true;
    }
    @Override
    protected Integer @NotNull [] get(@NotNull Event e) {
        int i;
        if (!past) {
            i = ((WardenAngerChangeEvent) e).getNewAnger();
        } else {
            i = ((WardenAngerChangeEvent) e).getOldAnger();
        }
        return new Integer[]{i};
    }
    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Integer[].class);
        }
        return new Class[0];
    }
    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Integer integer = delta instanceof Integer[] ? ((Integer[]) delta)[0] : null;
        if (integer == null) return;
        ((WardenAngerChangeEvent) e).setNewAnger(integer);
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the" + (past == null ? "" : (past ? " past" : " future")) + " anger level of the warden";
    }
}
