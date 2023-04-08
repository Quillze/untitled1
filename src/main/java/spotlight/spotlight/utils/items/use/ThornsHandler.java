package spotlight.spotlight.utils.items.use;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.utils.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ThornsHandler {
    public ThornsHandler() {
    }

    public static void activate(Player player) {
        if (player.getCooldown(Material.LIGHT) <= 0) {
            player.setCooldown(Material.LIGHT, 3000);
            new ThornsTask(player);
        }
    }

    private static class ThornsTask implements Listener {
        final Player player;

        ThornsTask(Player player) {
            this.player = player;
            this.task(this);
            Bukkit.getPluginManager().registerEvents(this, SpotlightSMP.getInstance());
        }

        void task(final ThornsTask _task) {
            (new BukkitRunnable() {
                int ticks;
                int seconds;

                public void run() {
                    this.ticks += 5;
                    if (this.ticks >= 20) {
                        ++this.seconds;
                        this.ticks = 0;
                    }

                    PlayerManager.actionBar(ThornsTask.this.player, "&eDamage avoider active for &f" + (30 - this.seconds) + " &eseconds");
                    if (this.seconds >= 30) {
                        HandlerList.unregisterAll(_task);
                        ThornsTask.this.player.setCooldown(Material.LIGHT, 600);
                        this.cancel();
                    }

                }
            }).runTaskTimer(SpotlightSMP.getInstance(), 5L, 5L);
        }

        @EventHandler(
                priority = EventPriority.HIGH
        )
        void onDamage(EntityDamageByEntityEvent e) {
            if (e.getEntity().getEntityId() == this.player.getEntityId()) {
                double damage = e.getDamage();
                e.setDamage(damage * 0.2);
                if (e.getDamager() instanceof LivingEntity) {
                    ((LivingEntity)e.getDamager()).damage(damage * 0.8);
                }
            }

        }
    }
}
