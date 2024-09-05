package it.jakegblp.lusk.elements.minecraft.boundingbox.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Name("Bounding Box - of Voxel Shape")
@Description("Converts shapes into a collection of Bounding Boxes equivalent to the shape they come from.")
@Examples({"broadcast bounding boxes of {_voxelShape}}"})
@Since("1.2")
public class ExprBoundingBoxesOfVoxelShape extends PropertyExpression<VoxelShape, BoundingBox> {
    static {
        Skript.registerExpression(ExprBoundingBoxesOfVoxelShape.class, BoundingBox.class, ExpressionType.PROPERTY,
                "[the] bounding boxes of %voxelshapes%",
                "%voxelshapes%'[s] bounding boxes");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(final Expression<?>[] vars, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.@NotNull ParseResult parser) {
        setExpr((Expression<? extends VoxelShape>) vars[0]);
        return true;
    }

    @Override
    public @NotNull Class<BoundingBox> getReturnType() {
        return BoundingBox.class;
    }

    @Override
    public boolean isSingle() {
        return getExpr().isSingle();
    }

    @Override
    protected BoundingBox[] get(Event e, VoxelShape[] source) {
        VoxelShape[] voxelShapes = getExpr().getArray(e);
        List<BoundingBox> boundingBoxList = new ArrayList<>();
        for (VoxelShape voxelShape : voxelShapes) {
            boundingBoxList.addAll(voxelShape.getBoundingBoxes());
        }
        return boundingBoxList.toArray(new BoundingBox[0]);
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the bounding boxes of " + (event != null ? getExpr().toString(event, debug) : "");
    }
}