package spotlight.spotlight.utils.items.use;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.messages.Message;
import com.ronanplugins.utils.PlayerManager;
import com.ronanplugins.utils.items.ItemCreator;
import com.ronanplugins.utils.items.ItemCreator.ITEM_TYPE;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class RiptideHandler {
    public RiptideHandler() {
    }

    public static void activate(Player player) {
        if (player.getCooldown(Material.LIGHT) <= 0) {
            player.setCooldown(Material.LIGHT, 3000);
            new RiptideTask(player);
        }
    }

    public static void removeRiptideItem(Player player) {
        ItemStack[] var1 = player.getInventory().getContents();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ItemStack item = var1[var3];
            if (item != null) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null && meta.getPersistentDataContainer().has(ItemCreator.getKey(ITEM_TYPE.OTHER), PersistentDataType.INTEGER)) {
                    item.setAmount(0);
                }
            }
        }

    }

    public static ItemStack getRiptide() {
        ItemStack item = (new ItemCreator(Material.TRIDENT)).setDisplayName(Message.color("&bRiptide Trident")).create(ITEM_TYPE.OTHER);
        item.addEnchantment(Enchantment.RIPTIDE, 3);
        return item;
    }

    private static class RiptideTask implements Listener {
        final Player player;

        RiptideTask(Player player) {
            this.player = player;
            this.task(this);
            Bukkit.getPluginManager().registerEvents(this, SpotlightSMP.getInstance());
            player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 600, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 600, 1));
            player.getInventory().addItem(new ItemStack[]{RiptideHandler.getRiptide()});
        }

        void task(final RiptideTask _task) {
            (new BukkitRunnable() {
                int ticks;
                int seconds;

                public void run() {
                    this.ticks += 5;
                    if (this.ticks >= 20) {
                        ++this.seconds;
                        this.ticks = 0;
                    }

                    PlayerManager.actionBar(RiptideTask.this.player, "&bRiptide active for &f" + (30 - this.seconds) + " &bseconds");
                    if (this.seconds >= 30) {
                        HandlerList.unregisterAll(_task);
                        RiptideTask.this.player.setCooldown(Material.LIGHT, 600);
                        RiptideHandler.removeRiptideItem(RiptideTask.this.player);
                        this.cancel();
                    }

                }
            }).runTaskTimer(SpotlightSMP.getInstance(), 5L, 5L);
        }
    }
}
