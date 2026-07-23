package it.impo.authSystem.manager;

import com.destroystokyo.paper.profile.PlayerProfile;
import it.impo.authSystem.AuthSystem;
import it.impo.authSystem.api.manager.AuthManager;
import it.impo.authSystem.config.LangLoader;
import it.impo.authSystem.config.constant.ConfigKey;
import it.impo.authSystem.config.constant.LangKey;
import it.impo.authSystem.database.BaseAuthTable;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class BaseAuthManager extends AuthManager {

    private static final int MAX_ATTEMPTS = 5;
    private static final int MAX_IP_ATTEMPTS = 10;
    private static final long PREMIUM_CLICK_EXPIRE_MS = 10_000L;

    private final AuthSystem plugin;
    private LangLoader lang;

    private final Set<UUID> authenticated = ConcurrentHashMap.newKeySet();
    private final Set<UUID> waitingAuth = ConcurrentHashMap.newKeySet();

    private final Map<UUID, Integer> failedAttempts = new ConcurrentHashMap<>();
    private final Map<String, Integer> ipFailedAttempts = new ConcurrentHashMap<>();
    private final Map<UUID, Long> premiumFirstClick = new ConcurrentHashMap<>();

    public BaseAuthManager(AuthSystem plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLangLoader();
    }

    public void reload() {
        this.lang = plugin.getLangLoader();
    }

    @Override
    public void onJoin(Player player) {
        UUID uuid = player.getUniqueId();

        plugin.getAuthTable().getPlayerByUuid(uuid).thenAccept(opt -> {
            if (opt.isEmpty()) {
                waitingAuth.add(uuid);
                sync(() -> lang.send(player, LangKey.NEED_REGISTER));
                return;
            }

            String ip = player.getAddress() != null
                    ? player.getAddress().getAddress().getHostAddress()
                    : "unknown";

            if (opt.get().premium()) {
                authenticated.add(uuid);
                plugin.getAuthTable().updateLastIp(uuid, ip);
                sync(() -> lang.send(player, LangKey.LOGIN_PREMIUM));
                return;
            }

            waitingAuth.add(uuid);
            sync(() -> lang.send(player, LangKey.LOGIN));
        });
    }

    @Override
    public void onQuit(Player player) {
        UUID uuid = player.getUniqueId();
        authenticated.remove(uuid);
        waitingAuth.remove(uuid);
        failedAttempts.remove(uuid);
        premiumFirstClick.remove(uuid);
    }

    @Override
    public void register(Player player, String password, String confirmPassword) {
        UUID uuid = player.getUniqueId();

        if (!waitingAuth.contains(uuid)) {
            lang.send(player, LangKey.ALREADY_REGISTERED);
            return;
        }

        if (!password.equals(confirmPassword)) {
            lang.send(player, LangKey.PASSWORD_MISMATCH);
            return;
        }

        if (password.length() < 6) {
            lang.send(player, LangKey.PASSWORD_TOO_SHORT);
            return;
        }

        String ip = player.getAddress() != null
                ? player.getAddress().getAddress().getHostAddress()
                : "unknown";

        String hash = BaseAuthTable.hashPassword(password);

        plugin.getAuthTable().registerPlayer(player.getName(), uuid, hash, ip).thenAccept(success -> {
            if (!success) {
                sync(() -> lang.send(player, LangKey.FAILED_REGISTRATION));
                return;
            }

            waitingAuth.remove(uuid);
            authenticated.add(uuid);
            sync(() -> lang.send(player, LangKey.REGISTERED));
        });
    }

    @Override
    public void login(Player player, String password) {
        UUID uuid = player.getUniqueId();

        if (!waitingAuth.contains(uuid)) {
            lang.send(player, LangKey.ALREADY_LOGGED_IN);
            return;
        }

        String ip = player.getAddress() != null
                ? player.getAddress().getAddress().getHostAddress()
                : "unknown";

        int ipAttempts = ipFailedAttempts.getOrDefault(ip, 0);
        if (ipAttempts >= MAX_IP_ATTEMPTS) {
            sync(() -> lang.send(player, LangKey.TOO_MUCH_FAILED_TRY));
            return;
        }

        plugin.getAuthTable().getPlayerByUuid(uuid).thenAccept(opt -> {
            if (opt.isEmpty()) {
                sync(() -> lang.send(player, LangKey.NEED_REGISTER));
                return;
            }

            if (!BaseAuthTable.checkPassword(password, opt.get().hashedPassword())) {
                int attempts = failedAttempts.merge(uuid, 1, Integer::sum);
                ipFailedAttempts.merge(ip, 1, Integer::sum);
                int remaining = MAX_ATTEMPTS - attempts;

                if (remaining <= 0) {
                    sync(() -> {
                        lang.send(player, LangKey.TOO_MUCH_FAILED_TRY);
                        String action = plugin.getConfigLoader().get(ConfigKey.TOO_MANY_ATTEMPTS, "kick");
                        if (action.equalsIgnoreCase("ban")) {
                            int banMinutes = plugin.getConfigLoader().get(ConfigKey.BAN_TIME, 60);
                            Date expiry = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(banMinutes));
                            BanList<PlayerProfile> banList = Bukkit.getBanList(BanList.Type.PROFILE);
                            banList.addBan(player.getPlayerProfile(), "Too many failed attempts", expiry, "AuthSystem");
                            player.kick(lang.get(LangKey.WRONG_PASSWORD_BAN));
                        } else {
                            player.kick(lang.get(LangKey.WRONG_PASSWORD_KICK));
                        }
                    });
                    return;
                }

                sync(() -> lang.send(player, LangKey.WRONG_PASSWORD,
                        Placeholder.parsed("remaining_try", String.valueOf(remaining))));
                return;
            }

            plugin.getAuthTable().updateLastIp(uuid, ip);

            waitingAuth.remove(uuid);
            authenticated.add(uuid);
            failedAttempts.remove(uuid);

            sync(() -> lang.send(player, LangKey.LOGGED_IN));
        });
    }

    @Override
    public void changePassword(Player player, String oldPassword, String newPassword) {
        if (!authenticated.contains(player.getUniqueId())) {
            lang.send(player, LangKey.ACTION_BEFORE_LOGIN);
            return;
        }

        if (newPassword.length() < 6) {
            lang.send(player, LangKey.NEW_PASSWORD_TOO_SHORT);
            return;
        }

        UUID uuid = player.getUniqueId();

        plugin.getAuthTable().getPlayerByUuid(uuid).thenAccept(opt -> {
            if (opt.isEmpty()) {
                sync(() -> lang.send(player, LangKey.PLAYER_NOT_FOUND_IN_DATABASE));
                return;
            }

            if (!BaseAuthTable.checkPassword(oldPassword, opt.get().hashedPassword())) {
                sync(() -> lang.send(player, LangKey.OLD_PASSWORD_IS_WRONG));
                return;
            }

            plugin.getAuthTable().updatePassword(uuid, BaseAuthTable.hashPassword(newPassword))
                    .thenAccept(success -> sync(() -> {
                        if (success) lang.send(player, LangKey.PASSWORD_CHANGED);
                        else lang.send(player, LangKey.ERROR_CHANGING_PASSWORD);
                    }));
        });
    }

    @Override
    public void adminChangePassword(Player admin, String targetName, String newPassword) {
        if (newPassword.length() < 6) {
            lang.send(admin, LangKey.NEW_PASSWORD_TOO_SHORT);
            return;
        }

        plugin.getAuthTable().getPlayerByName(targetName).thenAccept(opt -> sync(() -> {
            if (opt.isEmpty()) {
                lang.send(admin, LangKey.PLAYER_NOT_FOUND_IN_DATABASE);
                return;
            }

            UUID targetUuid = opt.get().uuid();
            plugin.getAuthTable().updatePassword(targetUuid, BaseAuthTable.hashPassword(newPassword))
                    .thenAccept(success -> sync(() -> {
                        if (success) lang.send(admin, LangKey.ADMIN_CHANGE_PASSWORD, Placeholder.parsed("target_name", targetName));
                        else lang.send(admin, LangKey.PLAYER_NOT_FOUND_IN_DATABASE);
                    }));
        }));
    }

    @Override
    public void unregister(Player admin, String targetName) {
        plugin.getAuthTable().getPlayerByName(targetName).thenAccept(opt -> {
            if (opt.isEmpty()) {
                sync(() -> lang.send(admin, LangKey.PLAYER_NOT_FOUND_IN_DATABASE));
                return;
            }

            UUID targetUuid = opt.get().uuid();
            plugin.getAuthTable().unregisterPlayer(targetUuid).thenAccept(success -> sync(() -> {
                if (!success) {
                    lang.send(admin, LangKey.PLAYER_NOT_FOUND_IN_DATABASE);
                    return;
                }

                Player target = Bukkit.getPlayerExact(targetName);
                if (target != null) {
                    authenticated.remove(target.getUniqueId());
                    waitingAuth.add(target.getUniqueId());
                    lang.send(target, LangKey.UNREGISTERED_BY_ADMIN);
                }
                lang.send(admin, LangKey.UNREGISTERED_ADMIN, Placeholder.parsed("target_name", targetName));
            }));
        });
    }

    @Override
    public void checkIp(Player admin, String targetName) {
        plugin.getAuthTable().getPlayerByName(targetName).thenAccept(opt -> {
            if (opt.isEmpty()) {
                sync(() -> lang.send(admin, LangKey.PLAYER_NOT_FOUND_IN_DATABASE));
                return;
            }

            UUID targetUuid = opt.get().uuid();
            plugin.getAuthTable().getLastIp(targetUuid).thenAccept(ipOpt -> sync(() -> {
                if (ipOpt.isEmpty()) lang.send(admin, LangKey.PLAYER_NOT_FOUND_IN_DATABASE);
                else lang.send(admin, LangKey.LAST_IP,
                        Placeholder.parsed("target_name", targetName),
                        Placeholder.parsed("last_ip", ipOpt.get()));
            }));
        });
    }

    @Override
    public void handlePremium(Player player) {
        if (!authenticated.contains(player.getUniqueId())) {
            lang.send(player, LangKey.ACTION_BEFORE_LOGIN);
            return;
        }

        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();

        Long firstClick = premiumFirstClick.get(uuid);

        if (firstClick == null || now - firstClick > PREMIUM_CLICK_EXPIRE_MS) {
            premiumFirstClick.put(uuid, now);
            lang.send(player, LangKey.CONFIRM_PREMIUM);
            return;
        }

        premiumFirstClick.remove(uuid);

        plugin.getAuthTable().getPlayerByUuid(uuid).thenAccept(opt -> {
            if (opt.isEmpty()) {
                sync(() -> lang.send(player, LangKey.PLAYER_NOT_FOUND_IN_DATABASE));
                return;
            }

            boolean newState = !opt.get().premium();
            plugin.getAuthTable().setPremium(uuid, newState)
                    .thenAccept(success -> sync(() -> {
                        if (!success) {
                            lang.send(player, LangKey.GENERIC_ERROR);
                            return;
                        }
                        if (newState) lang.send(player, LangKey.PREMIUM_CONFIRMED);
                        else lang.send(player, LangKey.PREMIUM_REMOVED);
                    }));
        });
    }

    @Override
    public boolean isAuthenticated(UUID uuid) {
        return authenticated.contains(uuid);
    }

    @Override
    public boolean isWaitingAuth(UUID uuid) {
        return waitingAuth.contains(uuid);
    }

    private void sync(Runnable runnable) {
        if (Bukkit.isPrimaryThread()) {
            runnable.run();
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }
}
