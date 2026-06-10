package it.impo.authSystem.server.adminCommands.subcommands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import it.impo.authSystem.AuthSystem;
import it.impo.authSystem.config.constant.LangKey;
import it.impo.authSystem.utils.Permission;

public class AdminChangePasswordCommand {

    private final AuthSystem plugin;

    public AdminChangePasswordCommand(AuthSystem plugin) {
        this.plugin = plugin;
    }

    public CommandAPICommand get() {
        return new CommandAPICommand("changepassword")
                .withArguments(new StringArgument("player"))
                .withArguments(new StringArgument("newPassword"))
                .executesPlayer((player, args) -> {
                    if(!player.hasPermission(Permission.ADMIN.getPermission()) || !player.hasPermission(Permission.ADMIN_CHANGE_PASSWORD.getPermission())) {
                        plugin.getLangLoader().send(player, LangKey.NO_PERMISSION);
                        return;
                    }

                    String targetName = (String) args.get("player");
                    String newPassword = (String) args.get("newPassword");
                    plugin.getAuthManager().adminChangePassword(player, targetName, newPassword);
                });
    }
}