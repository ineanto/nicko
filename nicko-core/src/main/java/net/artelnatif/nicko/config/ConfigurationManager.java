package net.artelnatif.nicko.config;

import net.artelnatif.nicko.Nicko;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ConfigurationManager {
    private final Nicko nicko;
    private final Yaml yaml = new Yaml();
    private final File directory;
    private final File file;

    public ConfigurationManager(Nicko nicko) {
        this.nicko = nicko;
        this.directory = nicko.getDataFolder();
        this.file = new File(directory, "config.yml");
    }

    public void save(Configuration configuration) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            yaml.dump(configuration, writer);
            writer.flush();
        }
    }

    public void saveDefaultConfig() {
        if (!file.exists()) {
            try {
                final InputStream input = ConfigurationManager.class.getResourceAsStream("config.yml");
                if (input != null) {
                    nicko.getLogger().info("Saved default configuration as config.yml");
                    Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Configuration load() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return yaml.loadAs(reader, Configuration.class);
        }
    }
}
