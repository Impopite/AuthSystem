package it.impo.defaultProject.database.utils;

import it.impo.defaultProject.DefaultProject;
import it.impo.defaultProject.config.constant.ConfigKey;
import lombok.Getter;

@Getter
public class DatabaseCredentials {

    private final String host;
    private final String database;
    private final String username;
    private final String password;
    private final int port;
    private final boolean ssl;

    public DatabaseCredentials(DefaultProject plugin) {
        var config = plugin.getConfigLoader();
        this.host = config.get(ConfigKey.DATABASE_HOST, "localhost");
        this.database = config.get(ConfigKey.DATABASE_NAME, "DefaultDatabaseName");
        this.username = config.get(ConfigKey.DATABASE_USERNAME, "root");
        this.password = config.get(ConfigKey.DATABASE_PASSWORD, "");
        this.port = config.get(ConfigKey.DATABASE_PORT, 3306);
        this.ssl = config.get(ConfigKey.DATABASE_SSL, false);
    }
}
