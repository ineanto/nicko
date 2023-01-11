package net.artelnatif.nicko.config;

import net.artelnatif.nicko.NickoBukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class NickoConfiguration {
    private final NickoBukkit nicko;
    private String prefix;

    private Boolean bungeecordSupport;
    private Boolean localStorage;
    private Boolean customLocale;

    private String sqlUsername, sqlPassword, sqlAddress;

    public NickoConfiguration(NickoBukkit nicko) {
        this.nicko = nicko;
    }

    //.............
    // SECTION ACCESSORS
    //.............

    public ConfigurationSection getBungeecordSection() { return getConfig().getConfigurationSection("bungeecord"); }

    // Unused for now
    public ConfigurationSection getRedisSection() { return getBungeecordSection().getConfigurationSection("redis"); }

    public ConfigurationSection getLocaleSection() { return getConfig().getConfigurationSection("locale"); }

    public ConfigurationSection getStorageSection() { return getConfig().getConfigurationSection("storage"); }

    //.............
    // GLOBAL
    //.............

    public String getPrefix() {
        if (prefix == null) {
            return prefix = getConfig().getString("prefix");
        }
        return prefix;
    }

    //.............
    // BUNGEECORD
    //.............

    public boolean isBungeecordSupport() {
        if (bungeecordSupport == null) {
            return bungeecordSupport = getBungeecordSection().getBoolean("enabled");
        }
        return bungeecordSupport;
    }

    public void setBungeecordSupport(Boolean bungeecordSupport) {
        this.bungeecordSupport = bungeecordSupport;
    }

    //.............
    // LOCALE
    //.............

    public boolean isCustomLocale() {
        if (customLocale == null) {
            return customLocale = getLocaleSection().getBoolean("use_custom_locale");
        }
        return customLocale;
    }

    public void setCustomLocale(boolean customLocale) {
        this.customLocale = customLocale;
    }

    //.............
    // STORAGE
    //.............

    public boolean isLocalStorage() {
        if (localStorage == null) {
            return localStorage = getStorageSection().getBoolean("local");
        }
        return localStorage;
    }

    public void setLocalStorage(boolean localStorage) {
        this.localStorage = localStorage;
    }

    public String getSQLUsername() {
        if (sqlUsername == null) {
            return sqlUsername = getStorageSection().getString("username");
        }
        return sqlUsername;
    }

    public void setSQLUsername(String sqlUsername) {
        this.sqlUsername = sqlUsername;
    }

    public String getSQLPassword() {
        if (sqlPassword == null) {
            return sqlPassword = getStorageSection().getString("password");
        }
        return sqlPassword;
    }

    public void setSQLPassword(String sqlPassword) {
        this.sqlPassword = sqlPassword;
    }

    public String getSQLAddress() {
        if (sqlAddress == null) {
            return sqlAddress = getStorageSection().getString("address");
        }
        return sqlAddress;
    }

    public void setSQLAddress(String sqlAddress) {
        this.sqlAddress = sqlAddress;
    }

    private FileConfiguration getConfig() {
        return nicko.getConfig();
    }
}
