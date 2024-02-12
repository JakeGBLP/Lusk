package it.jakegblp.lusk.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.destroystokyo.paper.event.block.BeaconEffectEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Beacon - Applied Effect is Primary Effect")
@Description("Checks if the applied effect in the Beacon Effect Applied Event is the primary effect.")
@Examples({"on beacon effect apply:\n\tif the applied effect is the primary effect:\n\t\tbroadcast \"the primary effect has been applied!\""})
@Since("1.0.3")
public class CondBeaconAppliedEffectPrimary extends Condition {
    static {
        Skript.registerCondition(CondBeaconAppliedEffectPrimary.class, "[the] applied [beacon] effect is [the] primary [beacon] effect",
                "[the] applied [beacon] effect is(n't| not) [the] primary [beacon] effect",
                "[the] applied [beacon] effect is [the] secondary [beacon] effect",
                "[the] applied [beacon] effect is(n't| not) [the] secondary [beacon] effect");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        if (!getParser().isCurrentEvent(BeaconEffectEvent.class)) {
            Skript.error("This condition can only be used in Beacon Effect Applied Event.");
            return false;
        }
        setNegated(matchedPattern == 1 || matchedPattern == 2);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the applied beacon effect is" + (isNegated() ? " not" : "") + " the primary beacon effect";
    }

    @Override
    public boolean check(@NotNull Event event) {
        if (event instanceof BeaconEffectEvent beaconEffectEvent) {
            return isNegated() ^ beaconEffectEvent.isPrimary();
        }
        return false;
    }
}