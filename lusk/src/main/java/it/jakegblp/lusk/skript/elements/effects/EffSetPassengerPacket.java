package it.jakegblp.lusk.skript.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.api.NMSApi;
import it.jakegblp.lusk.nms.core.async.ExecutionMode;
import it.jakegblp.lusk.nms.core.protocol.packets.client.SetPassengerPacket;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Player - Passenger Packet")
public class EffSetPassengerPacket extends Effect {

    static {
        Skript.registerEffect(EffSetPassengerPacket.class,
                "fake entit(y|ies) with id %numbers% ride entity with id %number% for %players%"
        );
    }

    private Expression<Number> passengerNumber;
    private Expression<Number> vehicleNumber;
    private Expression<Player> playerExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        passengerNumber = (Expression<Number>) expressions[0];
        vehicleNumber = (Expression<Number>) expressions[1];
        playerExpression = (Expression<Player>) expressions[2];
        return true;
    }

    @Override
    protected void execute(Event event) {
        Number[] nums = passengerNumber.getArray(event);
        int[] passengers = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            passengers[i] = nums[i].intValue();
        }
        final SetPassengerPacket setPassengerPacket = new SetPassengerPacket(vehicleNumber.getSingle(event).intValue(), passengers);
        NMSApi.sendPacket(playerExpression.getArray(event), setPassengerPacket, ExecutionMode.ASYNCHRONOUS);
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "fake ride";
    }
}
