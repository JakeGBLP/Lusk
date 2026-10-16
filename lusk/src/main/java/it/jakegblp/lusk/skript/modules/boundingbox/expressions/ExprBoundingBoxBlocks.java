package it.jakegblp.lusk.skript.modules.boundingbox.expressions;

import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.AABB;
import ch.njol.util.Kleenean;
import com.google.common.collect.Lists;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Bounding Box - Blocks Within")
@Description("Gets all the blocks within a bounding box in a specific world.")
@Example("broadcast blocks within box {_box} in {_world}")
@Keywords({"blocks", "bounding", "box", "boundingbox", "boundingboxes"})
@Since({"1.3", "2.0.0 (Removed optional prefixes)"})
public class ExprBoundingBoxBlocks extends SimpleExpression<Block> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.expression(ExprBoundingBoxBlocks.class, ExprBoundingBoxBlocks::new, Block.class)
                .patterns("[all [[of] the]|the] blocks within %boundingboxes% in %worlds%")
                .register(syntaxRegistry);
    }

    private Expression<BoundingBox> boundingBoxExpression;
    private Expression<World> worldExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(final Expression<?>[] expressions, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.@NotNull ParseResult parser) {
        boundingBoxExpression = (Expression<BoundingBox>) expressions[0];
        worldExpression = (Expression<World>) expressions[1];
        return true;
    }

    @Override
    protected Block @NotNull [] get(@NotNull Event event) {
        var boundingBoxes = boundingBoxExpression.getArray(event);
        return CommonUtils.flatMap(Block.class, worldExpression.getArray(event), world -> CommonUtils.flatMapToList(boundingBoxes, box -> Lists.newArrayList(new AABB(box.getMin().toLocation(world), box.getMax().toLocation(world)))));
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
        return "all of the blocks within " + boundingBoxExpression.toString(event, debug) + " in " + worldExpression.toString(event, debug);
    }
}