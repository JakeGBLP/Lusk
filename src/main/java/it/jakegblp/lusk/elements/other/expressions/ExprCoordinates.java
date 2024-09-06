package it.jakegblp.lusk.elements.other.expressions;

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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Name("Vector/Location/Bounding Box - Coordinate List")
@Description("Returns the Angry State of an entity.\n(Warden, PigZombie, Wolf, Enderman)\nCan be set for all except wardens.")
@Examples({"broadcast angry state of target"})
@Since("1.3")
public class ExprCoordinates extends PropertyExpression<Object, Double> {
    static {
        Skript.registerExpression(ExprCoordinates.class, Double.class, ExpressionType.PROPERTY,
                "[the] coord[inate](s| list) of %vector/location%",
                "%vector/location/boundingbox%'[s] coord[inate](s| list)");
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
            double x,y,z;
            if (o instanceof Location location) {
                x = location.getX();
                y = location.getY();
                z = location.getZ();
                list.add(x);
                list.add(y);
                list.add(z);
            } else if (o instanceof Vector vector) {
                x = vector.getX();
                y = vector.getY();
                z = vector.getZ();
                list.add(x);
                list.add(y);
                list.add(z);
            }
        }
        return list.toArray(new Double[0]);
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the coordinate list of " + (event != null ? getExpr().toString(event, debug) : "");
    }
}