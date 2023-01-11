package net.artelnatif.nicko.i18n;

import net.artelnatif.nicko.NickoBukkit;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;

public class LocaleFileManager {
    private final Yaml yaml = new Yaml();
    private final File folder = new File(NickoBukkit.getInstance().getDataFolder() + "/lang/");
    private final File file = new File(folder, "lang.yml");

    private HashMap<String, String> data = new HashMap<>();

    public boolean loadValues() {
        if (!file.exists()) return false;
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
            data = yaml.load(inputStream);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public String getFromFile(String key) {
        if (!file.exists() || data.isEmpty()) return key;
        return data.get(key);
    }

    public boolean dumpFromLocale(Locale locale) {
        if (locale == Locale.CUSTOM) return true;
        if (file.exists()) return true;
        final HashMap<String, String> values = yaml.load(this.getClass().getResourceAsStream(locale.getCode() + ".yml"));
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
