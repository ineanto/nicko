package net.artelnatif.nicko.storage.sql;

import net.artelnatif.nicko.Nicko;
import net.artelnatif.nicko.config.Configuration;
import net.artelnatif.nicko.storage.StorageProvider;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLStorageProvider implements StorageProvider {
    private final Nicko nicko;
    private Connection connection;

    private final String schemaName = "nicko";

    public SQLStorageProvider(Nicko nicko) {
        this.nicko = nicko;
    }

    @Override
    public boolean init() {
        try {
            final Configuration config = nicko.getConfig();
            final MariaDbDataSource dataSource = new MariaDbDataSource();
            dataSource.setUrl("jdbc:mariadb://" + config.getAddress());
            dataSource.setUser(config.getUsername());
            dataSource.setPassword(config.getPassword());
            connection = dataSource.getConnection();
            final boolean initialized = connection != null && !connection.isClosed();

            if (!initialized) return false;

            createDatabase();
            createTable();
            return true;
        } catch (SQLException e) {
            nicko.getLogger().severe("Couldn't establish a connection to the MySQL database: " + e.getMessage());
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

        String query = "CREATE TABLE IF NOT EXISTS %s.DATA (uuid binary(16) NOT NULL,name varchar(16) NOT NULL,skin varchar(16) NOT NULL,bungeecord boolean NOT NULL,PRIMARY KEY (UUID))";
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
