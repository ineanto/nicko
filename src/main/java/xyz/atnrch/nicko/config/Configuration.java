package xyz.atnrch.nicko.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Configuration {
    @JsonProperty("sql")
    private final DataSourceConfiguration sqlConfiguration;
    @JsonProperty("redis")
    private final DataSourceConfiguration redisConfiguration;
    private final String prefix;
    private final Boolean customLocale;

    public Configuration(DataSourceConfiguration sqlConfiguration, DataSourceConfiguration redisConfiguration, String prefix, Boolean customLocale) {
        this.sqlConfiguration = sqlConfiguration;
        this.redisConfiguration = redisConfiguration;
        this.prefix = prefix;
        this.customLocale = customLocale;
    }

    public Configuration() {
        this(
                new DataSourceConfiguration(false, "", 3306, "", ""),
                new DataSourceConfiguration(false, "", 6379, "", ""),
                "",
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

    public Boolean isCustomLocale() {
        return customLocale;
    }
}
