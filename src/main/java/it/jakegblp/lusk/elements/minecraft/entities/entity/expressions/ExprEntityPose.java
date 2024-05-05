package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pose;
import org.jetbrains.annotations.NotNull;

@Name("Entity - Pose")
@Description("Returns the entity's current pose. Note that the pose is only updated at the end of a tick, so it may be inconsistent.")
@Examples({"broadcast pose of target"})
@Since("1.0.2")
public class ExprEntityPose extends SimplePropertyExpression<Entity, Pose> {
    static {
        register(ExprEntityPose.class, Pose.class, "pose", "entity");
    }

    @Override
    public @NotNull Class<? extends Pose> getReturnType() {
        return Pose.class;
    }

    @Override
    public Pose convert(Entity e) {
        return e.getPose();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "pose";
    }
}