package it.jakegblp.lusk.elements.minecraft.blocks.brewingstand.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import it.jakegblp.lusk.api.BlockWrapper;
import it.jakegblp.lusk.utils.DeprecationUtils;
import org.jetbrains.annotations.Nullable;

@Name("Brewing - Remaining Time/Ticks")
@Description("Returns the brewing time of a Brewing Stand (the time before the brewing is over, 0 seconds = finished, 20 seconds = just started. Can be set to a longer time, progress won't be displayed until it reaches 20 seconds).\nCan be set.")
@Examples({"on brewing start:\n\tbroadcast the brewing time of event-block"})
@Since("1.0.2, 1.3 (Plural, Blockstate, Item, Ticks)")
@SuppressWarnings("unused")
public class ExprBrewingTime extends SimplerPropertyExpression<Object,Object> {

    static {
        register(ExprBrewingTime.class, Object.class, "[remaining] brewing (time[span]|:ticks)", "blocks/blockstates/itemtypes");
    }

    private boolean usesTicks;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        usesTicks = parseResult.hasTag("ticks");
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @Nullable Object convert(Object from) {
        Integer ticks = new BlockWrapper(from).getBrewingTime();
        if (ticks == null) return null;
        if (usesTicks) return ticks;
        return DeprecationUtils.fromTicks(ticks);
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class<?>[]{Timespan.class, Integer.class} : null;
    }

    @Override
    public void set(Object from, Object to) {
        if (to instanceof Integer integer) {
            new BlockWrapper(from).setBrewingTime(integer);
        } else if (to instanceof Timespan timespan) {
            new BlockWrapper(from).setBrewingTime((int)DeprecationUtils.getTicks(timespan));
        }
    }

    @Override
    protected String getPropertyName() {
        return "remaining brewing " + (usesTicks ? "ticks" : "time");
    }

    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }
}