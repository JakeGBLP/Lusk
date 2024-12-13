package it.jakegblp.lusk.elements.minecraft.blocks.hopper.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.HopperInventorySearchEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.EVENT_OR_SIMPLE;
import static it.jakegblp.lusk.utils.Constants.HAS_HOPPER_INVENTORY_SEARCH_EVENT;

@Name("Hopper - Searched Block")
@Description("Searched Block expression for the Hopper Inventory Search Event.")
@Examples({"on inventory search:\n\tbroadcast the searched block"})
@Since("1.0.4")
@SuppressWarnings("unused")
public class ExprHopperSearchedBlock extends SimpleExpression<Block> {
    static {
        if (HAS_HOPPER_INVENTORY_SEARCH_EVENT) {
            Skript.registerExpression(ExprHopperSearchedBlock.class, Block.class, EVENT_OR_SIMPLE,
                    "[the] searched block");
        }
    }

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
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
