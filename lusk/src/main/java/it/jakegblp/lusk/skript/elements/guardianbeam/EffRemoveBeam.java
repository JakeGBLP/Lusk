package it.jakegblp.lusk.skript.elements.guardianbeam;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.guardian.GuardianBeam;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Remove Guardian Beam")
@Description("Remove a client side guardian beam")
@Examples({""})
@Keywords({"packets", "packet", "protocol", "dispatch", "sync", "async", "guardian", "beam"})
@Since("1.0.0")
public class EffRemoveBeam extends Effect {

    static {
        Skript.registerEffect(EffRemoveBeam.class, "remove guardian beam [with] [id] %strings%");
    }

    private Expression<String> idExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        idExpression = (Expression<String>) expressions[0];
        return true;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    protected void execute(Event event) {
        final String[] id = idExpression.getArray(event);

        for (String s : id) {
            final GuardianBeam beam = GuardianBeam.getBeam(s);
            if(beam == null)
                continue;

            beam.destroy();
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "remove a guardian beam";
    }

}
