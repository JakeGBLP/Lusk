package it.jakegblp.lusk.elements.packets.entities.player.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.utils.EntityUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.NMSUtils.NMS;

@Name("Packets | Player - Block Destruction Progress")
@Description("""
Sends a block destruction progress packet for the block to the provided players.
Progress must range from 0 to 9, any number outside of that will result in the removal of the break progress.

If an entity is provided, its protocol id is used; if an integer is provided, that will be the id; if neither is provided, the id will be a random integer.
If your current system uses block breaking progress from 0 to 1, then the conversion is simply to multiply it by 10 and subtract 1
""")
@Examples("make player see break progress of {_block} as 5")
@Since("1.4")
public class EffPacketPlayerBlockDestruction extends Effect {

    static {
        if (NMS != null)
            Skript.registerEffect(EffPacketPlayerBlockDestruction.class,
                    "make %players% see [block] (break|damage|destruction) progress of %block% as %number% [with id %number%|by %entity%]",
                    "send block (break|damage|destruction) [progress] packet for %block% with progress %number% [and [with] id %number%|by %entity%] to %players%"
        );
    }

    private Expression<Player> playerExpression;
    private Expression<Number> progressExpression, idExpression;
    private Expression<Entity> entityExpression;
    private Expression<Block> blockExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (matchedPattern == 0) {
            playerExpression = (Expression<Player>) expressions[0];
            blockExpression = (Expression<Block>) expressions[1];
            progressExpression = (Expression<Number>) expressions[2];
            idExpression = (Expression<Number>) expressions[3];
            entityExpression = (Expression<Entity>) expressions[4];
        } else {
            blockExpression = (Expression<Block>) expressions[0];
            progressExpression = (Expression<Number>) expressions[1];
            idExpression = (Expression<Number>) expressions[2];
            entityExpression = (Expression<Entity>) expressions[3];
            playerExpression = (Expression<Player>) expressions[4];
        }
        return true;
    }

    @Override
    protected void execute(Event event) {
        assert NMS != null;
        Integer id = null;
        Number tempId = idExpression.getSingle(event),
               progress = progressExpression.getSingle(event);
        if (progress == null) return;
        Entity entity = entityExpression.getSingle(event);
        Block block = blockExpression.getSingle(event);
        if (tempId != null) id = tempId.intValue();
        else if (entity != null) id = EntityUtils.getEntityId(entity);
        NMS.sendBlockDestructionPacket(id, block, progress.intValue(), playerExpression.getAll(event));
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "make " + playerExpression.toString(event, debug) + " see block destruction progress of "
                + blockExpression.toString(event, debug) + " as " + progressExpression.toString(event, debug)
                + (idExpression != null ? " with id " + idExpression.toString(event, debug)
                : (entityExpression != null ? " by " + entityExpression.toString(event, debug) : ""));
    }

}
