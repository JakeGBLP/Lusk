package it.jakegblp.lusk.skript.elements.guardianbeam;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.common.Instances;
import it.jakegblp.lusk.nms.guardian.GuardianBeam;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

@Name("Persistentcy of a beam")
@Description("Returns a boolean if a beam is persistent to players")
@Examples({"persistentcy of guardian beam with id \"test\""})
@Keywords({"packets", "packet", "protocol", "dispatch", "sync", "async", "guardian", "beam"})
@Since("1.0.0")
public class ExprBeamPersistent extends SimpleExpression<Boolean> {

    static {
        Skript.registerExpression(ExprBeamPersistent.class, Boolean.class, ExpressionType.SIMPLE,
                "persistent[cy] of [guardian] beam (with|of) [id] %string%");
    }

    private Expression<String> idExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        idExpression = (Expression<String>) expressions[0];
        return true;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    protected Boolean[] get(Event event) {
        final String id = idExpression.getSingle(event);
        final GuardianBeam beam = GuardianBeam.getBeam(id);
        if(beam == null) {
            Instances.LUSK.getLogger().log(Level.SEVERE, "beam with id " + id + " does not exist");
            return null;
        }

        return new Boolean[]{beam.isPersistent()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "all guardian beams";
    }


}
