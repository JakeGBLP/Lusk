package it.jakegblp.lusk.skript.api.syntax.effect;

import it.jakegblp.lusk.skript.utils.AddonUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.function.Supplier;

public abstract class BooleanMetadataEffect extends SimpleMetadataEffect<Boolean> {

    public static <E extends BooleanMetadataEffect> void register(
            SyntaxRegistry syntaxRegistry,
            Class<E> effectClass,
            Supplier<E> supplier,
            String metadataProperty
    ) {
        AddonUtils.registerEffect(syntaxRegistry, effectClass, supplier,
                "(set [fake]|fake) " + metadataProperty + " of %protocolentityreferences% to %boolean% for %players%"
        );
    }

    @Getter
    @Setter
    protected boolean inverted = false;

    @Override
    protected final void execute(Event event) {
        Boolean value = valueExpression.getSingle(event);
        if (value == null) {
            error("Failed to send simple metadata: " + getNode().getKey());
            return;
        }
        AddonUtils.sendEasyMetadata(event, playerExpression, protocolEntityReferenceExpression, getMetadataKey().asItem(isInverted() != value));
    }
}
