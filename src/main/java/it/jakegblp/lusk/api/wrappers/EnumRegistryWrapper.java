package it.jakegblp.lusk.api.wrappers;

import ch.njol.skript.classes.ClassInfo;
import it.jakegblp.lusk.utils.RegistryUtils;
import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;

public class EnumRegistryWrapper<R extends Keyed, E extends Enum<E>> {
    private final String prefix, suffix;
    private final Class<?> clazz;

    public EnumRegistryWrapper(
            @NotNull Class<?> clazz,
            String prefix,
            String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.clazz = clazz;
    }

    @SuppressWarnings("unchecked")
    public ClassInfo<?> getClassInfo(@NotNull String codename) {
        if (clazz.isEnum())
            return new EnumWrapper<>((Class<E>) clazz, prefix, suffix).getClassInfo(codename);
        else {
            return RegistryClassInfo.create(
                    (Registry<R>) RegistryUtils.getRegistries().get(clazz),
                    (Class<R>) clazz,
                    codename,
                    prefix,
                    suffix);
        }
    }
}
