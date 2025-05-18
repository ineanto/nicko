package xyz.ineanto.nicko.appearance;

import javax.annotation.Nullable;

public record Appearance(
        @Nullable String name,
        @Nullable String skin
) {}
