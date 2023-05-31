package me.jake.lusk.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("JukeBox - Start/Stop Playing")
@Description("Makes a jukebox start/stop playing.")
@Examples({"""
        eject target block"""})
@Since("1.0.3")
public class EffJukeBoxPlay extends Effect {
    static {
        Skript.registerEffect(EffJukeBoxPlay.class, "make %blocks% start playing",
                "make %blocks% stop playing");
    }

    private Expression<Block> blockExpression;
    private boolean start;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        blockExpression = (Expression<Block>) expressions[0];
        start = matchedPattern == 0;
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "make " + (event == null ? "" : blockExpression.getArray(event)) + (start ? " start" : " stop") + " playing";
    }

    @Override
    protected void execute(@NotNull Event event) {
        Block[] blocks = blockExpression.getArray(event);
        for (Block block : blocks) {
            if (block.getState() instanceof Jukebox jukebox) {
                if (start) {
                    jukebox.startPlaying();
                } else {
                    jukebox.stopPlaying();
                }
            }
        }
    }
}