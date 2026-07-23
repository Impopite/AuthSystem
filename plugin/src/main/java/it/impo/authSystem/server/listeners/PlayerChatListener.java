package it.impo.authSystem.server.listeners;

import it.impo.authSystem.AuthSystem;
import it.impo.authSystem.config.constant.LangKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    private final AuthSystem plugin;

    public PlayerChatListener(AuthSystem plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        if (!plugin.getAuthManager().isAuthenticated(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            plugin.getLangLoader().send(event.getPlayer(), LangKey.ACTION_BEFORE_LOGIN);
        }
    }
}
