package it.jakegblp.lusk.elements.minecraft.entities.player.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import io.papermc.paper.event.player.PlayerFlowerPotManipulateEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("is Placing in Pot")
@Description("This Condition requires Paper.\n\nChecks whether or not the item is being placed inside the flower pot in a Flower Pot Manipulate Event.")
@Examples({"""
        on potting of cornflower:
          if the plant is being placed:
            broadcast "placed"
          else if the plant is being picked up:
            broadcast "picked up"
        """})
@Since("1.0.0")
@RequiredPlugins("Paper")
@SuppressWarnings("unused")
public class CondPlacingFlower extends Condition {
    static {
        if (Skript.classExists("io.papermc.paper.event.player.PlayerFlowerPotManipulateEvent")) {
            Skript.registerCondition(CondPlacingFlower.class, "[the] (flower|plant|item) is being (:placed|picked up)",
                    "[the] (flower|plant|item) is(n't| not) being (:placed|picked up)");
        }
    }

    private boolean placed;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        if (!(getParser().isCurrentEvent(PlayerFlowerPotManipulateEvent.class))) {
            Skript.error("This condition can only be used in the Flower Pot Manipulate event!");
            return false;
        }
        placed = parser.hasTag("placed");
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the item is" + (isNegated() ? " not" : "") + " being " + (placed ? "placed" : "picked up");
    }

    @Override
    public boolean check(@NotNull Event event) {
        return !placed ^ (isNegated()) ^ ((PlayerFlowerPotManipulateEvent) event).isPlacing();
    }
}