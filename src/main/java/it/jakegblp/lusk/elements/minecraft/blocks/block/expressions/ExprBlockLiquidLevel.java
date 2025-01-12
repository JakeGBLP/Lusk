package it.jakegblp.lusk.elements.minecraft.blocks.block.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.api.BlockWrapper;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.jetbrains.annotations.Nullable;

import static ch.njol.skript.paperlib.PaperLib.isPaper;
import static it.jakegblp.lusk.utils.Constants.PAPER_HAS_1_18_2_EXTENDED_ENTITY_API;
import static it.jakegblp.lusk.utils.LuskUtils.getKleenean;

@Name("Block - Liquid Level")
@Description("""
Represents the maximum, minimum or current amount of fluid contained within a block, either by itself or inside a cauldron.
Minimum Level requires Paper.

This expression includes liquid blocks, cauldron and composters.

For water and lava blocks the levels have special meanings: a level of 0 corresponds to a source block, 1-7 regular fluid heights, and 8-15 to "falling" fluids.
All falling fluids have the same behaviour, but the level corresponds to that of the block above them, equal to this. level - 8

**Note that counterintuitively, an adjusted level of 1 is the highest level, whilst 7 is the lowest.**

May not be higher than the max level.

Can be set, added to, removed from and reset.
Reset requires Paper.
""")
@Examples({"broadcast max block level of event-block", "set block level of {_block} to 3"})
@Since("1.3.2")
public class ExprBlockLiquidLevel extends SimplerPropertyExpression<Object,Integer> {

    static {
        register(ExprBlockLiquidLevel.class, Integer.class, "["+(PAPER_HAS_1_18_2_EXTENDED_ENTITY_API ? " (:max|:min)" : "max:max")+"[imum]] block [liquid] level", "blocks/itemtypes/blockdatas/blockstates");
    }

    Kleenean max;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        max = getKleenean(parseResult.hasTag("max"), parseResult.hasTag("min"));
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @Nullable Integer convert(Object from) {
        BlockWrapper blockWrapper = new BlockWrapper(from);
        return switch (max) {
            case TRUE -> blockWrapper.getMaxLiquidLevel();
            case FALSE -> blockWrapper.getMinLiquidLevel();
            case UNKNOWN -> blockWrapper.getLiquidLevel();
        };
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public boolean allowReset() {
        return isPaper();
    }

    @Override
    public boolean allowAdd() {
        return true;
    }

    @Override
    public boolean allowRemove() {
        return true;
    }

    @Override
    public void set(Object from, Integer to) {
        new BlockWrapper(from).setLiquidLevel(to);
    }

    @Override
    public void remove(Object from, Integer to) {
        add(from, -to);
    }

    @Override
    public void reset(Object from) {
        BlockWrapper blockWrapper = new BlockWrapper(from);
        blockWrapper.setLiquidLevel(blockWrapper.getMinLiquidLevel());
    }

    @Override
    public void add(Object from, Integer to) {
        BlockWrapper blockWrapper = new BlockWrapper(from);
        blockWrapper.setLiquidLevel(Math.min(blockWrapper.getLiquidLevel() + to,blockWrapper.getMaxLiquidLevel()));
    }

    @Override
    protected String getPropertyName() {
        return switch (max) {
            case FALSE -> "min ";
            case UNKNOWN -> "";
            case TRUE -> "max ";
        } + "block level";
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }
}
