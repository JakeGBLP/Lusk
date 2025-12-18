package it.jakegblp.lusk.skript.elements.guardianbeam;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.guardian.GuardianBeam;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Add player to Guardian Beam")
@Description("Add a player to a client side guardian beam")
@Examples({"""
        on join:
            add player to guardian beam with id "test"
        """})
@Keywords({"packets", "packet", "protocol", "dispatch", "sync", "async", "guardian", "beam"})
@Since("1.0.0")
public class EffAddPlayerToBeam extends Effect {

    static {
        Skript.registerEffect(EffAddPlayerToBeam.class, "add player['s] %player% to [guardian] beam [with] [id] %string%");
    }

    private Expression<Player> playerExpression;
    private Expression<String> idExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        playerExpression = (Expression<Player>) expressions[0];
        idExpression = (Expression<String>) expressions[1];
        return true;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    protected void execute(Event event) {
        final GuardianBeam beam = GuardianBeam.getBeam(idExpression.getSingle(event));
        if(beam == null)
            return;

        for (Player player : playerExpression.getArray(event)) {
            beam.addViewer(player.getUniqueId());
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "add player to a guardian beam";
    }

}
