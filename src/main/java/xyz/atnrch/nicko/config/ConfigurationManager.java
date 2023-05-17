package xyz.atnrch.nicko.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

public class ConfigurationManager {
    private final Logger logger = Logger.getLogger("ConfigurationManager");
    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    private final File file;

    public ConfigurationManager(File directory) {
        this.file = new File(directory, "config.yml");
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public void save(Configuration configuration) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            mapper.writeValue(writer, configuration);
            writer.flush();
        }
    }

    public void saveDefaultConfig() {
        if (!file.exists()) {
            try {
                final InputStream input = getClass().getResourceAsStream("/config.yml");
                if (input != null) {
                    logger.info("Saved default configuration as config.yml");
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
