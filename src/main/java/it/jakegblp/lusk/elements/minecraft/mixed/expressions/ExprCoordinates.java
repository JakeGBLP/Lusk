package it.jakegblp.lusk.elements.minecraft.mixed.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.api.enums.Axis4D;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

import static it.jakegblp.lusk.utils.VectorUtils.getCoordinate;

@Name("AxisAngle/Quaternion/Vector(2D/3D/4D)/Location/EulerAngle/Chunk - XYZ(W) Coordinate/Coordinate List")
@Description("""
        Gets a list or one of the coordinates of:
        - Locations
        - Vectors
        - Chunks (only X and Z)
        - EulerAngles
        - 2D Vectors (only X and Z)
        - 3D Vectors
        - 4D Vectors (XYZW)
        - Quaternions (XYZW)
        - AxisAngles (XYZW)
        
        Note: Out of all of these Lusk only implements EulerAngles.
        """)
@Examples({"broadcast coordinates of {_loc}"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprCoordinates extends PropertyExpression<Object, Number> {
    static {
        register(ExprCoordinates.class, Number.class,
                "(list:coord[inate](s| list)|(:x|:y|:z|w)[( |-)(coord[inate]|pos[ition]|loc[ation])] component)",
                "objects");
    }

    private Axis4D selectedAxis = null;
    private boolean list;

    @Override
    public boolean init(final Expression<?>[] vars, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.@NotNull ParseResult parser) {
        list = parser.hasTag("list");
        if (!list)
            selectedAxis = parser.tags.isEmpty() ? null : Axis4D.valueOf(parser.tags.getFirst().toUpperCase());
        setExpr(vars[0]);
        return true;
    }

    @Override
    public @NotNull Class<Number> getReturnType() {
        return Number.class;
    }

    @Override
    public boolean isSingle() {
        return !list && getExpr().isSingle();
    }

    @Override
    protected Number @NotNull [] get(@NotNull Event e, Object @NotNull [] source) {
        if (list) {
            return Arrays.stream(Axis4D.values())
                    .flatMap(axis -> getExpr().stream(e)
                            .map(o -> getCoordinate(o, axis))
                            .filter(Objects::nonNull))
                    .toArray(Number[]::new);
        } else {
            return getExpr().stream(e).map(o -> getCoordinate(o, selectedAxis)).toArray(Number[]::new);
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (list ? "the coordinates of " : "the "+ selectedAxis.name().toLowerCase()+" coordinate component of ") + getExpr().toString(event, debug);
    }
}