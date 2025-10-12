package it.jakegblp.lusk.skript.api;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.lang.ParseContext;
import com.google.common.collect.BiMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.comparator.Comparators;
import org.skriptlang.skript.lang.comparator.Relation;

import java.lang.reflect.Array;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import static org.skriptlang.skript.lang.comparator.Comparators.exactComparatorExists;

/**
 * @author Peter Güttinger, ShaneBeee, JakeGBLP
 */
public record ListWrapper<T>(@NotNull Class<T> listedValueClass, @NotNull BiMap<@NotNull String, T> parseMap) {

    public ListWrapper {
        registerComparator(listedValueClass);
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
        return parseMap.inverse().get(t).toLowerCase(Locale.ROOT).replace("_", " ");
    }

    private String getAllNames() {
        return parseMap.keySet().stream().sorted().collect(Collectors.joining(", "));
    }

    @SuppressWarnings("unchecked")
    public ClassInfo<T> getClassInfo(String codeName) {
        Set<T> values = parseMap.values();
        return new ClassInfo<>(listedValueClass, codeName).usage(getAllNames()).supplier(values.toArray(value -> (T[]) Array.newInstance(listedValueClass, values.size()))).parser(new SimpleListParser<>(this));
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
        return new ClassInfo<>(listedValueClass, codeName).supplier(values.toArray(value -> (T[]) Array.newInstance(listedValueClass, values.size()))).parser(new SimpleListParser<>(this));
    }

    private void registerComparator(Class<T> typeClass) {
        if (exactComparatorExists(typeClass, typeClass)) return;
        Comparators.registerComparator(typeClass, typeClass, (o1, o2) -> Relation.get(o1.equals(o2)));
    }

    static class SimpleListParser<T> extends SimpleParser<T> {

        private final ListWrapper<T> wrapper;

        public SimpleListParser(ListWrapper<T> wrapper) {
            super(true);
            this.wrapper = wrapper;
        }

        @Nullable
        @Override
        public T parse(@NotNull String s, @NotNull ParseContext context) {
            return wrapper.parse(s);
        }

        @Override
        public @NotNull String toString(T t) {
            return wrapper.toString(t, 0);
        }

    }

}