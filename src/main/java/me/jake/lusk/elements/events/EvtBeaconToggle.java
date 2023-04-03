package me.jake.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.coll.CollectionUtils;
import io.papermc.paper.event.block.BeaconActivatedEvent;
import io.papermc.paper.event.block.BeaconDeactivatedEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class EvtBeaconToggle extends SkriptEvent {
    static {
        if (Skript.classExists("io.papermc.paper.event.block.BeaconActivatedEvent") && Skript.classExists("io.papermc.paper.event.block.BeaconDeactivatedEvent")) {
            Skript.registerEvent("Beacon on/off", EvtBeaconToggle.class, CollectionUtils.array(BeaconActivatedEvent.class, BeaconDeactivatedEvent.class),
                            "beacon activat(e[d]|ing)",
                            "beacon deactivat(e[d]|ing)",
                            "beacon toggle")
                    .description("This Event requires Paper.\n\nCalled when a beacon is deactivated, either because its base block(s) or itself were destroyed.\nCalled when a beacon is activated. Activation occurs when the beacon beam becomes visible.")
                    .examples("on beacon activate:\n\tbroadcast \"A beacon has been activated!\"")
                    .since("1.0.0+, 1.0.2+ (Toggle)");
        }
    }

    private Boolean activate;

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        if (matchedPattern == 0) {
            activate = true;
        } else if (matchedPattern == 1) {
            activate = false;
        }
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        if (activate == null) return true;
        if (activate) {
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
        return "beacon " + (activate == null ? "toggle" : (activate ? "" : "de") + "activating");
    }

}
