package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Animal - Interested State")
@Description("Returns whether or not an entity is interested.\n(Wolf, Fox)")
@Examples({"broadcast interested state of target"})
@Since("1.0.2")
@DocumentationId("9056")
@SuppressWarnings("unused")
public class ExprEntityInterestedState extends SimpleExpression<Boolean> {
    static {
        // TODO: WHETHER
        Skript.registerExpression(ExprEntityInterestedState.class, Boolean.class, ExpressionType.PROPERTY,
                "[the] interested state of %livingentity%",
                "%livingentity%'[s] interested state");

    }

    private Expression<LivingEntity> livingEntityExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        livingEntityExpression = (Expression<LivingEntity>) exprs[0];
        return true;
    }

    @Override
    protected Boolean @NotNull [] get(@NotNull Event e) {
        LivingEntity livingEntity = livingEntityExpression.getSingle(e);
        boolean bool;
        if (livingEntity instanceof Wolf wolf) {
            bool = wolf.isInterested();
        } else if (livingEntity instanceof Fox fox) {
            bool = fox.isInterested();
        } else {
            bool = false;
        }
        return new Boolean[]{bool};
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Boolean.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Boolean aBoolean)
            if (livingEntityExpression.getSingle(e) instanceof Wolf wolf) wolf.setInterested(aBoolean);
            else if (livingEntityExpression.getSingle(e) instanceof Fox fox) fox.setInterested(aBoolean);
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
        return "the interested state of " + (e == null ? "" : livingEntityExpression.toString(e, debug));
    }
}