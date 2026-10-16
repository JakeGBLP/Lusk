package it.jakegblp.lusk.skript.modules.base.effects;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.block.BlockState;
import org.bukkit.block.Jukebox;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Jukebox - Start/Stop Playing")
@Description("Makes a jukebox start/stop playing.")
@Examples("make target block start playing")
@Since("1.0.3, 1.3 (Blockstate)")
public class EffJukeboxPlay extends Effect {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.effect(EffJukeboxPlay.class, EffJukeboxPlay::new)
                .patterns("make %blockstates% start playing", "make %blockstates% stop playing")
                .register(syntaxRegistry);
    }

    private Expression<BlockState> blockStateExpression;
    private boolean stop;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        blockStateExpression = (Expression<BlockState>) expressions[0];
        stop = matchedPattern == 1;
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "make " + blockStateExpression.toString(event, debug) + (stop ? " stop" : " start") + " playing";
    }

    @Override
    protected void execute(@NotNull Event event) {
        for (BlockState state : blockStateExpression.getAll(event))
            if (state instanceof Jukebox jukebox)
                if (stop)
                    jukebox.stopPlaying();
                else
                    jukebox.startPlaying();
    }
}