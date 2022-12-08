package net.artelnatif.nicko.storage.sql;

import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.storage.Storage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class SQLStorage extends Storage {
    private final NickoBukkit instance;

    public SQLStorage(NickoBukkit instance) {
        this.instance = instance;
    }

    @Override
    public SQLStorageProvider getProvider() {
        return new SQLStorageProvider(instance);
    }

    @Override
    public void store(UUID uuid, NickoProfile profile) {
        final Connection connection = getProvider().getConnection();
        try {
            connection.prepareStatement("");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isStored(UUID uuid) {
        return false;
    }

    @Override
    public Optional<NickoProfile> retrieve(UUID uuid) {
        return Optional.empty();
    }
}
