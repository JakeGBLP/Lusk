package it.jakegblp.lusk.common;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

// todo: make hyper-optimized version checking
public record Version(int major, int minor, int patch) implements Comparable<Version>, Copyable<Version> {
    public Version {
        if (major < 0 || minor < 0 || patch < 0)
            throw new IllegalArgumentException("Version numbers must be non-negative");
    }

    public Version(int major, int minor) {
        this(major, minor, 0);
    }

    public Version(int major) {
        this(major, 0);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static @NotNull Version of(int major, int minor, int patch) {
        return new Version(major, minor, patch);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Version of(int major, int minor) {
        return new Version(major, minor);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Version of(int major) {
        return new Version(major);
    }

    @Override
    public int compareTo(@NotNull Version o) {
        int cmp = Integer.compare(major, o.major);
        if (cmp != 0) return cmp;
        cmp = Integer.compare(minor, o.minor);
        if (cmp != 0) return cmp;
        return Integer.compare(patch, o.patch);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Version(int major1, int minor1, int patch1))) return false;
        return major == major1 && minor == minor1 && patch == patch1;
    }

    public boolean isGreaterThan(Version other) {
        return compareTo(other) > 0;
    }
    public boolean isGreaterOrEqual(Version other) {
        return compareTo(other) >= 0;
    }
    public boolean isLowerThan(Version other) {
        return compareTo(other) < 0;
    }
    public boolean isLowerOrEqual(Version other) {
        return compareTo(other) <= 0;
    }

    @Override
    public @NotNull String toString() {
        return major + "." + minor + "." + patch;
    }

    @Contract("_ -> new")
    public static @NotNull Version parse(@NotNull String unparsed) {
        String[] parts = unparsed.split("\\.");
        if (parts.length != 3)
            throw new IllegalArgumentException("Version must be in format x.x.x, found: " + unparsed);
        try {
            return new Version(
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[2])
            );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number: " + unparsed, e);
        }
    }

    @Override
    public Version copy() {
        return new Version(major, minor, patch);
    }
}
