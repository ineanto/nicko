package xyz.ineanto.nicko.storage.mysql;

import com.mysql.cj.jdbc.MysqlDataSource;
import xyz.ineanto.nicko.config.Configuration;
import xyz.ineanto.nicko.config.DataSourceConfiguration;
import xyz.ineanto.nicko.storage.StorageProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class MySQLStorageProvider implements StorageProvider {
    private final Logger logger = Logger.getLogger("MySQLStorageProvider");
    private final Configuration configuration;

    private Connection connection;

    private final String schemaName = "nicko";

    public MySQLStorageProvider(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean init() {
        try {
            final MysqlDataSource dataSource = new MysqlDataSource();
            final DataSourceConfiguration sqlConfiguration = configuration.getSqlConfiguration();
            dataSource.setUrl("jdbc:mysql://" + sqlConfiguration.getAddress() + ":" + sqlConfiguration.getPort());
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
        final String query = "CREATE TABLE IF NOT EXISTS %s.DATA ".replace("%s", schemaName) +
                             "(uuid varchar(36) NOT NULL," +
                             "name varchar(16)," +
                             "skin varchar(16)," +
                             "locale char(2) NOT NULL," +
                             "bungeecord boolean NOT NULL," +
                             "PRIMARY KEY (uuid))";

        final PreparedStatement statement = connection.prepareStatement(query);
        statement.executeUpdate();
    }

    private void createDatabase() throws SQLException {
        final Connection connection = getConnection();
        final String query = "CREATE DATABASE IF NOT EXISTS %s".replace("%s", schemaName);

        final PreparedStatement statement = connection.prepareStatement(query);
        statement.executeUpdate();
    }

    public Connection getConnection() {
        return connection;
    }
}
