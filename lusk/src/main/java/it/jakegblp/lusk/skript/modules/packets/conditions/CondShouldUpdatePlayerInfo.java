package it.jakegblp.lusk.skript.modules.packets.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerInfoUpdatePacket;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Locale;

import static it.jakegblp.lusk.skript.modules.packets.expressions.properties.ExprPlayerInfoUpdatePacketEntry.PLAYER_INFO_PROPERTY_CHOICE;

public class CondShouldUpdatePlayerInfo extends PropertyCondition<PlayerInfoUpdatePacket> {

    private PlayerInfoUpdatePacket.Action<?> action;

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyCondition(CondShouldUpdatePlayerInfo.class, CondShouldUpdatePlayerInfo::new)
                .property("playerinfoupdatepackets", PropertyType.WILL, "update [the] player'[s] " + PLAYER_INFO_PROPERTY_CHOICE)
                .register(syntaxRegistry);
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        action = PlayerInfoUpdatePacket.Action.valueOf(parseResult.tags.getFirst().replace(" ", "_").toUpperCase(Locale.ROOT));
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public boolean check(PlayerInfoUpdatePacket value) {
        return value.hasAction(action);
    }

    @Override
    protected String getPropertyName() {
        return "update the player'[s] " + action.getPropertyName();
    }
}
