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

import javax.annotation.Nullable;

@Name("JukeBox - Eject Disc")
@Description("Forces a jukebox to eject its disc.")
@Examples({"eject the record of target block"})
@Since("1.0.3")
public class EffJukeBoxEject extends Effect {
    static {
        Skript.registerEffect(EffJukeBoxEject.class, "eject [the] [music] (disc|record) (of|from) %blocks%",
                "eject %blocks%'[s] [music] (disc|record)");
    }

    private Expression<Block> blockExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        blockExpression = (Expression<Block>) expressions[0];
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "eject the record of " + (event == null ? "" : blockExpression.toString(event, debug));
    }

    @Override
    protected void execute(@NotNull Event event) {
        Block[] blocks = blockExpression.getArray(event);
        for (Block block : blocks) {
            if (block.getState() instanceof Jukebox jukebox) {
                jukebox.eject();
            }
        }
    }
}