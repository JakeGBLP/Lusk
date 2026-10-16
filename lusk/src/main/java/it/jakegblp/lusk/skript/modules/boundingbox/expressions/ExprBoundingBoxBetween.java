package it.jakegblp.lusk.skript.modules.boundingbox.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Bounding Box - Between")
@Description("Creates a bounding box from two corners")
@Example("set {_box} to bounding box from {_cornerA} to {_cornerB}")
@Since("1.2")
public class ExprBoundingBoxBetween extends SimpleExpression<BoundingBox> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.expression(ExprBoundingBoxBetween.class, ExprBoundingBoxBetween::new, BoundingBox.class)
                .patterns(
                        "[a] [new] bounding box from %vector/location% to %vector/location%",
                        "[a] [new] bounding box (between|using|with) %vector/location% [,] and %vector/location%")
                .register(syntaxRegistry);
    }

    private Expression<Object> fromExpression, toExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        fromExpression = (Expression<Object>) expressions[0];
        toExpression = (Expression<Object>) expressions[1];
        return true;
    }

    @Override
    protected BoundingBox @NotNull [] get(@NotNull Event event) {
        Object fromObject = fromExpression.getSingle(event);
        if (fromObject == null) return new BoundingBox[0];
        Object toObject = toExpression.getSingle(event);
        if (toObject == null) return new BoundingBox[0];
        Vector from;
        switch (fromObject) {
            case Vector vector -> from = vector;
            case Location location -> from = location.toVector();
            default -> {
                return new BoundingBox[0];
            }
        }
        Vector to;
        switch (toObject) {
            case Vector vector -> to = vector;
            case Location location -> to = location.toVector();
            default -> {
                return new BoundingBox[0];
            }
        }
        return new BoundingBox[]{BoundingBox.of(from, to)};
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
        return "a new bounding box from " + fromExpression.toString(event, debug) + " and " + toExpression.toString(event, debug);
    }
}