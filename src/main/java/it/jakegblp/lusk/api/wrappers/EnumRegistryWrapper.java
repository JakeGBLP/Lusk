package it.jakegblp.lusk.api.wrappers;

import ch.njol.skript.classes.ClassInfo;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.Constants.REGISTRIES;

public class EnumRegistryWrapper {
    private final String prefix, suffix;
    private final Class<?> clazz;

    public EnumRegistryWrapper(@NotNull Class<?> clazz, String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.clazz = clazz;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public ClassInfo<?> getClassInfo(@NotNull String codename) {
        if (clazz.isEnum()) return new EnumWrapper(clazz, prefix, suffix).getClassInfo(codename);
        else return new RegistryClassInfo(REGISTRIES.get(clazz), clazz, true, codename, prefix, suffix);
    }
}
