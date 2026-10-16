package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import it.jakegblp.lusk.skript.modules.base.BaseModule;
import org.skriptlang.skript.lang.properties.Property;
import org.skriptlang.skript.lang.properties.PropertyBaseExpression;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Volume")
@Description("Represents an object which has a volume.")
@Example("set {_volume} to volume of {_box}")
@Since({"1.2.1-beta1", "2.0.0 (Property)"})
@RelatedProperty("volume")
public class PropExprVolume extends PropertyBaseExpression<ExpressionPropertyHandler<?, ?>> {

	public static void register(SyntaxRegistry registry) {
		Registrations.propertyExpression(PropExprVolume.class, PropExprVolume::new, Object.class)
				.propertyOf("volume", "objects")
				.register(registry);
	}

	@Override
	public Property<ExpressionPropertyHandler<?, ?>> getProperty() {
		return BaseModule.VOLUME;
	}

}