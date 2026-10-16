package it.jakegblp.lusk.skript.modules.base.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockIgniteEvent;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Arrays;

public class EvtBlockIgnite extends SkriptEvent {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.event(EvtBlockIgnite.class, BlockIgniteEvent.class)
                .patterns("[lusk] [:player] block ignit(e|ion) [due to %-*ignitioncauses%]")
                .name("Ignite [Lusk Extension]")
                .description("""
                        **THIS IS AN EXTENDED VERSION OF SKRIPT'S IGNITE EVENT.**
                        
                        `event-player` = the Player that placed/ignited the fire block, or not set if not ignited by a Player.
                        
                        `event-entity` = the Entity that placed/ignited the fire block, or not set if not ignited by an Entity.
                        
                        `event-ignitecause` = the value detailing the cause of the block ignition.
                        
                        Called when a block is ignited. If you want to catch when a Player places fire, use the place event.
                        If this event is cancelled, the block will not be ignited.
                        """)
                .since("1.3")
                .keywords("ignite", "ignition", "extended", "extension")
                .examples("on lusk block ignite due to lava_ignition:", "on lusk player block ignition:")
                .register(syntaxRegistry);
        Registrations.registerEventValue(BlockIgniteEvent.class, Entity.class, BlockIgniteEvent::getIgnitingEntity);
        Registrations.registerEventValue(BlockIgniteEvent.class, BlockIgniteEvent.IgniteCause.class, BlockIgniteEvent::getCause);
    }

    private boolean justPlayer;
    private boolean allIgniteCauses = false;
    private Literal<BlockIgniteEvent.IgniteCause> igniteCauseLiterals;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        justPlayer = parseResult.hasTag("player");
        igniteCauseLiterals = (Literal<BlockIgniteEvent.IgniteCause>) args[0];
        var ignitionCauses = igniteCauseLiterals.getArray();
        if (Arrays.stream(ignitionCauses).distinct().count() != ignitionCauses.length) {
            Skript.warning("You've provided duplicate causes.");
        } else if (ignitionCauses.length == BlockIgniteEvent.IgniteCause.values().length) {
            allIgniteCauses = true;
            Skript.warning("You've selected every single ignition cause, this is the same as not putting any.");
        }
        return true;
    }

    @Override
    public boolean check(Event event) {
        BlockIgniteEvent e = (BlockIgniteEvent) event;
        return (allIgniteCauses
                || igniteCauseLiterals.getArray().length == 0
                || igniteCauseLiterals.stream(e).anyMatch(igniteCause -> igniteCause.equals(e.getCause()))
        ) && (!justPlayer || e.getPlayer() != null);
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "lusk " + (justPlayer ? "player " : "") + "block ignite";
    }
}