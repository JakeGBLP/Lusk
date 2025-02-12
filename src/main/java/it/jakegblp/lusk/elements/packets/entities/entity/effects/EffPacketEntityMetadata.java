package it.jakegblp.lusk.elements.packets.entities.entity.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.NMSUtils.NMS;

@Name("Packets | Entity - Metadata")
@Description("""
Sends a block destruction progress packet for the block to the provided players.
Progress must range from 0 to 9, any number outside of that will result in the removal of the break progress.

If an entity is provided, its protocol id is used; if an integer is provided, that will be the id; if neither is provided, the id will be a random integer.
If your current system uses block breaking progress from 0 to 1, then the conversion is simply to multiply it by 10 and subtract 1
""")
@Examples("make player see break progress of {_block} as 5")
@Since("1.4")
public class EffPacketEntityMetadata extends Effect {
    //todo: fix this, it doesnt actually work.
    static {
        if (NMS != null)
            Skript.registerEffect(EffPacketEntityMetadata.class,
                    "make %players% see entity with id %number% with metadata"
            );
    }

    private Expression<Player> playerExpression;
    private Expression<Number> idExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        playerExpression  = (Expression<Player>) expressions[0];
        idExpression = (Expression<Number>) expressions[1];
        return true;
    }

    @Override
    protected void execute(Event event) {
        assert NMS != null;
        Number id = idExpression.getSingle(event);
        if (id == null) return;
        NMS.sendEntityMetadataPacket(id.intValue(), playerExpression.getAll(event));
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "make " + playerExpression.toString(event, debug) + " see entity with id "+ idExpression.toString(event, debug) + " with metadata";
    }

}
