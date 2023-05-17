package xyz.atnrch.nicko.config;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TODO: 4/2/23 Convert using Bukkit YAML API
 * <a href="https://www.spigotmc.org/threads/tutorial-bukkit-custom-serialization.148781/">Link</a>
 **/
public class Configuration {
    @JsonProperty("sql")
    private final DataSourceConfiguration sqlConfiguration;
    @JsonProperty("redis")
    private final DataSourceConfiguration redisConfiguration;
    private final String prefix;
    private final Boolean local;
    private final Boolean customLocale;

    public Configuration(DataSourceConfiguration sqlConfiguration, DataSourceConfiguration redisConfiguration, String prefix, Boolean local, Boolean customLocale) {
        this.sqlConfiguration = sqlConfiguration;
        this.redisConfiguration = redisConfiguration;
        this.prefix = prefix;
        this.local = local;
        this.customLocale = customLocale;
    }

    public Configuration() {
        this(
                new DataSourceConfiguration("", 3306, "", ""),
                new DataSourceConfiguration("", 6379, "", ""),
                "",
                false,
                false
        );
    }

    public DataSourceConfiguration getSqlConfiguration() {
        return sqlConfiguration;
    }

    public DataSourceConfiguration getRedisConfiguration() {
        return redisConfiguration;
    }

    public String getPrefix() {
        return prefix;
    }

    public Boolean isLocal() {
        return local;
    }

    public Boolean isCustomLocale() {
        return customLocale;
    }
}
