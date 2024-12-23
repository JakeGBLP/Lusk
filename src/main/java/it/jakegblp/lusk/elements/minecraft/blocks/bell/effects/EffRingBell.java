package it.jakegblp.lusk.elements.minecraft.blocks.bell.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.api.BlockWrapper;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Bell - Ring")
@Description("Rings a bell.\nThe blockface must be cartesian.\n\n**NOTE: Skript has its own version of this effect, difference is in the fact that Skript's uses Directions while Lusk's uses Blockfaces, for that reason I will not deprecate this Effect.**")
@Examples({"ring target block", "make {_p} ring {_block} from north"})
@Since("1.1, 1.3 (Plural, BlockStates)")
public class EffRingBell extends Effect {

    static {
        Skript.registerEffect(EffRingBell.class, "[make %-entity%] ring %blocks/blockstates% [from %-blockface%]");
    }

    private Expression<Entity> entityExpression;
    private Expression<Object> blockExpression;
    private Expression<BlockFace> blockFaceExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        entityExpression = (Expression<Entity>) expressions[0];
        blockExpression = (Expression<Object>) expressions[1];
        blockFaceExpression = (Expression<BlockFace>) expressions[2];
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "make " + entityExpression.toString(event, debug) + " ring " + blockExpression.toString(event, debug) + " from " + blockFaceExpression.toString(event, debug);
    }

    @Override
    protected void execute(@NotNull Event event) {
        for (Object o : blockExpression.getAll(event)) {
            new BlockWrapper(o).ringBell(
                    entityExpression != null ? entityExpression.getSingle(event) : null,
                    blockFaceExpression != null ? blockFaceExpression.getSingle(event) : null
            );
        }
    }
}