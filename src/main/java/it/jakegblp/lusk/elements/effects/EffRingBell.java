package it.jakegblp.lusk.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.block.Bell;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Bell - Ring")
@Description("Rings a bell.\n The blockface must be cartesian.")
@Examples({"ring target block","make {_p} ring {_block} from north"})
@Since("1.1")
public class EffRingBell extends Effect {
    static {
        Skript.registerEffect(EffRingBell.class,
                "[make %-entity%] ring %block% [from %-blockface%]");
    }

    private Expression<Entity> entityExpression;
    private Expression<Block> blockExpression;
    private Expression<BlockFace> blockFaceExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        entityExpression = (Expression<Entity>) expressions[0];
        blockExpression = (Expression<Block>) expressions[1];
        blockFaceExpression = (Expression<BlockFace>) expressions[2];
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        boolean e = event != null;
        return "make "+(e?entityExpression.toString(event,debug):"")+" ring "+(e?blockExpression.toString(event,debug):"")+" from "+(e?blockFaceExpression.toString(event,debug):"");
    }

    @Override
    protected void execute(@NotNull Event event) {
        Block block = blockExpression.getSingle(event);
        if (block != null && block.getState() instanceof Bell bell) {
            Entity entity = entityExpression != null ? entityExpression.getSingle(event) : null;
            BlockFace blockFace = null;
            if (blockFaceExpression != null) {
                blockFace = blockFaceExpression.getSingle(event);
                if (blockFace != null && !blockFace.isCartesian()) blockFace = null;
            }
            if (entity == null) bell.ring(blockFace);
            else bell.ring(entity,blockFace);
        }
    }
}