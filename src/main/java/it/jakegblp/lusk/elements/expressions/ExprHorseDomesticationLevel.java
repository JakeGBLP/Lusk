package it.jakegblp.lusk.elements.expressions;

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
@Description("Returns the domestication level and the maximum domestication level of an horse.\nCan be set.")
@Examples({"broadcast horse domestication level of target"})
@Since("1.0.3")
public class ExprHorseDomesticationLevel extends SimpleExpression<Integer> {
    static {
        Skript.registerExpression(ExprHorseDomesticationLevel.class, Integer.class, ExpressionType.COMBINED,
                "[the] [max:max[imum]] horse domestication level of %entity%",
                "%entity%'[s] [max:max[imum]] horse domestication level");
    }

    private Expression<Entity> entityExpression;
    private boolean max;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) exprs[0];
        max = parseResult.hasTag("max");
        return true;
    }

    @Override
    protected Integer @NotNull [] get(@NotNull Event e) {
        Entity entity = entityExpression.getSingle(e);
        if (entity instanceof AbstractHorse horse) {
            return new Integer[]{max ? horse.getMaxDomestication() : horse.getDomestication()};
        }
        return new Integer[0];
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Integer.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Integer integer)
            if (entityExpression.getSingle(e) instanceof AbstractHorse horse)
                if (max) horse.setMaxDomestication(integer);
                else horse.setDomestication(integer);
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
        return "the "+ (max ? "max ":"") +"horse domestication level of " + (e == null ? "" : entityExpression.toString(e,debug));
    }
}