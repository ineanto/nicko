package xyz.ineanto.nicko.storage.mysql;

import xyz.ineanto.nicko.appearance.ActionResult;
import xyz.ineanto.nicko.config.Configuration;
import xyz.ineanto.nicko.language.Language;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.ineanto.nicko.storage.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class MySQLStorage extends Storage {
    private final Logger logger = Logger.getLogger("SQLStorage");
    private final Configuration configuration;

    private MySQLStorageProvider provider;

    public MySQLStorage(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public MySQLStorageProvider getProvider() {
        if (provider == null) {
            provider = new MySQLStorageProvider(configuration);
        }
        return provider;
    }

    @Override
    public ActionResult store(UUID uuid, NickoProfile profile) {
        final Connection connection = getProvider().getConnection();
        if (connection == null) return ActionResult.error();

        try {
            final PreparedStatement statement = isStored(uuid) ?
                    getUpdateStatement(connection, uuid, profile) : getInsertStatement(connection, uuid, profile);
            statement.executeUpdate();
            return ActionResult.ok();
        } catch (SQLException e) {
            logger.warning("Couldn't send SQL Request: " + e.getMessage());
            return ActionResult.error();
        }
    }

    @Override
    public boolean isStored(UUID uuid) {
        final Connection connection = getProvider().getConnection();
        if (connection == null) return false;

        try {
            final String sql = "SELECT uuid FROM nicko.DATA WHERE uuid = ?";

            final PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid.toString());

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
        if (!isStored(uuid)) return Optional.empty();

        try {
            final String sql = "SELECT * FROM nicko.DATA WHERE uuid = ?";

            final PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid.toString());

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

            final NickoProfile profile = new NickoProfile(name, skin, Language.fromCode(locale), bungeecord);
            return Optional.of(profile);
        } catch (SQLException e) {
            logger.warning("Couldn't fetch profile: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public ActionResult delete(UUID uuid) {
        final Connection connection = getProvider().getConnection();
        if (connection == null) return ActionResult.error();

        try {
            final String sql = "DELETE FROM nicko.DATA WHERE uuid = ?";
            final PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid.toString());
            int rows = statement.executeUpdate();
            return (rows == 1 ? ActionResult.ok() : ActionResult.error());
        } catch (SQLException e) {
            logger.warning("Couldn't delete profile: " + e.getMessage());
            return ActionResult.error();
        }
    }

    private PreparedStatement getInsertStatement(Connection connection, UUID uuid, NickoProfile profile) throws SQLException {
        final String sql = "INSERT IGNORE INTO nicko.DATA (`uuid`, `name`, `skin`, `locale`, `bungeecord`) VALUES (?, ?, ?, ?, ?)";
        final PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, uuid.toString());
        statement.setString(2, profile.getName() == null ? null : profile.getName());
        statement.setString(3, profile.getSkin() == null ? null : profile.getSkin());
        statement.setString(4, profile.getLocale().getCode());
        statement.setBoolean(5, profile.isRandomSkin());
        return statement;
    }

    private PreparedStatement getUpdateStatement(Connection connection, UUID uuid, NickoProfile profile) throws SQLException {
        final String sql = "UPDATE nicko.DATA SET name = ?, skin = ?, locale = ?, bungeecord = ? WHERE uuid = ?";
        final PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, profile.getName() == null ? null : profile.getName());
        statement.setString(2, profile.getSkin() == null ? null : profile.getSkin());
        statement.setString(3, profile.getLocale().getCode());
        statement.setBoolean(4, profile.isRandomSkin());
        statement.setString(5, uuid.toString());
        return statement;
    }
}
