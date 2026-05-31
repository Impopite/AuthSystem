package it.impo.defaultProject.config.constant;

import lombok.Getter;

@Getter
public enum ConfigKey {

    DATABASE_HOST("database.host"),
    DATABASE_NAME("database.name"),
    DATABASE_USERNAME("database.username"),
    DATABASE_PASSWORD("database.password"),
    DATABASE_PORT("database.port"),
    DATABASE_SSL("database.ssl"),

    LANG_FILE("generic.lang");

    private final String path;

    ConfigKey(String path) {
        this.path = path;
    }

}
