package xyz.ineanto.nicko.language;

import com.github.jsixface.YamlConfig;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.version.Version;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class CustomLanguage {
    private static final File directory = new File(Nicko.getInstance().getDataFolder(), "/locale/");
    private static final File file = new File(directory, "locale.yml");

    private final String version;
    private final Version versionObject;
    private final YamlConfig yamlFile;

    public CustomLanguage() throws IOException {
        this.yamlFile = new YamlConfig(new FileInputStream(file));
        this.version = yamlFile.getString("version");
        this.versionObject = Version.fromString(version);
    }

    public static void dumpIntoFile(Language language) throws IOException {
        if (language == Language.CUSTOM) return;
        if (file.exists()) return;
        if (!directory.exists()) directory.mkdirs();

        final String localeFileName = language.getCode() + ".yml";
        try {
            final InputStream resource = Nicko.getInstance().getResource(localeFileName);
            Files.copy(resource, file.toPath());
            resource.close();
        } catch (IOException e) {
            Nicko.getInstance().getLogger().severe("Unable to dump Locale: " + language.getCode() + "!");
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
