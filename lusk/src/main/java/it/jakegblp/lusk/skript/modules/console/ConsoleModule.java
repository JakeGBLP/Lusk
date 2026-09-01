package it.jakegblp.lusk.skript.modules.console;

import it.jakegblp.lusk.skript.api.module.LuskModule;
import it.jakegblp.lusk.skript.modules.console.events.SimpleConsoleEvents;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.function.IntConsumer;

public class ConsoleModule extends LuskModule {

    public ConsoleModule(IntConsumer adder) {
        super(adder);
    }

    @Override
    protected void registration(SyntaxRegistry registry) {
        SimpleConsoleEvents.register(registry);
    }

    @Override
    public String name() {
        return "console";
    }
}
