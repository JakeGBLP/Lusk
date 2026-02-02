package it.jakegblp.lusk.skript.api.entry;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SimpleNode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.patterns.PatternCompiler;
import ch.njol.skript.patterns.SkriptPattern;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

@Getter
public class PatternEntryData<T> extends ExpressionEntryData<T> {

    public final SkriptPattern pattern;

    public PatternEntryData(String key, String pattern, @Nullable Expression<? extends T> defaultValue, boolean optional, Class<? extends T> returnType) {
        super(key, defaultValue, optional, returnType);
        this.pattern = PatternCompiler.compile(pattern);
    }

    @Override
    public boolean canCreateWith(Node node) {
        if (!(node instanceof SimpleNode))
            return false;
        String key = node.getKey();
        if (key == null)
            return false;
        key = ScriptLoader.replaceOptions(key.substring(0, key.indexOf(':')));
        return pattern.match(key) != null;
    }
}
