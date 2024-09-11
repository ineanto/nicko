package xyz.ineanto.nicko.version;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public record Version(int major, int minor, int patch) implements Comparable<Version> {
    @Override
    public int compareTo(@NotNull Version otherVersion) {
        final Comparator<Version> comparator = Comparator
                .comparingInt(Version::major)
                .thenComparingInt(Version::minor)
                .thenComparingInt(Version::patch);
        return comparator.compare(this, otherVersion);
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patch;
    }

    public static Version fromString(String versionString) {
        if (versionString == null || versionString.isEmpty()) { return new Version(0, 0, 0); }
        final String[] split = versionString.split("\\.");
        try {
            return new Version(Integer.parseInt(split[0]),
                    Integer.parseInt(split[1]),
                    Integer.parseInt(split[2]));
        } catch (NumberFormatException exception) {
            return new Version(0, 0, 0);
        }
    }
}