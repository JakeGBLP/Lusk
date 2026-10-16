package it.jakegblp.lusk.skript.modules.boundingbox.expressions;

import ch.njol.skript.doc.*;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Bounding Box - Entities Within")
@Description("Gets all the entities within a bounding box in a specific world.")
@Example("broadcast entities within box {_box} in {_world}")
@Keywords({"bounding", "box", "boundingbox", "boundingboxes", "entities"})
@Since({"1.3", "2.0.0 (Removed optional prefixes)"})
public class ExprBoundingBoxEntities extends SimpleExpression<Entity> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.expression(ExprBoundingBoxEntities.class, ExprBoundingBoxEntities::new, Entity.class)
                .patterns("[all [[of] the]|the] %*entitydatas% within %boundingboxes% in %worlds%")
                .register(syntaxRegistry);
    }

    private Expression<EntityData<?>> entityDataExpression;
    private Expression<BoundingBox> boundingBoxExpression;
    private Expression<World> worldExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(final Expression<?>[] expressions, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.@NotNull ParseResult parser) {
        entityDataExpression = (Expression<EntityData<?>>) expressions[0];
        boundingBoxExpression = (Expression<BoundingBox>) expressions[1];
        worldExpression = (Expression<World>) expressions[2];
        return true;
    }

    @Override
    protected Entity @NotNull [] get(@NotNull Event event) {
        var entityDatas = entityDataExpression.getArray(event);
        var worlds = worldExpression.getArray(event);
        return CommonUtils.flatMap(Entity.class, boundingBoxExpression.getArray(event), boundingBox -> CommonUtils.flatMapToList(entityDatas, entityData -> CommonUtils.flatMapToList(worlds, world -> world.getNearbyEntities(boundingBox, entity -> entityData.getType().isAssignableFrom(entity.getClass())))));
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
        return "all of the " + entityDataExpression.toString(event, debug) + " within " + boundingBoxExpression.toString(event, debug) + " in " + worldExpression.toString(event, debug);
    }
}