package net.artelnatif.nicko.storage.sql;

import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.config.NickoConfiguration;
import net.artelnatif.nicko.storage.StorageProvider;

import java.sql.*;

public class SQLStorageProvider implements StorageProvider {
    private final NickoBukkit instance;
    private Connection connection;

    public SQLStorageProvider(NickoBukkit instance) {
        this.instance = instance;
    }

    @Override
    public boolean init() {
        try {
            final NickoConfiguration config = instance.getNickoConfig();
            connection = DriverManager.getConnection("jdbc://" + config.getSQLAddress(), config.getSQLUsername(), config.getSQLPassword());
            final boolean initialized = connection != null && !connection.isClosed();

            if (initialized) {
                if (!doesTableExist()) {
                    instance.getLogger().info("Creating SQL tables...");
                    return createTables();
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public boolean close() {
        try {
            connection.close();
            return connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean createTables() {
        final Connection connection = getConnection();

        final String query = """
                CREATE TABLE IF NOT EXISTS 'NICKO' (
                uuid uuid NOT NULL,
                name varchar(16) NOT NULL,
                skin varchar(16) NOT NULL,
                bungeecord boolean NOT NULL,
                PRIMARY KEY (UUID)
                )
                """;

        try {
            final PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            return result.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean doesTableExist() {
        final Connection connection = getConnection();

        final String query = "SELECT UUID FROM 'NICKO'";

        try {
            final PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            return result.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
