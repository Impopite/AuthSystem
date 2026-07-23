package it.impo.authSystem.api.data;

import org.jspecify.annotations.NonNull;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Immutable data record representing a registered player in the authentication database.
 *
 * @param username the player's in-game name (stored in lowercase)
 * @param uuid the player's unique ID
 * @param hashedPassword the BCrypt-hashed password
 * @param lastIp the last known IP address
 * @param registeredAt the timestamp when the player registered
 * @param premium whether the player has premium (online-mode) authentication enabled
 * @see it.impo.authSystem.api.database.AuthTable#getPlayerByName(String)
 * @see it.impo.authSystem.api.database.AuthTable#getPlayerByUuid(UUID)
 */
public record PlayerData(String username, UUID uuid, String hashedPassword, String lastIp, Timestamp registeredAt, boolean premium) {

    /**
     * Returns a string representation of this player's data.
     * <p>
     * The password hash is excluded from the output for security.
     *
     * @return a formatted string containing username, UUID, last IP, registration time, and premium status
     */
    @Override
    public @NonNull String toString() {
        return "PlayerData {username='" + username + "', uuid=" + uuid + ", lastIp='" + lastIp + "', registeredAt=" + registeredAt + ", premium=" + premium + '}';
    }
}
