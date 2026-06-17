package it.jakegblp.lusk.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

public final class URIUtils {
    public static @Nullable URI toURI(@NotNull String input) {
        try {
            return URI.create(input);
        } catch (IllegalArgumentException ignored) {}
        Path path = Path.of(input);
        if (Files.exists(path))
            return path.toUri();
        return null;
    }
}
