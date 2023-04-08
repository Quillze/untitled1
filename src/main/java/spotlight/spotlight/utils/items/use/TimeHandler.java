package spotlight.spotlight.utils.items.use;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.utils.Moon_Types;
import com.ronanplugins.utils.PlayerManager;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TimeHandler {
    public TimeHandler() {
    }

    public static void activate(Player player) {
        if (player.getCooldown(Material.LIGHT) <= 0) {
            if (!SpotlightSMP.getInstance().getMoonHandler().isActive(player.getWorld())) {
                player.getWorld().setTime((long)(new Random()).nextInt(24000));
                player.setCooldown(Material.LIGHT, 12000);
            } else {
                Moon_Types var10001 = SpotlightSMP.getInstance().getMoonHandler().getMoonType(player.getWorld());
                PlayerManager.actionBar(player, "§7" + (var10001 == Moon_Types.FIRE ? "Fire" : "Water") + " Moon is active, Spotlight disabled!");
            }
        }

    }
}
