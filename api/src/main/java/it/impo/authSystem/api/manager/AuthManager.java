package it.impo.authSystem.api.manager;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Abstract manager responsible for handling player authentication lifecycle.
 * <p>
 * Covers registration, login, password management, premium authentication,
 * and admin operations. Implementations should be thread-safe since methods
 * may be called from async contexts.
 *
 * @see it.impo.authSystem.api.AuthSystemApi#getAuthManager()
 */
public abstract class AuthManager {

    /**
     * Called when a player joins the server.
     * <p>
     * Checks whether the player is registered and sets the appropriate
     * authentication state (waiting for auth or auto-login for premium players).
     *
     * @param player the player who joined
     */
    public abstract void onJoin(Player player);

    /**
     * Called when a player leaves the server.
     * <p>
     * Cleans up all authentication state associated with the player,
     * including session data and failed attempt counters.
     *
     * @param player the player who left
     */
    public abstract void onQuit(Player player);

    /**
     * Registers a new player account with the given password.
     * <p>
     * The password is confirmed by comparing {@code password} and {@code confirmPassword}.
     * On success the player is marked as authenticated.
     *
     * @param player the player to register
     * @param password the chosen password
     * @param confirmPassword the password confirmation (must match {@code password})
     */
    public abstract void register(Player player, String password, String confirmPassword);

    /**
     * Authenticates a registered player with the given password.
     * <p>
     * On success the player is moved from the waiting state to authenticated.
     * Failed attempts are tracked and may result in a kick or ban
     * depending on the server configuration.
     *
     * @param player the player attempting to log in
     * @param password the password to verify
     */
    public abstract void login(Player player, String password);

    /**
     * Changes the password of an already authenticated player.
     * <p>
     * Requires the player to provide their current password for verification.
     *
     * @param player the authenticated player
     * @param oldPassword the current password (used for verification)
     * @param newPassword the new password to set (minimum 6 characters)
     */
    public abstract void changePassword(Player player, String oldPassword, String newPassword);

    /**
     * Allows an admin to reset another player's password.
     *
     * @param admin the admin performing the action
     * @param targetName the name of the target player
     * @param newPassword the new password to set (minimum 6 characters)
     */
    public abstract void adminChangePassword(Player admin, String targetName, String newPassword);

    /**
     * Removes a player's registration, resetting them to an unauthenticated state.
     * <p>
     * If the target player is online they will be moved back to the waiting-auth state.
     *
     * @param admin the admin performing the action
     * @param targetName the name of the player to unregister
     */
    public abstract void unregister(Player admin, String targetName);

    /**
     * Retrieves and displays the last known IP address of a player.
     *
     * @param admin the admin requesting the information
     * @param targetName the name of the player to look up
     */
    public abstract void checkIp(Player admin, String targetName);

    /**
     * Toggles premium (online-mode) authentication for the given player.
     * <p>
     * Requires a double-click confirmation within 10 seconds.
     * When enabled, the player will be automatically logged in on future joins
     * using their Mojang session verification.
     *
     * @param player the authenticated player toggling premium mode
     */
    public abstract void handlePremium(Player player);

    /**
     * Checks whether a player has successfully authenticated.
     *
     * @param uuid the unique ID of the player
     * @return {@code true} if the player is authenticated, {@code false} otherwise
     */
    public abstract boolean isAuthenticated(UUID uuid);

    /**
     * Checks whether a player is registered but not yet authenticated.
     *
     * @param uuid the unique ID of the player
     * @return {@code true} if the player is waiting for authentication, {@code false} otherwise
     */
    public abstract boolean isWaitingAuth(UUID uuid);
}
