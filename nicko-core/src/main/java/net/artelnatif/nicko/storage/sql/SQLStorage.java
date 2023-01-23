package net.artelnatif.nicko.storage.sql;

import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.i18n.I18NDict;
import net.artelnatif.nicko.storage.Storage;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class SQLStorage extends Storage {
    private final NickoBukkit instance;

    private SQLStorageProvider provider;

    public SQLStorage(NickoBukkit instance) {
        this.instance = instance;
    }

    @Override
    public SQLStorageProvider getProvider() {
        if (provider == null) {
            provider = new SQLStorageProvider(instance);
        }
        return provider;
    }

    @Override
    public ActionResult<Void> store(UUID uuid, NickoProfile profile) {
        final Connection connection = getProvider().getConnection();
        try {
            final String sql = """ 
                    INSERT IGNORE INTO nicko.DATA
                    (`uuid`, `name`, `skin`, `bungeecord`)
                    VALUES
                    (?, ?, ?, ?)
                    """;

            final PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, uuidToBin(uuid));
            statement.setString(2, profile.getName());
            statement.setString(3, profile.getSkin());
            statement.setBoolean(4, profile.isBungeecordTransfer());
            statement.executeUpdate();
            return new ActionResult<>();
        } catch (SQLException e) {
            instance.getLogger().warning("Unable to store player. (%s)".formatted(e.getMessage()));
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

    private byte[] uuidToBin(UUID uuid) {
        final byte[] uuidBytes = new byte[16];
        final ByteBuffer buffer = ByteBuffer.wrap(uuidBytes)
                .order(ByteOrder.BIG_ENDIAN)
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits());
        return buffer.array();
    }

    private UUID binToUUID(byte[] array) {
        final ByteBuffer buffer = ByteBuffer.wrap(array);
        return new UUID(buffer.getLong(), buffer.getLong());
    }
}
