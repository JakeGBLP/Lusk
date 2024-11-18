package it.jakegblp.lusk.elements.minecraft.entities.player.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Player - Sleep")
@Description("Attempts to, or forces one or more players to sleep in a bed.\nAttempting will fail under vanilla conditions in which the player cannot sleep, ex: Daytime, too far away, etc")
@Examples("try to make player sleep at {_bed} # not forced\nforcefully make player sleep at {_bed} # forced")
@Since("1.3")
@SuppressWarnings("unused")
public class EffPlayerSleep extends Effect {

    static {
        Skript.registerEffect(EffPlayerSleep.class,
                "(forcefully|attempt:(try|attempt) to) make %players% sleep at %location%",
                "try making %players% sleep at %location%",
                "force %players% to sleep at %location%");
    }

    private Expression<Player> playersExpression;
    private Expression<Location> locationExpression;
    private boolean force;

    @Override
    protected void execute(Event event) {
        Location location = locationExpression.getSingle(event);
        if (location != null) {
            playersExpression.stream(event).forEach(player -> player.sleep(location, force));
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return (force ? "forcefully" : "attempt to") + " make "+playersExpression.toString(event,b)+" sleep at "+locationExpression.toString(event,b);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        playersExpression = (Expression<Player>) expressions[0];
        locationExpression = (Expression<Location>) expressions[1];
        force = i == 0 ? !parseResult.hasTag("attempt") : i == 2;
        return true;
    }
}
