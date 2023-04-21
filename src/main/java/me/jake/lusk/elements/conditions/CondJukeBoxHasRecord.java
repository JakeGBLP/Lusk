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
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("JukeBox - Has Record")
@Description("Checks if a jukebox contains a music disc.")
@Examples({"if {_j} has a record:"})
@Since("1.0.3")
public class CondJukeBoxHasRecord extends Condition {
    static {
        Skript.registerCondition(CondJukeBoxHasRecord.class,
                "%block% has [a] ([music] disc|record)",
                "%entity% does(n't| not) have [a] ([music] disc|record)");
    }

    private Expression<Block> blockExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        blockExpression = (Expression<Block>) expressions[0];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (event == null ? "" : blockExpression.getSingle(event)) + (isNegated() ? " does not have" : " has") + " a record";
    }

    @Override
    public boolean check(@NotNull Event event) {
        Block block = blockExpression.getSingle(event);
        if (block.getState() instanceof Jukebox jukebox) {
            return isNegated() ^ jukebox.hasRecord();
        }
        return false;
    }
}