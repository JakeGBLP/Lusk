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
import it.jakegblp.lusk.api.enums.XYZ;
import org.bukkit.event.Event;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Bounding Box - Height and Width x/z")
@Description("Gets the height, X width and Z width of 1 or more bounding boxes.")
@Examples({"broadcast box height of bounding box of target"})
@Since("1.2.1")
public class ExprBoundingBoxMeasurements extends PropertyExpression<BoundingBox, Double> {
    static {
        Skript.registerExpression(ExprBoundingBoxMeasurements.class, Double.class, ExpressionType.PROPERTY,
                "[the] [bounding[ ]]box ((:x|:z) width|height) of %boundingboxes%",
                "%boundingboxes%'[s] [bounding[ ]]box ((:x|:z) width|height)");
    }

    XYZ coordinate;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(final Expression<?>[] vars, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.ParseResult parser) {
        setExpr((Expression<? extends BoundingBox>) vars[0]);
        coordinate = parser.hasTag("x") ? XYZ.X : parser.hasTag("z") ? XYZ.Z : XYZ.Y;
        return true;
    }


    @Override
    @NotNull
    public Class<Double> getReturnType() {
        return Double.class;
    }

    @Override
    public boolean isSingle() {
        return getExpr().isSingle();
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
        return "the bounding box " +
                (coordinate.isY() ? "height " : ((coordinate.isX() ? "x" : "z") + " width ")) +
                (event != null ? getExpr().toString(event, debug) : "");
    }
}