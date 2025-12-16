package it.jakegblp.lusk.skript.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.async.ExecutionMode;
import it.jakegblp.lusk.nms.core.world.player.CompletablePlayerProfile;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffPlayerProfileMakeCompletable extends Effect {

    static {
        Skript.registerEffect(EffPlayerProfileMakeCompletable.class,
                "make %completableplayerprofiles% completable [sync:[async:a]sync[hronously]]",
                "make %completableplayerprofiles% not completable");
    }

    private Expression<CompletablePlayerProfile> completablePlayerProfileExpression;
    private @Nullable ExecutionMode executionMode;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        completablePlayerProfileExpression = (Expression<CompletablePlayerProfile>) expressions[0];
        executionMode = ExecutionMode.fromBooleans(parseResult.hasTag("async"), parseResult.hasTag("sync"), matchedPattern == 0);
        return true;
    }

    @Override
    protected void execute(Event event) {
        for (CompletablePlayerProfile completablePlayerProfile : completablePlayerProfileExpression.getAll(event)) {
            if (executionMode == null)
                completablePlayerProfile.setShouldComplete(false);
            else {
                completablePlayerProfile.setExecutionMode(executionMode);
                completablePlayerProfile.setShouldComplete(true);
            }
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "make " + completablePlayerProfileExpression.toString(event, debug) + (executionMode == null ? " not completable" : " completable" + (executionMode.isInherited() ? "" : " " + executionMode.name().toLowerCase()));
    }
}
