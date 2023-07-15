package xyz.atnrch.nicko.storage.sql;

import org.mariadb.jdbc.MariaDbDataSource;
import xyz.atnrch.nicko.config.Configuration;
import xyz.atnrch.nicko.config.DataSourceConfiguration;
import xyz.atnrch.nicko.storage.StorageProvider;

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
            final DataSourceConfiguration sqlConfiguration = configuration.getSqlConfiguration();
            dataSource.setUrl("jdbc:mariadb://" + sqlConfiguration.getAddress() + ":" + sqlConfiguration.getPort());
            dataSource.setUser(sqlConfiguration.getUsername());
            dataSource.setPassword(sqlConfiguration.getPassword());
            connection = dataSource.getConnection();
            connection.setAutoCommit(true);
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
                       "(uuid varchar(36) NOT NULL," +
                       "name varchar(16)," +
                       "skin varchar(16)," +
                       "locale char(2) NOT NULL," +
                       "bungeecord boolean NOT NULL," +
                       "PRIMARY KEY (uuid))";
        query = query.replace("%s", schemaName);

        final PreparedStatement statement = connection.prepareStatement(query);
        statement.executeUpdate();
    }

    private void createDatabase() throws SQLException {
        final Connection connection = getConnection();

        String query = "CREATE DATABASE IF NOT EXISTS %s";
        query = query.replace("%s", schemaName);

        final PreparedStatement statement = connection.prepareStatement(query);
        statement.executeUpdate();
    }

    public Connection getConnection() {
        return connection;
    }
}
