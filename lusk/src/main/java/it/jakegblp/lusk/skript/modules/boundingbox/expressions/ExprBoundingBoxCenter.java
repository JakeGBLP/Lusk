package it.jakegblp.lusk.skript.modules.boundingbox.expressions;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Bounding Box - Center")
@Description("Gets the center of one or more bounding boxes.")
@Example("broadcast box center of bounding box of target")
@Keywords({"bounding", "box", "boundingbox", "boundingboxes"})
@Since("2.0.0")
public class ExprBoundingBoxCenter extends SimplePropertyExpression<BoundingBox, Vector> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprBoundingBoxCenter.class, ExprBoundingBoxCenter::new, Vector.class)
                .propertyOf("[bounding[ ]]box center", "boundingboxes")
                .defaultExpression(true)
                .register(syntaxRegistry);
    }

    @Override
    public @Nullable Vector convert(BoundingBox from) {
        return from.getCenter();
    }

    @Override
    @NotNull
    public Class<Vector> getReturnType() {
        return Vector.class;
    }

    @Override
    protected String getPropertyName() {
        return "box center";
    }
}