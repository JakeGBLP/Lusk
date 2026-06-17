package it.jakegblp.lusk.skript.api.entry;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SimpleNode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.localization.Message;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.skript.log.ParseLogHandler;
import ch.njol.skript.patterns.MatchResult;
import ch.njol.skript.patterns.PatternCompiler;
import ch.njol.skript.patterns.SkriptPattern;
import it.jakegblp.lusk.skript.api.syntax.expression.ExpressionResult;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryData;
import org.skriptlang.skript.lang.entry.EntryValidator;

@Getter
public class PatternEntryData<T> extends EntryData<ExpressionResult<? extends T>> {

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> Expression<T> getSimpleOptional(EntryContainer entryContainer, String key, boolean useDefaultValue) {
        ExpressionResult<T> result = (ExpressionResult<T>) entryContainer.getOptional(key, useDefaultValue);
        if (result == null) return null;
        return result.getValue();
    }

    @Nullable
    public static <T> Expression<T> getSimpleOptional(EntryContainer entryContainer, String key) {
        return getSimpleOptional(entryContainer, key, false);
    }

    protected static final Message M_IS = new Message("is");

    protected final Class<? extends T> returnType;
    protected final SkriptPattern pattern;
    protected MatchResult matchResult;

    public PatternEntryData(String key, String pattern, boolean optional, Class<? extends T> returnType) {
        super(key, null, optional);
        this.returnType = returnType;
        this.pattern = PatternCompiler.compile(pattern);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    protected ExpressionResult<T> getValue(String value) {
        Expression<T> expression;
        try (ParseLogHandler log = new ParseLogHandler().start()) {
            expression = (Expression<T>) new SkriptParser(value, SkriptParser.ALL_FLAGS, ParseContext.DEFAULT)
                    .parseExpression(returnType);
            if (expression == null) // print an error if it couldn't parse
                log.printError(
                        "'" + value + "' " + M_IS + " " + SkriptParser.notOfType(returnType),
                        ErrorQuality.NOT_AN_EXPRESSION
                );
        }
        return new ExpressionResult<>(expression, matchResult);
    }

    public Expression<? extends T> getSimpleValue(String value) {
        return getValue(value).getValue();
    }

    @Override
    public @Nullable ExpressionResult<? extends T> getValue(Node node) {
        assert node instanceof SimpleNode;
        String key = node.getKey();
        if (key == null)
            throw new IllegalArgumentException("EntryData#getValue() called with invalid node.");
        return getValue(key.substring(key.indexOf(getSeparator()) + getSeparator().length()));
    }

    public String getSeparator() {
        return EntryValidator.EntryValidatorBuilder.DEFAULT_ENTRY_SEPARATOR;
    }

    @Override
    public boolean canCreateWith(Node node) {
        if (!(node instanceof SimpleNode))
            return false;
        String key = node.getKey();
        if (key == null)
            return false;
        key = ScriptLoader.replaceOptions(key);
        key = key.substring(0, key.indexOf(':'));
        matchResult = pattern.match(key);
        return matchResult != null;
    }
}
