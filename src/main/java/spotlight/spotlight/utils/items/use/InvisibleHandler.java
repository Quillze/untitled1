package spotlight.spotlight.utils.items.use;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.utils.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class InvisibleHandler {
    public InvisibleHandler() {
    }

    public static void activate(Player player) {
        if (player.getCooldown(Material.LIGHT) <= 0) {
            player.setCooldown(Material.LIGHT, 1200);
            task(player);
        }
    }

    private static void task(final Player player) {
        (new BukkitRunnable() {
            int ticks = 0;
            int seconds = 0;

            public void run() {
                ++this.ticks;
                if (this.ticks >= 20) {
                    ++this.seconds;
                    this.ticks = 0;
                }

                if (!player.isOp()) {
                    this.cancel();
                } else {
                    if (this.seconds < 20) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 300, 0));
                        PlayerManager.actionBar(player, "&aInvisibility enabled for &f" + (20 - this.seconds) + " &aSeconds");
                    } else {
                        player.removePotionEffect(PotionEffectType.INVISIBILITY);
                        PlayerManager.actionBar(player, "&cInvisibility terminated!");
                        player.setCooldown(Material.LIGHT, 160);
                    }

                }
            }
        }).runTaskTimer(SpotlightSMP.getInstance(), 5L, 5L);
    }

    public static void deactivate(Player player) {
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
    }
}
