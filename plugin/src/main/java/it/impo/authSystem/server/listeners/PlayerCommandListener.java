package it.impo.authSystem.server.listeners;

import it.impo.authSystem.AuthSystem;
import it.impo.authSystem.config.constant.LangKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Set;

public class PlayerCommandListener implements Listener {

    private static final Set<String> ALLOWED_COMMANDS = Set.of("/login", "/register");

    private final AuthSystem plugin;

    public PlayerCommandListener(AuthSystem plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (plugin.getAuthManager().isAuthenticated(event.getPlayer().getUniqueId())) return;

        String message = event.getMessage().toLowerCase();
        String baseCommand = message.contains(" ") ? message.substring(0, message.indexOf(' ')) : message;

        if (ALLOWED_COMMANDS.contains(baseCommand)) return;

        plugin.getLangLoader().send(event.getPlayer(), LangKey.ACTION_BEFORE_LOGIN);
        event.setCancelled(true);
    }
}
