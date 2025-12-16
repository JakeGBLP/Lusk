package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.async.ExecutionMode;
import it.jakegblp.lusk.nms.core.world.player.CompletablePlayerProfile;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ExprPlayerProfileCompletable extends SimpleExpression<CompletablePlayerProfile> {

    static {
        Skript.registerExpression(ExprPlayerProfileCompletable.class,  CompletablePlayerProfile.class, ExpressionType.COMBINED,
                "[sync:[async:a]sync[hronously]] completable %completableplayerprofiles%",
                "uncompletable %completableplayerprofiles%");
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
    public String toString(@Nullable Event event, boolean debug) {
        return (executionMode == null ? "uncompletable" : (executionMode.isInherited() ? "" : executionMode.name().toLowerCase() + " completable")) + completablePlayerProfileExpression.toString(event, debug);
    }

    @Override
    protected CompletablePlayerProfile @Nullable [] get(Event event) {
        var profiles = completablePlayerProfileExpression.getAll(event);
        for (CompletablePlayerProfile profile : profiles) {
            if (profile == null) continue;
            if (executionMode == null)
                profile.setShouldComplete(false);
            else {
                profile.setExecutionMode(executionMode);
                profile.setShouldComplete(true);
            }
        }
        return CommonUtils.nonNull(CompletablePlayerProfile.class, profiles);
    }

    @Override
    public boolean isSingle() {
        return completablePlayerProfileExpression.isSingle();
    }

    @Override
    public Class<? extends CompletablePlayerProfile> getReturnType() {
        return CompletablePlayerProfile.class;
    }
}
