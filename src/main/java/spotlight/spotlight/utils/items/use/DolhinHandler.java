package spotlight.spotlight.utils.items.use;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.utils.Moon_Types;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import spotlight.spotlight.*;

public class DolhinHandler {
    public DolhinHandler() {
    }

    public static void activate(Player player) {
        if (player.isInWater()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 600, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 0));
        }

        boolean moon_active = SpotlightSMP.getInstance().getMoonHandler().getMoonType(player.getWorld()) == Moon_Types.WATER;
        if (moon_active) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 600, 8));
        }

    }
}
