package it.jakegblp.lusk.elements.minecraft.entitysnapshot.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.sections.EffSecSpawn;
import ch.njol.skript.util.Direction;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.HAS_ENTITY_SNAPSHOT;

/**
This effect is heavily influenced by {@link EffSecSpawn Skript's Spawn effect section}.
@author JakeGBLP, SkriptLang
 */
@Name("Entity Snapshot - Spawn")
@Description("Creates an entity using this template and spawns it at the provided location.\nThis is an effect, not a section and can't be used as such.\nThis effect is heavily influenced by Skript's Spawn effect (EffSecSpawn).")
@Examples("spawn snapshot {_entitySnapshot} at {_location}")
@Since("1.3")
@RequiredPlugins("1.20.2")
public class EffEntitySnapshotSpawn extends Effect {

    static {
        if (HAS_ENTITY_SNAPSHOT)
            Skript.registerEffect(EffEntitySnapshotSpawn.class,
                    "(spawn|summon) [entity[ |-]]snapshot[s] %entitysnapshots% [%directions% %locations%]",
                    "(spawn|summon) %number% of [entity[ |-]]snapshot[s] %entitysnapshots% [%directions% %locations%]");
    }

    @Nullable
    private Expression<Number> amount;
    private Expression<EntitySnapshot> snapshots;
    private Expression<Location> locations;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        amount = matchedPattern == 0 ? null : (Expression<Number>) (expressions[0]);
        snapshots = (Expression<EntitySnapshot>) expressions[matchedPattern];
        locations = Direction.combine((Expression<? extends Direction>) expressions[1 + matchedPattern], (Expression<? extends Location>) expressions[2 + matchedPattern]);
        return true;
    }

    @Override
    protected void execute(Event event) {
        Number numberAmount = amount != null ? amount.getSingle(event) : 1;
        if (numberAmount != null) {
            double amount = numberAmount.doubleValue();
            for (Location location : locations.getArray(event)) {
                for (EntitySnapshot snapshot : snapshots.getAll(event)) {
                    for (int i = 0; i < amount; i++) {
                        // Was this private at some point? If you're reading this, and you happen to know why skBee uses reflection to set it, please let me know :D
                        EffSecSpawn.lastSpawned = snapshot.createEntity(location);
                    }
                }
            }
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "spawn " + (amount != null ? amount.toString(event, debug) + " of " : "") + "entity snapshots " +
                snapshots.toString(event, debug) + " " + locations.toString(event, debug);
    }
}
