package me.jake.lusk.elements.expressions;

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
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Horse - Domestication Level")
@Description("Returns the domestication level of an horse.\nCan be set.")
@Examples({"broadcast domestication level of target"})
@Since("1.0.3")
public class ExprHorseDomesticationLevel extends SimpleExpression<Integer> {
    static {
        Skript.registerExpression(ExprHorseDomesticationLevel.class, Integer.class, ExpressionType.COMBINED,
                "[the] [horse] domestication level of %entity%");
    }

    private Expression<Entity> entityExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) exprs[0];
        return true;
    }
    @Override
    protected Integer @NotNull [] get(@NotNull Event e) {
        Entity entity = entityExpression.getSingle(e);
        if (entity instanceof AbstractHorse horse) {
            return new Integer[]{horse.getDomestication()};
        }
        return new Integer[0];
    }
    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Integer[].class);
        }
        return new Class[0];
    }
    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Integer integer = delta instanceof Integer[] ? ((Integer[]) delta)[0] : null;
        if (integer == null) return;
        Entity entity = entityExpression.getSingle(e);
        if (entity instanceof AbstractHorse horse) {
            horse.setDomestication(integer);
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the horse domestication level of " + (e == null ? "" : entityExpression.getSingle(e));
    }
}