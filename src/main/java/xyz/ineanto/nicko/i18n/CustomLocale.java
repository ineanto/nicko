package xyz.ineanto.nicko.i18n;

import com.github.jsixface.YamlConfig;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.version.Version;

import java.io.*;
import java.nio.file.Files;
import java.util.logging.Logger;

public class CustomLocale {
    private static final Logger logger = Logger.getLogger("CustomLocale");
    private static final File directory = new File(NickoBukkit.getInstance().getDataFolder(), "/locale/");
    private static final File file = new File(directory, "locale.yml");

    private final String version;
    private final Version versionObject;
    private final YamlConfig yamlFile;

    public CustomLocale(InputStream input) throws IOException {
        this.yamlFile = new YamlConfig(input);
        this.version = yamlFile.getString("version");
        this.versionObject = Version.fromString(version);
    }

    public CustomLocale() throws IOException {
        this.yamlFile = new YamlConfig(new FileInputStream(file));
        this.version = yamlFile.getString("version");
        this.versionObject = Version.fromString(version);
    }

    public static void dumpIntoFile(Locale locale) throws IOException {
        if (locale == Locale.CUSTOM) return;
        if (file.exists()) return;
        if (!directory.exists()) directory.mkdirs();

        final String localeFileName = locale.getCode() + ".yml";
        try {
            final InputStream resource = NickoBukkit.getInstance().getResource(localeFileName);
            if (resource != null) {
                Files.copy(resource, file.toPath());
                resource.close();
            }
        } catch (IOException e) {
            logger.severe("Unable to dump Locale: " + locale.getCode() + "!");
        }
    }

    public String getVersion() {
        return version;
    }

    public Version getVersionObject() {
        return versionObject;
    }

    public YamlConfig getYamlFile() {
        return yamlFile;
    }

    public File getDirectory() {
        return directory;
    }

    public File getFile() {
        return file;
    }
}
