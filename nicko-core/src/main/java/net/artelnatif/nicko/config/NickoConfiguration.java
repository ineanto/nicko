package net.artelnatif.nicko.config;

import net.artelnatif.nicko.NickoBukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class NickoConfiguration {
    private final NickoBukkit nicko;
    private String prefix;
    private String fallbackLocale;

    private Boolean bungeecordSupport;
    private Boolean localStorage;
    private Boolean customLocale;

    private String sqlUsername, sqlPassword, sqlAddress;

    public NickoConfiguration(NickoBukkit nicko) {
        this.nicko = nicko;
    }

    public ConfigurationSection getBungeecordSection() { return getConfig().getConfigurationSection("bungeecord"); }

    public ConfigurationSection getStorageSection() { return getConfig().getConfigurationSection("storage"); }

    public ConfigurationSection getLocaleSection() { return getBungeecordSection().getConfigurationSection("locale"); }

    public ConfigurationSection getRedisSection() { return getBungeecordSection().getConfigurationSection("redis"); }

    public String getPrefix() {
        if (prefix == null) {
            return prefix = getConfig().getString("prefix");
        }
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getFallbackLocale() {
        if (fallbackLocale == null) {
            return fallbackLocale = getLocaleSection().getString("fallback");
        }
        return fallbackLocale;
    }

    public void setFallbackLocale(String fallbackLocale) {
        this.fallbackLocale = fallbackLocale;
    }

    public boolean isCustomLocaleEnabled() {
        if (customLocale == null) {
            return customLocale = getStorageSection().getBoolean("local");
        }
        return customLocale;
    }

    public void setCustomLocaleEnabled(Boolean localStorage) {
        this.localStorage = localStorage;
    }

    public boolean isBungeecordEnabled() {
        if (bungeecordSupport == null) {
            return bungeecordSupport = getBungeecordSection().getBoolean("enabled");
        }
        return bungeecordSupport;
    }

    public void setBungeecordSupport(Boolean bungeecordSupport) {
        this.bungeecordSupport = bungeecordSupport;
    }

    public boolean isLocalStorage() {
        if (localStorage == null) {
            return localStorage = getStorageSection().getBoolean("local");
        }
        return localStorage;
    }

    public void setLocalStorage(Boolean localStorage) {
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
