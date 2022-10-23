package net.artelnatif.nicko.storage.sql;

import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.config.NickoConfiguration;
import net.artelnatif.nicko.storage.StorageProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
            connection = DriverManager.getConnection("jdbc://" + config.getStorageAddress(), config.getStorageUsername(), config.getStoragePassword());
            return !connection.isClosed() && connection != null;
        } catch (SQLException e) {
            return false;
        }
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
}
