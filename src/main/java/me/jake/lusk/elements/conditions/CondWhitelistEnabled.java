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
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Whitelist is Enabled")
@Description("Checks whether the server has a whitelist or not.")
@Examples({"""
        """})
@Since("1.0.2")
public class CondWhitelistEnabled extends Condition {
    static {
        Skript.registerCondition(CondWhitelistEnabled.class, "[the] whitelist is enabled",
                    "[the] whitelist is disabled");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the whitelist is " + (isNegated() ? "disabled" : "enabled");
    }

    @Override
    public boolean check(@NotNull Event event) {
        return (isNegated()) ^ Bukkit.hasWhitelist();
    }
}