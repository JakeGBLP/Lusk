package it.jakegblp.lusk.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public final class URLUtils {
    public static @Nullable URL toURL(@NotNull String input) {
        try {
            URI uri = URI.create(input);
            if (uri.isAbsolute())
                return uri.toURL();
        } catch (IllegalArgumentException | MalformedURLException ignored) {}
        Path path = Path.of(input);
        if (Files.exists(path)) {
            try {
                return path.toUri().toURL();
            } catch (MalformedURLException ignored) {}
        }
        return null;
    }
}
