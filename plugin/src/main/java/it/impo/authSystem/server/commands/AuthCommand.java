package it.impo.authSystem.server.commands;

import dev.jorel.commandapi.CommandAPICommand;
import it.impo.authSystem.AuthSystem;
import it.impo.authSystem.server.commands.subcommands.ChangePasswordCommand;
import it.impo.authSystem.server.commands.subcommands.LoginCommand;
import it.impo.authSystem.server.commands.subcommands.PremiumCommand;
import it.impo.authSystem.server.commands.subcommands.RegisterCommand;

public class AuthCommand {

    private final AuthSystem plugin;

    public AuthCommand(AuthSystem plugin) {
        this.plugin = plugin;
    }

    public CommandAPICommand[] get() {
        return new CommandAPICommand[]{
                new RegisterCommand(plugin).get(),
                new LoginCommand(plugin).get(),
                new ChangePasswordCommand(plugin).get(),
                new PremiumCommand(plugin).get()
        };
    }
}