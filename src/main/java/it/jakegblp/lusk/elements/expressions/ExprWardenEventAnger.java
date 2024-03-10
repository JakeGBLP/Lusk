package it.jakegblp.lusk.elements.expressions;

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
import io.papermc.paper.event.entity.WardenAngerChangeEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Warden Anger (Event)")
@Description("Returns the past/future anger of the warden in the Warden Anger Change event.\nThe future anger level can be set.")
@Examples({""})
@Since("1.0.1")
public class ExprWardenEventAnger extends SimpleExpression<Integer> {
    static {
        if (Skript.classExists("io.papermc.paper.event.entity.WardenAngerChangeEvent")) {
            Skript.registerExpression(ExprWardenEventAnger.class, Integer.class, ExpressionType.SIMPLE,
                    "[the] [future|:past] anger [level] [of [the] warden]");
        }
    }

    private boolean past;

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(WardenAngerChangeEvent.class)) {
            Skript.error("This expression can only be used in the Warden Anger Change Event!");
            return false;
        }
        past = parseResult.hasTag("past");
        return true;
    }

    @Override
    protected Integer @NotNull [] get(@NotNull Event e) {
        int i = past ? ((WardenAngerChangeEvent) e).getOldAnger() : ((WardenAngerChangeEvent) e).getNewAnger();
        return new Integer[]{i};
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Integer.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Integer integer)
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
        return "the" + (past ? " past" : " future") + " anger level of the warden";
    }
}
