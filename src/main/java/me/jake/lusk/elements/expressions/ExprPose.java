package me.jake.lusk.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pose;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Pose")
@Description("Returns the entity's current pose. Note that the pose is only updated at the end of a tick, so it may be inconsistent.")
@Examples({"broadcast pose of target"})
@Since("1.0.2")
public class ExprPose extends SimplePropertyExpression<Entity, Pose> {
    static {
        register(ExprPose.class, Pose.class, "pose", "entity");
    }

    @Override
    public @NotNull Class<? extends Pose> getReturnType() {
        return Pose.class;
    }

    @Override
    @Nullable
    public Pose convert(Entity e) {
        if (e != null) {
            return e.getPose();
        }
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "pose";
    }
}