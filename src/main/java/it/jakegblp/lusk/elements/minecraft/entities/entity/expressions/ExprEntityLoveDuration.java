package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityEnterLoveModeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.DeprecationUtils.fromTicks;
import static it.jakegblp.lusk.utils.DeprecationUtils.getTicks;

@Name("Entity - Love Duration")
@Description("Returns the love duration of an animal.\n Can be set.")
@Examples({"on love:\n\tbroadcast the love duration", "broadcast love duration of target"})
@Since("1.0.2, 1.0.3 (per Entity)")
@SuppressWarnings("unused")
public class ExprEntityLoveDuration extends SimpleExpression<Timespan> {
    static {
        // todo: make plural, property expression?, utils
        Skript.registerExpression(ExprEntityLoveDuration.class, Timespan.class, ExpressionType.PROPERTY,
                "[the] love duration [of %entity%]",
                "%entity%'[s] love duration");
    }

    boolean event;
    private Expression<Entity> entityExpression = null;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        event = getParser().isCurrentEvent(EntityEnterLoveModeEvent.class);
        if (!event && matchedPattern == 0 && exprs.length == 0) {
            Skript.error("An entity must be specified outside of the Love event!");
            return false;
        }
        if (exprs.length == 1) {
            entityExpression = (Expression<Entity>) exprs[0];
        }
        return true;
    }

    @Override
    protected Timespan @NotNull [] get(@NotNull Event e) {
        if (event && entityExpression == null) {
            return new Timespan[]{fromTicks(((EntityEnterLoveModeEvent) e).getTicksInLove())};
        } else {
            Entity entity = entityExpression.getSingle(e);
            if (entity instanceof Animals animal) {
                return new Timespan[]{fromTicks(animal.getLoveModeTicks())};
            }
        }
        return new Timespan[0];
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Timespan.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Timespan timespan)
            if (entityExpression != null)
                if (entityExpression.getSingle(e) instanceof Animals animal)
                    animal.setLoveModeTicks((int) getTicks(timespan));
                else if (e instanceof EntityEnterLoveModeEvent entityEnterLoveModeEvent)
                    entityEnterLoveModeEvent.setTicksInLove((int) getTicks(timespan));
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Timespan> getReturnType() {
        return Timespan.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the love duration" + (entityExpression == null ? "" : (" of " + (e == null ? "" : entityExpression.toString(e, debug))));
    }
}
