package spotlight.spotlight.utils;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.events.PlayerCoolDownEvent;
import com.ronanplugins.events.SpotLightListeners;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Runable implements Runnable {
    public static List<UUID> uuids = new ArrayList();

    public Runable() {
    }

    public void run() {
        Iterator var1 = uuids.iterator();

        while(var1.hasNext()) {
            UUID uuid = (UUID)var1.next();
            Player player = Bukkit.getPlayer(uuid);
            Bukkit.getServer().getPluginManager().callEvent(new PlayerCoolDownEvent(player, Material.LIGHT));
        }

        var1 = Bukkit.getOnlinePlayers().iterator();

        while(var1.hasNext()) {
            Player p = (Player)var1.next();
            SpotLightListeners.checkSpotlightInHands(p, p.getInventory().getItemInMainHand(), false);
            SpotlightSMP.getInstance().getManager().downLightEffects(p);
        }

    }
}
