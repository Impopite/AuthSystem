package it.impo.authSystem.server.commands.subcommands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import it.impo.authSystem.AuthSystem;

public class ChangePasswordCommand {

    private final AuthSystem plugin;

    public ChangePasswordCommand(AuthSystem plugin) {
        this.plugin = plugin;
    }

    public CommandAPICommand get() {
        return new CommandAPICommand("changepassword")
                .withArguments(new StringArgument("oldPassword"))
                .withArguments(new StringArgument("newPassword"))
                .executesPlayer((player, args) -> {
                    String oldPassword = (String) args.get("oldPassword");
                    String newPassword = (String) args.get("newPassword");
                    plugin.getAuthManager().changePassword(player, oldPassword, newPassword);
                });
    }
}