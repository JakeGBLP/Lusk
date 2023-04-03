package me.jake.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import com.destroystokyo.paper.event.server.WhitelistToggleEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class EvtWhitelistToggle extends SkriptEvent {
    static {
        if (Skript.classExists("com.destroystokyo.paper.event.server.WhitelistToggleEvent")) {
            Skript.registerEvent("Whitelist Toggle", EvtWhitelistToggle.class, WhitelistToggleEvent.class,
                            "whitelist enable",
                            "whitelist disable",
                            "whitelist toggle")
                    .description("Called when the whitelist is toggled\n\nThis event requires Paper.")
                    .examples("on whitelist enable:\n\tbroadcast \"Whitelist enabled!\"")
                    .since("1.0.2+");
        }
    }

    private Boolean enable;

    @Override
    public boolean init(Literal @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        if (matchedPattern == 0) {
            enable = true;
        } else if (matchedPattern == 1) {
            enable = false;
        }
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        if (enable == null) return true;
        if (enable) {
            return ((WhitelistToggleEvent) e).isEnabled();
        } else {
            return !((WhitelistToggleEvent) e).isEnabled();
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "whitelist " + (enable == null ? "toggle" : (enable ? "enable" : "disable"));
    }
}
