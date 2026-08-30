package it.jakegblp.lusk.skript.api.module;

import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.function.IntConsumer;

public abstract class LuskModule implements AddonModule {

    private final IntConsumer adder;

    protected LuskModule(IntConsumer adder) {
        this.adder = adder;
    }

    public void increaseClassInfoCount(int increase) {
        adder.accept(increase);
    }

    protected abstract void registration(SyntaxRegistry registry);

    @Override
    public final void load(SkriptAddon addon) {
        registration(addon.syntaxRegistry());
    }
}