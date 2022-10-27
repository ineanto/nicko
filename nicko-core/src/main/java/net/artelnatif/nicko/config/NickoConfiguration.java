package net.artelnatif.nicko.config;

import net.artelnatif.nicko.NickoBukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class NickoConfiguration {
    private final NickoBukkit nicko;

    public NickoConfiguration(NickoBukkit nicko) {
        this.nicko = nicko;
    }

    public String getPrefix() {
        return getConfig().getString("prefix");
    }

    public String getDefaultLocale() { return getConfig().getString("locale"); }

    public ConfigurationSection getBungeecordSection() { return getConfig().getConfigurationSection("bungeecord"); }

    public ConfigurationSection getStorageSction() { return getConfig().getConfigurationSection("storage"); }

    public ConfigurationSection getRedisSection() { return getBungeecordSection().getConfigurationSection("redis"); }

    public boolean isLocalStorage() { return getStorageSction().getBoolean("local"); }

    public boolean isBungeecordEnabled() { return getBungeecordSection().getBoolean("enabled"); }

    public String getStorageUsername() { return getStorageSction().getString("username"); }

    public String getStoragePassword() { return getStorageSction().getString("password"); }

    public String getStorageAddress() { return getStorageSction().getString("address"); }

    private FileConfiguration getConfig() {
        return nicko.getConfig();
    }
}
