package it.impo.authSystem.server.commands.subcommands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import it.impo.authSystem.AuthSystem;

public class RegisterCommand {

    private final AuthSystem plugin;

    public RegisterCommand(AuthSystem plugin) {
        this.plugin = plugin;
    }

    public CommandAPICommand get() {
        return new CommandAPICommand("register")
                .withArguments(new StringArgument("password"))
                .withArguments(new StringArgument("confirmPassword"))
                .executesPlayer((player, args) -> {
                    String password = (String) args.get("password");
                    String confirmPassword = (String) args.get("confirmPassword");
                    plugin.getAuthManager().register(player, password, confirmPassword);
                });
    }
}
