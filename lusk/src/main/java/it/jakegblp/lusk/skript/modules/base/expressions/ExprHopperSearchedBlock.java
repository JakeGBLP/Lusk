package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.Skript;
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
import org.bukkit.event.Event;
import org.bukkit.event.inventory.HopperInventorySearchEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Hopper Searched Block")
@Description("The block searched in the Hopper Inventory Search Event.")
@Example("""
        on hopper inventory search:
            broadcast the searched block""")
@Since("1.0.4")
public class ExprHopperSearchedBlock extends SimpleExpression<Block> {
    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.expression(ExprHopperSearchedBlock.class, ExprHopperSearchedBlock::new, Block.class)
                .patterns("[the] searched block")
                .register(syntaxRegistry);
    }

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        if (!getParser().isCurrentEvent(HopperInventorySearchEvent.class)) {
            Skript.error("This expression can only be used in the Hopper Inventory Search Event!");
            return false;
        }
        return true;
    }

    @Override
    protected Block @NotNull [] get(@NotNull Event e) {
        return new Block[]{((HopperInventorySearchEvent) e).getSearchBlock()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Block> getReturnType() {
        return Block.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the searched block";
    }
}