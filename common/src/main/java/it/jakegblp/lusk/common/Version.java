package it.jakegblp.lusk.common;

import org.jetbrains.annotations.NotNull;

public record Version(int major, int minor, int patch) implements Comparable<Version> {
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

    public static Version of(int major, int minor, int patch) {
        return new Version(major, minor, patch);
    }

    public static Version of(int major, int minor) {
        return new Version(major, minor);
    }

    public static Version of(int major) {
        return new Version(major);
    }

    @Override
    public int compareTo(Version o) {
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

    public static Version parse(String s) {
        String[] parts = s.split("\\.");
        if (parts.length != 3)
            throw new IllegalArgumentException("Version must be in format x.x.x, found: " + s);
        try {
            return new Version(
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[2])
            );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number: " + s, e);
        }
    }
}
