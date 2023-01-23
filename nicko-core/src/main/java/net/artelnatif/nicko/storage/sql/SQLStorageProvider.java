package net.artelnatif.nicko.storage.sql;

import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.config.NickoConfiguration;
import net.artelnatif.nicko.storage.StorageProvider;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLStorageProvider implements StorageProvider {
    private final NickoBukkit instance;
    private Connection connection;
    private MariaDbDataSource dataSource;

    private final String schemaName = "nicko";

    public SQLStorageProvider(NickoBukkit instance) {
        this.instance = instance;
    }

    @Override
    public boolean init() {
        try {
            final NickoConfiguration config = instance.getNickoConfig();
            dataSource = new MariaDbDataSource();
            dataSource.setUrl("jdbc:mariadb://" + config.getSQLAddress());
            dataSource.setUser(config.getSQLUsername());
            dataSource.setPassword(config.getSQLPassword());
            connection = dataSource.getConnection();
            final boolean initialized = connection != null && !connection.isClosed();

            if (!initialized) return false;

            instance.getLogger().info("Creating SQL database...");
            createDatabase();

            instance.getLogger().info("Creating SQL table...");
            createTable();
            return true;
        } catch (SQLException e) {
            instance.getLogger().severe("Couldn't establish a connection to the MySQL database: " + e.getMessage());
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

        final String query = """
                CREATE TABLE IF NOT EXISTS %s.DATA (
                uuid binary(16) NOT NULL,
                name varchar(16) NOT NULL,
                skin varchar(16) NOT NULL,
                bungeecord boolean NOT NULL,
                PRIMARY KEY (UUID)
                )
                """.formatted(schemaName);

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

        final String query = """
                CREATE DATABASE IF NOT EXISTS %s
                """.formatted(schemaName);

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
