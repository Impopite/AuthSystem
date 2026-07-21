package it.impo.authSystem.config.constant;

public enum ConfigKey {

    DATABASE_HOST("database.host"),
    DATABASE_NAME("database.name"),
    DATABASE_USERNAME("database.username"),
    DATABASE_PASSWORD("database.password"),
    DATABASE_PORT("database.port"),
    DATABASE_SSL("database.ssl"),

    TIMEOUT_SECONDS("auth.timeout-seconds"),
    TOO_MANY_ATTEMPTS("auth.too-many-attempts"),
    BAN_TIME("auth.ban-time"),

    LANG_FILE("generic.lang");

    private final String path;

    ConfigKey(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
