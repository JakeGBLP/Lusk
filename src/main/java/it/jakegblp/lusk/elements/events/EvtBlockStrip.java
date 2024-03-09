package it.jakegblp.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import it.jakegblp.lusk.utils.Constants;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class EvtBlockStrip extends SkriptEvent {
    static {
        Skript.registerEvent("Block - on Block Strip", EvtBlockStrip.class, PlayerInteractEvent.class, "[block] strip[p(ed|ing)]", "[block] axe", "[block] deoxidize")
                .description("Called when a player changes a block by right clicking it with an axe.")
                .examples("on strip:\n\tbroadcast \"a block has been stripped!\"")
                .since("1.0.0");
    }

    @Override
    public boolean init(Literal @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        PlayerInteractEvent event = ((PlayerInteractEvent) e);
        if (Constants.axes.contains(event.getMaterial())) {
            if (event.getAction().isRightClick()) {
                Block block = event.getClickedBlock();
                if (block != null) {
                    Material material = block.getType();
                    return Constants.axeables.contains(material);
                }
            }
        }
        return false;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "strip";
    }

}