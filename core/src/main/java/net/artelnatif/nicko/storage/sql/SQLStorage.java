package net.artelnatif.nicko.storage.sql;

import net.artelnatif.nicko.Nicko;
import net.artelnatif.nicko.bukkit.i18n.I18NDict;
import net.artelnatif.nicko.bukkit.i18n.Locale;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.storage.Storage;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.sql.*;
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
            final String sql = "INSERT IGNORE INTO nicko.DATA (`uuid`, `name`, `skin`, `locale`, `bungeecord`) VALUES (?, ?, ?, ?, ?)";

            final PreparedStatement statement = connection.prepareStatement(sql);
            statement.setBinaryStream(1, uuidToBin(uuid));
            statement.setString(2, profile.getName() == null ? null : profile.getName());
            statement.setString(3, profile.getSkin() == null ? null : profile.getSkin());
            statement.setString(4, profile.getLocale().getCode());
            statement.setBoolean(5, profile.isBungeecordTransfer());
            statement.executeUpdate();
            return new ActionResult<>();
        } catch (SQLException e) {
            nicko.getLogger().warning("Couldn't send SQL Request: " + e.getMessage());
            return new ActionResult<>(I18NDict.Error.SQL_ERROR);
        }
    }

    @Override
    public boolean isStored(UUID uuid) {
        final Connection connection = getProvider().getConnection();
        if (connection == null) return false;

        try {
            final String sql = "SELECT * FROM nicko.DATA WHERE uuid = ?";

            final PreparedStatement statement = connection.prepareStatement(sql);
            statement.setBinaryStream(1, uuidToBin(uuid));

            final ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            nicko.getLogger().warning("Couldn't check if data is present: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<NickoProfile> retrieve(UUID uuid) {
        final Connection connection = getProvider().getConnection();
        if (connection == null) return Optional.empty();

        try {
            final String sql = "SELECT * FROM nicko.DATA WHERE uuid = ?";

            final PreparedStatement statement = connection.prepareStatement(sql);
            statement.setBinaryStream(1, uuidToBin(uuid));

            final ResultSet resultSet = statement.executeQuery();
            String name = "";
            String skin = "";
            String locale = "";
            boolean bungeecord = false;
            while (resultSet.next()) {
                name = resultSet.getString("name");
                skin = resultSet.getString("skin");
                locale = resultSet.getString("locale");
                bungeecord = resultSet.getBoolean("bungeecord");
            }

            final NickoProfile profile = new NickoProfile(name, skin, Locale.fromCode(locale), bungeecord);
            return Optional.of(profile);
        } catch (SQLException e) {
            nicko.getLogger().warning("Couldn't fetch profile: " + e.getMessage());
            return Optional.empty();
        }
    }

    private ByteArrayInputStream uuidToBin(UUID uuid) {
        byte[] bytes = new byte[16];
        ByteBuffer.wrap(bytes)
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits());
        return new ByteArrayInputStream(bytes);
    }
}
