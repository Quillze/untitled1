package spotlight.spotlight.utils.items.use;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.messages.MessagesCore;
import com.ronanplugins.messages.Messenger;
import com.ronanplugins.utils.items.Drops;
import com.ronanplugins.utils.items.ItemCreator;
import com.ronanplugins.utils.items.ItemHandler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import org.bukkit.BanEntry;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.BanList.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class ReviveHandler {
    public ReviveHandler() {
    }

    public static void onUse(Player player) {
        open(player, 0);
    }

    private static void open(Player player, int page) {
        List<BanEntry> banList = new ArrayList(Bukkit.getBanList(Type.NAME).getBanEntries());
        Inventory inventory = Bukkit.createInventory((InventoryHolder)null, 54, "Banned Players (" + banList.size() + ")");

        int start;
        for(start = 45; start < 54; ++start) {
            inventory.setItem(start, (new ItemCreator(Material.GRAY_STAINED_GLASS_PANE)).setDisplayName("&cUnban Players").create((ItemCreator.ITEM_TYPE)null));
        }

        if (!banList.isEmpty()) {
            start = page * 45;
            int end = start + 45;

            for(int i = start; i < end && i < banList.size(); ++i) {
                BanEntry ban = (BanEntry)banList.get(i);
                ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta meta = head.getItemMeta();

                assert meta != null;

                SkullMeta skullMeta = (SkullMeta)head.getItemMeta();

                assert skullMeta != null;

                skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(ban.getTarget()));
                skullMeta.setDisplayName("§c" + ban.getTarget());
                head.setItemMeta(skullMeta);
                inventory.addItem(new ItemStack[]{head});
            }

            ItemStack item;
            if (start + 45 < banList.size()) {
                item = (new ItemCreator(Material.PAPER)).setDisplayName("&eNext Page").addData(key(), PersistentDataType.INTEGER, page + 1).create((ItemCreator.ITEM_TYPE)null);
                inventory.setItem(53, item);
            }

            if (page > 0) {
                item = (new ItemCreator(Material.PAPER)).setDisplayName("&cLast Page").addData(key(), PersistentDataType.INTEGER, page - 1).create((ItemCreator.ITEM_TYPE)null);
                inventory.setItem(45, item);
            }
        }

        player.openInventory(inventory);
    }

    private static NamespacedKey key() {
        return new NamespacedKey(SpotlightSMP.getInstance(), "Page");
    }

    public static void onInv(InventoryClickEvent e) {
        if (e.getView().getTitle().contains("Banned Players (")) {
            e.setCancelled(true);
            if (((Inventory)Objects.requireNonNull(e.getClickedInventory())).getItem(e.getSlot()) != null) {
                ItemStack stack = e.getClickedInventory().getItem(e.getSlot());
                if (stack == null) {
                    return;
                }

                if (stack.getType().equals(Material.PLAYER_HEAD)) {
                    SkullMeta meta = (SkullMeta)stack.getItemMeta();
                    if (meta != null) {
                        final Player player = (Player)e.getWhoClicked();
                        OfflinePlayer banned = meta.getOwningPlayer();
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pardon " + banned.getName());
                        String str = MessagesCore.UNBAN_MESSAGE.get().replace("{0}", player.getName()).replace("{1}", "" + banned.getName());
                        Iterator var6 = Bukkit.getOnlinePlayers().iterator();

                        while(var6.hasNext()) {
                            Player _player = (Player)var6.next();
                            Messenger.sms(_player, str);
                        }

                        e.getWhoClicked().closeInventory();
                        (new BukkitRunnable() {
                            int ticks;
                            int seconds;

                            public void run() {
                                this.ticks += 5;
                                if (this.ticks >= 20) {
                                    this.ticks = 0;
                                    ++this.seconds;
                                }

                                if (player.isOnline() && this.seconds <= 8) {
                                    Firework fw = (Firework)player.getWorld().spawnEntity(player.getLocation().add(0.0, 3.0, 0.0), EntityType.FIREWORK);
                                    FireworkMeta meta = fw.getFireworkMeta();
                                    meta.setPower(3);
                                    meta.addEffect(FireworkEffect.builder().withColor(Color.fromBGR((new Random()).nextInt(256), (new Random()).nextInt(256), (new Random()).nextInt(256))).withFlicker().with(org.bukkit.FireworkEffect.Type.BALL_LARGE).build());
                                    fw.setFireworkMeta(meta);
                                } else {
                                    this.cancel();
                                }

                            }
                        }).runTaskTimer(SpotlightSMP.getInstance(), 0L, 5L);
                        ItemStack[] var12 = e.getWhoClicked().getInventory().getContents();
                        int var13 = var12.length;

                        for(int var8 = 0; var8 < var13; ++var8) {
                            ItemStack item = var12[var8];
                            if (item != null && ItemHandler.getCraftable(item) == Drops.REVIVE) {
                                item.setAmount(item.getAmount() - 1);
                                break;
                            }
                        }
                    }
                } else if (e.getSlot() == 53 || e.getSlot() == 45) {
                    ItemMeta meta = stack.getItemMeta();

                    assert meta != null;

                    if (meta.getPersistentDataContainer().has(key(), PersistentDataType.INTEGER)) {
                        int newPage = (Integer)meta.getPersistentDataContainer().getOrDefault(key(), PersistentDataType.INTEGER, 0);
                        open((Player)e.getWhoClicked(), newPage);
                    }
                }
            }
        }

    }
}
