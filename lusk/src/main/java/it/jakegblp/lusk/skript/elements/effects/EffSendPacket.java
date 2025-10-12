package it.jakegblp.lusk.skript.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.api.NMSApi;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("[NMS] Player - Dispatch (Send) Packet(s)")
@Description("Sends the provided client packets to the provided players' client.")
@Examples({
        "dispatch packet {_packet} to {_player}"
})
@Keywords({
        "packets", "packet", "protocol", "dispatch"
})
@Since("2.0.0")
public class EffSendPacket extends Effect {

    static {
        Skript.registerEffect(EffSendPacket.class,
                "(dispatch|send) [the] packet[s] %clientpackets% to %players%",
                "bundle [the] packet[s] %clientpackets% and (dispatch|send) [them] to %players%");
    }

    private Expression<ClientboundPacket> clientboundPacketExpression;
    private Expression<Player> playerExpression;
    private boolean bundle;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        clientboundPacketExpression = (Expression<ClientboundPacket>) expressions[0];
        playerExpression = (Expression<Player>) expressions[1];
        bundle = matchedPattern == 1;
        return true;
    }

    @Override
    protected void execute(Event event) {
        var players = playerExpression.getAll(event);
        var packets = clientboundPacketExpression.getAll(event);
        if (bundle)
            NMSApi.sendBundledPackets(players, packets);
        else
            NMSApi.sendPackets(players, packets);
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        SyntaxStringBuilder builder = new SyntaxStringBuilder(event, debug);
        if (bundle)
            builder.append("bundle the packets ").append(clientboundPacketExpression).append(" and dispatch them to ").append(playerExpression);
        else
            builder.append("dispatch the packets ").append(clientboundPacketExpression).append(" to ").append(playerExpression);
        return builder.toString();
    }
}
