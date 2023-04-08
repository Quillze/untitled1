package spotlight.spotlight.utils.items.use;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.utils.Moon_Types;
import com.ronanplugins.utils.PlayerManager;
import com.ronanplugins.utils.items.Trigger_Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class FreezeHandler {
    public FreezeHandler() {
    }

    public static void activate(Player player, Trigger_Type triggerType) {
        if (triggerType == Trigger_Type.HELD_IN_HAND) {
            ItemStack boots = player.getInventory().getBoots();
            if (boots == null) {
                PlayerManager.actionBar(player, "&cFrost Walker not applied! No Boots!");
                return;
            }

            boots.addUnsafeEnchantment(Enchantment.FROST_WALKER, 2);
        } else if (triggerType == Trigger_Type.RIGHT_CLICK) {
            if (player.getCooldown(Material.LIGHT) > 0) {
                return;
            }

            boolean moon_active = SpotlightSMP.getInstance().getMoonHandler().getMoonType(player.getWorld()) == Moon_Types.FIRE;
            List<Block> blocks = get(player.getLocation().add(-3.0, moon_active ? -6.0 : -1.0, -3.0), player.getLocation().add(3.0, moon_active ? 9.0 : 3.0, 3.0));
            PlayerManager.actionBar(player, "&bBarrier up!");
            timer(blocks, player);
            player.setCooldown(Material.LIGHT, 1200);
        }

    }

    private static void timer(List<Block> blocks, final Player player) {
        final HashMap<Block, Material> blockMat = new HashMap();
        Iterator var3 = blocks.iterator();

        while(var3.hasNext()) {
            Block block = (Block)var3.next();
            if (block.getType() == Material.AIR) {
                blockMat.put(block, block.getType());
                block.setType(Material.BLUE_ICE, false);
            }
        }

        (new BukkitRunnable() {
            public void run() {
                Iterator var1 = blockMat.keySet().iterator();

                while(var1.hasNext()) {
                    Block block = (Block)var1.next();
                    if (block.getType() == Material.BLUE_ICE && block.getType() != blockMat.get(block)) {
                        block.setType((Material)blockMat.get(block), false);
                    }
                }

                PlayerManager.actionBar(player, "&cBarrier has fallen!");
                player.setCooldown(Material.LIGHT, 200);
            }
        }).runTaskLater(SpotlightSMP.getInstance(), 300L);
    }

    private static List<Block> get(Location l1, Location l2) {
        List<Block> blocks = new ArrayList();
        int minX = Math.min(l1.getBlockX(), l2.getBlockX());
        int maxX = Math.max(l1.getBlockX(), l2.getBlockX());
        int minY = Math.min(l1.getBlockY(), l2.getBlockY());
        int maxY = Math.max(l1.getBlockY(), l2.getBlockY());
        int minZ = Math.min(l1.getBlockZ(), l2.getBlockZ());
        int maxZ = Math.max(l1.getBlockZ(), l2.getBlockZ());

        int z;
        int y;
        Block b;
        for(z = minX; z <= maxX; ++z) {
            for(y = minY; y <= maxY; ++y) {
                b = (new Location(l1.getWorld(), (double)z, (double)y, (double)minZ)).getBlock();
                blocks.add(b);
            }
        }

        for(z = minX; z <= maxX; ++z) {
            for(y = minY; y <= maxY; ++y) {
                b = (new Location(l1.getWorld(), (double)z, (double)y, (double)maxZ)).getBlock();
                blocks.add(b);
            }
        }

        for(z = minZ; z <= maxZ; ++z) {
            for(y = minY; y <= maxY; ++y) {
                b = (new Location(l1.getWorld(), (double)minX, (double)y, (double)z)).getBlock();
                blocks.add(b);
            }
        }

        for(z = minZ; z <= maxZ; ++z) {
            for(y = minY; y <= maxY; ++y) {
                b = (new Location(l1.getWorld(), (double)maxX, (double)y, (double)z)).getBlock();
                blocks.add(b);
            }
        }

        return blocks;
    }

    public static void deactivate(Player player) {
        ItemStack[] var1 = player.getInventory().getContents();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ItemStack item = var1[var3];
            if (item != null) {
                item.removeEnchantment(Enchantment.FROST_WALKER);
            }
        }

    }
}
