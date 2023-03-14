package net.artelnatif.nicko.i18n;

import com.github.jsixface.YamlConfig;
import de.studiocode.invui.util.IOUtils;
import net.artelnatif.nicko.NickoBukkit;

import java.io.*;
import java.nio.file.Files;

public class LocaleFileManager {
    private final File folder = new File(NickoBukkit.getInstance().getDataFolder() + "/lang/");
    private final File file = new File(folder, "lang.yml");

    public String get(String key) {
        if (!file.exists()) return key;
        try (BufferedInputStream inputStream = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
            final YamlConfig yamlConfig = YamlConfig.load(inputStream);
            return yamlConfig.getString(key);
        } catch (IOException e) {
            return key;
        }
    }

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
            e.printStackTrace();
            return false;
        }
    }
}
