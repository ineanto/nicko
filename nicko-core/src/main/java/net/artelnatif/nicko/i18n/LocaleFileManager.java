package net.artelnatif.nicko.i18n;

import com.github.jsixface.YamlConfig;
import net.artelnatif.nicko.NickoBukkit;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;

public class LocaleFileManager {
    private final Yaml yaml = new Yaml();
    private final File folder = new File(NickoBukkit.getInstance().getDataFolder() + "/lang/");
    private final File file = new File(folder, "lang.yml");

    public String get(String key) {
        if (!file.exists()) return key;
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
            final YamlConfig yamlConfig = YamlConfig.load(inputStream);
            return yamlConfig.getString(key);
        } catch (IOException e) {
            return key;
        }
    }

    public boolean dumpFromLocale(Locale locale) {
        if (locale == Locale.CUSTOM) return true;
        if (file.exists()) return true;
        final HashMap<String, String> values = yaml.load(this.getClass().getClassLoader().getResourceAsStream(locale.getCode() + ".yml"));
        try {
            if (file.createNewFile()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    yaml.dump(values, writer);
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
