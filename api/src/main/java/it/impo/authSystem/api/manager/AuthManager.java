package it.impo.authSystem.api.manager;

import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class AuthManager {

    public abstract void onJoin(Player player);

    public abstract void onQuit(Player player);

    public abstract void register(Player player, String password, String confirmPassword);

    public abstract void login(Player player, String password);

    public abstract void changePassword(Player player, String oldPassword, String newPassword);

    public abstract void adminChangePassword(Player admin, String targetName, String newPassword);

    public abstract void unregister(Player admin, String targetName);

    public abstract void checkIp(Player admin, String targetName);

    public abstract void handlePremium(Player player);

    public abstract boolean isAuthenticated(UUID uuid);

    public abstract boolean isWaitingAuth(UUID uuid);
}
