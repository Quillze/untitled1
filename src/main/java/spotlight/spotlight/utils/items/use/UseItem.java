package spotlight.spotlight.utils.items.use;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.utils.Bundles;
import com.ronanplugins.utils.LightTypes;
import com.ronanplugins.utils.PlayerManager;
import com.ronanplugins.utils.SpotlightHelper;
import com.ronanplugins.utils.Bundles.BUNDLE;
import com.ronanplugins.utils.items.Drops;
import com.ronanplugins.utils.items.ItemHandler;
import com.ronanplugins.utils.items.Spotlight;
import com.ronanplugins.utils.items.Trigger_Type;
import java.util.Arrays;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import spotlight.spotlight.utils.items.*;

public class UseItem {
    public UseItem() {
    }

    public static void handle(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();
        if (ItemHandler.isSMPItem(e.getItem())) {
            ItemStack item = player.getInventory().getItemInMainHand();
            Spotlight spotlight = ItemHandler.getSpotlight(item);
            if (spotlight == null) {
                item = player.getInventory().getItemInOffHand();
                spotlight = ItemHandler.getSpotlight(item);
            }

            if (spotlight != null) {
                e.setCancelled(true);
                useSpotlight(player, spotlight, action, e);
            }

            item = player.getInventory().getItemInMainHand();
            Drops drop = ItemHandler.getCraftable(item);
            if (drop == null) {
                item = player.getInventory().getItemInOffHand();
                drop = ItemHandler.getCraftable(item);
            }

            if (drop != null) {
                e.setCancelled(true);
                if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
                    useDrop(player, item, drop);
                }
            }

            if (ItemHandler.isRiptide(player.getInventory().getItemInMainHand())) {
                e.setCancelled(true);
            } else if (ItemHandler.isRiptide(player.getInventory().getItemInOffHand())) {
                e.setCancelled(true);
            }

        }
    }

    private static void useSpotlight(Player player, Spotlight spotlight, Action action, PlayerInteractEvent e) {
        if (!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) {
            if ((action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) && Arrays.asList(spotlight.getTriggerType()).contains(Trigger_Type.LEFT_CLICK)) {
                SpotlightHelper.activate(player, Trigger_Type.LEFT_CLICK);
            }
        } else if (Arrays.asList(spotlight.getTriggerType()).contains(Trigger_Type.RIGHT_CLICK)) {
            SpotlightHelper.activate(player, Trigger_Type.RIGHT_CLICK);
        }

    }

    private static void useDrop(final Player player, ItemStack stack, Drops drop) {
        PlayerManager playerManager = SpotlightSMP.getInstance().getManager();
        switch (drop) {
            case REROLL:
                stack.setAmount(stack.getAmount() - 1);
                (new BukkitRunnable() {
                    int times = 0;

                    public void run() {
                        if (this.times == 5) {
                            this.cancel();
                            SpotlightHelper.switchSpotlight(player);
                        } else {
                            PlayerManager.actionBar(player, "&aRerolling in &f" + (5 - this.times));
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                            ++this.times;
                        }

                    }
                }).runTaskTimer(SpotlightSMP.getInstance(), 0L, 20L);
                break;
            case SHARD:
                if (playerManager.has(player, LightTypes.DOWN_LIGHT)) {
                    stack.setAmount(stack.getAmount() - 1);
                    playerManager.removeDownLight(player);
                    if (!playerManager.has(player, LightTypes.DOWN_LIGHT)) {
                        ItemStack[] var4 = player.getInventory().getContents();
                        int var5 = var4.length;

                        for(int var6 = 0; var6 < var5; ++var6) {
                            ItemStack pitem = var4[var6];
                            if (ItemHandler.getCraftable(pitem) == Drops.DOWNLIGHT) {
                                pitem.setAmount(0);
                            }
                        }
                    }
                }

                playerManager.downLightEffects(player);
                break;
            case REVIVE:
                ReviveHandler.onUse(player);
                break;
            case BUNDLE_PVP:
                stack.setAmount(stack.getAmount() - 1);
                Bundles.claim(player, BUNDLE.PVP);
                break;
            case BUNDLE_VILLAGER:
                stack.setAmount(stack.getAmount() - 1);
                Bundles.claim(player, BUNDLE.VILLAGER);
        }

    }
}
