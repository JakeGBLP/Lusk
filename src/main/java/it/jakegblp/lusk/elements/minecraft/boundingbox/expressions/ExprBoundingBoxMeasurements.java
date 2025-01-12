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
import org.bukkit.Axis;
import org.bukkit.event.Event;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Bounding Box - Height and Width X/Z")
@Description("Gets the height, X width and Z width of 1 or more bounding boxes.")
@Examples({"broadcast box height of bounding box of target"})
@Since("1.2.1")
@SuppressWarnings("unused")
public class ExprBoundingBoxMeasurements extends PropertyExpression<BoundingBox, Double> {
    static {
        Skript.registerExpression(ExprBoundingBoxMeasurements.class, Double.class, ExpressionType.PROPERTY,
                "[the] [bounding[ ]]box ((:x|:z) width|height) of %boundingboxes%",
                "%boundingboxes%'[s] [bounding[ ]]box ((:x|:z) width|height)");
    }

    Axis coordinate;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(final Expression<?>[] vars, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.ParseResult parser) {
        setExpr((Expression<? extends BoundingBox>) vars[0]);
        coordinate = parser.hasTag("x") ? Axis.X : parser.hasTag("z") ? Axis.Z : Axis.Y;
        return true;
    }


    @Override
    @NotNull
    public Class<Double> getReturnType() {
        return Double.class;
    }

    @Override
    protected Double @NotNull [] get(@NotNull Event e, BoundingBox @NotNull [] source) {
        return get(source, box -> switch (coordinate) {
            case X -> box.getWidthX();
            case Y -> box.getHeight();
            case Z -> box.getWidthZ();
        });
    }

    @Override
    @NotNull
    public String toString(@Nullable Event event, boolean debug) {
        return "bounding box " +
                (coordinate == Axis.Y ? "height " : ((coordinate == Axis.X ? "x" : "z") + " width ")) +
                getExpr().toString(event, debug);
    }
}