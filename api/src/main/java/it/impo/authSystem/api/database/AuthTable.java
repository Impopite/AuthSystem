package it.impo.authSystem.api.database;

import it.impo.authSystem.api.data.PlayerData;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class AuthTable {

    public abstract void createTable() throws SQLException;

    public abstract CompletableFuture<Boolean> registerPlayer(String username, UUID uuid, String passwordHash, String ip);

    public abstract CompletableFuture<Boolean> unregisterPlayer(String username);

    public abstract CompletableFuture<Boolean> updatePassword(String username, String newPasswordHash);

    public abstract CompletableFuture<Boolean> updateLastIp(String username, String newIp);

    public abstract CompletableFuture<Boolean> setPremium(String username, boolean isPremium);

    public abstract CompletableFuture<Optional<PlayerData>> getPlayerByName(String username);

    public abstract CompletableFuture<Optional<PlayerData>> getPlayerByUuid(UUID uuid);

    public abstract CompletableFuture<Boolean> isRegistered(String username);

    public abstract CompletableFuture<Optional<String>> getLastIp(String username);

    public abstract CompletableFuture<Boolean> isPremium(UUID uuid);
}
