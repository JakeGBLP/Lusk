package it.jakegblp.lusk.utils;

import ch.njol.skript.registrations.Classes;
import com.google.common.collect.ImmutableMap;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import it.jakegblp.lusk.Lusk;
import org.bukkit.Registry;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public class RegistryUtils {

    public static ImmutableMap<Class<?>, Registry<?>> generateRegistries() {
        HashMap<Class<?>, Registry<?>> registries = new HashMap<>();
        for (Class<?> clazz
                : Constants.PAPER_HAS_PAPER_REGISTRY_KEY
                ? new Class[]{RegistryKey.class, Registry.class}
                : new Class[]{Registry.class}) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getType() == clazz
                        && Modifier.isStatic(field.getModifiers())
                        && Modifier.isFinal(field.getModifiers())) {
                    String className = ClassUtils.getFieldClassGenerics(field)[0];
                    try {
                        Class<?> theClass = Class.forName(className);
                        if (Classes.getExactClassInfo(theClass) == null) {
                            registries.putIfAbsent(Class.forName(className), getRegistryFromField(field));
                        }
                    } catch (IllegalAccessException | ClassNotFoundException e) {
                        String message = (e instanceof IllegalAccessException)
                                ? "The class '" + className + "' is not accessible."
                                : "The class '" + className + "' was not found.";
                        Lusk.getInstance().getLogger().severe(message);
                    }
                }
            }
        }
        return ImmutableMap.copyOf(registries);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Nullable
    public static Registry<?> getRegistryFromField(Field field) throws IllegalAccessException {
        Object object = field.get(null);
        if (Constants.PAPER_HAS_PAPER_REGISTRY_KEY && object instanceof RegistryKey registryKey) {
            return RegistryAccess.registryAccess().getRegistry(registryKey);
        } else if (object instanceof Registry<?> registry) {
            return registry;
        }
        return null;
    }
}
