package it.jakegblp.lusk.skript.elements.effects.eventonly;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.events.BlockUpdateEvent;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

// todo: refine
public class BlockUpdateBlockData extends Effect {
    static {
        Skript.registerEffect(BlockUpdateBlockData.class,
                "set block data to %blockdata%"
        );
    }

    private Expression<BlockData> blockDataExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        blockDataExpression = (Expression<BlockData>) expressions[0];
        return true;
    }


    @Override
    protected void execute(Event event) {
        if(!(event instanceof BlockUpdateEvent e)){
            Skript.error("You cannot use this outside of a block update event");
            return;
        }
        e.setBlockData(blockDataExpression.getSingle(event));


    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "set the block data of the block update event";
    }


}
