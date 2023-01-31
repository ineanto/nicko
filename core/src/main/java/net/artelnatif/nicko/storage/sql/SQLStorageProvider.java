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
    private MariaDbDataSource dataSource;

    private final String schemaName = "nicko";

    public SQLStorageProvider(Nicko nicko) {
        this.nicko = nicko;
    }

    @Override
    public boolean init() {
        try {
            final Configuration config = nicko.getConfig();
            dataSource = new MariaDbDataSource();
            dataSource.setUrl("jdbc:mariadb://" + config.getAddress());
            dataSource.setUser(config.getUsername());
            dataSource.setPassword(config.getPassword());
            connection = dataSource.getConnection();
            final boolean initialized = connection != null && !connection.isClosed();

            if (!initialized) return false;

            nicko.getLogger().info("Creating SQL database...");
            createDatabase();

            nicko.getLogger().info("Creating SQL table...");
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

    private void createTable() {
        final Connection connection = getConnection();

        String query = "CREATE TABLE IF NOT EXISTS %s.DATA (uuid binary(16) NOT NULL,name varchar(16) NOT NULL,skin varchar(16) NOT NULL,bungeecord boolean NOT NULL,PRIMARY KEY (UUID))";
        query = query.replace("%s", schemaName);

        try {
            final PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            // TODO: 12/10/22 Handle error
            throw new RuntimeException(e);
        }
    }

    private void createDatabase() {
        final Connection connection = getConnection();

        String query = "CREATE DATABASE IF NOT EXISTS %s";
        query = query.replace("%s", schemaName);

        try {
            final PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            // TODO: 12/10/22 Handle error
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
