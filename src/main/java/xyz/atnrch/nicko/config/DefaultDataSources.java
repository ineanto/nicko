package xyz.atnrch.nicko.config;

public class DefaultDataSources {
    public static final DataSourceConfiguration REDIS_EMPTY = new DataSourceConfiguration(false, "127.0.0.1", 6379, "", "");
    public static final SQLDataSourceConfiguration MARIADB_EMPTY = new SQLDataSourceConfiguration(false, "127.0.0.1", 3306, "root", "", true);
    public static final SQLDataSourceConfiguration SQL_EMPTY = new SQLDataSourceConfiguration(false, "127.0.0.1", 3306, "root", "", false);

}
