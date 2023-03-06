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

@Name("Sever allows Nether/End")
@Description("Checks if the server allows the Nether or the End")
@Examples({"if the server does not allow the end:\n\tbroadcast \"No End here!\""})
@Since("1.0.0")
public class CondAllows extends Condition {

    static {
        Skript.registerCondition(CondAllows.class, "[the] server ([does[negated:(n't| not)]] allow|allow[s]) [the] (:nether|end)");
    }

    private boolean nether;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        nether = parser.hasTag("nether");
        setNegated(parser.hasTag("negated"));
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "server allows nether/end";
    }

    @Override
    public boolean check(@NotNull Event event) {
        if (nether) {
            return isNegated() ^ Bukkit.getServer().getAllowNether();
        } else {
            return isNegated() ^ Bukkit.getServer().getAllowEnd();
        }
    }
}