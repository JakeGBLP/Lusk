package it.jakegblp.lusk.skript.modules.boundingbox.expressions;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.Axis;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Locale;

@Name("Bounding Box - Corners/Center Coordinates")
@Description("Gets the x,y or z coordinate for the lesser corner, the greater corner or the center of one or more bounding boxes.")
@Example("broadcast box lesser x-loc of target")
@Keywords({"bounding", "box", "boundingbox", "boundingboxes", "coordinates"})
@Since("1.3")
public class ExprBoundingBoxCoordinates extends SimplePropertyExpression<BoundingBox, Double> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprBoundingBoxCoordinates.class, ExprBoundingBoxCoordinates::new, Double.class)
                .propertyOf("(:lesser|:center|:greater) (:x|:y|:z)(-| )coord[inate]", "boundingboxes")
                .defaultExpression(true)
                .register(syntaxRegistry);
    }

    public enum Point {
        LESSER,
        CENTER,
        GREATER
    }

    private Point point;
    private Axis axis;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(final Expression<?>[] vars, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.ParseResult parser) {
        setExpr((Expression<? extends BoundingBox>) vars[0]);
        point = Point.valueOf(parser.tags.getFirst().toUpperCase(Locale.ENGLISH));
        axis = Axis.valueOf(parser.tags.getLast().toUpperCase(Locale.ENGLISH));
        return true;
    }

    @Override
    public @Nullable Double convert(BoundingBox box) {
        return switch (point) {
            case LESSER -> switch (axis) {
                case X -> box.getMinX();
                case Y -> box.getMinY();
                case Z -> box.getMinZ();
            };
            case CENTER -> switch (axis) {
                case X -> box.getCenterX();
                case Y -> box.getCenterY();
                case Z -> box.getCenterZ();
            };
            case GREATER -> switch (axis) {
                case X -> box.getMaxX();
                case Y -> box.getMaxY();
                case Z -> box.getMaxZ();
            };
        };
    }

    @Override
    @NotNull
    public Class<Double> getReturnType() {
        return Double.class;
    }

    @Override
    protected String getPropertyName() {
        return point.name().toLowerCase(Locale.ENGLISH) + " " + axis.name().toLowerCase(Locale.ENGLISH) + " coordinate";
    }
}