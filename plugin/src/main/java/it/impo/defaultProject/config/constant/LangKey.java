package it.impo.defaultProject.config.constant;

import lombok.Getter;

@Getter
public enum LangKey {

    NO_PERMISSION("message.no-permission"),
    UNKNOWN_COMMAND("message.unknown-command"),
    CONSOLE_CANT_DO("message.console-cant-do-command"),
    PLAYER_NOT_FOUND("message.player-not-found"),

    PREFIX("prefix");

    private final String path;

    LangKey(String path) {
        this.path = path;
    }
}
