package xyz.ineanto.nicko.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class ConfigurationManager {
    private final Logger logger = Logger.getLogger("ConfigurationManager");
    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    private final File file;
    private final File backupFile;

    public ConfigurationManager(File directory) {
        final String date = Instant.now()
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        this.file = new File(directory, "config.yml");
        this.backupFile = new File(directory, "config.old-" + date + ".yml");
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
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

    public File getFile() {
        return file;
    }

    public File getBackupFile() {
        return backupFile;
    }
}
