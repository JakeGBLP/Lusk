package it.jakegblp.lusk.elements.minecraft.blockface.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;


@Name("BlockFace - Between Blocks")
@Description("""
Returns the face relation of a block compared to another block.

If the blocks are not connected nothing will be returned.
""")
@Examples(
        {
                """
                set {_A} to block at location(10,10,10,"world")
                set {_B} to block at location(10,11,10,"world") # 1 block above
                
                set {_blockFace} to blockface from {_A} to {_B}
                
                broadcast {_blockFace} # 'up face'
                """
        })
@Since("1.2")
public class ExprBlockFaceBetween extends SimpleExpression<BlockFace> {
    static {
        Skript.registerExpression(ExprBlockFaceBetween.class, BlockFace.class, ExpressionType.COMBINED,
                "[the] [block[ ]]face from %block% to %block%");
    }
    Expression<Block> fromExpression;
    Expression<Block> toExpression;

    @Override
    protected BlockFace @NotNull [] get(@NotNull Event event) {
        Block from = fromExpression.getSingle(event);
        if (from != null) {
            Block to = toExpression.getSingle(event);
            if (to != null) {
                return new BlockFace[] {from.getFace(to)};
            }
        }
        return new BlockFace[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends BlockFace> getReturnType() {
        return BlockFace.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the blockface from "+(event != null ? fromExpression.toString(event, debug) : "")+" to "+(event != null ? toExpression.toString(event, debug) : "");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        fromExpression  = (Expression<Block>) expressions[0];
        toExpression = (Expression<Block>) expressions[1];
        return true;
    }
}
