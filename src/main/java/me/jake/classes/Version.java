package me.jake.lusk.classes;

import java.util.ArrayList;
import java.util.List;

public class Version {

    private int main;
    private int major;
    private int minor;

    public Version(int main, int major, int minor) {
        this.main = main;
        this.major = major;
        this.minor = minor;
    }

    public Version(int main, int major) {
        this.main = main;
        this.major = major;
        this.minor = 0;
    }

    public Version(int main) {
        this.main = main;
        this.major = 0;
        this.minor = 0;
    }

    public void setMain(int main) {
        this.main = main;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getMain() {
        return main;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    @Override
    public String toString() {
        if (this.minor == 0) {
            if (this.major == 0) {
                return String.valueOf(this.main);
            } else {
                return this.main + "." + this.major;
            }
        } else {
            return this.main + "." + this.major + "." + this.minor;
        }
    }

    public static Version parse(String string) {
        if (!isValid(string)) {
            return null;
        }
        String[] split = (string+".").split("\\.");
        int size = split.length;
        List<Integer> ints = new ArrayList<>(size);
        for (String s : split) {
            try {
                ints.add(Integer.valueOf(s));
            } catch (NumberFormatException ignored) {
            }
        }
        try {
            return switch (size) {
                case 3 -> new Version(ints.get(0), ints.get(1), ints.get(2));
                case 2 -> new Version(ints.get(0), ints.get(1));
                case 1 -> new Version(ints.get(0));
                default -> null;
            };
        } catch (IndexOutOfBoundsException ignored) {
        }
        return null;
    }

    public boolean isNewerThan(Version otherVersion) {
        if (this.getMain() > otherVersion.getMain()) {
            return true;
        } else if (this.getMain() == otherVersion.getMain()) {
            if (this.getMajor() > otherVersion.getMajor()) {
                return true;
            } else if (this.getMajor() == otherVersion.getMajor()) {
                return this.getMinor() > otherVersion.getMinor();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isNewerThanOrEqualTo(Version otherVersion) {
        return this.isNewerThan(otherVersion) || this == otherVersion;
    }
    public boolean isOldenThanOrEqualTo(Version otherVersion) {
        return (!this.isNewerThan(otherVersion)) || this == otherVersion;
    }
    public static boolean isValid(String string) {
        return (string.matches("^[0-9]+(.[0-9]+){0,2}$"));
    }
}
