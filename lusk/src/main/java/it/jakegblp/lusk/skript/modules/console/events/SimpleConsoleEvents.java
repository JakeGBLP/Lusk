package it.jakegblp.lusk.skript.modules.console.events;

import it.jakegblp.lusk.nms.core.logger.ConsoleLogInjector;
import it.jakegblp.lusk.nms.core.logger.events.ConsoleLogEvent;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class SimpleConsoleEvents {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.event(ConsoleLogEvent.class)
                .patterns("(console|server) (log|print)")
                .name("Console Log")
                .description("Called when the server's console is used to log something.")
                .examples("on console log:")
                .since("2.0.0")
                .keywords("log", "logging", "console")
                .register(syntaxRegistry);
        Registrations.registerEventValue(ConsoleLogEvent.class, String.class, ConsoleLogEvent::getMessage);
        ConsoleLogInjector.inject(line -> {
            Server server = Bukkit.getServer();
            ConsoleLogEvent event = new ConsoleLogEvent(!server.isPrimaryThread(), line.getMessageStrippedAnsi());
            server.getPluginManager().callEvent(event);
            if (event.isCancelled()) line.cancel();
        });
    }

}
