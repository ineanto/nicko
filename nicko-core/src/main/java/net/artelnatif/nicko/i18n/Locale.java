package net.artelnatif.nicko.i18n;

import java.io.Serializable;
import java.util.HashMap;

public enum Locale implements Serializable {
    ENGLISH("en", "English"),
    FRENCH("fr", "Français"),
    CUSTOM("custom", "Server Custom");

    private static HashMap<String, Locale> locales;
    private static Locale defaultLocale;

    private final String code;
    private transient final String name;

    Locale(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static HashMap<String, Locale> getLocales() {
        if (locales == null) {
            return locales = new HashMap<>() {{
                for (Locale value : Locale.values()) {
                    put(value.getCode(), value);
                }
            }};
        }
        return locales;
    }

    public static Locale fromCode(String code) {
        return getLocales().getOrDefault(code, defaultLocale);
    }

    public static void setDefault(Locale defaultLocale) {
        Locale.defaultLocale = defaultLocale;
    }

    public static Locale getDefault() {
        return defaultLocale;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
