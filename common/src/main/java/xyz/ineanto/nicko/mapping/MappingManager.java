package xyz.ineanto.nicko.mapping;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import xyz.ineanto.nicko.mapping.v1_20.Mapping1_20;

import java.util.List;
import java.util.Optional;

public class MappingManager {
    private final List<Mapping> mappings = List.of(
            new Mapping1_20()
    );

    public Optional<Mapping> getMappingFor(Server server) {
        return mappings.stream()
                .filter(mapping -> mapping.supportedVersions().contains(Bukkit.getMinecraftVersion()))
                .findFirst();
    }
}
