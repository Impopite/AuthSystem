package it.impo.authSystem.server.commands.subcommands;

import dev.jorel.commandapi.CommandAPICommand;
import it.impo.authSystem.AuthSystem;

public class PremiumCommand {

    private final AuthSystem plugin;

    public PremiumCommand(AuthSystem plugin) {
        this.plugin = plugin;
    }

    public CommandAPICommand get() {
        return new CommandAPICommand("premium")
                .executesPlayer((player, args) -> {
                    plugin.getAuthManager().handlePremium(player);
                });
    }
}