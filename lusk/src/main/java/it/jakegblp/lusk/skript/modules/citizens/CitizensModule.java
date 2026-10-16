package it.jakegblp.lusk.skript.modules.citizens;

import it.jakegblp.lusk.skript.api.module.LuskModule;
import it.jakegblp.lusk.skript.modules.citizens.conditions.CondNPC;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.function.IntConsumer;

public class CitizensModule extends LuskModule {

    public CitizensModule(IntConsumer adder) {
        super(adder);
    }

    @Override
    protected void registration(SyntaxRegistry registry) {
        CondNPC.register(registry);
    }

    @Override
    public String name() {
        return "citizens";
    }
}
