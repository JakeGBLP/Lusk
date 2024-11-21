package it.jakegblp.lusk.elements.minecraft.blocks.block.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.utils.BlockUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Block - Water Log")
@Description("Rings a bell.\nThe blockface must be cartesian.\n\n**NOTE: Skript has its own version of this effect, difference is in the fact that Skript's uses Directions while Lusk's uses Blockfaces, for that reason I will not deprecate this Effect.**")
@Examples({"ring target block", "make {_p} ring {_block} from north"})
@Since("1.3")
@SuppressWarnings("unused")
public class EffBlockWaterLog extends Effect {
    static {
        Skript.registerEffect(EffBlockWaterLog.class,
                "[not:un]water[ |-]log %blocks/blockdatas%",
                "make %blocks/blockdatas% [:not] water[ |-]logged");
    }

    private Expression<Object> blockExpression;
    private boolean waterlog;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        blockExpression = (Expression<Object>) expressions[0];
        waterlog = !parser.hasTag("not");
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "make " + blockExpression.toString(event, debug) + (waterlog ? "" : " not") + " waterlogged";
    }

    @Override
    protected void execute(@NotNull Event event) {
        Object object = blockExpression.getSingle(event);
        BlockUtils.setWaterlogged(object, waterlog);
    }
}