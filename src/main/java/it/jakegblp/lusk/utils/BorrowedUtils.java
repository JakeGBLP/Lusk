package it.jakegblp.lusk.utils;

import ch.njol.skript.Skript;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BorrowedUtils {
    /**
     * Gets a Minecraft NamespacedKey from string
     * <p>If a namespace is not provided, it will default to "minecraft:" namespace</p>
     *
     * @param key   Key for new Minecraft NamespacedKey
     * @param error Whether to send a skript/console error if one occurs
     * @return new Minecraft NamespacedKey
     * @author ShaneBeee
     */
    @Nullable
    public static NamespacedKey getNamespacedKey(@NotNull String key, boolean error) {
        if (!key.contains(":")) key = "minecraft:" + key;
        if (key.length() > 255) {
            if (error)
                Skript.error("An invalid key was provided, key must be less than 256 characters: "+key);
            return null;
        }
        key = key.toLowerCase();
        if (key.contains(" ")) {
            key = key.replace(" ", "_");
        }

        NamespacedKey namespacedKey = NamespacedKey.fromString(key);
        if (namespacedKey == null && error)
            Skript.error("An invalid key was provided, that didn't follow [a-z0-9/._-:]. key: "+key);
        return namespacedKey;
    }
}
