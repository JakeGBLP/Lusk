package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pose;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.Constants.PAPER_1_20_1;

@Name("Entity - Pose/Fixed Pose")
@Description("""
Returns the provided entities' current pose.
Note that the pose is only updated at the end of a tick, so it may be inconsistent.

Can be set.

Setting the "Fixed" pose will make it stay until manually changed, this requires Paper 1.20.1+
""")
@Examples({"broadcast pose of target"})
@Since("1.0.2, 1.3 (Plural), 1.3.3 (Changeable and Fixed)")
public class ExprEntityPose extends SimplerPropertyExpression<Entity, Pose> {

    static {
        register(ExprEntityPose.class, Pose.class, (PAPER_1_20_1 ? "[:fixed] " : "") + "[entity] pose", "entities");
    }

    private boolean fixed;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        fixed = parseResult.hasTag("fixed");
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @NotNull Class<? extends Pose> getReturnType() {
        return Pose.class;
    }

    @Override
    public boolean allowSet() {
        return PAPER_1_20_1;
    }

    @Override
    public void set(Entity from, Pose to) {
        from.setPose(to, fixed);
    }

    @Override
    public Pose convert(Entity e) {
        return e.getPose();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return (fixed ? "fixed ": "") + "entity pose";
    }
}