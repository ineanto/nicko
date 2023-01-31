package net.artelnatif.nicko.storage.sql;

import com.google.common.io.ByteStreams;
import net.artelnatif.nicko.Nicko;
import net.artelnatif.nicko.bukkit.i18n.I18NDict;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.storage.Storage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class SQLStorage extends Storage {
    private final Nicko nicko;

    private SQLStorageProvider provider;

    public SQLStorage(Nicko nicko) {
        this.nicko = nicko;
    }

    @Override
    public SQLStorageProvider getProvider() {
        if (provider == null) {
            provider = new SQLStorageProvider(nicko);
        }
        return provider;
    }

    @Override
    public ActionResult<Void> store(UUID uuid, NickoProfile profile) {
        final Connection connection = getProvider().getConnection();
        if (connection == null) return new ActionResult<>(I18NDict.Error.SQL_ERROR);

        try {
            final String sql = "INSERT IGNORE INTO nicko.DATA (`uuid`, `name`, `skin`, `bungeecord`) VALUES (?, ?, ?, ?)";

            final PreparedStatement statement = connection.prepareStatement(sql);
            statement.setBinaryStream(1, uuidToBin(uuid));
            statement.setString(2, profile.getName());
            statement.setString(3, profile.getSkin());
            statement.setBoolean(4, profile.isBungeecordTransfer());
            statement.executeUpdate();
            return new ActionResult<>();
        } catch (SQLException e) {
            nicko.getLogger().warning("Couldn't send SQL Request: " + e.getMessage());
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

    private ByteArrayInputStream uuidToBin(UUID uuid) {
        byte[] bytes = new byte[16];
        ByteBuffer.wrap(bytes)
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits());
        return new ByteArrayInputStream(bytes);
    }

    private UUID binToUUID(InputStream stream) {
        final ByteBuffer buffer = ByteBuffer.allocate(16);
        try {
            buffer.put(ByteStreams.toByteArray(stream));
            buffer.flip();
            return new UUID(buffer.getLong(), buffer.getLong());
        } catch (IOException ignored) { return null; }
    }
}
