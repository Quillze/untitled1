package spotlight.spotlight.utils.items.use;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.utils.PlayerManager;
import com.ronanplugins.utils.items.Trigger_Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class AwayHandler {
    public AwayHandler() {
    }

    public static void activate(Player player, Trigger_Type triggerType) {
        if (triggerType == Trigger_Type.RIGHT_CLICK) {
            if (player.getCooldown(Material.LIGHT) <= 0) {
                player.setCooldown(Material.LIGHT, 1200);
                timer(player);
            }
        }
    }

    private static void timer(final Player player) {
        (new BukkitRunnable() {
            int ticks = 0;
            int seconds = 0;

            public void run() {
                if (!player.isOnline()) {
                    this.cancel();
                }

                Collection<Entity> entities = ((World)Objects.requireNonNull(player.getLocation().getWorld())).getNearbyEntities(player.getLocation(), 10.0, 20.0, 10.0, (entityx) -> {
                    return entityx instanceof Monster;
                });
                Iterator var2 = entities.iterator();

                while(var2.hasNext()) {
                    Entity entity = (Entity)var2.next();
                    Vector dirToCenter = player.getLocation().subtract(entity.getLocation()).toVector();
                    entity.setVelocity(dirToCenter.normalize().multiply(-0.15));
                }

                PlayerManager.actionBar(player, "&aPushing for &f" + (10 - this.seconds) + "&a seconds!");
                if (this.ticks >= 20) {
                    ++this.seconds;
                    this.ticks = 0;
                }

                if (this.seconds >= 10) {
                    this.cancel();
                    player.setCooldown(Material.LIGHT, 200);
                } else {
                    this.ticks += 2;
                }

            }
        }).runTaskTimer(SpotlightSMP.getInstance(), 0L, 2L);
    }
}
