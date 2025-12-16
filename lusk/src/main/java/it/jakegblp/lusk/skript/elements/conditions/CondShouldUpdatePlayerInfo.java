package it.jakegblp.lusk.skript.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerInfoUpdatePacket;

import java.util.Locale;

import static it.jakegblp.lusk.skript.elements.expressions.packets.properties.ExprPlayerInfoPacketEntry.PLAYER_INFO_PROPERTY_CHOICE;

public class CondShouldUpdatePlayerInfo extends PropertyCondition<PlayerInfoUpdatePacket> {

    static {
        register(CondShouldUpdatePlayerInfo.class, PropertyType.WILL, "update [the] player'[s] "+PLAYER_INFO_PROPERTY_CHOICE, "playerinfopackets");
    }

    private PlayerInfoUpdatePacket.Action<?> action;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        action = PlayerInfoUpdatePacket.Action.valueOf(parseResult.tags.get(0).replace(" ", "_").toUpperCase(Locale.ROOT));
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public boolean check(PlayerInfoUpdatePacket value) {
        return value.hasAction(action);
    }

    @Override
    protected String getPropertyName() {
        return "update the player'[s] "+action.getPropertyName();
    }
}
