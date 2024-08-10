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
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Bounding Box - Height and Width x/z")
@Description("Gets the height, X width and Z width of 1 or more bounding boxes.")
@Examples({"broadcast box height of bounding box of target"})
@Since("1.2.1-beta1")
public class ExprBoundingBoxMeasurements extends PropertyExpression<BoundingBox, Double> {
    static {
        Skript.registerExpression(ExprBoundingBoxMeasurements.class, Double.class, ExpressionType.PROPERTY,
                "[the] [bounding[ ]]box height of %boundingboxes%",
                "%boundingboxes%'[s] [bounding[ ]]box height",
                "[the] [bounding[ ]]box (:x|z) width of %boundingboxes%",
                "%boundingboxes%'[s] [bounding[ ]]box (:x|z) width");
    }

    boolean height;
    boolean x;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(final Expression<?>[] vars, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.ParseResult parser) {
        setExpr((Expression<? extends BoundingBox>) vars[0]);
        height = matchedPattern <= 1;
        x = parser.hasTag("x");
        return true;
    }


    @Override
    public @NotNull Class<Double> getReturnType() {
        return Double.class;
    }

    @Override
    public boolean isSingle() {
        return getExpr().isSingle();
    }

    @Override
    protected Double @NotNull [] get(@NotNull Event e, BoundingBox @NotNull [] source) {
        return get(source, box -> {
            if (height) return box.getHeight();
            else if (x) return box.getWidthX();
            else return box.getWidthZ();
        });
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the bounding box "+(height ? "height " : ((x ? "x" : "z")+" width ")) + (event != null ? getExpr().toString(event, debug) : "");
    }
}