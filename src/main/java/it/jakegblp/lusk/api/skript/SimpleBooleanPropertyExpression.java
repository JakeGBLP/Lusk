package it.jakegblp.lusk.api.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public abstract class SimpleBooleanPropertyExpression<F> extends SimplerPropertyExpression<F, Boolean> {

    /**
     * Registers a property expression that gets or is set to a boolean; includes the following patterns:<br>
     * 1. <code>[the] %prefix% %property% [state|property] of %fromType%</code><br>
     * 2. <code>%fromType%'[s] %prefix% %property% [state|property]</code><br>
     * 3. <code>whether or not [the] %prefix% %fromType% %property%</code><br>
     * 4. <code>whether [the] %prefix% %fromType% %property% [or not]</code>
     *
     * @param expressionClass the class of the expression to register
     * @param type the class of the return type
     * @param prefix a (usually optional) string that comes before the property to indicate what kind of object it can be used for
     * @param property a string indicates what this expression will return based on the given object
     * @param fromType a string containing lowercase classinfos that indicate what this expression can be used against
     * @param <T> the returned type of the expression
     */
    public static <T extends Boolean> void register(
            Class<? extends Expression<T>> expressionClass,
            Class<T> type,
            @Nullable String prefix,
            String property,
            String fromType) {
        prefix = prefix != null ? prefix + " " : "";
        String[] patterns = {
                "[the] " + prefix + property + " [state|property] of %" + fromType + "%",
                "%" + fromType + "%'[s] " + prefix + property + " [state|property]",
                "whether or not [the] " + prefix + "%" + fromType + "% " + property,
                "whether [the] " + prefix + "%" + fromType + "% " + property + " [or not]"
        };
        Skript.registerExpression(expressionClass, type, ExpressionType.PROPERTY,patterns);
    }

    private boolean isNegated;

    /**
     * @return if this boolean property expression is negated.
     */
    public final boolean isNegated() {
        return isNegated;
    }

    /**
     * Sets the negated value of this boolean property expression on init, false by default.
     * @param matchedPattern the matched pattern on init.
     * @param parseResult the parse result on init.
     * @return the negation value, false if not overridden.
     */
    public boolean setNegated(int matchedPattern, SkriptParser.ParseResult parseResult) {
        return false;
    }

    /**
     * Sets the boolean value of this property.<br>
     * Note: Negation is handled internally, it must not be used here.
     */
    @Override
    public void set(F from, Boolean to) {
        super.set(from, to);
    }

    /**
     * {@inheritDoc}
     * Resets the boolean value of this property.
     */
    @Override
    public void reset(F from) {
        super.reset(from);
    }

    /**
     * {@inheritDoc}<br><br>
     * Sets whether this boolean property should be negated, false by default.<br><br>
     * If this method is overridden, it should return
     * {@link #init(Expression[], int, Kleenean, SkriptParser.ParseResult) super.init(...)}
     * to handle negation and literals.
     */
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        isNegated = setNegated(matchedPattern,parseResult);
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET -> allowSet() ? new Class[] {Boolean.class} : null;
            case RESET -> allowReset() ? new Class[0] : null;
            case ADD, DELETE, REMOVE, REMOVE_ALL -> null;
        };
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.RESET) {
            for (F from : getExpr().getAll(event))
                reset(from);
        } else if (mode == Changer.ChangeMode.SET && delta != null && delta[0] instanceof Boolean bool) {
            bool ^= isNegated();
            for (F from : getExpr().getAll(event))
                set(from,bool);
        }
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }
}
