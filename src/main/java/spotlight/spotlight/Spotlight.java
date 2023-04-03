package spotlight.spotlight;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.*;

public final class Spotlight extends JavaPlugin {

    private static Spotlight instance;

    public static Logger log = Logger.getLogger("Spotlight");
    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public static Spotlight getInstance() {
        return instance;
    }
}
