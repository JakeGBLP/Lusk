package it.jakegblp.lusk.skript.modules.boundingbox.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import it.jakegblp.lusk.skript.modules.boundingbox.BoundingBoxModule;
import org.bukkit.util.BoundingBox;
import org.skriptlang.skript.lang.properties.Property;
import org.skriptlang.skript.lang.properties.PropertyBaseExpression;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Bounding Box")
@Description("Represents an object which has a bounding box.")
@Example("set {_box} to bounding box of {_block}")
@Since({"1.0.2", "1.2 (Blocks)", "1.3 (BlockStates)", "2.0.0 (Property)"})
@RelatedProperty("bounding box")
public class PropExprBoundingBox extends PropertyBaseExpression<ExpressionPropertyHandler<?, BoundingBox>> {

	public static void register(SyntaxRegistry registry) {
		Registrations.propertyExpression(PropExprBoundingBox.class, PropExprBoundingBox::new, Object.class)
				.propertyOf("bounding box", "objects")
				.register(registry);
	}

	@Override
	public Property<ExpressionPropertyHandler<?, BoundingBox>> getProperty() {
		return BoundingBoxModule.BOUNDING_BOX;
	}


}