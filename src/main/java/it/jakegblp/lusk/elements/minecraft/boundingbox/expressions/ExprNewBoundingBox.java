package it.jakegblp.lusk.elements.minecraft.boundingbox.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Bounding Box - New Bounding Box")
@Description("Creates a new empty Bounding Box, one from two corners or a copy of a given bounding box")
@Examples({"broadcast a new empty bounding box"})
@Since("1.2, 1.3 (Copy)")
public class ExprNewBoundingBox extends SimpleExpression<BoundingBox> {
    static {
        Skript.registerExpression(ExprNewBoundingBox.class, BoundingBox.class, ExpressionType.COMBINED,
                "[a[n]] (new|[new] empty) bounding box",
                "[a] [new] bounding box (between|from|using|with) %vector/location% (,|[,] and|to) %vector/location%",
                "[a] [new [cloned|copied]|cloned|copied] bounding box from %boundingbox%");
    }

    private boolean empty;
    private Expression<Object> objectExpression1;
    private Expression<Object> objectExpression2 = null;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        empty = matchedPattern == 0;
        if (!empty) {
            objectExpression1 = (Expression<Object>) expressions[0];
            if (matchedPattern != 2) {
                objectExpression2 = (Expression<Object>) expressions[1];
            }
        }
        return true;
    }

    @Override
    protected BoundingBox @NotNull [] get(@NotNull Event event) {
        if (empty) return new BoundingBox[]{new BoundingBox()};
        Object o1 = objectExpression1.getSingle(event);
        if (o1 instanceof BoundingBox boundingBox) return new BoundingBox[]{boundingBox.clone()};
        Object o2 = objectExpression2.getSingle(event);
        Vector v1 = o1 instanceof Location location ? location.toVector() : o1 instanceof Vector vector ? vector : null;
        Vector v2 = o2 instanceof Location location ? location.toVector() : o2 instanceof Vector vector ? vector : null;
        if (v1 != null && v2 != null)
            return new BoundingBox[]{new BoundingBox(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ())};
        return new BoundingBox[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<BoundingBox> getReturnType() {
        return BoundingBox.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        if (empty) return "a new empty bounding box";
        if (objectExpression2 == null)
            return "a new bounding box from " + objectExpression1.toString(event, debug);
        return "a new bounding box from " + objectExpression1.toString(event, debug) + " and " + objectExpression2.toString(event, debug);
    }
}