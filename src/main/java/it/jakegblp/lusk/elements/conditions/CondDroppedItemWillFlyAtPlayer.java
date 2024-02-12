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
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Dropped Item - Will Fly at Player")
@Description("Checks whether or not the dropped item in the Pickup Attempt Event will fly at the player.")
@Examples("")
@Since("1.0.4")
public class CondDroppedItemWillFlyAtPlayer extends Condition {
    static {
        Skript.registerCondition(CondDroppedItemWillFlyAtPlayer.class, "[the] [dropped] item will fly (towards [the]|at) player",
                "[the] [dropped] item w(ill not|on't) fly (towards [the]|at) player");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        if (!(getParser().isCurrentEvent(PlayerAttemptPickupItemEvent.class))) {
            Skript.error("This condition can only be used in the Pickup Attempt Event!");
            return false;
        }
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the dropped item will " + (isNegated() ? "not" : "") + " fly at player";
    }

    @Override
    public boolean check(@NotNull Event event) {
        return (isNegated()) ^ ((PlayerAttemptPickupItemEvent) event).getFlyAtPlayer();
    }
}