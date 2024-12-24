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

@Name("Jukebox - Eject Disc")
@Description("Forces a jukebox to eject its disc.")
@Examples({"eject the music disc of target block"})
@Since("1.0.3, 1.3 (Blockstate)")
@DocumentationId("9131")
public class EffJukeboxEject extends Effect {
    static {
        Skript.registerEffect(EffJukeboxEject.class, "eject [the] [music] (disc|record) (of|from) %blocks/blockstates%",
                "eject %blocks/blockstates%'[s] [music] (disc|record)");
    }

    private Expression<Object> objectExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        objectExpression = (Expression<Object>) expressions[0];
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "eject the music disc of " + objectExpression.toString(event, debug);
    }

    @Override
    protected void execute(@NotNull Event event) {
        for (Object o : objectExpression.getAll(event)) {
            new BlockWrapper(o).ejectJukeboxDisc();
        }
    }
}