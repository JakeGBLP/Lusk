package it.jakegblp.lusk.elements.minecraft.boundingbox.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.Axis;
import org.bukkit.event.Event;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Bounding Box - Corners/Center Coordinates")
@Description("Gets the x,y or z coordinate for the lesser corner, the greater corner or the center of one or more bounding boxes.")
@Examples({"broadcast box lesser x-loc of target"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprBoundingBoxCoordinates extends PropertyExpression<BoundingBox, Double> {
    static {
        Skript.registerExpression(ExprBoundingBoxCoordinates.class, Double.class, ExpressionType.PROPERTY,
                "[the] [bounding[ ]]box (:lesser|:greater|center) (:x|:y|z)(-loc|(-| )coord[inate]) of %boundingboxes%",
                "%boundingboxes%'[s] [bounding[ ]]box (:lesser|:greater|center) (:x|:y|z)(-loc|(-| )coord[inate])");
    }

    @Nullable
    Boolean state;
    Axis coordinate;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(final Expression<?>[] vars, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.ParseResult parser) {
        setExpr((Expression<? extends BoundingBox>) vars[0]);
        state = parser.hasTag("lesser") ? Boolean.FALSE : parser.hasTag("greater") ? true : null;
        if (parser.hasTag("x")) coordinate = Axis.X;
        else if (parser.hasTag("y")) coordinate = Axis.Y;
        else coordinate = Axis.Z;
        return true;
    }

    @Override
    @NotNull
    public Class<Double> getReturnType() {
        return Double.class;
    }

    @Override
    protected Double @NotNull [] get(@NotNull Event e, BoundingBox @NotNull [] source) {
        return get(source, box -> state == null ? switch (coordinate) {
            case X -> box.getCenterX();
            case Y -> box.getCenterY();
            case Z -> box.getCenterZ();
        } : state ? switch (coordinate) {
            case X -> box.getMaxX();
            case Y -> box.getMaxY();
            case Z -> box.getMaxZ();
        } : switch (coordinate) {
            case X -> box.getMinX();
            case Y -> box.getMinY();
            case Z -> box.getMinZ();
        });
    }

    @Override
    @NotNull
    public String toString(@Nullable Event event, boolean debug) {
        return "bounding box " +
                (state == null ? "center" : state ? "greater" : "lesser") +
                coordinate +
                "-coordinate of " +
                getExpr().toString(event, debug);
    }
}