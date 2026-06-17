package it.jakegblp.lusk.skript.modules.packets.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.api.NMSApi;
import it.jakegblp.lusk.nms.core.async.ExecutionMode;
import it.jakegblp.lusk.nms.core.protocol.packets.client.SetPassengersPacket;
import it.jakegblp.lusk.nms.core.world.entity.ProtocolEntityReference;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.List;

// todo: redo whole class
public class EffSetPassengerPacket extends Effect {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerEffect(syntaxRegistry, EffSetPassengerPacket.class, EffSetPassengerPacket::new,
                "make [entit(y|ies)] %protocolentityreferences% ride %protocolentityreference% for %players%");
    }

    private Expression<ProtocolEntityReference> passengersExpression, vehicleExpression;
    private Expression<Player> playersExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        passengersExpression = (Expression<ProtocolEntityReference>) expressions[0];
        vehicleExpression = (Expression<ProtocolEntityReference>) expressions[1];
        playersExpression = (Expression<Player>) expressions[2];
        return true;
    }

    @Override
    protected void execute(Event event) {
        var players = playersExpression.getArray(event);
        if (players.length == 0) return;
        var vehicle = vehicleExpression.getSingle(event);
        if (vehicle == null) return;
        var passengers = passengersExpression.getArray(event);
        if (passengers.length == 0) return;
        NMSApi.sendPacket(playersExpression.getArray(event), new SetPassengersPacket(vehicle, List.of(passengers)), ExecutionMode.ASYNCHRONOUS);
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "make entities " + passengersExpression.toString(event, debug) + " ride " + vehicleExpression.toString(event, debug) + " for " + playersExpression.toString(event, debug);
    }
}
