package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Client Sided Custom Name Visibility")
@Description("Gets whether or not the entity's custom name is displayed client side.\n" +
        "This value has no effect on players, they will always display their name.")
@Examples({"broadcast custom name visibility of target"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprCustomNameVisibility extends PropertyExpression<Entity, Boolean> {
    static {
        register(ExprCustomNameVisibility.class, Boolean.class,
                "[client[[-| ]side[d]]] custom[ |-]name visibility",
                "entities");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr((Expression<? extends Entity>) expressions[0]);
        return true;
    }

    @Override
    protected Boolean @NotNull [] get(@NotNull Event event, Entity @NotNull [] source) {
        return getExpr().stream(event)
                .map(Entity::isCustomNameVisible)
                .toArray(Boolean[]::new);
    }

    @Override
    public Class<?>[] acceptChange(final Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Boolean.class} : null;
    }

    @Override
    public void change(@NotNull Event event, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET && delta.length > 0 && delta[0] instanceof Boolean visibility) {
            for (Entity entity : getExpr().getArray(event)) {
                entity.setCustomNameVisible(visibility);
            }
        }
    }

    @Override
    public @NotNull Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "client sided custom name visibility";
    }
}