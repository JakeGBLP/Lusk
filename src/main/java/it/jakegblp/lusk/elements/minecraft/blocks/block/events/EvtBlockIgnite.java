package it.jakegblp.lusk.elements.minecraft.blocks.block.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockIgniteEvent;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.ArrayUtils.haveSameElements;
import static it.jakegblp.lusk.utils.CompatibilityUtils.registerEventValue;

public class EvtBlockIgnite extends SkriptEvent {

    static {
        Skript.registerEvent("Block - on Ignite [Lusk Extension]",EvtBlockIgnite.class, BlockIgniteEvent.class,
                "[lusk] [:player] block ignit(e|ion) [due to %-*ignitecauses%]")
                .description("""
                        **THIS IS AN EXTENDED VERSION OF SKRIPT'S IGNITE EVENT.**
                        
                        `event-player` = the Player that placed/ignited the fire block, or not set if not ignited by a Player.
                        
                        `event-entity` = the Entity that placed/ignited the fire block, or not set if not ignited by an Entity.
                        
                        `event-ignitecause` = the value detailing the cause of the block ignition.
                        
                        Called when a block is ignited. If you want to catch when a Player places fire, use the place event.
                        If this event is cancelled, the block will not be ignited.
                        """)
                .examples("on lusk block ignite due to lava_ignition:","on lusk player block ignition:")
                .since("1.3");

        registerEventValue(BlockIgniteEvent.class, Entity.class, BlockIgniteEvent::getIgnitingEntity, EventValues.TIME_NOW);
        registerEventValue(BlockIgniteEvent.class, BlockIgniteEvent.IgniteCause.class, BlockIgniteEvent::getCause, EventValues.TIME_NOW);
    }

    private boolean justPlayer;
    private boolean allIgniteCauses = false;
    private Literal<BlockIgniteEvent.IgniteCause> igniteCauseLiterals;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        justPlayer = parseResult.hasTag("player");
        igniteCauseLiterals = (Literal<BlockIgniteEvent.IgniteCause>) args[0];
        if (haveSameElements(igniteCauseLiterals.getArray(), BlockIgniteEvent.IgniteCause.values())) {
            allIgniteCauses = true;
            Skript.warning("You've selected every single ignition cause, this is the same as not putting any.");
        }
        return true;
    }

    @Override
    public boolean check(Event event) {
        BlockIgniteEvent e = (BlockIgniteEvent) event;
        return (allIgniteCauses
                || igniteCauseLiterals.getAll().length == 0
                || igniteCauseLiterals.stream(e).anyMatch(igniteCause -> igniteCause.equals(e.getCause()))
               ) && (!justPlayer || e.getPlayer() != null);
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "lusk "+ (justPlayer ? "player " : "")+ "block ignite";
    }
}
