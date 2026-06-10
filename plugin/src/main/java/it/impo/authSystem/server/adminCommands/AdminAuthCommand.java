package it.impo.authSystem.server.adminCommands;

import dev.jorel.commandapi.CommandAPICommand;
import it.impo.authSystem.AuthSystem;
import it.impo.authSystem.server.adminCommands.subcommands.AdminChangePasswordCommand;
import it.impo.authSystem.server.adminCommands.subcommands.CheckIpCommand;
import it.impo.authSystem.server.adminCommands.subcommands.ReloadCommand;
import it.impo.authSystem.server.adminCommands.subcommands.UnregisterCommand;
import it.impo.authSystem.utils.Permission;

public class AdminAuthCommand {

    private final AuthSystem plugin;

    public AdminAuthCommand(AuthSystem plugin) {
        this.plugin = plugin;
    }

    public CommandAPICommand[] get() {
        return new CommandAPICommand[]{
                create()
        };
    }

    protected CommandAPICommand create() {
        return new CommandAPICommand("auth")
                .withPermission(Permission.STAFF.getPermission())
                .withSubcommands(
                        new AdminChangePasswordCommand(plugin).get(),
                        new UnregisterCommand(plugin).get(),
                        new CheckIpCommand(plugin).get(),
                        new ReloadCommand(plugin).get()
                );
    }
}