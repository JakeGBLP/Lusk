package it.jakegblp.lusk.elements.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.command.EffectCommandEvent;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import org.bukkit.command.CommandSender;

public class EvtSkriptEvents {
    static {
        Skript.registerEvent("Skript - on Effect Command", SimpleEvent.class, EffectCommandEvent.class,
                        "[s(k|c)ript] [chat] effect command"
                )
                .description("Called when someone uses a skript effect through the chat, also known as effect command, this can be enabled through skript's config.")
                .examples(
                        """
                        on effect command:
                            if the effect command contains "op":
                                set effect command to "send ""why are you trying to op someone?""\"
                        """
                )
                .since("1.3");
        EventValues.registerEventValue(EffectCommandEvent.class, String.class, new Getter<>() {
            @Override
            public String get(EffectCommandEvent e) {
                return e.getCommand();
            }
        }, 0);
        EventValues.registerEventValue(EffectCommandEvent.class, String.class, new Getter<>() {
            @Override
            public String get(EffectCommandEvent e) {
                return e.getCommand();
            }
        }, 0);
        EventValues.registerEventValue(EffectCommandEvent.class, CommandSender.class, new Getter<>() {
            @Override
            public CommandSender get(EffectCommandEvent e) {
                return e.getSender();
            }
        }, 0);
    }
}
