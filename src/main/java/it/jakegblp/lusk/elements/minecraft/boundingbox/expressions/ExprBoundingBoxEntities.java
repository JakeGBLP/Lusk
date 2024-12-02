package it.jakegblp.lusk.elements.minecraft.boundingbox.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Bounding Box - Entities Within")
@Description("Gets all the entities within a bounding box in a specific world.")
@Examples({"broadcast entities within {_box} in {_world}"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprBoundingBoxEntities extends SimpleExpression<Entity> {
    static {
        Skript.registerExpression(ExprBoundingBoxEntities.class, Entity.class, ExpressionType.COMBINED,
        "[all [[of] the]|the] %*entitydatas% (of|in|within) [bounding] box[es] %boundingboxes% in [world[s]] %worlds%");
    }
    private Expression<EntityData<?>> entityDataExpression;
    private Expression<BoundingBox> boundingBoxExpression;
    private Expression<World> worldExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(final Expression<?>[] vars, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.@NotNull ParseResult parser) {
        entityDataExpression = (Expression<EntityData<?>>) vars[0];
        boundingBoxExpression = (Expression<BoundingBox>) vars[1];
        worldExpression = (Expression<World>) vars[2];
        return true;
    }

    @Override
    protected Entity @NotNull [] get(@NotNull Event event) {
        return boundingBoxExpression.stream(event).flatMap(boundingBox -> entityDataExpression.stream(event).flatMap(entityData -> {
            Class<? extends Entity> entityClass = entityData.getType();
            return worldExpression.stream(event).flatMap(world -> world.getNearbyEntities(boundingBox, entity -> entityClass.isAssignableFrom(entity.getClass())).stream());
        })).toArray(Entity[]::new);
    }

    @Override
    public @NotNull Class<Entity> getReturnType() {
        return Entity.class;
    }

    @Override
    public boolean isSingle() {
        return false;
    }


    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "all of the "+entityDataExpression.toString(event, debug)+" within bounding boxes "+boundingBoxExpression.toString(event, debug)+ " in world "+worldExpression.toString(event, debug);
    }
}