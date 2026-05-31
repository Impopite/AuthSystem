package it.impo.defaultProject;

import it.impo.defaultProject.api.DefaultProjectApi;
import it.impo.defaultProject.config.ConfigLoader;
import it.impo.defaultProject.database.utils.DatabaseCredentials;
import it.impo.defaultProject.database.utils.HikariCP;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public final class DefaultProject extends JavaPlugin implements DefaultProjectApi {

    @Getter
    private final String projectName = getDescription().getName();

    @Getter
    private ConfigLoader configLoader;

    private HikariCP hikariCP;
    @Getter
    private DatabaseCredentials databaseCredentials;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();

        getLogger().info("");
        getLogger().info(CYAN + "====================================" + RESET);
        getLogger().info(CYAN + projectName + RESET);
        getLogger().info(GRAY + "   Developed by " + WHITE + "zImpoo" + RESET);
        getLogger().info(CYAN + "====================================" + RESET);

        BukkitAudiences adventure = BukkitAudiences.create(this);
        this.configLoader = new ConfigLoader(this, adventure).load();
        configLoader.load();

        this.databaseCredentials = new DatabaseCredentials(this);
        this.hikariCP = new HikariCP(this, databaseCredentials);
        /*
        try {
            // CREATE TABLE QUERIES...
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
         */

        long took = System.currentTimeMillis() - start;

        getLogger().info(GREEN + "Commands loaded" + RESET);
        getLogger().info(GREEN + "Databases loaded" + RESET);
        getLogger().info(GREEN + "Config loaded" + RESET);
        getLogger().info("");
        getLogger().info(GREEN +  "enabled successfully in " + took + "ms" + RESET);
        getLogger().info(CYAN + "====================================" + RESET);

    }

    @Override
    public void onDisable() {
        hikariCP.close();

        getLogger().info("");
        getLogger().info(RED + "====================================" + RESET);
        getLogger().info(RED + projectName + RESET);
        getLogger().info(GRAY + "   Developed by " + WHITE + "zImpoo" + RESET);
        getLogger().info(RED + "====================================" + RESET);
        getLogger().info(RED + "Plugin disabled safely." + RESET);
        getLogger().info(RED + "====================================" + RESET);
    }

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String CYAN = "\u001B[36m";
    private static final String GRAY = "\u001B[37m";
    private static final String WHITE = "\u001B[97m";
}
