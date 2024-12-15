package it.jakegblp.lusk.elements.minecraft.entities.endersignal.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.PAPER_HAS_ENDER_SIGNAL_SET_TARGET_LOCATION;

@Name("Ender Signal - Set Target Location Without Updating")
@Description("Sets the location the provided ender signals are moving towards without changing the drop chance and the despawn timer.")
@Examples("set the ender signal target location of {_enderSignal} to {_location} without updating")
@Since("1.3")
@RequiredPlugins("Paper")
public class EffEnderSignalTargetLocation extends Effect {

    static {
        if (PAPER_HAS_ENDER_SIGNAL_SET_TARGET_LOCATION)
            Skript.registerEffect(EffEnderSignalTargetLocation.class,
                    "set [the] ender (signal|eye) target location of %entities% to %location% without updating [it]",
                    "set %entities%'[s] ender (signal|eye) target location to %location% without updating [it]");
    }

    private Expression<Entity> entityExpression;
    private Expression<Location> locationExpression;

    @Override
    protected void execute(Event event) {
        Location location = locationExpression.getSingle(event);
        if (location == null) return;
        for (Entity entity : entityExpression.getAll(event)) {
            if (entity instanceof EnderSignal enderSignal) {
                enderSignal.setTargetLocation(location, false);
            }
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "set the ender signal target location of "
                + entityExpression.toString(event, debug) + " to "
                + locationExpression.toString(event, debug) + " without updating it";
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) expressions[0];
        locationExpression = (Expression<Location>) expressions[1];
        return true;
    }
}
