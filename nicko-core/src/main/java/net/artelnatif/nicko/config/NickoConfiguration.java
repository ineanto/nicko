package net.artelnatif.nicko.config;

import net.artelnatif.nicko.NickoBukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class NickoConfiguration {
    private final NickoBukkit nicko;
    private String prefix;
    private String defaultLocale;

    private Boolean bungeecordSupport;
    private Boolean localStorage;

    private String sqlUsername, sqlPassword, sqlAddress;

    public NickoConfiguration(NickoBukkit nicko) {
        this.nicko = nicko;
    }

    public ConfigurationSection getBungeecordSection() { return getConfig().getConfigurationSection("bungeecord"); }

    public ConfigurationSection getStorageSection() { return getConfig().getConfigurationSection("storage"); }

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

    public String getDefaultLocale() {
        if (defaultLocale == null) {
            return defaultLocale = getConfig().getString("locale");
        }
        return defaultLocale;
    }

    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
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
        return getStorageSection().getString("password");
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
