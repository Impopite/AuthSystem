package it.impo.authSystem.database;

import com.zaxxer.hikari.HikariDataSource;
import it.impo.authSystem.api.data.PlayerData;
import it.impo.authSystem.api.database.AuthTable;
import org.intellij.lang.annotations.Language;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class BaseAuthTable extends AuthTable {

    private final HikariDataSource dataSource;

    @Language("SQL")
    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS auth_players (
                id            INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
                username      VARCHAR(16)  NOT NULL,
                uuid          VARCHAR(36)  NOT NULL,
                password_hash VARCHAR(60)  NOT NULL,
                last_ip       VARCHAR(45)  NOT NULL,
                registered_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                premium       TINYINT(1)   NOT NULL DEFAULT 0,
                UNIQUE KEY uq_username (username),
                UNIQUE KEY uq_uuid     (uuid)
            );
            """;

    @Language("SQL")
    private static final String INSERT_PLAYER =
            "INSERT INTO auth_players (username, uuid, password_hash, last_ip) VALUES (?, ?, ?, ?);";

    @Language("SQL")
    private static final String DELETE_PLAYER =
            "DELETE FROM auth_players WHERE uuid = ?;";

    @Language("SQL")
    private static final String UPDATE_PASSWORD =
            "UPDATE auth_players SET password_hash = ? WHERE uuid = ?;";

    @Language("SQL")
    private static final String UPDATE_LAST_IP =
            "UPDATE auth_players SET last_ip = ? WHERE uuid = ?;";

    @Language("SQL")
    private static final String UPDATE_PREMIUM =
            "UPDATE auth_players SET premium = ? WHERE uuid = ?;";

    @Language("SQL")
    private static final String SELECT_BY_NAME =
            "SELECT * FROM auth_players WHERE username = ?;";

    @Language("SQL")
    private static final String SELECT_BY_UUID =
            "SELECT * FROM auth_players WHERE uuid = ?;";

    @Language("SQL")
    private static final String SELECT_EXISTS =
            "SELECT 1 FROM auth_players WHERE uuid = ? LIMIT 1;";

    @Language("SQL")
    private static final String SELECT_LAST_IP =
            "SELECT last_ip FROM auth_players WHERE uuid = ?;";

    @Language("SQL")
    private static final String SELECT_PREMIUM =
            "SELECT premium FROM auth_players WHERE uuid = ?;";

    public BaseAuthTable(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void createTable() throws SQLException {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(CREATE_TABLE)) {
            ps.execute();
        }
    }

    @Override
    public CompletableFuture<Boolean> registerPlayer(String username, UUID uuid, String passwordHash, String ip) {
        return supplyAsync(() -> {
            try (Connection c = dataSource.getConnection();
                 PreparedStatement ps = c.prepareStatement(INSERT_PLAYER)) {

                ps.setString(1, username.toLowerCase());
                ps.setString(2, uuid.toString());
                ps.setString(3, passwordHash);
                ps.setString(4, ip);
                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> unregisterPlayer(UUID uuid) {
        return supplyAsync(() -> {
            try (Connection c = dataSource.getConnection();
                 PreparedStatement ps = c.prepareStatement(DELETE_PLAYER)) {

                ps.setString(1, uuid.toString());
                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> updatePassword(UUID uuid, String newPasswordHash) {
        return supplyAsync(() -> {
            try (Connection c = dataSource.getConnection();
                 PreparedStatement ps = c.prepareStatement(UPDATE_PASSWORD)) {

                ps.setString(1, newPasswordHash);
                ps.setString(2, uuid.toString());
                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> updateLastIp(UUID uuid, String newIp) {
        return supplyAsync(() -> {
            try (Connection c = dataSource.getConnection();
                 PreparedStatement ps = c.prepareStatement(UPDATE_LAST_IP)) {

                ps.setString(1, newIp);
                ps.setString(2, uuid.toString());
                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> setPremium(UUID uuid, boolean isPremium) {
        return supplyAsync(() -> {
            try (Connection c = dataSource.getConnection();
                 PreparedStatement ps = c.prepareStatement(UPDATE_PREMIUM)) {

                ps.setBoolean(1, isPremium);
                ps.setString(2, uuid.toString());
                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Optional<PlayerData>> getPlayerByName(String username) {
        return supplyAsync(() -> {
            try (Connection c = dataSource.getConnection();
                 PreparedStatement ps = c.prepareStatement(SELECT_BY_NAME)) {

                ps.setString(1, username.toLowerCase());
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) return Optional.empty();
                return Optional.of(playerDataFromResult(rs));

            } catch (SQLException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        });
    }

    @Override
    public CompletableFuture<Optional<PlayerData>> getPlayerByUuid(UUID uuid) {
        return supplyAsync(() -> {
            try (Connection c = dataSource.getConnection();
                 PreparedStatement ps = c.prepareStatement(SELECT_BY_UUID)) {

                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) return Optional.empty();
                return Optional.of(playerDataFromResult(rs));

            } catch (SQLException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> isRegistered(UUID uuid) {
        return supplyAsync(() -> {
            try (Connection c = dataSource.getConnection();
                 PreparedStatement ps = c.prepareStatement(SELECT_EXISTS)) {

                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                return rs.next();

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Optional<String>> getLastIp(UUID uuid) {
        return supplyAsync(() -> {
            try (Connection c = dataSource.getConnection();
                 PreparedStatement ps = c.prepareStatement(SELECT_LAST_IP)) {

                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) return Optional.empty();
                return Optional.of(rs.getString("last_ip"));

            } catch (SQLException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> isPremium(UUID uuid) {
        return supplyAsync(() -> {
            try (Connection c = dataSource.getConnection();
                 PreparedStatement ps = c.prepareStatement(SELECT_PREMIUM)) {

                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) return false;
                return rs.getBoolean("premium");

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    private PlayerData playerDataFromResult(ResultSet rs) throws SQLException {
        return new PlayerData(
                rs.getString("username"),
                UUID.fromString(rs.getString("uuid")),
                rs.getString("password_hash"),
                rs.getString("last_ip"),
                rs.getTimestamp("registered_at"),
                rs.getBoolean("premium")
        );
    }

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    public static boolean checkPassword(String plainPassword, String storedHash) {
        return BCrypt.checkpw(plainPassword, storedHash);
    }
}