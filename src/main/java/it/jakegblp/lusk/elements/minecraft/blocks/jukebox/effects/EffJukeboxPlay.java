package it.jakegblp.lusk.elements.minecraft.blocks.jukebox.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.api.BlockWrapper;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Jukebox - Start/Stop Playing")
@Description("Makes a jukebox start/stop playing.")
@Examples({"make target block start playing"})
@Since("1.0.3, 1.3 (Blockstate)")
@DocumentationId("9132")
public class EffJukeboxPlay extends Effect {
    static {
        Skript.registerEffect(EffJukeboxPlay.class, "make %blocks/blockstates% (:stop|start) playing",
                "make %blocks/blockstates% stop playing");
    }

    private Expression<Object> objectExpression;
    private boolean stop;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        objectExpression = (Expression<Object>) expressions[0];
        stop = parser.hasTag("stop");
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "make " + objectExpression.toString(event, debug) + (stop ? " stop" : " start") + " playing";
    }

    @Override
    protected void execute(@NotNull Event event) {
        for (Object o : objectExpression.getAll(event)) {
            BlockWrapper block = new BlockWrapper(o);
            if (stop) {
                block.stopPlayingJukebox();
            } else {
                block.startPlayingJukebox();
            }
        }
    }
}