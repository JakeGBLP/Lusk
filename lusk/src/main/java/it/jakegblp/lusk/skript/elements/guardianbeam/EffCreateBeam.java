package it.jakegblp.lusk.skript.elements.guardianbeam;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.common.Instances;
import it.jakegblp.lusk.nms.guardian.GuardianBeam;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Create Guardian Beam")
@Description("Creates a new client sided guardian beam\nThese do not persist after reboots\nIf a player leaves and rejoin they WILL continue to see the beam if you use the persistent option.")
@Examples({""})
@Keywords({"packets", "packet", "protocol", "dispatch", "sync", "async", "guardian", "beam"})
@Since("1.0.0")
public class EffCreateBeam extends Effect {

    static {
        Skript.registerEffect(EffCreateBeam.class, "create [a] [new] guardian beam [(at|from)] %location% to %location% for %players% [with] id %string% [:persistently]");
    }

    private Expression<Location> fromExpression;
    private Expression<Location> toExpression;
    private Expression<Player> playerExpression;
    private Expression<String> idExpression;
    private boolean persistent;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        fromExpression = (Expression<Location>) expressions[0];
        toExpression = (Expression<Location>) expressions[1];
        playerExpression = (Expression<Player>) expressions[2];
        idExpression = (Expression<String>) expressions[3];
        persistent = parseResult.hasTag("persistently");
        return true;
    }

    @Override
    protected void execute(Event event) {
        final Player[] players = playerExpression.getArray(event);

        final GuardianBeam guardianBeam = new GuardianBeam(Instances.LUSK, idExpression.getSingle(event), fromExpression.getSingle(event), toExpression.getSingle(event), persistent);

        for (Player player : players)
            guardianBeam.addViewer(player.getUniqueId());
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "create a guardian beam";
    }

}
