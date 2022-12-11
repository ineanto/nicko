package net.artelnatif.nicko.storage.sql;

import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.config.NickoConfiguration;
import net.artelnatif.nicko.storage.StorageProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLStorageProvider implements StorageProvider {
    private final NickoBukkit instance;
    private Connection connection;

    private final String schemaName = "nicko";
    private final String tableName = "DATA";

    public SQLStorageProvider(NickoBukkit instance) {
        this.instance = instance;
    }

    @Override
    public boolean init() {
        try {
            final NickoConfiguration config = instance.getNickoConfig();
            connection = DriverManager.getConnection("jdbc:mariadb://" + config.getSQLAddress(), config.getSQLUsername(), config.getSQLPassword());
            final boolean initialized = connection != null && !connection.isClosed();

            if (initialized) {
                instance.getLogger().info("Creating SQL database...");
                createDatabase();

                instance.getLogger().info("Creating SQL table...");
                createTable();
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public boolean close() {
        if(connection == null) { return true; }
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
                CREATE TABLE IF NOT EXISTS %s.%s (
                uuid uuid NOT NULL,
                name varchar(16) NOT NULL,
                skin varchar(16) NOT NULL,
                bungeecord boolean NOT NULL,
                PRIMARY KEY (UUID)
                )
                """.formatted(schemaName, tableName);

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
}
