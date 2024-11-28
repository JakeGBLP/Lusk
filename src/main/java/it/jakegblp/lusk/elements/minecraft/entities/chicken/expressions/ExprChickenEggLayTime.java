package it.jakegblp.lusk.elements.minecraft.entities.chicken.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.DeprecationUtils.fromTicks;
import static it.jakegblp.lusk.utils.DeprecationUtils.getTicks;

@Name("Chicken - Egg Lay Time")
@Description("Returns the time till a chicken lays an egg.")
@Examples({"set egg lay time of target to 2 minutes"})
@Since("1.0.3")
@SuppressWarnings("unused")
public class ExprChickenEggLayTime extends SimplePropertyExpression<LivingEntity, Object> {

    static {
        register(ExprChickenEggLayTime.class,Object.class, "[chicken] egg lay (time|:ticks)", "livingentities");
    }

    private boolean usesTicks;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        usesTicks = parseResult.hasTag("ticks");
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @Nullable Object convert(LivingEntity from) {
        if (from instanceof Chicken chicken) {
            int ticks = chicken.getEggLayTime();
            if (usesTicks) return ticks;
            return fromTicks(ticks);
        }
        return null;
    }

    @Override
    protected String getPropertyName() {
        return "chicken egg lay " + (usesTicks ? "ticks" : "time");
    }

    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class<?>[]{Timespan.class, Integer.class} : null;
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET && delta != null) {
            Integer ticks;
            if (delta[0] instanceof Timespan timespan) {
                ticks = (int) getTicks(timespan);
            } else if (delta[0] instanceof Integer integer) {
                ticks = integer;
            } else {
                ticks = null;
            }
            if (ticks != null) {
                getExpr().stream(event).forEach(livingEntity -> {
                    if (livingEntity instanceof Chicken chicken) {
                        chicken.setEggLayTime(ticks);
                    }
                });
            }
        }
    }
}
