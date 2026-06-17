package it.jakegblp.lusk.skript.api.syntax.effect;

import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.function.Supplier;

public abstract class SimpleBooleanVerbMetadataEffect extends SimpleExpandedBooleanMetadataEffect {

    public static <E extends SimpleBooleanVerbMetadataEffect> void registerVerb(
            SyntaxRegistry syntaxRegistry,
            Class<E> effectClass,
            Supplier<E> supplier,
            String verb
    ) {
        register(syntaxRegistry, effectClass, supplier, verb, verb + "ing");
    }

    @Override
    public final String getStatePropertyName() {
        return getMakePropertyName() + "ing";
    }
}
