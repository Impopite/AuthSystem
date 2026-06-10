package it.impo.authSystem.server.listeners;

import it.impo.authSystem.AuthSystem;
import it.impo.authSystem.config.constant.LangKey;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {

    private final AuthSystem plugin;

    public PlayerJoinQuitListener(AuthSystem plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getAuthManager().onJoin(player);

        int timeoutSeconds = plugin.getConfig().getInt("auth.timeout-seconds", 60);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!player.isOnline()) return;
            if (plugin.getAuthManager().isWaitingAuth(player.getUniqueId())) {
                player.kick(plugin.getLangLoader().get(LangKey.FAILED_AUTH_TIME));
            }
        }, 20L * timeoutSeconds);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        plugin.getAuthManager().onQuit(event.getPlayer());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (plugin.getAuthManager().isAuthenticated(event.getPlayer().getUniqueId())) return;
        event.setCancelled(true);
    }
}
