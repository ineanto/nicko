package xyz.ineanto.nicko.language;

import xyz.ineanto.nicko.version.Version;

import java.io.Serializable;

public enum Language implements Serializable {
    ENGLISH("en", "English"),
    FRENCH("fr", "Français"),
    CUSTOM("cm", "Server Custom");

    public static final Version VERSION = new Version(1, 3, 0);

    private final String code;
    private transient final String name;

    Language(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Language fromCode(String code) {
        for (Language value : values()) {
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
