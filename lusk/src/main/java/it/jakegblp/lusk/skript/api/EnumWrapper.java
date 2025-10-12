package it.jakegblp.lusk.skript.api;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.util.StringUtils;
import it.jakegblp.lusk.common.CanonicallyOrdered;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.comparator.Comparators;
import org.skriptlang.skript.lang.comparator.Relation;

import java.util.*;

import static org.skriptlang.skript.lang.comparator.Comparators.exactComparatorExists;

/**
 * @author Peter Güttinger, ShaneBeee, JakeGBLP
 */
public class EnumWrapper<E extends Enum<E>> {

    private final Class<E> enumClass;
    private final String[] names;
    private final HashMap<String, E> parseMap = new HashMap<>();

    public EnumWrapper(@NotNull Class<E> c, E[] constants, @Nullable String prefix, @Nullable String suffix) {
        assert c.isEnum();
        this.enumClass = c;
        this.names = new String[constants.length];
        for (int i = 0; i < constants.length; i++) {
            E enumConstant = constants[i];
            String name = enumConstant.name().toLowerCase(Locale.ROOT);
            if (prefix != null && !name.startsWith(prefix))
                name = prefix + "_" + name;
            if (suffix != null && !name.endsWith(suffix))
                name = name + "_" + suffix;
            parseMap.put(name, enumConstant);
            names[i] = name;
        }
        //System.out.println("class: " + c.getName()+" and names: "+ Arrays.toString(names) +" and map: \n" + parseMap.entrySet().stream().map(entry -> "  {"+entry.getKey()+", "+entry.getValue()+"}").collect(Collectors.joining("\n")));
        registerComparator(c);
    }

    public EnumWrapper(@NotNull Class<E> c, @Nullable String prefix, @Nullable String suffix) {
        this(c, c.getEnumConstants(), prefix, suffix);
    }

    public EnumWrapper(@NotNull Class<E> c) {
        this(c, null, null);
    }

    @Nullable
    public E parse(final String s) {
        return parseMap.get(s.toLowerCase(Locale.ROOT).replace(" ", "_"));
    }

    /**
     * Replace a specific key with another
     * <br>Useful to prevent conflicts
     *
     * @param toReplace   MetadataKey to replace
     * @param replacement New replacement key
     */
    public void replace(String toReplace, String replacement) {
        if (parseMap.containsKey(toReplace)) {
            E e = parseMap.get(toReplace);
            replacement = replacement.replace(" ", "_");
            parseMap.put(replacement, e);
            parseMap.remove(toReplace);
            names[e instanceof CanonicallyOrdered canonicallyOrdered ? canonicallyOrdered.getCanonicalOrder() : e.ordinal()] = replacement;
        }
    }

    @SuppressWarnings("unused")
    public String toString(final E e, final int flags) {
        return names[e instanceof CanonicallyOrdered canonicallyOrdered ? canonicallyOrdered.getCanonicalOrder() : e.ordinal()];
    }

    private String getAllNames() {
        List<String> names = new ArrayList<>();
        Collections.addAll(names, this.names);
        Collections.sort(names);
        return StringUtils.join(names, ", ");
    }

    /**
     * Create ClassInfo with default parser and usage
     *
     * @param codeName Name for class info
     * @return ClassInfo with default parser and usage
     */
    public ClassInfo<E> getClassInfo(String codeName) {
        return new ClassInfo<>(this.enumClass, codeName).usage(getAllNames()).parser(new EnumParser<>(this));
    }

    /**
     * Create ClassInfo with default parser and usage
     * <p>If using `.usage()` use this method to prevent double call/assertion error</p>
     *
     * @param codeName Name for class info
     * @return ClassInfo with default parser and usage
     */
    public ClassInfo<E> getClassInfoWithoutUsage(String codeName) {
        return new ClassInfo<>(this.enumClass, codeName).parser(new EnumParser<>(this));
    }

    private void registerComparator(Class<E> c) {
        if (exactComparatorExists(c, c)) return;
        Comparators.registerComparator(c, c, (o1, o2) -> Relation.get(o1.equals(o2)));
    }

    static class EnumParser<T extends Enum<T>> extends Parser<T> {

        private final EnumWrapper<T> enumWrapper;

        public EnumParser(EnumWrapper<T> enumWrapper) {
            this.enumWrapper = enumWrapper;
        }

        @Nullable
        @Override
        public T parse(@NotNull String s, @NotNull ParseContext context) {
            return enumWrapper.parse(s);
        }

        @Override
        public @NotNull String toString(T o, int flags) {
            return enumWrapper.toString(o, flags);
        }

        @Override
        public @NotNull String toVariableNameString(T o) {
            return toString(o, 0);
        }

    }

}