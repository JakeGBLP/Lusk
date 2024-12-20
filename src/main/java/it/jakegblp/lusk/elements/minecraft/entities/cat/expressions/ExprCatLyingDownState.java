package it.jakegblp.lusk.elements.minecraft.entities.cat.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
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

@Name("Cat - Lying Down State")
@Description("Returns whether or not a cat is lying down.\nCan be set.")
@Examples({"broadcast lying down state of target"})
@Since("1.0.3")
@SuppressWarnings("unused")
public class ExprCatLyingDownState extends SimpleExpression<Boolean> {
    static {
        Skript.registerExpression(ExprCatLyingDownState.class, Boolean.class, ExpressionType.PROPERTY,
                "[the] cat [is] lying down state of %entity%",
                "%entity%'[s] cat [is] lying down state",
                "whether [the] cat %entity% is lying down [or not]",
                "whether [or not] [the] cat %entity% is lying down");
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
        if (entity instanceof Cat cat) {
            return new Boolean[]{cat.isLyingDown()};
        }
        return new Boolean[0];
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Boolean.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Boolean aBoolean)
            if (entityExpression.getSingle(e) instanceof Cat cat) cat.setLyingDown(aBoolean);
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
        return "the cat lying down state of " + (e == null ? "" : entityExpression.toString(e, debug));
    }
}