package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.world.entity.ClientEntityReference;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ExprEntityReference extends SimpleExpression<ClientEntityReference> {

    static {
        Skript.registerExpression(ExprEntityReference.class, ClientEntityReference.class, ExpressionType.COMBINED,
                "entity %entity/integer% for %player%");
    }

    protected Expression<Object> referenceExpression;
    protected Expression<Player> playerExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        referenceExpression = (Expression<Object>) expressions[0];
        playerExpression = (Expression<Player>) expressions[1];
        return true;
    }

    @Override
    protected ClientEntityReference @Nullable [] get(Event event) {
        Player player = playerExpression.getSingle(event);
        if (player == null) return new ClientEntityReference[0];
        Object reference = referenceExpression.getSingle(event);
        if (reference instanceof Entity entity) return new ClientEntityReference[] {ClientEntityReference.of(player, entity)};
        else if (reference instanceof Number number) return new ClientEntityReference[] {ClientEntityReference.of(player, number.intValue())};
        else return new ClientEntityReference[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends ClientEntityReference> getReturnType() {
        return ClientEntityReference.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return referenceExpression.toString(event, debug) + " for " + playerExpression.toString(event, debug);
    }
}
