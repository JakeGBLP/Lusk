package it.jakegblp.lusk.elements.minecraft.boundingbox.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.AABB;
import ch.njol.util.Kleenean;
import com.google.common.collect.Lists;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Bounding Box - Blocks Within")
@Description("Gets all the blocks within a bounding box in a specific world.")
@Examples({"broadcast blocks within {_box} in {_world}"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprBoundingBoxBlocks extends SimpleExpression<Block> {
    static {
        Skript.registerExpression(ExprBoundingBoxBlocks.class, Block.class, ExpressionType.COMBINED,
                "[all [[of] the]|the] blocks within [bounding] box[es] %boundingboxes% in [world[s]] %worlds%");
    }

    private Expression<BoundingBox> boundingBoxExpression;
    private Expression<World> worldExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(final Expression<?>[] vars, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.@NotNull ParseResult parser) {
        boundingBoxExpression = (Expression<BoundingBox>) vars[0];
        worldExpression = (Expression<World>) vars[1];
        return true;
    }

    @Override
    protected Block @NotNull [] get(@NotNull Event event) {
        return worldExpression.stream(event).flatMap(world -> boundingBoxExpression.stream(event).flatMap(boundingBox ->
                        Lists.newArrayList(
                                new AABB(boundingBox.getMin().toLocation(world), boundingBox.getMax().toLocation(world))
                                        .iterator()).stream())).toArray(Block[]::new);
    }

    @Override
    public @NotNull Class<Block> getReturnType() {
        return Block.class;
    }

    @Override
    public boolean isSingle() {
        return false;
    }


    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "all of the blocks within bounding boxes " +
                boundingBoxExpression.toString(event, debug) +
                " in worlds " + worldExpression.toString(event, debug);
    }
}