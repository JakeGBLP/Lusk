package it.jakegblp.lusk.skript.api;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SimpleNode;
import ch.njol.skript.lang.Expression;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

@Getter
public class DoubleKeyExpressionEntryData<T> extends ExpressionEntryData<T> {

    private final String otherKey;

    private boolean other;

    public DoubleKeyExpressionEntryData(String key1, String key2, @Nullable Expression<T> defaultValue, boolean optional) {
        super(key1, defaultValue, optional);
        this.otherKey = key2;
    }

    public DoubleKeyExpressionEntryData(String key1, String key2, @Nullable Expression<T> defaultValue, boolean optional, Class<T> returnType) {
        super(key1, defaultValue, optional, returnType);
        this.otherKey = key2;
    }

    public DoubleKeyExpressionEntryData(String key1, String key2, @Nullable Expression<T> defaultValue, boolean optional, Class<T> returnType, int flags) {
        super(key1, defaultValue, optional, returnType, flags);
        this.otherKey = key2;
    }

    @SafeVarargs
    public DoubleKeyExpressionEntryData(String key1, String key2, @Nullable Expression<T> defaultValue, boolean optional, Class<T>... returnTypes) {
        super(key1, defaultValue, optional, returnTypes);
        this.otherKey = key2;
    }

    @SafeVarargs
    public DoubleKeyExpressionEntryData(String key1, String key2, @Nullable Expression<T> defaultValue, boolean optional, int flags, Class<T>... returnTypes) {
        super(key1, defaultValue, optional, flags, returnTypes);
        this.otherKey = key2;
    }

    public String getMainKey() {
        return super.getKey();
    }

    @Override
    public String getKey() {
        return other ? getOtherKey() : getMainKey();
    }

    public String getPrefix(Node node) {
        String key = node.getKey();
        assert key != null;
        return key.substring(0, key.indexOf(getSeparator()));
    }


    @Override
    public boolean canCreateWith(Node node) {
        if (!(node instanceof SimpleNode))
            return false;
        String key = node.getKey();
        if (key == null)
            return false;
        key = ScriptLoader.replaceOptions(key);
        other = key.startsWith(this.otherKey);
        String mainPrefix = getMainKey() + getSeparator();
        String otherPrefix = getOtherKey() + getSeparator();
        String prefix = other ? otherPrefix : mainPrefix;
        return key.regionMatches(true, 0, prefix, 0, prefix.length());
    }
}
