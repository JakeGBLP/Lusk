package it.jakegblp.lusk.skript.api.syntax.effect;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.world.entity.ProtocolEntityReference;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.function.Supplier;

public abstract class ExpandedBooleanMetadataEffect extends BooleanMetadataEffect {

    public static <E extends ExpandedBooleanMetadataEffect> void register(
            SyntaxRegistry syntaxRegistry,
            Class<E> effectClass,
            Supplier<E> supplier,
            String mainPattern,
            String truePattern,
            String falsePattern
    ) {
        syntaxRegistry.register(SyntaxRegistry.EFFECT, SyntaxInfo.builder(effectClass)
                .addPatterns(mainPattern, truePattern, falsePattern)
                .supplier(supplier)
                .build()
        );
    }

    protected Kleenean pattern;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        pattern = Kleenean.get(matchedPattern - 1);
        if (pattern.isFalse())
            return super.init(expressions, matchedPattern, isDelayed, parseResult);
        else {
            protocolEntityReferenceExpression = (Expression<ProtocolEntityReference>) expressions[0];
            playerExpression = (Expression<Player>) expressions[1];
            return true;
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return switch (pattern) {
            case FALSE -> toStringMain(event, debug);
            case UNKNOWN -> toStringTrue(event, debug);
            case TRUE -> toStringFalse(event, debug);
        };
    }

    public String toStringMain(Event event, boolean debug) {
        return super.toString(event, debug);
    }

    public abstract String toStringTrue(Event event, boolean debug);
    public abstract String toStringFalse(Event event, boolean debug);
}
