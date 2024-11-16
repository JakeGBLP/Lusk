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
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Bounding Box - Corners/Center")
@Description("Gets either the lesser or the greater corner or the lesser of one or more bounding boxes.\n\nReturns vectors.")
@Examples({"broadcast box lesser of target"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprBoundingBoxLocations extends PropertyExpression<BoundingBox, Vector> {
    static {
        Skript.registerExpression(ExprBoundingBoxLocations.class, Vector.class, ExpressionType.PROPERTY,
                "[the] [bounding[ ]]box (center:cent(er|re)|(:greater|lesser) corner) of %boundingboxes%",
                "%boundingboxes%'[s] [bounding[ ]]box (center:cent(er|re)|(:greater|lesser) corner)");
    }

    @Nullable
    Boolean state;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(final Expression<?>[] vars, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.ParseResult parser) {
        setExpr((Expression<? extends BoundingBox>) vars[0]);
        state = parser.hasTag("center") ? null : parser.hasTag("greater");
        return true;
    }

    @Override
    @NotNull
    public Class<Vector> getReturnType() {
        return Vector.class;
    }

    @Override
    protected Vector @NotNull [] get(@NotNull Event e, BoundingBox @NotNull [] source) {
        return get(source, box -> {
            if (state == null) return box.getCenter();
            else if (state) return box.getMax();
            else return box.getMin();
        });
    }

    @Override
    @NotNull
    public String toString(@Nullable Event event, boolean debug) {
        StringBuilder builder = new StringBuilder("the bounding box ");
        if (state == null) builder.append("center");
        else {
            if (state) builder.append("greater");
            else builder.append("lesser");
            builder.append(" corner");
        }
        builder.append(" of ").append(getExpr().toString(event, debug));
        return builder.toString();
    }
}