package it.impo.authSystem.api;

import it.impo.authSystem.api.database.AuthTable;
import it.impo.authSystem.api.manager.AuthManager;

public interface AuthSystemApi {

    AuthTable getAuthTable();

    AuthManager getAuthManager();
}
