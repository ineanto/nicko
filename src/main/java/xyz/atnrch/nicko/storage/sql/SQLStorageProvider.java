package xyz.atnrch.nicko.storage.sql;

import xyz.atnrch.nicko.config.Configuration;
import xyz.atnrch.nicko.config.DataSourceConfiguration;
import xyz.atnrch.nicko.storage.StorageProvider;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class SQLStorageProvider implements StorageProvider {
    private final Logger logger = Logger.getLogger("SQLStorageProvider");
    private final Configuration configuration;

    private Connection connection;

    private final String schemaName = "nicko";

    public SQLStorageProvider(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean init() {
        try {
            final MariaDbDataSource dataSource = new MariaDbDataSource();
            final DataSourceConfiguration dataSourceConfiguration = configuration.getSqlConfiguration();
            dataSource.setUrl("jdbc:mariadb://" + dataSourceConfiguration.getAddress() + ":" + dataSourceConfiguration.getPort());
            dataSource.setUser(dataSourceConfiguration.getUsername());
            dataSource.setPassword(dataSourceConfiguration.getPassword());
            connection = dataSource.getConnection();
            final boolean initialized = connection != null && !connection.isClosed();

            if (!initialized) return false;

            createDatabase();
            createTable();
            return true;
        } catch (SQLException e) {
            logger.severe("Couldn't establish a connection to the MySQL database: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean close() {
        if (connection == null) { return true; }
        try {
            connection.close();
            return connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    private void createTable() throws SQLException {
        final Connection connection = getConnection();

        String query = "CREATE TABLE IF NOT EXISTS %s.DATA " +
                       "(uuid binary(16) NOT NULL," +
                       "name varchar(16)," +
                       "skin varchar(16)," +
                       "locale char(2) NOT NULL," +
                       "bungeecord boolean NOT NULL," +
                       "PRIMARY KEY (UUID))";
        query = query.replace("%s", schemaName);

        final PreparedStatement statement = connection.prepareStatement(query);
        statement.executeUpdate();
        statement.close();
    }

    private void createDatabase() throws SQLException {
        final Connection connection = getConnection();

        String query = "CREATE DATABASE IF NOT EXISTS %s";
        query = query.replace("%s", schemaName);

        final PreparedStatement statement = connection.prepareStatement(query);
        statement.executeUpdate();
        statement.close();
    }

    public Connection getConnection() {
        return connection;
    }
}
