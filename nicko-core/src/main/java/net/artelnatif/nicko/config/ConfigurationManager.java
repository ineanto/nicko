package net.artelnatif.nicko.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.artelnatif.nicko.Nicko;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ConfigurationManager {
    private final Nicko nicko;
    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    private final File directory;
    private final File file;

    public ConfigurationManager(Nicko nicko) {
        this.nicko = nicko;
        this.directory = nicko.getDataFolder();
        this.file = new File(directory, "config.yml");
    }

    public void save(Configuration configuration) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            mapper.writeValue(writer, configuration);
            writer.flush();
        }
    }

    public void saveDefaultConfig() {
        if (!file.exists()) {
            System.out.println("FILE DOES NOT EXISTS");
            try {
                final InputStream input = getClass().getResourceAsStream("/config.yml");
                if (input != null) {
                    nicko.getLogger().info("Saved default configuration as config.yml");
                    Files.createDirectories(file.getParentFile().toPath());
                    Files.createFile(file.toPath());
                    Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Configuration load() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return mapper.readValue(reader, Configuration.class);
        }
    }
}
