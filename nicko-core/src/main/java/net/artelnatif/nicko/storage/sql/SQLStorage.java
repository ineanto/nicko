package net.artelnatif.nicko.storage.sql;

import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.i18n.I18NDict;
import net.artelnatif.nicko.storage.Storage;

import java.sql.*;
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
            final String sql = """ 
                    INSERT INTO nicko.DATA
                    (uuid, name, skin, bungeecord)
                    VALUES
                    (?, ?, ?, ?)
                    ON DUPLICATE KEY UPDATE uuid = %s
                    """.formatted(uuid.toString());

            final PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(0, uuid);
            statement.setString(1, profile.getName());
            statement.setString(2, profile.getSkin());
            statement.setBoolean(3, profile.isBungeecordTransfer());
            return new ActionResult<>();
        } catch (SQLException e) {
            instance.getLogger().warning("Unable to store player.");
            return new ActionResult<>(I18NDict.Error.SQL_ERROR);
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
