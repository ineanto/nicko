package xyz.ineanto.nicko.i18n;

import com.github.jsixface.YamlConfig;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.xenondevs.invui.util.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.logging.Logger;

public class LocaleFileManager {
    private final Logger logger = Logger.getLogger("LocaleFileManager");
    private final File folder = new File(NickoBukkit.getInstance().getDataFolder() + "/lang/");
    private final File file = new File(folder, "lang.yml");
    private YamlConfig yamlFile;

    public boolean dumpFromLocale(Locale locale) {
        if (locale == Locale.CUSTOM) return true;
        if (file.exists()) return true;
        final InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(locale.getCode() + ".yml");
        try {
            if (folder.mkdirs()) {
                if (file.createNewFile()) {
                    try (FileOutputStream outputStream = new FileOutputStream(file)) {
                        IOUtils.copy(inputStream, outputStream, 8192);
                    }
                }
            }
            return true;
        } catch (IOException e) {
            logger.severe("Unable to dump Locale: " + locale.getCode() + "!");
            return false;
        }
    }

    public YamlConfig getYamlFile() {
        if (yamlFile == null) {
            try (BufferedInputStream inputStream = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
                yamlFile = new YamlConfig(inputStream);
            } catch (IOException ignored) {
                return null;
            }
        }
        return yamlFile;
    }
}
