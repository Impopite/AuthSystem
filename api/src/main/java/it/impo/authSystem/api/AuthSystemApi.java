package it.impo.authSystem.api;

import it.impo.authSystem.api.database.AuthTable;
import it.impo.authSystem.api.manager.AuthManager;

/**
 * Main entry point for the AuthSystem API.
 * <p>
 * Provides access to the authentication manager and the database table.
 * Obtain an instance via {@link org.bukkit.Bukkit#getPluginManager()}:
 * <pre>{@code
 * AuthSystemApi api = Bukkit.getPluginManager()
 *     .getPlugin("AuthSystem", AuthSystemApi.class);
 * }</pre>
 *
 * @see AuthManager
 * @see AuthTable
 */
public interface AuthSystemApi {

    /**
     * Returns the database table used to store and retrieve player authentication data.
     *
     * @return the {@link AuthTable} instance
     */
    AuthTable getAuthTable();

    /**
     * Returns the authentication manager responsible for login, registration, and session handling.
     *
     * @return the {@link AuthManager} instance
     */
    AuthManager getAuthManager();
}
