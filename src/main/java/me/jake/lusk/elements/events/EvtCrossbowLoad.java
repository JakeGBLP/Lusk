package me.jake.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EvtCrossbowLoad extends SkriptEvent {

    static {
        if (Skript.classExists("io.papermc.paper.event.entity.EntityLoadCrossbowEvent")) {
            Skript.registerEvent("Crossbow Load", EvtCrossbowLoad.class, EntityLoadCrossbowEvent.class, "[entity] (crossbow load|load of crossbow)")
                    .description("Called when a LivingEntity loads a crossbow with a projectile.\n\nThis event requires Paper.")
                    .examples("")
                    .since("1.0.1");
        }
    }

    @Override
    public boolean init(Literal @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "entity crossbow load";
    }

}
