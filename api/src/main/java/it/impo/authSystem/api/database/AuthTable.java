package it.impo.authSystem.api.database;

import it.impo.authSystem.api.data.PlayerData;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Abstract database table for storing and retrieving player authentication data.
 * <p>
 * All query methods return {@link CompletableFuture} to support async execution.
 * Implementations should use a connection pool (e.g. HikariCP) and never block
 * the calling thread.
 *
 * @see PlayerData
 * @see it.impo.authSystem.api.AuthSystemApi#getAuthTable()
 */
public abstract class AuthTable {

    /**
     * Creates the {@code auth_players} table if it does not already exist.
     * <p>
     * Should be called once during plugin startup.
     *
     * @throws SQLException if the database operation fails
     */
    public abstract void createTable() throws SQLException;

    /**
     * Registers a new player in the database.
     *
     * @param username the player's name (stored in lowercase)
     * @param uuid the player's unique ID
     * @param passwordHash the BCrypt-hashed password
     * @param ip the player's IP address at registration time
     * @return a {@link CompletableFuture} that completes with {@code true} if the row was inserted
     */
    public abstract CompletableFuture<Boolean> registerPlayer(String username, UUID uuid, String passwordHash, String ip);

    /**
     * Removes a player from the database by UUID.
     *
     * @param uuid the unique ID of the player to remove
     * @return a {@link CompletableFuture} that completes with {@code true} if the row was deleted
     */
    public abstract CompletableFuture<Boolean> unregisterPlayer(UUID uuid);

    /**
     * Updates the stored password hash for a player.
     *
     * @param uuid the unique ID of the player
     * @param newPasswordHash the new BCrypt-hashed password
     * @return a {@link CompletableFuture} that completes with {@code true} if the row was updated
     */
    public abstract CompletableFuture<Boolean> updatePassword(UUID uuid, String newPasswordHash);

    /**
     * Updates the last known IP address for a player.
     *
     * @param uuid the unique ID of the player
     * @param newIp the new IP address to store
     * @return a {@link CompletableFuture} that completes with {@code true} if the row was updated
     */
    public abstract CompletableFuture<Boolean> updateLastIp(UUID uuid, String newIp);

    /**
     * Sets or unsets the premium (online-mode) flag for a player.
     *
     * @param uuid the unique ID of the player
     * @param isPremium {@code true} to enable premium, {@code false} to disable
     * @return a {@link CompletableFuture} that completes with {@code true} if the row was updated
     */
    public abstract CompletableFuture<Boolean> setPremium(UUID uuid, boolean isPremium);

    /**
     * Looks up a player by their username.
     *
     * @param username the player's name (matched in lowercase)
     * @return a {@link CompletableFuture} that completes with an {@link Optional} containing
     *         the {@link PlayerData} if found, or empty otherwise
     */
    public abstract CompletableFuture<Optional<PlayerData>> getPlayerByName(String username);

    /**
     * Looks up a player by their UUID.
     *
     * @param uuid the player's unique ID
     * @return a {@link CompletableFuture} that completes with an {@link Optional} containing
     *         the {@link PlayerData} if found, or empty otherwise
     */
    public abstract CompletableFuture<Optional<PlayerData>> getPlayerByUuid(UUID uuid);

    /**
     * Checks whether a player is registered in the database.
     *
     * @param uuid the unique ID of the player
     * @return a {@link CompletableFuture} that completes with {@code true} if the player exists
     */
    public abstract CompletableFuture<Boolean> isRegistered(UUID uuid);

    /**
     * Retrieves the last known IP address of a player.
     *
     * @param uuid the unique ID of the player
     * @return a {@link CompletableFuture} that completes with an {@link Optional} containing
     *         the IP address string if found, or empty otherwise
     */
    public abstract CompletableFuture<Optional<String>> getLastIp(UUID uuid);

    /**
     * Checks whether a player has premium (online-mode) authentication enabled.
     *
     * @param uuid the unique ID of the player
     * @return a {@link CompletableFuture} that completes with {@code true} if the player is premium
     */
    public abstract CompletableFuture<Boolean> isPremium(UUID uuid);
}
