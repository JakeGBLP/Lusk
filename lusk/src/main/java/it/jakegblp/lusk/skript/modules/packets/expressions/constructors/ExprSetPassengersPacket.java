package it.jakegblp.lusk.skript.modules.packets.expressions.constructors;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.SetPassengersPacket;
import it.jakegblp.lusk.nms.core.world.entity.ProtocolEntityReference;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.List;

public class ExprSetPassengersPacket extends SimpleExpression<SetPassengersPacket> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerExpression(syntaxRegistry, ExprSetPassengersPacket.class, ExprSetPassengersPacket::new, SetPassengersPacket.class,
                "[a] new [set] passengers packet with vehicle %protocolentityreference% [and [with] passenger[s] %-protocolentityreferences%]");
    }

    protected Expression<ProtocolEntityReference> vehicleExpression, passengersExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        vehicleExpression = (Expression<ProtocolEntityReference>) expressions[0];
        passengersExpression = (Expression<ProtocolEntityReference>) expressions[1];
        return true;
    }

    @Override
    protected SetPassengersPacket @Nullable [] get(Event event) {
        var vehicle = vehicleExpression.getSingle(event);
        if (vehicle == null) return new SetPassengersPacket[0];
        if (passengersExpression != null) {
            var passengers = passengersExpression.getArray(event);
            if (passengers.length == 0) return new SetPassengersPacket[0];
            return new SetPassengersPacket[] {new SetPassengersPacket(vehicle, List.of(passengers))};
        }
        return new SetPassengersPacket[] {new SetPassengersPacket(vehicle)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends SetPassengersPacket> getReturnType() {
        return SetPassengersPacket.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new set passengers packet with vehicle " + vehicleExpression.toString(event, debug) + " and passengers " + passengersExpression.toString(event, debug);
    }
}
