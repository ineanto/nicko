package net.artelnatif.nicko.storage.sql;

import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.i18n.I18NDict;
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
    public ActionResult<Void> store(UUID uuid, NickoProfile profile) {
        final Connection connection = getProvider().getConnection();
        try {
            connection.prepareStatement("");
        } catch (SQLException e) {
            instance.getLogger().warning("Unable to store player.");
            return new ActionResult<>(I18NDict.Error.UNEXPECTED_ERROR);
        }

        return new ActionResult<>();
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
