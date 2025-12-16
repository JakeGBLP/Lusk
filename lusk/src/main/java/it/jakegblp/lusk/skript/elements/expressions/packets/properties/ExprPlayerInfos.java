package it.jakegblp.lusk.skript.elements.expressions.packets.properties;

import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerInfoUpdatePacket;
import it.jakegblp.lusk.nms.core.world.player.PlayerInfo;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ExprPlayerInfos extends PropertyExpression<PlayerInfoUpdatePacket, PlayerInfo> {

    static {
        register(ExprPlayerInfos.class, PlayerInfo.class, "[packet] player infos", "playerinfopackets");
    }

    @Override
    protected PlayerInfo[] get(Event event, PlayerInfoUpdatePacket[] source) {
        return CommonUtils.flatMap(PlayerInfo.class, source, PlayerInfoUpdatePacket::getPlayerInfos);
    }

    @Override
    public Class<? extends PlayerInfo> getReturnType() {
        return PlayerInfo.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "packet player infos of "+getExpr().toString(event, debug);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        setExpr((Expression<? extends PlayerInfoUpdatePacket>) expressions[0]);
        return true;
    }
}

