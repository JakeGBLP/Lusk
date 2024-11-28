package it.jakegblp.lusk.elements.minecraft.entities.cat.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Cat - Looking Up")
@Description("Returns whether or not a cat is looking up.\nCan be set.")
@Examples({"broadcast looking up state of target"})
@Since("1.0.3")
@DocumentationId("11897")
@SuppressWarnings("unused")
public class ExprCatLookingUpState extends SimpleExpression<Boolean> {
    static {
        // todo: verbose propertyexpression, utils?, plural
        Skript.registerExpression(ExprCatLookingUpState.class, Boolean.class, ExpressionType.PROPERTY,
                "[the] cat [is] looking up state of %entity%",
                "%entity%'[s] cat [is] looking up state",
                "whether [the] cat %entity% is looking up [or not]",
                "whether [or not] [the] cat %entity% is looking up");
    }

    private Expression<Entity> entityExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) exprs[0];
        return true;
    }

    @Override
    protected Boolean @NotNull [] get(@NotNull Event e) {
        Entity entity = entityExpression.getSingle(e);
        if (entity instanceof Cat cat) return new Boolean[]{cat.isHeadUp()};
        return new Boolean[0];
    }

    @Override
    public Class<?> @Nullable [] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Boolean.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Boolean aBoolean)
            if (entityExpression.getSingle(e) instanceof Cat cat) cat.setHeadUp(aBoolean);
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the cat looking up state of " + (e == null ? "" : entityExpression.toString(e, debug));
    }
}