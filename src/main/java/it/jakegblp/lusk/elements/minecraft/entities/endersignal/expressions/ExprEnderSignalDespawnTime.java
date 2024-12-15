package it.jakegblp.lusk.elements.minecraft.entities.endersignal.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import it.jakegblp.lusk.utils.DeprecationUtils;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

@Name("Ender Signal - Despawn Time/ticks")
@Description("""
Gets how long the provided ender signals have been alive for.
Can be set.
Either as ticks or a timespan.
When greater than 80 ticks (or 4 seconds), it will despawn on the next tick.
""")
@Examples("send the ender signal despawn time of {_enderSignal}")
@Since("1.3")
public class ExprEnderSignalDespawnTime extends SimplerPropertyExpression<Entity, Object> {

    static {
        register(ExprEnderSignalDespawnTime.class, Object.class, "ender (signal|eye) despawn (time[r]|:ticks)", "entities");
    }

    private boolean usesTicks;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        usesTicks = parseResult.hasTag("ticks");
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @Nullable Object convert(Entity from) {
        if (from instanceof EnderSignal enderSignal) {
            int ticks = enderSignal.getDespawnTimer();
            if (usesTicks) {
                return ticks;
            } else {
                return DeprecationUtils.fromTicks(ticks);
            }
        }
        return null;
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            if (usesTicks) {
                return new Class[] {Integer.class};
            } else {
                return new Class[] {Timespan.class};
            }
        } else if (mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.DELETE) {
            return new Class[0];
        }
        return null;
    }

    @Override
    public void set(Entity from, Object to) {
        if (from instanceof EnderSignal enderSignal) {
            if (to instanceof Integer integer) {
                enderSignal.setDespawnTimer(integer);
            } else if (to instanceof Timespan timespan) {
                enderSignal.setDespawnTimer((int) DeprecationUtils.getTicks(timespan));
            }
        }
    }

    @Override
    protected String getPropertyName() {
        return "ender signal despawn " + (usesTicks ? "ticks" : "time");
    }

    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }
}
