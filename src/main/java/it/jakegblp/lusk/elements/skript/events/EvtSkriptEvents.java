package it.jakegblp.lusk.elements.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.command.CommandEvent;
import ch.njol.skript.command.EffectCommandEvent;
import ch.njol.skript.lang.util.SimpleEvent;
import org.bukkit.command.CommandSender;

import static it.jakegblp.lusk.utils.CompatibilityUtils.registerEventValue;

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
        registerEventValue(EffectCommandEvent.class, String.class, CommandEvent::getCommand,0);
        registerEventValue(EffectCommandEvent.class, CommandSender.class, CommandEvent::getSender, 0);
    }
}
