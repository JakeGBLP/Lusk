package it.jakegblp.lusk.skript.modules.packets.expressions;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.world.entity.guardian.GuardianBeam;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprGuardianBeam extends SimpleExpression<GuardianBeam> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerExpression(syntaxRegistry, ExprGuardianBeam.class, ExprGuardianBeam::new, GuardianBeam.class,
                "[a] [new] [:persistent] [virtual] guardian beam from %location/vector% to %location/vector%");
    }

    private Expression<Object> fromExpression, toExpression;
    private boolean persistent;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        fromExpression = (Expression<Object>) expressions[0];
        toExpression = (Expression<Object>) expressions[1];
        persistent = parseResult.hasTag("persistent");
        return true;
    }

    @Override
    protected GuardianBeam @Nullable [] get(Event event) {
        Vector from = AddonUtils.getVectorFromExpression(fromExpression, event);
        if (from != null) {
            Vector to = AddonUtils.getVectorFromExpression(toExpression, event);
            if (to != null)
                return new GuardianBeam[] {new GuardianBeam(from, to, persistent)};
        }
        return new GuardianBeam[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends GuardianBeam> getReturnType() {
        return GuardianBeam.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new " + (persistent ? "persistent " : "") + "virtual guardian beam from %location% to %location%";
    }
}
