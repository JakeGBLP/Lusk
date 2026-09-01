package it.jakegblp.lusk.skript.modules.console.events;

import ch.njol.skript.lang.util.SimpleEvent;
import it.jakegblp.lusk.nms.core.logger.ConsoleLogInjector;
import it.jakegblp.lusk.nms.core.logger.events.ConsoleLogEvent;
import it.jakegblp.lusk.skript.api.syntax.event.SimpleEventValues;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class SimpleConsoleEvents {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerEvent(syntaxRegistry, SimpleEvent.class, ConsoleLogEvent.class,
                "Console Log", "Called when the server's console is used to log something.", "on console log:",
                "2.0.0", new String[]{"log", "logging", "console"}, "(console|server) (log|print)");
        SimpleEventValues.registerEventValue(ConsoleLogEvent.class, String.class, ConsoleLogEvent::getMessage);
        ConsoleLogInjector.inject(l -> {
            Server server = Bukkit.getServer();
            ConsoleLogEvent event = new ConsoleLogEvent(!server.isPrimaryThread(), l.messageStrippedAnsi());
            server.getPluginManager().callEvent(event);
            if (event.isCancelled()) l.cancel();
        });
    }

}
