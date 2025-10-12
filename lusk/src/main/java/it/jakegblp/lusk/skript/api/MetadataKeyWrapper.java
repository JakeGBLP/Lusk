package it.jakegblp.lusk.skript.api;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import com.google.common.collect.BiMap;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.comparator.Comparators;
import org.skriptlang.skript.lang.comparator.Relation;

import java.util.Locale;
import java.util.stream.Collectors;

import static org.skriptlang.skript.lang.comparator.Comparators.exactComparatorExists;

/**
 * @author Peter Güttinger, ShaneBeee, JakeGBLP
 */
public record MetadataKeyWrapper(BiMap<String, MetadataKey<?, ?>> parseMap) {

    public MetadataKeyWrapper(@NotNull BiMap<@NotNull String, @NotNull MetadataKey<?, ?>> parseMap) {
        this.parseMap = parseMap;
        registerComparator();
    }

    @Nullable
    public MetadataKey<?, ?> parse(final String s) {
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
            MetadataKey<?, ?> key = parseMap.get(toReplace);
            replacement = replacement.replace(" ", "_");
            parseMap.put(replacement, key);
            parseMap.remove(toReplace);
        }
    }

    @SuppressWarnings("unused")
    public String toString(final MetadataKey<?, ?> key, final int flags) {
        return parseMap.inverse().get(key);
    }

    private String getAllNames() {
        return parseMap.keySet().stream().sorted().collect(Collectors.joining(", "));
    }

    /**
     * Create ClassInfo with default parser and usage
     *
     * @param codeName Name for class info
     * @return ClassInfo with default parser and usage
     */
    public ClassInfo<MetadataKey> getClassInfo(String codeName) {
        return new ClassInfo<>(MetadataKey.class, codeName).usage(getAllNames()).supplier((MetadataKey[]) parseMap.values().toArray()).parser(new MetadataKeyParser(this));
    }

    /**
     * Create ClassInfo with default parser and usage
     * <p>If using `.usage()` use this method to prevent double call/assertion error</p>
     *
     * @param codeName Name for class info
     * @return ClassInfo with default parser and usage
     */
    public ClassInfo<MetadataKey> getClassInfoWithoutUsage(String codeName) {
        return new ClassInfo<>(MetadataKey.class, codeName).supplier((MetadataKey[]) parseMap.values().toArray()).parser(new MetadataKeyParser(this));
    }

    private void registerComparator() {
        if (exactComparatorExists(MetadataKey.class, MetadataKey.class)) return;
        Comparators.registerComparator(MetadataKey.class, MetadataKey.class, (o1, o2) -> Relation.get(o1.equals(o2)));
    }

    static class MetadataKeyParser extends Parser<MetadataKey<?, ?>> {

        private final MetadataKeyWrapper wrapper;

        public MetadataKeyParser(MetadataKeyWrapper wrapper) {
            this.wrapper = wrapper;
        }

        @Nullable
        @Override
        public MetadataKey<?, ?> parse(@NotNull String s, @NotNull ParseContext context) {
            return wrapper.parse(s);
        }

        @Override
        public @NotNull String toString(MetadataKey<?, ?> o, int flags) {
            return wrapper.toString(o, flags);
        }

        @Override
        public @NotNull String toVariableNameString(MetadataKey<?, ?> o) {
            return toString(o, 0);
        }

    }

}