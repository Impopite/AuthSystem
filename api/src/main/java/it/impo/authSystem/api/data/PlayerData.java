package it.impo.authSystem.api.data;

import org.jspecify.annotations.NonNull;

import java.sql.Timestamp;
import java.util.UUID;

public record PlayerData(String username, UUID uuid, String hashedPassword, String lastIp, Timestamp registeredAt, boolean premium) {

    @Override
    public @NonNull String toString() {
        return "PlayerData {username='" + username + "', uuid=" + uuid + ", lastIp='" + lastIp + "', registeredAt=" + registeredAt + ", premium=" + premium + '}';
    }
}
