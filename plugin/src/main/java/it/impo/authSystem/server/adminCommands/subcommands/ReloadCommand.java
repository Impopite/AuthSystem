package it.impo.authSystem.server.adminCommands.subcommands;

import dev.jorel.commandapi.CommandAPICommand;
import it.impo.authSystem.AuthSystem;
import it.impo.authSystem.config.constant.LangKey;
import it.impo.authSystem.manager.BaseAuthManager;
import it.impo.authSystem.utils.Permission;

public class ReloadCommand {

    private final AuthSystem plugin;

    public ReloadCommand(AuthSystem plugin) {
        this.plugin = plugin;
    }

    public CommandAPICommand get() {
        return new CommandAPICommand("reload")
                .executesPlayer((player, args) -> {
                    if(!player.hasPermission(Permission.ADMIN.getPermission()) || !player.hasPermission(Permission.RELOAD.getPermission())) {
                        plugin.getLangLoader().send(player, LangKey.NO_PERMISSION);
                        return;
                    }

                    plugin.getConfigLoader().reload();
                    ((BaseAuthManager) plugin.getAuthManager()).reload();
                    plugin.getLangLoader().send(player, LangKey.RELOAD_SUCCESS);
                });
    }
}