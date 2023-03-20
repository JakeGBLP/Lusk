package me.jake.lusk.elements.experimental;

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
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Custom Spawn")
@Description("Spawns a 1.19.4 entity.")
@Examples({""})
@Since("1.0.2")
public class EffCustomSpawn extends Effect {

    static {
        Skript.registerEffect(EffCustomSpawn.class, "(spawn|summon) (display (:text|:block|:item)) at %location%");
    }

    private Expression<Location> locationExpression;

    private EntityType entityType;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        if (parser.hasTag("text")) {
            entityType = EntityType.TEXT_DISPLAY;
        } else if (parser.hasTag("block")) {
            entityType = EntityType.BLOCK_DISPLAY;
        } else {
            entityType = EntityType.ITEM_DISPLAY;

        }
        locationExpression = (Expression<Location>) expressions[0];
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        assert event != null;
        return "spawn display entity at " + locationExpression.getSingle(event);
    }

    @Override
    protected void execute(@NotNull Event event) {
        Location location = locationExpression.getSingle(event);
        assert location != null;
        World world = location.getWorld();
        world.spawnEntity(location, entityType);
    }
}