package it.jakegblp.lusk.skript.elements.expressions.packets.constructors;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.TeamPacket;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ExprSecTeamDeletePacket extends SimpleExpression<TeamPacket> {

    static {
        Skript.registerExpression(ExprSecTeamDeletePacket.class, TeamPacket.class, ExpressionType.COMBINED,
                "[a] new team delet(e|ion) packet for team [named|with name] %string%"
        );
    }

    private Expression<String> nameExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result) {
        nameExpression = (Expression<String>) expressions[0];
        return true;
    }

    @Override
    protected TeamPacket @Nullable [] get(Event event) {
        String name = nameExpression.getSingle(event);
        return name == null ? new TeamPacket[0] : new TeamPacket[] {TeamPacket.delete(name)};
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
        return "a new team delete packet for team with name "+nameExpression.toString(event, debug);
    }
}
