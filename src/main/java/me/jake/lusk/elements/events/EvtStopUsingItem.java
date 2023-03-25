package me.jake.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class EvtStopUsingItem extends SkriptEvent {
    static {
        if (Skript.classExists("io.papermc.paper.event.player.PlayerStopUsingItemEvent")) {
            Skript.registerEvent("Stop Using Item", EvtStopUsingItem.class, PlayerStopUsingItemEvent.class,
                            "stop using (item|%itemtype%)"
                    )
                    .description("This Event requires Paper.\n\nCalled when the server detects a player stopping using an item. Examples of this are letting go of the interact button when holding a bow, an edible item, or a spyglass.")
                    .examples("""
                            """)
                    .since("1.0.0");
        }
    }

    private ItemType itemType;

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        if (args.length > 0) {
            itemType = args[0] == null ? null : (ItemType) (args[0]).getSingle();
        }
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        if (itemType == null) {
            return true;
        }
        final @NotNull Material item = ((PlayerStopUsingItemEvent) e).getItem().getType();
        return itemType.getMaterial() == item;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "Stop using " + (itemType == null ? "item" : itemType);
    }
}
