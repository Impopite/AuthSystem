package it.impo.authSystem.config.constant;

public enum LangKey {

    NO_PERMISSION("message.no-permission"),
    UNKNOWN_COMMAND("message.unknown-command"),
    CONSOLE_CANT_DO("message.console-cant-do-command"),
    PLAYER_NOT_FOUND("message.player-not-found"),
    GENERIC_ERROR("message.generic-error"),
    PLAYER_NOT_FOUND_IN_DATABASE("message.player-not-found-in-database"),
    TOO_MUCH_FAILED_TRY("message.too-much-failed-try"),
    ACTION_BEFORE_LOGIN("message.action-before-login"),
    RELOAD_SUCCESS("message.reload-success"),
    LAST_IP("message.last-ip"),
    FAILED_AUTH_TIME("message.failed-auth-time"),

    NEED_REGISTER("register.need-register"),
    REGISTERED("register.registered"),
    ALREADY_REGISTERED("register.already-registered"),
    PASSWORD_MISMATCH("register.password-mismatch"),
    PASSWORD_TOO_SHORT("register.password-too-short"),
    FAILED_REGISTRATION("register.failed-registration"),

    LOGIN("login.need-login"),
    LOGGED_IN("login.logged-in"),
    LOGIN_PREMIUM("login.login-premium"),
    ALREADY_LOGGED_IN("login.already-logged-in"),
    WRONG_PASSWORD("login.wrong-password"),
    WRONG_PASSWORD_KICK("login.wrong-password-kick"),
    WRONG_PASSWORD_BAN("login.wrong-password-ban"),

    PASSWORD_CHANGED("change-password.password-changed"),
    NEW_PASSWORD_TOO_SHORT("change-password.new-password-too-short"),
    OLD_PASSWORD_IS_WRONG("change-password.old-password-is-wrong"),
    ERROR_CHANGING_PASSWORD("change-password.error-changing-password"),
    ADMIN_CHANGE_PASSWORD("change-password.admin-change-password"),

    UNREGISTERED_BY_ADMIN("unregister.unregistered-by-admin"),
    UNREGISTERED_ADMIN("unregister.unregistered-admin"),

    CONFIRM_PREMIUM("premium.confirm-premium"),
    PREMIUM_CONFIRMED("premium.premium-confirmed"),
    PREMIUM_REMOVED("premium.premium-removed"),

    PREFIX("prefix");

    private final String path;

    LangKey(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
