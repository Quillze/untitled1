package spotlight.spotlight;

import org.bukkit.*;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;
import spotlight.spotlight.events.*;

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


    void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new JoinLeaveEvent(), this);
    }
    public static Spotlight getInstance() {
        return instance;
    }
}
