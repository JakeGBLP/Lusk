package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.world.entity.ProtocolEntityReference;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprProtocolEntityReference extends SimpleExpression<ProtocolEntityReference> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerExpression(syntaxRegistry, ExprProtocolEntityReference.class, ExprProtocolEntityReference::new, ProtocolEntityReference.class,
                "[protocol] entity reference (with|from) id %number%",
                "[protocol] entity reference of %entity%");
    }

    protected Expression<Object> referenceExpression;
    protected boolean fromEntity;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        referenceExpression = (Expression<Object>) expressions[0];
        fromEntity = matchedPattern == 1;
        return true;
    }

    @Override
    protected ProtocolEntityReference @Nullable [] get(Event event) {
        Object reference = referenceExpression.getSingle(event);
        if (fromEntity && reference instanceof Entity entity) return new ProtocolEntityReference[] {ProtocolEntityReference.of(entity)};
        else if (!fromEntity && reference instanceof Number number) return new ProtocolEntityReference[] {ProtocolEntityReference.of(number.intValue())};
        else return new ProtocolEntityReference[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends ProtocolEntityReference> getReturnType() {
        return ProtocolEntityReference.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "protocol entity reference " + (fromEntity ? "of " : "from id ") + referenceExpression.toString(event, debug);
    }
}
