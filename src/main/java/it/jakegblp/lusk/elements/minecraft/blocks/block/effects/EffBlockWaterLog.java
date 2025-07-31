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
import it.jakegblp.lusk.api.BlockWrapper;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Block - Water Log")
@Description("Changes whether one or more blocks or blockstates are waterlogged.")
@Examples({"waterlog target block"})
@Since("1.3")
@SuppressWarnings("unused")
public class EffBlockWaterLog extends Effect {
    static {
        Skript.registerEffect(EffBlockWaterLog.class,
                "[not:un]water[ |-]log %blocks/blockdatas/blockstates/itemtypes%",
                "make %blocks/blockdatas/blockstates/itemtypes% [:not] water[ |-]logged");
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
        new BlockWrapper(blockExpression.getSingle(event), true).setWaterLogged(waterlog);
    }
}