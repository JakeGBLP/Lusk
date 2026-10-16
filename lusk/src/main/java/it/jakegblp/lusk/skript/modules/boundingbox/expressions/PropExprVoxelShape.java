package it.jakegblp.lusk.skript.modules.boundingbox.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import it.jakegblp.lusk.skript.modules.boundingbox.BoundingBoxModule;
import org.bukkit.util.VoxelShape;
import org.skriptlang.skript.lang.properties.Property;
import org.skriptlang.skript.lang.properties.PropertyBaseExpression;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Voxel Shape")
@Description("Represents an object which has a voxel shape.")
@Example("set {_shape} to voxel shape of {_block}")
@Since("2.0.0")
@RelatedProperty("voxel shape")
public class PropExprVoxelShape extends PropertyBaseExpression<ExpressionPropertyHandler<?, VoxelShape>> {

	public static void register(SyntaxRegistry registry) {
		Registrations.propertyExpression(PropExprVoxelShape.class, PropExprVoxelShape::new, Object.class)
				.propertyOf("voxel shape", "objects")
				.register(registry);
	}

	@Override
	public Property<ExpressionPropertyHandler<?, VoxelShape>> getProperty() {
		return BoundingBoxModule.VOXEL_SHAPE;
	}
}