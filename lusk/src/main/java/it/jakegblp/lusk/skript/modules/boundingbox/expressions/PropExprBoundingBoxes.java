package it.jakegblp.lusk.skript.modules.boundingbox.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import it.jakegblp.lusk.skript.modules.boundingbox.BoundingBoxModule;
import org.jetbrains.annotations.NotNull;
import org.skriptlang.skript.lang.properties.Property;
import org.skriptlang.skript.lang.properties.PropertyBaseExpression;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Bounding Boxes")
@Description("Represents an object which has multiple bounding boxes.")
@Example("set {_boxes::*} to bounding boxes of {_shape}")
@Since({"1.2", "2.0.0 (Property)"})
@RelatedProperty("bounding boxes")
public class PropExprBoundingBoxes extends PropertyBaseExpression<ExpressionPropertyHandler<?, Object>> {

	public static void register(SyntaxRegistry registry) {
		Registrations.propertyExpression(PropExprBoundingBoxes.class, PropExprBoundingBoxes::new, Object.class)
				.propertyOf("bounding boxes", "objects")
				.register(registry);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public @NotNull Property<ExpressionPropertyHandler<?, Object>> getProperty() {
		return BoundingBoxModule.BOUNDING_BOXES;
	}
}