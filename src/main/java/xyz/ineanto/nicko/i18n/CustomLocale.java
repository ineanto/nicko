package xyz.ineanto.nicko.i18n;

import com.github.jsixface.YamlConfig;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.version.Version;
import xyz.xenondevs.invui.util.IOUtils;

import java.io.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class CustomLocale {
    private final Logger logger = Logger.getLogger("CustomLocale");
    private final File directory = new File(NickoBukkit.getInstance().getDataFolder() + "/lang/");

    private final File file;
    private final File backupFile;
    private final NickoBukkit instance;
    private final String version;
    private final Version versionObject;
    private final BufferedInputStream inputStream;
    private final BufferedOutputStream outputStream;
    private final YamlConfig yamlFile;

    public CustomLocale(NickoBukkit instance) throws FileNotFoundException {
        this.instance = instance;
        this.file = new File(directory, "lang.yml");
        final String date = Instant.now()
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        this.backupFile = new File(directory, "lang.old-" + date + ".yml");
        this.inputStream = new BufferedInputStream(new FileInputStream(file));
        this.outputStream = new BufferedOutputStream(new FileOutputStream(file));
        this.yamlFile = new YamlConfig(inputStream);
        this.version = yamlFile.getString("version");
        this.versionObject = Version.fromString(version);
    }

    public boolean dumpIntoFile(Locale locale) {
        if (locale == Locale.CUSTOM) return true;
        if (file.exists()) return true;

        try {
            if (directory.mkdirs()) {
                if (file.createNewFile()) {
                    IOUtils.copy(inputStream, outputStream, 8192);
                }
            }
            return true;
        } catch (IOException e) {
            logger.severe("Unable to dump Locale: " + locale.getCode() + "!");
            return false;
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

    public File getBackupFile() {
        return backupFile;
    }

    public File getFile() {
        return file;
    }
}
