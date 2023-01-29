package net.artelnatif.nicko.config;

public record Configuration(
        String address,
        String username,
        String password,
        String prefix,
        Boolean local,
        Boolean bungeecord,
        Boolean customLocale) { }
