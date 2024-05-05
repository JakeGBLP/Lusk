package it.jakegblp.lusk.elements.minecraft.server.conditions;

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

@Name("Server - allows Nether/End/Flight")
@Description("Checks if the server allows the Nether, the End or Flight")
@Examples({"if the server does not allow the end:\n\tbroadcast \"No End here!\""})
@Since("1.0.0, 1.0.3 (is Allowed), 1.2 (Flight)")
public class CondServerAllows extends Condition {
    static {
        Skript.registerCondition(CondServerAllows.class,
                "[the] server allows ([the] (:end|:nether)|(to fly|flight))",
                "[the] server does(n'| no)t allow ([the] (:end|:nether)|(to fly|flight))",
                "([the] (:end|:nether)|flight) is[not:(n't| not)] (allowed|enabled)");
    }

    private Boolean end;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        end = parser.hasTag("end") ? Boolean.TRUE : parser.hasTag("nether") ? false : null;
        setNegated(matchedPattern == 1 || parser.hasTag("not"));
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (end == null ? "flight" : ("the " + (end ? "end" : "nether"))) + " is" + (isNegated() ? " not" : "") + " allowed";
    }

    @Override
    public boolean check(@NotNull Event event) {
        return isNegated() ^ (end == null ? Bukkit.getAllowFlight() : end ? Bukkit.getAllowEnd() : Bukkit.getAllowNether());
    }
}