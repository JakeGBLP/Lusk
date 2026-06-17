package it.jakegblp.lusk.skript.api.syntax.expression;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.patterns.MatchResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@AllArgsConstructor
public class ExpressionResult<T> {
    protected final @Nullable Expression<T> value;
    protected final @NotNull MatchResult matchResult;

    public T getSingle(Event event) {
        if (value == null) return null;
        return value.getSingle(event);
    }

    public SkriptParser.ParseResult getParseResult() {
        return matchResult.toParseResult();
    }

    public boolean hasTag(String tag) {
        return getParseResult().hasTag(tag);
    }
}
