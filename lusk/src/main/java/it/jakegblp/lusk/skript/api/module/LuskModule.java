package it.jakegblp.lusk.skript.api.module;

import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.registration.SyntaxRegistry;

public abstract class LuskModule implements AddonModule {

    protected abstract void registration(SyntaxRegistry registry);

    @Override
    public final void load(SkriptAddon addon) {
        registration(addon.syntaxRegistry());

    }
}