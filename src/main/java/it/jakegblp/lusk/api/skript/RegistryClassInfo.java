package it.jakegblp.lusk.api.skript;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.util.StringUtils;
import ch.njol.yggdrasil.Fields;
import com.google.common.base.Preconditions;
import it.jakegblp.lusk.api.GenericRelation;
import it.jakegblp.lusk.utils.BorrowedUtils;
import it.jakegblp.lusk.utils.CompatibilityUtils;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@link ClassInfo} wrapper class for {@link Registry Bukkit Registries}
 *
 * @param <T> Type of item in the registry
 * @author ShaneBeee, JakeGBLP
 */
@SuppressWarnings({"unused"})
public class RegistryClassInfo<T extends Keyed> extends ClassInfo<T> {

    /**
     * Create a Registry ClassInfo
     *
     * @param registry      Registry to wrap
     * @param registryClass Class of registry
     * @param codename      Codename for ClassInfo
     * @return ClassInfo from Registry
     */
    public static <T extends Keyed> RegistryClassInfo<T> create(@NotNull Registry<T> registry, @NotNull Class<T> registryClass, @NotNull String codename) {
        return create(registry, registryClass, true, codename, null, null);
    }

    /**
     * Create a Registry ClassInfo with optional prefix and suffix
     *
     * @param registry      Registry to wrap
     * @param registryClass Class of registry
     * @param codename      Codename for ClassInfo
     * @param prefix        Optional prefix to prepend to items in registry
     * @param suffix        Optional suffix to append to items in registry
     * @return ClassInfo from Registry
     */
    public static <T extends Keyed> RegistryClassInfo<T> create(@NotNull Registry<T> registry, @NotNull Class<T> registryClass, @NotNull String codename, @Nullable String prefix, @Nullable String suffix) {
        return create(registry, registryClass, true, codename, prefix, suffix);
    }

    /**
     * Create a Registry ClassInfo
     *
     * @param registry      Registry to wrap
     * @param registryClass Class of registry
     * @param usage         Whether to create usage
     * @param codename      Codename for ClassInfo
     * @return ClassInfo from Registry
     */
    public static <T extends Keyed> RegistryClassInfo<T> create(@NotNull Registry<T> registry, @NotNull Class<T> registryClass, boolean usage, @NotNull String codename) {
        return create(registry, registryClass, usage, codename, null, null);
    }

    /**
     * Create a Registry ClassInfo with optional prefix and suffix
     *
     * @param registry      Registry to wrap
     * @param registryClass Class of registry
     * @param usage         Whether to create usage
     * @param codename      Codename for ClassInfo
     * @param prefix        Optional prefix to prepend to items in registry
     * @param suffix        Optional suffix to append to items in registry
     * @return ClassInfo from Registry
     */
    @SuppressWarnings("ConstantValue")
    public static <T extends Keyed> RegistryClassInfo<T> create(@NotNull Registry<T> registry, @NotNull Class<T> registryClass, boolean usage, @NotNull String codename, @Nullable String prefix, @Nullable String suffix) {
        // Safety precautions
        Preconditions.checkArgument(registry != null, "Registry cannot be null");
        Preconditions.checkArgument(registryClass != null, "RegistryClass cannot be null");
        Preconditions.checkArgument(!codename.isEmpty(), "Codename cannot be empty");
        return new RegistryClassInfo<>(registry, registryClass, usage, codename, prefix, suffix);
    }


    private final Registry<T> registry;
    @Nullable
    private final String prefix, suffix;

    public RegistryClassInfo(Registry<T> registry, Class<T> registryClass, boolean usage, String codename, @Nullable String prefix, @Nullable String suffix) {
        super(registryClass, codename);
        this.registry = registry;
        this.prefix = prefix == null ? null : prefix.replaceAll(" ", "_");
        this.suffix = suffix == null ? null : suffix.replaceAll(" ", "_");
        if (!CompatibilityUtils.exactComparatorExists(registryClass, registryClass)) {
            CompatibilityUtils.registerComparator(registryClass, registryClass, (o1, o2) -> GenericRelation.get(o1.equals(o2)));
        }
        if (usage) this.usage(getNames());
        this.supplier(registry::iterator);
        this.parser(new Parser<>() {
            @Override
            public @Nullable T parse(String string, ParseContext context) {
                return RegistryClassInfo.this.parse(string);
            }

            @Override
            public @NotNull String toString(T o, int flags) {
                return RegistryClassInfo.this.toString(o);
            }

            @Override
            public @NotNull String toVariableNameString(T o) {
                return toString(o, 0);
            }
        });
        this.serializer(new Serializer<>() {
            @Override
            public @NotNull Fields serialize(T object) {
                Fields fields = new Fields();
                fields.putObject("key", object.getKey().toString());
                return fields;
            }

            @Override
            public void deserialize(T o, @NotNull Fields f) {
            }

            @Override
            protected T deserialize(@NotNull Fields fields) throws StreamCorruptedException {
                String key = fields.getObject("key", String.class);
                if (key == null) {
                    throw new StreamCorruptedException("Key is null");
                }
                NamespacedKey namespacedKey = NamespacedKey.fromString(key);
                if (namespacedKey == null) {
                    throw new StreamCorruptedException("NamespacedKey is null for key: " + key);
                }
                T registryObject = RegistryClassInfo.this.registry.get(namespacedKey);
                if (registryObject == null) {
                    throw new StreamCorruptedException("RegistryObject is null for key: " + key);
                }
                return registryObject;
            }

            @Override
            public boolean mustSyncDeserialization() {
                return true;
            }

            @Override
            protected boolean canBeInstantiated() {
                return false;
            }
        });
    }

    /**
     * Get names of all items in the registry
     *
     * @return Names of all items
     */
    public String getNames() {
        List<String> keys = new ArrayList<>();
        this.registry.iterator().forEachRemaining(object -> keys.add(getName(object)));
        Collections.sort(keys);
        return StringUtils.join(keys, ", ");
    }

    private String getName(T object) {
        String key = object.getKey().getKey();
        if (this.prefix != null && !this.prefix.isEmpty()) key = prefix + "_" + key;
        if (this.suffix != null && !this.suffix.isEmpty()) key = key + "_" + suffix;
        return key;
    }

    /**
     * Convert to string for use in Skript
     *
     * @param object Item to put into string
     * @return String form of item
     */
    public @NotNull String toString(T object) {
        NamespacedKey namespacedKey;
        try {
            namespacedKey = object.getKey();
        } catch (IllegalArgumentException ignore) {
            return "invalid key for: " + object;
        }
        String key = namespacedKey.getKey();
        if (this.prefix != null && !this.prefix.isEmpty()) key = prefix + "_" + key;
        if (this.suffix != null && !this.suffix.isEmpty()) key = key + "_" + suffix;
        return namespacedKey.getNamespace() + ":" + key;
    }

    /**
     * Parse the string as a registry item
     *
     * @param string String to parse
     * @return Item from registry
     */
    @Nullable
    private T parse(String string) {
        string = string.replace(" ", "_");
        if (this.prefix != null) {
            if (!string.contains(this.prefix)) return null;
            string = string.replace(prefix + "_", "").replace(prefix, "");
        }
        if (this.suffix != null) {
            if (!string.contains(this.suffix)) return null;
            string = string.replace("_" + suffix, "").replace(suffix, "");
        }
        string = string.trim();
        NamespacedKey key = BorrowedUtils.getNamespacedKey(string, false);
        if (key == null) return null;
        return this.registry.get(key);
    }

}