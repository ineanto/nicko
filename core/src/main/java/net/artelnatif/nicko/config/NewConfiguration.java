package net.artelnatif.nicko.config;

public class NewConfiguration {
    private final DataSourceConfiguration sqlConfiguration;
    private final DataSourceConfiguration redisConfiguration;
    private final String prefix;
    private final Boolean local;
    private final Boolean customLocale;

    public NewConfiguration(DataSourceConfiguration sqlConfiguration, DataSourceConfiguration redisConfiguration, String prefix, Boolean local, Boolean customLocale) {
        this.sqlConfiguration = sqlConfiguration;
        this.redisConfiguration = redisConfiguration;
        this.prefix = prefix;
        this.local = local;
        this.customLocale = customLocale;
    }

    public NewConfiguration() {
        this(
                new DataSourceConfiguration("", "", "", ""),
                new DataSourceConfiguration("", "", "", ""),
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
