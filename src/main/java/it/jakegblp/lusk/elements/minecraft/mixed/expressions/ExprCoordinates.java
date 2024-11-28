package it.jakegblp.lusk.elements.minecraft.mixed.expressions;

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
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

@Name("Vector/Location - Coordinate List")
@Description("Returns the XYZ coordinates of a location or a vector.")
@Examples({"broadcast coordinates of {_loc}"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprCoordinates extends PropertyExpression<Object, Double> {
    static {
        // TODO: simple PROPERTY EXPR
        Skript.registerExpression(ExprCoordinates.class, Double.class, ExpressionType.PROPERTY,
                "[the] coord[inate](s| list) of %vectors/locations%",
                "%vectors/locations%'[s] coord[inate](s| list)");
    }

    @Override
    public boolean init(final Expression<?>[] vars, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.@NotNull ParseResult parser) {
        setExpr(vars[0]);
        return true;
    }

    @Override
    public @NotNull Class<Double> getReturnType() {
        return Double.class;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    protected Double @NotNull [] get(@NotNull Event e, Object @NotNull [] source) {
        List<Double> list = new ArrayList<>();
        for (Object o : getExpr().getArray(e)) {
            double x = 0, y = 0, z = 0;
            if (o instanceof Location location) {
                x = location.getX();
                y = location.getY();
                z = location.getZ();
            } else if (o instanceof Vector vector) {
                x = vector.getX();
                y = vector.getY();
                z = vector.getZ();
            }
            list.add(x);
            list.add(y);
            list.add(z);
        }
        return list.toArray(new Double[0]);
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the coordinates of " + (event != null ? getExpr().toString(event, debug) : "");
    }
}