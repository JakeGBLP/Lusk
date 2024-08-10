package it.jakegblp.lusk.elements.minecraft.boundingbox.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Bounding Box - Volume")
@Description("Gets the volume of 1 or more bounding boxes.")
@Examples({"broadcast volume of bounding box of target"})
@Since("1.2.1-beta1")
public class ExprBoundingBoxVolume extends SimplePropertyExpression<BoundingBox, Double> {

    static {
        register(ExprBoundingBoxVolume.class, Double.class, "[[bounding[ ]]box] volume", "boundingboxes");
    }

    @Override
    public @Nullable Double convert(BoundingBox from) {
        return from.getVolume();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "bounding box volume";
    }

    @Override
    public @NotNull Class<? extends Double> getReturnType() {
        return Double.class;
    }
}