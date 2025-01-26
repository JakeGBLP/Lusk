package it.jakegblp.lusk.elements.minecraft.mixed.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import it.jakegblp.lusk.utils.CompatibilityUtils;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.jetbrains.annotations.Nullable;

@Name("Creeper/Primed TNT/TNT Minecart - Max/Regular Fuse Time/Ticks")
@Description("""
        The fuse ticks of a Creeper, Primed TNT or TNT Minecart, this differs based on which one of these 3 is used, read below.
        Can be set, added to, removed from and reset (which sets it to 0, this can or cannot be what you want based on the entity, read below).
        
        The `max` keyword only applies to creepers.
        
        `creeper`:
        - the time that the creeper has been in the primed state for;
        - if max is used it will return the maximum amount of time that the creeper can stay in the ignited state for until it explodes;
        - both math and regular fuse time must be greater than 0, regular fuse time cannot be greater than max fuse time;
        `primed tnt`: the time until the primed tnt explodes;
        `tnt minecart`: the time until the minecart explodes, -1 if not ignited.
        """)
@Examples("set the primed fuse ticks of {_creeper} to 10")
@Since("1.3")
public class ExprFuseTicks extends SimplerPropertyExpression<Object, Object> {

    static {
        register(ExprFuseTicks.class, Object.class, "[:max] [primed|ignited] fuse (:ticks|time[span]|duration)", "entities");
    }

    private boolean usesTicks, max;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        usesTicks = parseResult.hasTag("ticks");
        max = parseResult.hasTag("max");
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @Nullable Object convert(Object from) {
        int ticks;
        if (from instanceof Creeper creeper) {
            ticks = max ? creeper.getMaxFuseTicks() : creeper.getFuseTicks();
        } else if (from instanceof TNTPrimed tntPrimed) {
            ticks = tntPrimed.getFuseTicks();
        } else if (from instanceof ExplosiveMinecart minecart) {
            ticks = minecart.getFuseTicks();
        } else return null;
        if (usesTicks) return ticks;
        return CompatibilityUtils.fromTicks(ticks);
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public boolean allowAdd() {
        return true;
    }

    @Override
    public boolean allowRemove() {
        return true;
    }

    @Override
    public boolean allowReset() {
        return true;
    }

    @Override
    public void set(Object from, Object to) {
        int ticks;
        if (to instanceof Number number) ticks = number.intValue();
        else if (to instanceof Timespan timespan) ticks = (int) CompatibilityUtils.getTicks(timespan);
        else return;
        ticks = Math.max(ticks, 0);
        if (from instanceof Creeper creeper) {
            if (max) creeper.setMaxFuseTicks(ticks);
            else creeper.setFuseTicks(Math.min(ticks, creeper.getMaxFuseTicks()));
        } else if (from instanceof TNTPrimed tntPrimed) {
            tntPrimed.setFuseTicks(ticks);
        } else if (from instanceof ExplosiveMinecart minecart) {
            minecart.setFuseTicks(ticks);
        }
    }

    @Override
    public void add(Object from, Object to) {
        int ticks;
        if (to instanceof Number number) ticks = number.intValue();
        else if (to instanceof Timespan timespan) ticks = (int) CompatibilityUtils.getTicks(timespan);
        else return;
        ticks = Math.max(ticks, 0);
        if (from instanceof Creeper creeper) {
            if (max) creeper.setMaxFuseTicks(creeper.getMaxFuseTicks()+ticks);
            else creeper.setFuseTicks(Math.min(creeper.getFuseTicks()+ticks, creeper.getMaxFuseTicks()));
        } else if (from instanceof TNTPrimed tntPrimed) {
            tntPrimed.setFuseTicks(tntPrimed.getFuseTicks()+ticks);
        } else if (from instanceof ExplosiveMinecart minecart) {
            minecart.setFuseTicks(ticks+minecart.getFuseTicks());
        }
    }

    @Override
    public void remove(Object from, Object to) {
        int ticks;
        if (to instanceof Number number) ticks = number.intValue();
        else if (to instanceof Timespan timespan) ticks = (int) CompatibilityUtils.getTicks(timespan);
        else return;
        add(from, -ticks);
    }

    @Override
    public void reset(Object from) {
        set(from, 0);
    }

    @Override
    protected String getPropertyName() {
        return (max ? "max ": "")+"primed fuse " + (usesTicks ? "ticks" : "timespan");
    }

    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }
}
