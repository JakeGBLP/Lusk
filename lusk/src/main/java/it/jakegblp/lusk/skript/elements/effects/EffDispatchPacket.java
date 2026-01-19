package it.jakegblp.lusk.skript.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.api.NMSApi;
import it.jakegblp.lusk.nms.core.async.ExecutionMode;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import it.jakegblp.lusk.skript.api.async.AsyncableSyntax;
import it.jakegblp.lusk.skript.api.async.AsyncableSyntaxesWrapper;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

import static it.jakegblp.lusk.skript.utils.AddonUtils.warningForElement;

@Name("[NMS] Player - Dispatch (Send) Packet(s)")
@Description("Sends the provided client packets to the provided players' client.")
@Examples({
        "dispatch packet {_packet} to {_player}"
})
@Keywords({
        "packets", "packet", "protocol", "dispatch", "sync", "async"
})
@Since("2.0.0")
public class EffDispatchPacket extends Effect implements AsyncableSyntaxesWrapper {

    public static void scanPackets(AsyncableSyntaxesWrapper asyncableSyntaxesWrapper) {
        var executionMode = asyncableSyntaxesWrapper.getInitExecutionMode();
        scan: for (int i = 0; i < 2; i++) {
            for (AsyncableSyntax asyncableSyntax : asyncableSyntaxesWrapper.getAsyncableSyntaxes()) {
                var currentExecutionMode = asyncableSyntax.getInitExecutionMode();
                if (i == 0 && executionMode.isInherited()) {
                    if (currentExecutionMode != null && !currentExecutionMode.isInherited()) {
                        executionMode = currentExecutionMode;
                        if (currentExecutionMode.isAsync()) continue scan;
                    }
                } else if (i == 1 && (currentExecutionMode != null && !currentExecutionMode.isInherited() && executionMode != currentExecutionMode))
                    warningForElement(asyncableSyntax, "You're trying to send packets " + executionMode.name().toLowerCase(Locale.ROOT) + "ly but this "+ asyncableSyntax.getSyntaxTypeName()+" is defined as "+currentExecutionMode.name().toLowerCase(Locale.ROOT)+", inner definitions will be ignored, to remove this warning simply make the inner element not specify whether it's synchronous or asynchronous.");
            }
        }
    }

    static {
        Skript.registerEffect(EffDispatchPacket.class,
                "[sync:[async:a]sync[hronously]] (dispatch|send) [:bundled] packet[s] %clientpackets% to %players%");
    }

    private Expression<? extends ClientboundPacket> clientboundPacketExpression;
    private Expression<Player> playerExpression;
    private boolean bundled;
    @Getter
    private ExecutionMode initExecutionMode;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        clientboundPacketExpression = (Expression<? extends ClientboundPacket>) expressions[0];
        playerExpression = (Expression<Player>) expressions[1];
        bundled = parseResult.hasTag("bundled");
        initExecutionMode = ExecutionMode.fromBooleans(parseResult.hasTag("async"), parseResult.hasTag("sync"));
        scanPackets(this);
        return true;
    }

    @Override
    protected void execute(Event event) {
        try {
            var players = playerExpression.getAll(event);
            var packets = clientboundPacketExpression.getArray(event);
            if (bundled)
                NMSApi.sendBundledPackets(players, packets, initExecutionMode);
            else
                NMSApi.sendPackets(players, packets, initExecutionMode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return new SyntaxStringBuilder(event, debug)
                .append(initExecutionMode.isInherited() ? "" : (initExecutionMode.name().toLowerCase(Locale.ROOT) + "ly"))
                .append("dispatch")
                .append(bundled ? "bundled" : "")
                .append("packets")
                .append(clientboundPacketExpression)
                .append("to").append(playerExpression).toString();
    }

    @Override
    public List<AsyncableSyntax> getAsyncableSyntaxes() {
        return AsyncableSyntaxesWrapper.filterAsyncableSyntaxes(clientboundPacketExpression);
    }
}
