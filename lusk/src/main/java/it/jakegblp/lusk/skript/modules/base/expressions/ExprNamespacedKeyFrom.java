package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import static it.jakegblp.lusk.common.CommonUtils.createNamespacedKey;

public class ExprNamespacedKeyFrom extends SimpleExpression<NamespacedKey> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerExpression(syntaxRegistry, ExprNamespacedKeyFrom.class, ExprNamespacedKeyFrom::new, NamespacedKey.class,
                "[[a] new] namespaced[ ]key from %string%");
    }

    private Expression<String> stringExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        stringExpression = (Expression<String>) expressions[0];
        return true;
    }
    @Override
    protected NamespacedKey @Nullable [] get(Event event) {
        return new NamespacedKey[] {createNamespacedKey(stringExpression.getSingle(event))};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends NamespacedKey> getReturnType() {
        return NamespacedKey.class;
    }


    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new namespaced key from " + stringExpression.toString(event, debug);
    }
}
