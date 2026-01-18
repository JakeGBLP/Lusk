package it.jakegblp.lusk.skript.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.AbstractNMS;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Player - Spin Attack")
public class EffSpinAttack extends Effect {

    static {
        Skript.registerEffect(EffSpinAttack.class,
                "make [player[s]] %players% spin attack for %timespan%"
        );
    }

    private Expression<Player> playerExpression;
    private Expression<Timespan> timespanExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        playerExpression = (Expression<Player>) expressions[0];
        timespanExpression = (Expression<Timespan>) expressions[1];
        return true;
    }

    @Override
    protected void execute(Event event) {
        long ticks = timespanExpression.getSingle(event).getAs(Timespan.TimePeriod.TICK);
        for (Player player : playerExpression.getArray(event)) {
            AbstractNMS.NMS.setPlayerSpinAttack(player, (int) ticks);
        }

    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "spin attack";
    }
}
