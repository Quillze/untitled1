package spotlight.spotlight.utils.items.use;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.utils.Moon_Types;
import com.ronanplugins.utils.PlayerManager;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WeatherHandler {
    public WeatherHandler() {
    }

    public static void activate(Player player) {
        if (player.getCooldown(Material.LIGHT) <= 0) {
            if (!SpotlightSMP.getInstance().getMoonHandler().isActive(player.getWorld())) {
                World world = player.getWorld();
                if (world.isClearWeather()) {
                    world.setStorm(true);
                    world.setWeatherDuration(11000);
                    PlayerManager.actionBar(player, "§7Weather changed to §bRain");
                } else if (world.hasStorm() && !world.isThundering()) {
                    world.setThundering(true);
                    world.setWeatherDuration(5000);
                    world.setThunderDuration(2500);
                    PlayerManager.actionBar(player, "§7Weather changed to §9Thunder");
                } else {
                    world.setThundering(false);
                    world.setStorm(false);
                    world.setClearWeatherDuration(24000);
                    PlayerManager.actionBar(player, "§7Weather changed to §aClear");
                }

                player.setCooldown(Material.LIGHT, 12000);
            } else {
                Moon_Types var10001 = SpotlightSMP.getInstance().getMoonHandler().getMoonType(player.getWorld());
                PlayerManager.actionBar(player, "§7" + (var10001 == Moon_Types.FIRE ? "Fire" : "Water") + " Moon is active, Spotlight disabled!");
            }

        }
    }
}
