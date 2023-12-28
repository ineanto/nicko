package xyz.ineanto.nicko.i18n;

import xyz.ineanto.nicko.version.Version;

import java.io.Serializable;

public enum Locale implements Serializable {
    ENGLISH("en", "English"),
    FRENCH("fr", "Français"),
    CUSTOM("cm", "Server Custom");

    public static final Version VERSION = new Version(1, 1, 2);

    private final String code;
    private transient final String name;

    Locale(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Locale fromCode(String code) {
        for (Locale value : values()) {
            if (code.equals(value.code)) return value;
        }
        return ENGLISH;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
