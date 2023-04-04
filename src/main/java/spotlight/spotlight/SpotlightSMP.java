package spotlight.spotlight;

import com.ronanplugins.commands.Commands;
import com.ronanplugins.events.ServerListeners;
import com.ronanplugins.events.SpotLightListeners;
import com.ronanplugins.file.Files;
import com.ronanplugins.file.FileOther.FILETYPE;
import com.ronanplugins.utils.Craftings;
import com.ronanplugins.utils.MoonHandler;
import com.ronanplugins.utils.PlayerManager;
import com.ronanplugins.utils.Runable;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpotlightSMP extends JavaPlugin {
    private static SpotlightSMP instance;
    private final Permissions perms = new Permissions();
    private final Commands cmd = new Commands(this);
    private final Files files = new Files();
    private final Craftings crafting = new Craftings();
    private final PlayerManager manager = new PlayerManager(this);
    private final MoonHandler moonHandler = new MoonHandler();
    private SpotLightListeners listeners;
    boolean smpEnabled;

    public SpotlightSMP() {
    }

    public void onEnable() {
        instance = this;
        this.init();
        Bukkit.getOnlinePlayers().forEach((player) -> {
            if (player.hasCooldown(Material.LIGHT)) {
                Runable.uuids.add(player.getUniqueId());
            }

        });
        this.moonHandler.load();
        Bukkit.getScheduler().runTaskTimer(this, new Runable(), 100L, 100L);
    }

    private void init() {
        this.loadAll();
        this.events();
        this.crafting.load();
    }

    public boolean onCommand(@NotNull CommandSender sendi, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        this.cmd.commandExecuted(sendi, label, args);
        return true;
    }

    public @Nullable List<String> onTabComplete(@NotNull CommandSender sendi, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return this.cmd.onTabComplete(sendi, args);
    }

    private void events() {
        this.listeners = new SpotLightListeners(this);
        new ServerListeners(this);
    }

    public void setListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public void loadAll() {
        this.files.loadAll();
        this.perms.register();
        this.cmd.load();
        this.smpEnabled = this.files.getType(FILETYPE.CONFIG).getBoolean("Enabled");
    }

    public void onDisable() {
    }

    public static SpotlightSMP getInstance() {
        return instance;
    }

    public Permissions getPerms() {
        return this.perms;
    }

    public Commands getCmd() {
        return this.cmd;
    }

    public Files getFiles() {
        return this.files;
    }

    public Craftings getCrafting() {
        return this.crafting;
    }

    public PlayerManager getManager() {
        return this.manager;
    }

    public MoonHandler getMoonHandler() {
        return this.moonHandler;
    }

    public SpotLightListeners getListeners() {
        return this.listeners;
    }

    public boolean isSmpEnabled() {
        return this.smpEnabled;
    }

    public void setSmpEnabled(boolean smpEnabled) {
        this.smpEnabled = smpEnabled;
    }
}
