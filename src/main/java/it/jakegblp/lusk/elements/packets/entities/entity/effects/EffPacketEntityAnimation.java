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
import it.jakegblp.lusk.api.enums.EntityAnimation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.NMSUtils.NMS;

@Name("Packets | Entity - Play Entity Animation")
@Description("""
Sends an animation packet with the provided entity animation for the entities to the provided players.
""")
@Examples("send entity wake up animation packet for target to player")
@Since("1.4")
public class EffPacketEntityAnimation extends Effect {

    static {
        if (NMS != null)
            Skript.registerEffect(EffPacketEntityAnimation.class,
                    "play entity %entityanimation% for %entities% to %players%",
                    "send entity %entityanimation% packet for %entities% to %players%"
        );
    }

    private Expression<EntityAnimation> entityAnimationExpression;
    private Expression<Entity> entityExpression;
    private Expression<Player> playerExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        entityAnimationExpression = (Expression<EntityAnimation>) expressions[0];
        entityExpression = (Expression<Entity>) expressions[1];
        playerExpression = (Expression<Player>) expressions[2];
        return true;
    }

    @Override
    protected void execute(Event event) {
        assert NMS != null;
        EntityAnimation entityAnimation = entityAnimationExpression.getSingle(event);
        for (Entity entity : entityExpression.getAll(event))
            NMS.sendEntityAnimationPacket(entity, entityAnimation, playerExpression.getAll(event));
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "send entity " + entityAnimationExpression.toString(event, debug) + " packet for "
                + entityExpression.toString(event, debug) + " to " + playerExpression.toString(event, debug);
    }

}
