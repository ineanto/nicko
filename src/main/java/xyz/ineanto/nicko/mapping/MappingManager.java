package xyz.ineanto.nicko.mapping;

import org.bukkit.Bukkit;
import xyz.ineanto.nicko.mapping.v1_20.Mapping1_20;
import xyz.ineanto.nicko.mapping.v1_20_2.Mapping1_20_2;
import xyz.ineanto.nicko.mapping.v1_20_4.Mapping1_20_4;
import xyz.ineanto.nicko.mapping.v1_20_6.Mapping1_20_6;
import xyz.ineanto.nicko.mapping.v1_21.Mapping1_21;

import java.util.List;
import java.util.Optional;

public class MappingManager {
    private final List<Mapping> mappings = List.of(
            new Mapping1_20(),
            new Mapping1_20_2(),
            new Mapping1_20_4(),
            new Mapping1_20_6(),
            new Mapping1_21()
    );

    public Optional<Mapping> getMappingForServer() {
        return mappings.stream()
                .filter(mapping -> mapping.supportedVersions().contains(Bukkit.getMinecraftVersion()))
                .findFirst();
    }
}
