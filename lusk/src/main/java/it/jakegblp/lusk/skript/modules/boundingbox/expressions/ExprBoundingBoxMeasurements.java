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

@Name("Bounding Box - Height and Width X/Z")
@Description("Gets the height, X width and Z width of one or more bounding boxes.")
@Example("broadcast box height of bounding box of target")
@Keywords({"width", "height", "bounding", "box", "boundingbox", "boundingboxes"})
@Since("1.2.1")
public class ExprBoundingBoxMeasurements extends SimplePropertyExpression<BoundingBox, Double> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprBoundingBoxMeasurements.class, ExprBoundingBoxMeasurements::new, Double.class)
                .propertyOf("[bounding[ ]]box ((:x|:z) width|y:height)", "boundingboxes")
                .defaultExpression(true)
                .register(syntaxRegistry);
    }

    private Axis axis;

    @Override
    public boolean init(final Expression<?>[] vars, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.ParseResult parser) {
        var init = super.init(vars, matchedPattern, isDelayed, parser);
        axis = Axis.valueOf(parser.tags.getFirst().toUpperCase(Locale.ENGLISH));
        return init;
    }

    @Override
    public @Nullable Double convert(BoundingBox box) {
        return switch (axis) {
            case X -> box.getWidthX();
            case Z -> box.getWidthZ();
            case Y -> box.getHeight();
        };
    }

    @Override
    @NotNull
    public Class<Double> getReturnType() {
        return Double.class;
    }

    @Override
    protected String getPropertyName() {
        return "box " + (axis == Axis.Y ? "height" : axis.name().toLowerCase(Locale.ENGLISH) + " width");
    }
}