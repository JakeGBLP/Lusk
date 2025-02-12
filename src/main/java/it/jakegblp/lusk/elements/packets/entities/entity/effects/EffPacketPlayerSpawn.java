package it.jakegblp.lusk.elements.packets.entities.entity.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Direction;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.LuskUtils.consoleLog;
import static it.jakegblp.lusk.utils.NMSUtils.NMS;

@Name("Packets | Player - Spawn Fake Player")
@Description("""
Spawns a fake player for the provided players.
Parameters:
- String: the name of the player
- Location: the location to spawn the player at
""")
@Examples("spawn fake player named \"JakeGBLP\" at player for player")
@Since("1.4")
public class EffPacketPlayerSpawn extends Effect {

    static {
        consoleLog("NMS: {0}", NMS != null);
        if (NMS != null)
            Skript.registerEffect(EffPacketPlayerSpawn.class,
                    "spawn fake player named %string% %direction% %location% for %players%"
        );
    }

    private Expression<String> nameExpression;
    private Expression<Location> locationExpression;
    private Expression<Player> playerExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        nameExpression = (Expression<String>) expressions[0];
        locationExpression = Direction.combine((Expression<Direction>) expressions[1], (Expression<Location>) expressions[2]);
        playerExpression = (Expression<Player>) expressions[3];
        return true;
    }

    @Override
    protected void execute(Event event) {
        assert NMS != null;
        Location location = locationExpression.getSingle(event);
        if (location == null) return;
        String name = nameExpression.getSingle(event);
        if (name == null) return;
        NMS.spawnPlayer(name, location, true, playerExpression.getAll(event)).update();
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "spawn fake player named " + nameExpression.toString(event, debug) + " "
                + locationExpression.toString(event, debug) + " for " + playerExpression.toString(event, debug);
    }

}
