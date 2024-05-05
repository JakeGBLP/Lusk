package it.jakegblp.lusk.elements.minecraft.entities.player.events.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Should Trigger Advancements")
@Description("This Condition requires Paper.\n\nChecks whether or not the Slot Change event should trigger advancements.")
@Examples({"""
        on item obtain:
          if the event will trigger any advancements:
            make the event not trigger any advancements
        """})
@Since("1.0.0")
public class CondTriggerAdvancements extends Condition {

    static {
        if (Skript.classExists("io.papermc.paper.event.player.PlayerInventorySlotChangeEvent")) {
            Skript.registerCondition(CondTriggerAdvancements.class, "[the] event will trigger [any] advancements",
                    "[the] event w(ill not|on't) trigger [any] advancements");
        }
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        if (!(getParser().isCurrentEvent(PlayerInventorySlotChangeEvent.class))) {
            Skript.error("This condition can only be used in the Slot Change event!");
            return false;
        }
        if (matchedPattern == 1) {
            setNegated(true);
        }
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the event will " + (isNegated() ? "not" : "") + " trigger any advancements";
    }

    @Override
    public boolean check(@NotNull Event event) {
        return (isNegated()) ^ ((PlayerInventorySlotChangeEvent) event).shouldTriggerAdvancements();
    }
}