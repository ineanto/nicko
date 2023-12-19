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
}
