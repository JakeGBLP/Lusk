package it.jakegblp.lusk.elements.minecraft.entities.itemframe.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.SkriptUtils.registerEventValue;

@SuppressWarnings("unused")
public class EvtItemFrameChange extends SkriptEvent {
    static {
        if (Skript.classExists("io.papermc.paper.event.player.PlayerItemFrameChangeEvent")) {
            Skript.registerEvent("Item Frame - on Change", EvtItemFrameChange.class, PlayerItemFrameChangeEvent.class,
                            "item[ |-]frame [item] [place:(insert|place)|:remove|:rotate|interact|change]"
                    )
                    .description("Called when an ItemFrame is having an item rotated, added, or removed from it.")
                    .examples("""
                            on itemframe rotate:
                              broadcast "rotated"

                            on itemframe remove:
                              broadcast "remove"

                            on itemframe place:
                              broadcast "place"
                            """)
                    .since("1.0.0")
                    .requiredPlugins("Paper");
            registerEventValue(PlayerItemFrameChangeEvent.class, Entity.class, PlayerItemFrameChangeEvent::getItemFrame, 0);
            registerEventValue(PlayerItemFrameChangeEvent.class, ItemStack.class, PlayerItemFrameChangeEvent::getItemStack, 0);
        }
    }

    private String action;

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        if (parseResult.hasTag("place")) {
            action = "PLACE";
        } else if (parseResult.hasTag("remove")) {
            action = "REMOVE";
        } else if (parseResult.hasTag("rotate")) {
            action = "ROTATE";
        }
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        if (action == null) {
            return true;
        }
        return action.equals(((PlayerItemFrameChangeEvent) e).getAction().toString());
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return switch (action) {
            case "PLACE" -> "item frame place";
            case "REMOVE" -> "item frame remove";
            case "ROTATE" -> "item frame rotate";
            default -> "item frame interact";
        };
    }
}
