package it.impo.authSystem.loader;

import it.impo.authSystem.AuthSystem;
import it.impo.authSystem.api.database.AuthTable;
import it.impo.authSystem.server.adminCommands.AdminAuthCommand;
import it.impo.authSystem.server.commands.AuthCommand;
import it.impo.authSystem.server.listeners.PlayerChatListener;
import it.impo.authSystem.server.listeners.PlayerCommandListener;
import it.impo.authSystem.server.listeners.PlayerJoinQuitListener;

import java.sql.SQLException;

public class Loader extends PluginLoader {

    public Loader(AuthSystem plugin) {
        super(plugin);
    }

    @Override
    protected void setupDatabase(AuthTable table) throws SQLException {
        table.createTable();
    }

    @Override
    protected void setupListeners() {
        registerListeners(
                new PlayerChatListener(plugin),
                new PlayerCommandListener(plugin),
                new PlayerJoinQuitListener(plugin)
        );
    }

    @Override
    protected void setupCommands() {
        registerCommands(
                new AuthCommand(plugin).get(),
                new AdminAuthCommand(plugin).get()
        );
    }
}
