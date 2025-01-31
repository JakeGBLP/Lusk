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
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static ch.njol.skript.paperlib.PaperLib.isPaper;

@Name("Server - is Running Paper")
@Description("Checks whether the server is running PaperMC.")
@Examples("if the server is running paper:")
@Since("1.4")
public class CondServerIsRunningPaper extends Condition {
    static {
        Skript.registerCondition(CondServerIsRunningPaper.class,
                "[[the] server is] running paper[mc]",
                "([the] server is(n't| not)|not) running paper[mc]");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "server is " + (isNegated() ? "not " : "") + "running paper";
    }

    @Override
    public boolean check(@NotNull Event event) {
        return isNegated() ^ isPaper();
    }
}