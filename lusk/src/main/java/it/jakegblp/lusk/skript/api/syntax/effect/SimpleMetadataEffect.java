package it.jakegblp.lusk.skript.api.syntax.effect;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.world.entity.ProtocolEntityReference;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.skript.api.syntax.NMSSyntax;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.function.Supplier;

/**
 * Handles the repeating parts of a simple metadata effect.<br>
 * For this to work the pattern(s) must have the {@link ProtocolEntityReference Protocol Entity References} as first expression,
 *  the value as the second and {@link Player players} as the third.
 */
public abstract class SimpleMetadataEffect<T> extends Effect implements NMSSyntax {

    public static <E extends SimpleMetadataEffect<?>> void registerSimple(
            SyntaxRegistry syntaxRegistry,
            Class<E> effectClass,
            Supplier<E> supplier,
            String metadataProperty,
            String requiredType
    ) {
        AddonUtils.registerEffect(syntaxRegistry, effectClass, supplier,
                "(set [fake]|fake) " + metadataProperty + " of %protocolentityreferences% to %" + requiredType + "% for %players%"
        );
    }

    protected Expression<ProtocolEntityReference> protocolEntityReferenceExpression;
    protected Expression<T> valueExpression;
    protected Expression<Player> playerExpression;

    public abstract MetadataKey<? extends Entity, T> getMetadataKey();

    @Override
    protected void execute(Event event) {
        T value = valueExpression.getSingle(event);
        if (value == null) {
            error("Failed to send simple metadata: " + getNode().getKey());
            return;
        }
        AddonUtils.sendEasyMetadata(event, playerExpression, protocolEntityReferenceExpression, getMetadataKey().asItem(value));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        protocolEntityReferenceExpression = (Expression<ProtocolEntityReference>) expressions[0];
        valueExpression = (Expression<T>) expressions[1];
        playerExpression = (Expression<Player>) expressions[2];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return new SyntaxStringBuilder(event, debug).append("set fake ", getMetadataPropertyName(), " of ", protocolEntityReferenceExpression, " to ", valueExpression, " for ", playerExpression).toString();
    }

    public abstract String getMetadataPropertyName();
}
