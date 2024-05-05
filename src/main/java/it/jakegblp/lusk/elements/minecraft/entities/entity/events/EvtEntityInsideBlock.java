package it.jakegblp.lusk.elements.minecraft.entities.entity.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import io.papermc.paper.event.entity.EntityInsideBlockEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class EvtEntityInsideBlock extends SkriptEvent {
    static {
        if (Skript.classExists("io.papermc.paper.event.entity.EntityInsideBlockEvent")) {
            Skript.registerEvent("Entity - Inside Block Event", EvtEntityInsideBlock.class, EntityInsideBlockEvent.class, "[entity] (collide with [a] block|in[side] [a] block)")
                    .description("""
                            This Event requires Paper.

                            Called when an entity enters the hitbox of a block. Only called for blocks that react when an entity is inside. If cancelled, any action that would have resulted from that entity being in the block will not happen (such as extinguishing an entity in a cauldron).
                            Blocks this is currently called for:

                            Big dripleaf
                            Bubble column
                            Buttons
                            Cactus
                            Campfire
                            Cauldron
                            Crops
                            Ender Portal
                            Fires
                            Frogspawn
                            Honey
                            Hopper
                            Detector rails
                            Nether portals
                            Powdered snow
                            Pressure plates
                            Sweet berry bush
                            Tripwire
                            Waterlily
                            Web
                            Wither rose""")
                    .examples("on wake up:\n\tbroadcast \"A bat has woken up!\"")
                    .since("1.0.0");
        }
    }


    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "entity in block";
    }
}
