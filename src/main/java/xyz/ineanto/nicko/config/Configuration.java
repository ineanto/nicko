package xyz.ineanto.nicko.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import xyz.ineanto.nicko.version.Version;

public class Configuration {
    public static final Version VERSION = new Version(1, 2, 0);
    public static final Configuration DEFAULT = new Configuration(VERSION.toString(),
            DefaultDataSources.SQL_EMPTY,
            DefaultDataSources.REDIS_EMPTY,
            false);

    private final transient Version versionObject;

    @JsonProperty("version")
    private final String version;
    @JsonProperty("sql")
    private final SQLDataSourceConfiguration sqlConfiguration;
    @JsonProperty("redis")
    private final DataSourceConfiguration redisConfiguration;
    @JsonProperty("customLocale")
    private final Boolean customLocale;

    public Configuration(@JsonProperty("version") String version,
                         @JsonProperty("sql") SQLDataSourceConfiguration sqlConfiguration,
                         @JsonProperty("redis") DataSourceConfiguration redisConfiguration,
                         @JsonProperty("customLocale") Boolean customLocale) {
        this.version = version;
        this.versionObject = Version.fromString(version);
        this.sqlConfiguration = sqlConfiguration;
        this.redisConfiguration = redisConfiguration;
        this.customLocale = customLocale;
    }

    public String getVersion() {
        return version;
    }

    public Version getVersionObject() {
        return versionObject;
    }

    public SQLDataSourceConfiguration getSqlConfiguration() {
        return sqlConfiguration;
    }

    public DataSourceConfiguration getRedisConfiguration() {
        return redisConfiguration;
    }

    public Boolean isCustomLocale() {
        return customLocale;
    }
}
