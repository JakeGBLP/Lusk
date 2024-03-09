package it.jakegblp.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import org.bukkit.entity.Pose;
import org.bukkit.entity.Warden;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityPoseChangeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class EvtWardenSniff extends SkriptEvent {
    static {
        Skript.registerEvent("Warden - on Sniff Toggle", EvtWardenSniff.class, EntityPoseChangeEvent.class,
                        "warden (sniff[ing] [start|begin]|start sniffing)",
                        "warden (sniff[ing] stop|stop sniffing)",
                        "warden (sniff toggle|toggle sniff)")
                .description("Called when a warden sniffs around.")
                .examples("on warden sniff:\n\tbroadcast event-entity")
                .since("1.0.2+");
    }

    private Boolean start;

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        if (matchedPattern == 0) {
            start = true;
        } else if (matchedPattern == 1) {
            start = false;
        }
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        EntityPoseChangeEvent event = (EntityPoseChangeEvent) e;
        if (!(event.getEntity() instanceof Warden)) return false;
        if (start == null) return true;
        return start ? event.getPose() == Pose.SNIFFING : event.getEntity().getPose() == Pose.SNIFFING;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "warden " + (start == null ? "sniff toggle" : (start ? "sniff start" : "sniff stop"));
    }

}
