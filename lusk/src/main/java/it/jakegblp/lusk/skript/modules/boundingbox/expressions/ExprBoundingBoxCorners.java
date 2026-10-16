package it.jakegblp.lusk.skript.modules.boundingbox.expressions;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Bounding Box - Corners")
@Description("Gets either corner of one or more bounding boxes as vectors.")
@Example("broadcast box lesser corner of bounding box of target")
@Example("send box greater corner of {_box}")
@Keywords({"bounding", "box", "boundingbox", "boundingboxes", "corners"})
@Since({"1.3", "2.0.0 (Moved center to a separate expression)"})
public class ExprBoundingBoxCorners extends SimplePropertyExpression<BoundingBox, Vector> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprBoundingBoxCorners.class, ExprBoundingBoxCorners::new, Vector.class)
                .propertyOf("[[bounding] box] (:lesser|greater) corner", "boundingboxes")
                .defaultExpression(true)
                .register(syntaxRegistry);
    }

    private boolean lesser;

    @Override
    public boolean init(final Expression<?>[] vars, final int matchedPattern, final @NotNull Kleenean isDelayed, final SkriptParser.ParseResult parser) {
        var init = super.init(vars, matchedPattern, isDelayed, parser);
        lesser = parser.hasTag("lesser");
        return init;
    }

    @Override
    public @Nullable Vector convert(BoundingBox from) {
        return lesser ? from.getMin() : from.getMax();
    }

    @Override
    @NotNull
    public Class<Vector> getReturnType() {
        return Vector.class;
    }


    @Override
    protected String getPropertyName() {
        return (lesser ? "lesser" : "greater") + " corner";
    }
}