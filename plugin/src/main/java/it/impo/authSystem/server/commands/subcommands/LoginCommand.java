package it.impo.authSystem.server.commands.subcommands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import it.impo.authSystem.AuthSystem;

public class LoginCommand {

    private final AuthSystem plugin;

    public LoginCommand(AuthSystem plugin) {
        this.plugin = plugin;
    }

    public CommandAPICommand get() {
        return new CommandAPICommand("login")
                .withArguments(new StringArgument("password"))
                .executesPlayer((player, args) -> {
                    String password = (String) args.get("password");
                    plugin.getAuthManager().login(player, password);
                });
    }
}