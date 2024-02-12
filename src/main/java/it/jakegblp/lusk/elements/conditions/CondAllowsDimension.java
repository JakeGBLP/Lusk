package it.jakegblp.lusk.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Nether/End - is Allowed")
@Description("Checks if the server allows the Nether or the End")
@Examples({"if the server does not allow the end:\n\tbroadcast \"No End here!\""})
@Since("1.0.0, 1.0.3 (is Allowed)")
public class CondAllowsDimension extends Condition {
    static {
        Skript.registerCondition(CondAllowsDimension.class,
                "[the] server allows [the] end",
                "[the] server allows [the] nether",
                "[the] server does(n'| no)t allow [the] end",
                "[the] server does(n'| no)t allow [the] nether",
                "[the] end is allowed",
                "[the] nether is allowed",
                "[the] end is(n't| not) allowed",
                "[the] nether is(n't| not) allowed");
    }

    private boolean nether;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        nether = !Utils.isEven(matchedPattern);
        setNegated(switch (matchedPattern) {
            case 2, 3, 6, 7 -> true;
            default -> false;
        });
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the " + (nether ? "nether" : "end") + " is" + (isNegated() ? " not" : "") + " allowed";
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