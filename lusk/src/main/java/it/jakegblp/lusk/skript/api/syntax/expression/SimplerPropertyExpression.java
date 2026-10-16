package it.jakegblp.lusk.skript.api.syntax.expression;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import it.jakegblp.lusk.skript.api.syntax.Changeable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.function.Supplier;

public abstract class SimplerPropertyExpression<F, T> extends SimplePropertyExpression<F, T> implements Changeable<F, T> {

    public static <E extends SimplerPropertyExpression<?, T>, T> void register(SyntaxRegistry registry, Class<E> expressionClass, Supplier<E> supplier, Class<T> returnType, String property, String fromType) {
        Registrations.propertyExpression(expressionClass, supplier, returnType)
                .propertyOf(property, fromType)
                .register(registry);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        change(mode, getExpr().getAll(event), delta == null ? null : (T) delta[0]);
    }
}
