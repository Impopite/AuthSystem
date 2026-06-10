package it.impo.authSystem.server.adminCommands.subcommands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import it.impo.authSystem.AuthSystem;
import it.impo.authSystem.config.constant.LangKey;
import it.impo.authSystem.utils.Permission;

public class UnregisterCommand {

    private final AuthSystem plugin;

    public UnregisterCommand(AuthSystem plugin) {
        this.plugin = plugin;
    }

    public CommandAPICommand get() {
        return new CommandAPICommand("unregister")
                .withArguments(new StringArgument("player"))
                .executesPlayer((player, args) -> {
                    if(!player.hasPermission(Permission.ADMIN.getPermission()) || !player.hasPermission(Permission.UNREGISTER.getPermission())) {
                        plugin.getLangLoader().send(player, LangKey.NO_PERMISSION);
                        return;
                    }

                    String targetName = (String) args.get("player");
                    plugin.getAuthManager().unregister(player, targetName);
                });
    }
}