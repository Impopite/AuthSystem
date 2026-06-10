package it.impo.authSystem.utils;

public enum Permission {

    ADMIN("command.admin"),
    STAFF("command.staff"),
    ADMIN_CHANGE_PASSWORD("command.adminChangePassword"),
    CHECK_IP("command.checkIp"),
    RELOAD("command.reload"),
    UNREGISTER("command.unregister"),
    ;

    private final String permission;

    Permission(String permission) {
        this.permission = "authsystem." + permission;
    }

    public String getPermission() {
        return permission;
    }
}
