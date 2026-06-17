package it.jakegblp.lusk.skript.modules.packets.expressions;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.world.entity.ProtocolEntityReference;
import it.jakegblp.lusk.nms.core.world.level.LevelUtil;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprEntityFromIdInWorld extends SimpleExpression<Entity> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerExpression(syntaxRegistry, ExprEntityFromIdInWorld.class, ExprEntityFromIdInWorld::new, Entity.class,
                "entit(y|ies) (with id|from [id|protocol]) %protocolentityreferences% [in %world%]",
                "entit(y|ies) from protocol [%protocolentityreferences%] [in %world%]");
    }

    private Expression<ProtocolEntityReference> protocolEntityReferenceExpression;
    private Expression<World> worldExpression;
    private boolean firstPattern;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        protocolEntityReferenceExpression = (Expression<ProtocolEntityReference>) expressions[0];
        worldExpression = (Expression<World>) expressions[1];
        firstPattern = matchedPattern == 0;
        return true;
    }

    @Override
    protected Entity @Nullable [] get(Event event) {
        World world = worldExpression.getSingle(event);
        if (world == null) return new Entity[0];
        return CommonUtils.map(Entity.class, protocolEntityReferenceExpression.getArray(event), protocolEntityReference -> LevelUtil.getEntityFromID(protocolEntityReference.getId(), world));
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Entity> getReturnType() {
        return Entity.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return firstPattern ? "entities with id " + protocolEntityReferenceExpression.toString(event, debug) + " in " + worldExpression.toString(event, debug)
                : "entities from protocol" + (protocolEntityReferenceExpression == null ? "" : " " + protocolEntityReferenceExpression.toString(event, debug)) + (worldExpression == null ? "" : " in " + worldExpression.toString(event, debug));
    }
}
