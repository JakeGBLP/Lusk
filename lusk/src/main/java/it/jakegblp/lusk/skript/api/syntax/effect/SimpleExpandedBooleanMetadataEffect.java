package it.jakegblp.lusk.skript.api.syntax.effect;

import org.bukkit.event.Event;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.function.Supplier;

public abstract class SimpleExpandedBooleanMetadataEffect extends ExpandedBooleanMetadataEffect {

    public static <E extends SimpleExpandedBooleanMetadataEffect> void register(
            SyntaxRegistry syntaxRegistry,
            Class<E> effectClass,
            Supplier<E> supplier,
            String makePropertyName,
            String statePropertyName
    ) {
        register(syntaxRegistry, effectClass, supplier,
                "(set [fake]|fake) " + statePropertyName + " [state] of %protocolentityreferences% to %boolean% for %players%",
                "make %protocolentityreferences% " + makePropertyName + " for %players%",
                "make %protocolentityreferences% not " + makePropertyName + " for %players%");
    }

    @Override
    public final String getMetadataPropertyName() {
        return getStatePropertyName();
    }

    public abstract String getMakePropertyName();
    public abstract String getStatePropertyName();

    @Override
    public String toStringTrue(Event event, boolean debug) {
        return "make " + protocolEntityReferenceExpression.toString(event, debug) + " " + getMakePropertyName() + " for " + playerExpression.toString(event, debug);
    }

    @Override
    public String toStringFalse(Event event, boolean debug) {
        return "make " + protocolEntityReferenceExpression.toString(event, debug) + " not " + getMakePropertyName() + " for " + playerExpression.toString(event, debug);
    }
}
