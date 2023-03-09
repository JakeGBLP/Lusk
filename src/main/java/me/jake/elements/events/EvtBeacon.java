package me.jake.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.papermc.paper.event.block.BeaconActivatedEvent;
import io.papermc.paper.event.block.BeaconDeactivatedEvent;

public class EvtBeacon extends SkriptEvent {

    static {
        if (Skript.classExists("io.papermc.paper.event.block.BeaconActivatedEvent") && Skript.classExists("io.papermc.paper.event.block.BeaconDeactivatedEvent")) {
            Skript.registerEvent("Beacon on/off", EvtBeacon.class, CollectionUtils.array(BeaconActivatedEvent.class, BeaconDeactivatedEvent.class),
                            "beacon activat(e[d]|ing)",
                                     "beacon deactivat(e[d]|ing)")
                    .description("This Event requires Paper.\n\nCalled when a beacon is deactivated, either because its base block(s) or itself were destroyed.\nCalled when a beacon is activated. Activation occurs when the beacon beam becomes visible.")
                    .examples("on beacon activate:\n\tbroadcast \"A beacon has been activated!\"")
                    .since("1.0.0");
        }
    }

    private boolean activating;

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        activating = matchedPattern == 1;
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        if (!activating) {
            if (e instanceof BeaconActivatedEvent) {
                return true;

            } else return !(e instanceof BeaconDeactivatedEvent);
        } else {
            if (e instanceof BeaconActivatedEvent) {
                return false;

            } else if (e instanceof BeaconDeactivatedEvent) {
                return true;

            }
        }
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "beacon " + (activating ? "" : "de") + "activating";
    }

}
