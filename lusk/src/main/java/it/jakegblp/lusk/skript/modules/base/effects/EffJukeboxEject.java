package it.jakegblp.lusk.skript.modules.base.effects;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
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

@Name("Jukebox - Eject Disc")
@Description("Forces a jukebox to eject its disc.")
@Example("eject the music disc of target block")
@Since("1.0.3, 1.3 (Blockstate)")
public class EffJukeboxEject extends Effect {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.effect(EffJukeboxEject.class, EffJukeboxEject::new)
                .patterns("eject [the] [music] (disc|record) (of|from) %blockstates%",
                        "eject %blockstates%'[s] [music] (disc|record)")
                .register(syntaxRegistry);
    }

    private Expression<BlockState> blockStateExpression;

    @Override
    protected void execute(@NotNull Event event) {
        for (BlockState state : blockStateExpression.getAll(event))
            if (state instanceof Jukebox jukebox)
                jukebox.eject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        blockStateExpression = (Expression<BlockState>) expressions[0];
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "eject the music disc of " + blockStateExpression.toString(event, debug);
    }
}