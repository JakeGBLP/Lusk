package it.jakegblp.lusk.skript.api.classinfo;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.lang.ParseContext;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.jakegblp.lusk.skript.api.parser.SimpleParser;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import org.skriptlang.skript.lang.comparator.Comparators;
import org.skriptlang.skript.lang.comparator.Relation;

import java.lang.reflect.Array;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.skriptlang.skript.lang.comparator.Comparators.exactComparatorExists;

/**
 * @author Peter Güttinger, ShaneBeee, JakeGBLP
 */
@NullMarked
public record EnumLikeClassInfoWrapper<T>(Class<T> listedValueClass, BiMap<String, T> parseMap, @Nullable String prefix,
                                          @Nullable String suffix) {

    @SuppressWarnings("NullableProblems")
    public EnumLikeClassInfoWrapper {
        BiMap<String, T> newMap = HashBiMap.create(parseMap.size());
        for (var entry : parseMap.entrySet()) {
            String key = entry.getKey();
            T value = entry.getValue();
            StringBuilder newKey = new StringBuilder();
            if (prefix != null)
                newKey.append(prefix).append('_');
            newKey.append(key.toLowerCase(Locale.ROOT));
            if (suffix != null)
                newKey.append('_').append(suffix);

            newMap.put(newKey.toString().replace(" ", "_"), value);
        }
        parseMap = newMap;
        registerComparator(listedValueClass);
    }

    @SuppressWarnings("NullableProblems")
    public EnumLikeClassInfoWrapper(Class<T> listedValueClass, Map<String, T> parseMap, @Nullable String prefix, @Nullable String suffix) {
        this(listedValueClass, HashBiMap.create(parseMap), prefix, suffix);
    }

    public EnumLikeClassInfoWrapper(Class<T> listedValueClass, BiMap<String, T> parseMap) {
        this(listedValueClass, parseMap, null, null);
    }

    @SuppressWarnings("NullableProblems")
    public EnumLikeClassInfoWrapper(Class<T> listedValueClass, Map<String, T> parseMap) {
        this(listedValueClass, HashBiMap.create(parseMap), null, null);
    }

    @Nullable
    public T parse(final String s) {
        return parseMap.get(s.toLowerCase(Locale.ROOT).replace(" ", "_"));
    }

    public void replace(String toReplace, String replacement) {
        if (parseMap.containsKey(toReplace)) {
            T t = parseMap.get(toReplace);
            replacement = replacement.replace(" ", "_");
            parseMap.put(replacement, t);
            parseMap.remove(toReplace);
        }
    }

    @SuppressWarnings("unused")
    public String toString(final T t, final int flags) {
        return parseMap.inverse().getOrDefault(t, t.toString()).toLowerCase(Locale.ROOT).replace("_", " ");
    }

    private String getAllNames() {
        return parseMap.keySet().stream().sorted().collect(Collectors.joining(", "));
    }

    @SuppressWarnings("unchecked")
    public ClassInfo<T> getClassInfo(String codeName) {
        Set<T> values = parseMap.values();
        return new ClassInfo<>(listedValueClass, codeName).usage(getAllNames()).supplier(values.toArray(_ -> (T[]) Array.newInstance(listedValueClass, values.size()))).parser(new SimpleListParser<>(this));
    }

    /**
     * If using `.usage()` use this method to prevent double call/assertion error</p>
     *
     * @param codeName Name for class info
     * @return ClassInfo with default parser and usage
     */
    @SuppressWarnings("unchecked")
    public ClassInfo<T> getClassInfoWithoutUsage(String codeName) {
        Set<T> values = parseMap.values();
        return new ClassInfo<>(listedValueClass, codeName).supplier(values.toArray(_ -> (T[]) Array.newInstance(listedValueClass, values.size()))).parser(new SimpleListParser<>(this));
    }

    private void registerComparator(Class<T> typeClass) {
        if (exactComparatorExists(typeClass, typeClass)) return;
        Comparators.registerComparator(typeClass, typeClass, (o1, o2) -> Relation.get(o1.equals(o2)));
    }

    static class SimpleListParser<T> extends SimpleParser<T> {

        private final EnumLikeClassInfoWrapper<T> wrapper;

        public SimpleListParser(EnumLikeClassInfoWrapper<T> wrapper) {
            super(true);
            this.wrapper = wrapper;
        }

        @Nullable
        @Override
        public T parse(String s, ParseContext context) {
            return wrapper.parse(s);
        }

        @Override
        public String toString(T t) {
            return wrapper.toString(t, 0);
        }

    }

}