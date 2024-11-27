package it.jakegblp.lusk.elements.minecraft.blocks.jukebox.effects;

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
import org.jetbrains.annotations.Nullable;

@Name("JukeBox - Start/Stop Playing")
@Description("Makes a jukebox start/stop playing.")
@Examples({"make target block start playing"})
@Since("1.0.3")
public class EffJukeBoxPlay extends Effect {
    static {
        Skript.registerEffect(EffJukeBoxPlay.class, "make %blocks% (:stop|start) playing",
                "make %blocks% stop playing");
    }

    private Expression<Block> blockExpression;
    private boolean stop;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        blockExpression = (Expression<Block>) expressions[0];
        stop = parser.hasTag("stop");
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "make " + (event == null ? "" : blockExpression.toString(event, debug)) + (stop ? " stop" : " start") + " playing";
    }

    @Override
    protected void execute(@NotNull Event event) {
        Block[] blocks = blockExpression.getArray(event);
        for (Block block : blocks) {
            if (block.getState() instanceof Jukebox jukebox) {
                if (stop) {
                    jukebox.stopPlaying();
                } else {
                    jukebox.startPlaying();
                }
            }
        }
    }
}