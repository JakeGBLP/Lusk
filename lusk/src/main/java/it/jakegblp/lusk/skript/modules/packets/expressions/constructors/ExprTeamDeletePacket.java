package it.jakegblp.lusk.skript.modules.packets.expressions.constructors;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.TeamPacket;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprTeamDeletePacket extends SimpleExpression<TeamPacket> {

    private Expression<String> nameExpression;

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.expression(ExprTeamDeletePacket.class, ExprTeamDeletePacket::new, TeamPacket.class)
                .patterns("[a] new team delet(e|ion) packet for team [named|with name] %string%")
                .register(syntaxRegistry);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result) {
        nameExpression = (Expression<String>) expressions[0];
        return true;
    }

    @Override
    protected TeamPacket @Nullable [] get(Event event) {
        String name = nameExpression.getSingle(event);
        return name == null ? new TeamPacket[0] : new TeamPacket[]{TeamPacket.delete(name)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends TeamPacket> getReturnType() {
        return TeamPacket.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new team delete packet for team with name " + nameExpression.toString(event, debug);
    }
}
