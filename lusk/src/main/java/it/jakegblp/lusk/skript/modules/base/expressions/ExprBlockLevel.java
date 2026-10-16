package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import it.jakegblp.lusk.skript.api.syntax.expression.SimplerPropertyExpression;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import it.jakegblp.lusk.skript.utils.BlockDataHolder;
import org.bukkit.block.data.Levelled;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Block - Liquid Level")
@Description("""
        Represents the maximum, minimum or current amount of fluid contained within a block, either by itself or inside a cauldron.
        Minimum Level requires Paper and 1.18.2.
        
        This expression includes liquid blocks, cauldron and composters.
        
        For water and lava blocks the levels have special meanings: a level of 0 corresponds to a source block, 1-7 regular fluid heights, and 8-15 to "falling" fluids.
        All falling fluids have the same behaviour, but the level corresponds to that of the block above them, equal to this. level - 8
        
        **Note that counterintuitively, an adjusted level of 1 is the highest level, whilst 7 is the lowest.**
        
        May not be higher than the max level.
        
        Can be set, added to, removed from and reset.
        Reset requires Paper and 1.18.2.
        """)
@Example("broadcast max block level of event-block")
@Example("set block level of {_block} to 3")
@Since("1.3.2")
public class ExprBlockLevel extends SimplerPropertyExpression<BlockDataHolder<?>, Integer> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprBlockLevel.class, ExprBlockLevel::new, Integer.class)
                .propertyOf("[(:max|:min)[imum]] block level", "blockdataholders")
                .defaultExpression(true)
                .register(syntaxRegistry);
    }

    private Kleenean max;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        max = AddonUtils.getKleenean(parseResult.hasTag("max"), parseResult.hasTag("min"));
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @Nullable Integer convert(BlockDataHolder from) {
        return from.getBlockData() instanceof Levelled levelled ? switch (max) {
            case TRUE -> levelled.getMaximumLevel();
            case FALSE -> levelled.getMinimumLevel();
            case UNKNOWN -> levelled.getLevel();
        } : null;
    }

    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case ADD, REMOVE, SET, RESET -> new Class[] {Integer.class};
            default -> null;
        };
    }

    @Override
    public void set(BlockDataHolder<?> from, Integer to) {
        from.editBlockData(Levelled.class, levelled -> levelled.setLevel(Math.clamp(to, levelled.getMinimumLevel(), levelled.getMaximumLevel())));
    }

    @Override
    public void remove(BlockDataHolder<?> from, Integer to) {
        add(from, -to);
    }

    @Override
    public void reset(BlockDataHolder<?> from) {
        if (from.getBlockData() instanceof Levelled levelled)
            levelled.setLevel(levelled.getMinimumLevel());
    }

    @Override
    public void add(BlockDataHolder<?> from, Integer to) {
        if (from.getBlockData() instanceof Levelled levelled)
            set(from, levelled.getLevel() + to);
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
