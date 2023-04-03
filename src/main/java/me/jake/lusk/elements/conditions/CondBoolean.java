package me.jake.lusk.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Name("Boolean")
@Description("Returns if the given object is true or false.")
@Examples({"if {isJailed::%uuid of player%}:\n\tbroadcast \"%player% is Jailed!\""})
@Since("1.0.0")
public class CondBoolean extends Condition {
    static {
        Skript.registerCondition(CondBoolean.class, "%boolean%", "!%boolean%");
    }

    private Expression<Boolean> bool;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        bool = (Expression<Boolean>) expressions[0];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        assert event != null;
        return (isNegated() ? "!" : "") + Objects.requireNonNull(bool.getSingle(event));
    }

    @Override
    public boolean check(@NotNull Event event) {
        return Boolean.TRUE.equals(bool.getSingle(event));
    }
}