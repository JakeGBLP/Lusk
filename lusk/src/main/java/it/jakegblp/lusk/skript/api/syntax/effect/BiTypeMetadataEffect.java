package it.jakegblp.lusk.skript.api.syntax.effect;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class BiTypeMetadataEffect<F, S> extends SimpleMetadataEffect<F> {

    protected boolean first;
    protected String firstTypePropertyName, secondTypePropertyName;

    public BiTypeMetadataEffect(String firstTypePropertyName, String secondTypePropertyName) {
        this.firstTypePropertyName = firstTypePropertyName;
        this.secondTypePropertyName = secondTypePropertyName;
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        first = matchedPattern == 0;
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    public abstract @NotNull F asFirstType(S secondType);

    @Override
    @SuppressWarnings("unchecked")
    protected void execute(Event event) {
        var value = valueExpression.getSingle(event);
        if (value == null) {
            error("Failed to send simple metadata: " + getNode().getKey());
            return;
        }
        if (!first) value = asFirstType((S) value);
        AddonUtils.sendEasyMetadata(event, playerExpression, protocolEntityReferenceExpression, getMetadataKey().asItem(value));
    }

    @Override
    public String getMetadataPropertyName() {
        return first ? firstTypePropertyName : secondTypePropertyName;
    }
}
