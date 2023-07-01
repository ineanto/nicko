package xyz.atnrch.nicko.storage.sql;

import xyz.atnrch.nicko.config.Configuration;
import xyz.atnrch.nicko.disguise.ActionResult;
import xyz.atnrch.nicko.disguise.NickoProfile;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.i18n.Locale;
import xyz.atnrch.nicko.storage.Storage;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class SQLStorage extends Storage {
    private final Logger logger = Logger.getLogger("SQLStorage");
    private final Configuration configuration;

    private SQLStorageProvider provider;

    public SQLStorage(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SQLStorageProvider getProvider() {
        if (provider == null) {
            provider = new SQLStorageProvider(configuration);
        }
        return provider;
    }

    @Override
    public ActionResult store(UUID uuid, NickoProfile profile) {
        final Connection connection = getProvider().getConnection();
        if (connection == null) return ActionResult.error(I18NDict.Error.SQL_ERROR);

        try {
            final PreparedStatement statement = isStored(uuid) ?
                    getUpdateStatement(connection, uuid, profile) : getInsertStatement(connection, uuid, profile);
            statement.executeUpdate();
            return ActionResult.ok();
        } catch (SQLException e) {
            logger.warning("Couldn't send SQL Request: " + e.getMessage());
            return ActionResult.error(I18NDict.Error.SQL_ERROR);
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
            logger.warning("Couldn't check if data is present: " + e.getMessage());
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
            logger.warning("Couldn't fetch profile: " + e.getMessage());
            return Optional.empty();
        }
    }

    private PreparedStatement getInsertStatement(Connection connection, UUID uuid, NickoProfile profile) throws SQLException {
        final String sql = "INSERT IGNORE INTO nicko.DATA (`uuid`, `name`, `skin`, `locale`, `bungeecord`) VALUES (?, ?, ?, ?, ?)";
        final PreparedStatement statement = connection.prepareStatement(sql);
        statement.setBinaryStream(1, uuidToBin(uuid));
        statement.setString(2, profile.getName() == null ? null : profile.getName());
        statement.setString(3, profile.getSkin() == null ? null : profile.getSkin());
        statement.setString(4, profile.getLocale().getCode());
        statement.setBoolean(5, profile.isBungeecordTransfer());
        return statement;
    }

    private PreparedStatement getUpdateStatement(Connection connection, UUID uuid, NickoProfile profile) throws SQLException {
        final String sql = "UPDATE nicko.DATA SET name = ?, skin = ?, locale = ?, bungeecord = ? WHERE uuid = ?";
        final PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, profile.getName() == null ? null : profile.getName());
        statement.setString(2, profile.getSkin() == null ? null : profile.getSkin());
        statement.setString(3, profile.getLocale().getCode());
        statement.setBoolean(4, profile.isBungeecordTransfer());
        statement.setBinaryStream(5, uuidToBin(uuid));
        return statement;
    }

    private ByteArrayInputStream uuidToBin(UUID uuid) {
        byte[] bytes = new byte[16];
        ByteBuffer.wrap(bytes)
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits());
        return new ByteArrayInputStream(bytes);
    }
}
