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
import it.jakegblp.lusk.api.events.PlayerInventorySlotDropEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("on Inventory Slot Drop - Drop Info")
@Description("""
        Checks if the item is being dropped from the cursor or the inventory, if the whole stack is being dropped or both.
        """)
@Examples({"if the item is not getting dropped from the cursor:", "if the whole stack is being dropped:"})
@Since("1.3")
public class CondSlotDropInfo extends Condition {
    static {
        Skript.registerCondition(CondSlotDropInfo.class,
                "[the] item is[not:( not|n't)] [being|getting] dropped from the (:cursor|:inventory)",
                "[the] (whole|full) stack is[not:( not|n't)] [being|getting] dropped [from the (:cursor|:inventory)]");
    }

    private boolean choice = true;
    private boolean cursor;
    private boolean fullStack;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        if (!(getParser().isCurrentEvent(PlayerInventorySlotDropEvent.class))) {
            Skript.error("This condition can only be used in the Inventory Slot Drop event!");
            return false;
        }
        fullStack = matchedPattern == 1;
        if (parser.hasTag("cursor")) {
            cursor = true;
        } else if (parser.hasTag("inventory")) {
            cursor = false;
        } else {
            choice = false;
        }
        setNegated(parser.hasTag("not"));
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        StringBuilder builder = new StringBuilder();
        builder.append("the ").append(fullStack ? "full stack" : "item").append(" is ").append(isNegated() ? "not " : "").append(" being dropped");
        if (choice) {
            builder.append(" from ").append(cursor ? "cursor" : "inventory");
        }
        return builder.toString();
    }

    @Override
    public boolean check(@NotNull Event event) {
        PlayerInventorySlotDropEvent e = (PlayerInventorySlotDropEvent) event;
        boolean value;
        if (fullStack) {
            value = e.isDropsAll();
            if (choice) {
                value = value && (cursor == e.isDropsFromCursor());
            }
        } else value = choice ? cursor == e.isDropsFromCursor() : !e.isDropsFromCursor();
        return isNegated() ^ value;
    }
}