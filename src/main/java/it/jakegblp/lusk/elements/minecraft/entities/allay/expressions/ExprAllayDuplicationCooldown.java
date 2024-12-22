package it.jakegblp.lusk.elements.minecraft.entities.allay.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.utils.DeprecationUtils;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.DeprecationUtils.fromTicks;

@Name("Allay - Duplication Cooldown")
@Description("Returns the duplication cooldown of an Allay.\nCan be set.")
@Examples({"broadcast duplication cooldown of target"})
@Since("1.0.2, 1.3 (Plural, Ticks)")
@SuppressWarnings("unused")
public class ExprAllayDuplicationCooldown extends SimplePropertyExpression<Entity,Object> {
    static {
        register(ExprAllayDuplicationCooldown.class, Object.class,
                "allay duplication cooldown [time[span]|:ticks]", "entities");
    }

    private boolean usesTicks;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        usesTicks = parseResult.hasTag("ticks");
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @Nullable Object convert(Entity from) {
        if (from instanceof Allay allay) {
            long ticks = allay.getDuplicationCooldown();
            return usesTicks ? ticks : fromTicks(ticks);
        }
        return null;
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        Long ticks;
        if (delta != null && delta[0] != null) {
            Object value = delta[0];
            if (value instanceof Long l) ticks = l;
            else if (value instanceof Timespan timespan) ticks = DeprecationUtils.getTicks(timespan);
            else ticks = null;
        } else ticks = null;
        assert mode == Changer.ChangeMode.RESET || ticks != null;
        getExpr().stream(event).forEach(livingEntity -> {
            if (livingEntity instanceof Allay allay) {
                switch (mode) {
                    case REMOVE -> allay.setDuplicationCooldown(allay.getDuplicationCooldown()-ticks);
                    case ADD -> allay.setDuplicationCooldown(allay.getDuplicationCooldown()+ticks);
                    case SET -> allay.setDuplicationCooldown(ticks);
                    case RESET -> allay.setDuplicationCooldown(0L);
                }
            }
        });
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case ADD, SET, REMOVE -> new Class[] {Timespan.class, Long.class};
            case RESET -> new Class[0];
            default -> null;
        };
    }

    @Override
    protected String getPropertyName() {
        return "allay duplication cooldown " + (usesTicks ? "ticks" : "timespan");
    }

    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }
}