package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("BlockFace - Between Blocks")
@Description("""
        Returns the face relation of a block compared to another block.
        
        If the blocks are not connected nothing will be returned.
        """)
@Example("""
        set {_A} to block at location(10,10,10,"world")
        set {_B} to block at location(10,11,10,"world") # 1 block above
        
        set {_blockFace} to blockface from {_A} to {_B}
        
        broadcast {_blockFace} # 'up face'
        """)
@Since("1.2")
public class ExprBlockFaceBetween extends SimpleExpression<BlockFace> {

    protected Expression<Block> fromExpression;
    protected Expression<Block> toExpression;

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.expression(ExprBlockFaceBetween.class, ExprBlockFaceBetween::new, BlockFace.class)
                .patterns("[the] [block[ ]]face (from|between) %block% (to|and) %block%")
                .register(syntaxRegistry);
    }

    @Override
    protected BlockFace @NotNull [] get(@NotNull Event event) {
        Block from = fromExpression.getSingle(event);
        if (from == null) return new BlockFace[0];
        Block to = toExpression.getSingle(event);
        return to != null ? new BlockFace[]{from.getFace(to)} : new BlockFace[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        fromExpression = (Expression<Block>) expressions[0];
        toExpression = (Expression<Block>) expressions[1];
        return true;
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
        return "the blockface from " + fromExpression.toString(event, debug) + " to " + toExpression.toString(event, debug);
    }
}