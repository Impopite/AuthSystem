package it.impo.authSystem;

import it.impo.authSystem.api.AuthSystemApi;
import it.impo.authSystem.api.database.AuthTable;
import it.impo.authSystem.api.manager.AuthManager;
import it.impo.authSystem.config.ConfigLoader;
import it.impo.authSystem.config.LangLoader;
import it.impo.authSystem.database.BaseAuthTable;
import it.impo.authSystem.database.utils.DatabaseCredentials;
import it.impo.authSystem.database.utils.HikariCP;
import it.impo.authSystem.loader.Loader;
import it.impo.authSystem.manager.BaseAuthManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public final class AuthSystem extends JavaPlugin implements AuthSystemApi {

    private final String projectName = this.getDescription().getName();

    private ConfigLoader configLoader;
    private LangLoader langLoader;
    private AuthManager authManager;

    private HikariCP hikariCP;
    private AuthTable authTable;
    private BukkitAudiences adventure;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();

        getLogger().info("");
        getLogger().info(CYAN + "====================================" + RESET);
        getLogger().info(CYAN + projectName + RESET);
        getLogger().info(GRAY + "   Developed by " + WHITE + "zImpoo" + RESET);
        getLogger().info(CYAN + "====================================" + RESET);

        BukkitAudiences adventure = BukkitAudiences.create(this);
        this.adventure = adventure;
        this.configLoader = new ConfigLoader(this, adventure).load();
        this.langLoader = configLoader.getLangLoader();
        this.authManager = new BaseAuthManager(this);

        DatabaseCredentials databaseCredentials = new DatabaseCredentials(this);
        this.hikariCP = new HikariCP(this, databaseCredentials);
        this.authTable = new BaseAuthTable(hikariCP.getDataSource());

        Loader loader = new Loader(this);
        loader.load(authTable);


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
        adventure.close();

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

    @Override
    public AuthTable getAuthTable() {
        return authTable;
    }

    @Override
    public AuthManager getAuthManager() {
        return authManager;
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    public LangLoader getLangLoader(){
        return langLoader;
    }

    public String getProjectName() {
        return projectName;
    }
}
