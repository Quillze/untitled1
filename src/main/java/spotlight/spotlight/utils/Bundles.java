package spotlight.spotlight.utils;

import com.ronanplugins.utils.items.ItemCreator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

public class Bundles {
    public Bundles() {
    }

    public static void claim(Player player, BUNDLE bundle) {
        List<ItemStack> items = new ArrayList();
        switch (bundle) {
            case PVP:
                ItemStack item = ItemCreator.createBasic(Material.DIAMOND_HELMET, 1);
                items.add(ItemCreator.enchant(item, Enchantment.PROTECTION_ENVIRONMENTAL, 1, false));
                item = ItemCreator.createBasic(Material.DIAMOND_CHESTPLATE, 1);
                items.add(ItemCreator.enchant(item, Enchantment.PROTECTION_ENVIRONMENTAL, 1, false));
                item = ItemCreator.createBasic(Material.DIAMOND_LEGGINGS, 1);
                items.add(ItemCreator.enchant(item, Enchantment.PROTECTION_ENVIRONMENTAL, 1, false));
                item = ItemCreator.createBasic(Material.DIAMOND_BOOTS, 1);
                items.add(ItemCreator.enchant(item, Enchantment.PROTECTION_ENVIRONMENTAL, 1, false));
                items.add(ItemCreator.createPotion(PotionType.STRENGTH, 2, 2));
                items.add(ItemCreator.createPotion(PotionType.SPEED, 2, 2));
                item = ItemCreator.createBasic(Material.DIAMOND_SWORD, 1);
                items.add(ItemCreator.enchant(item, Enchantment.DAMAGE_ALL, 1, false));
                item = ItemCreator.createBasic(Material.DIAMOND_AXE, 1);
                items.add(ItemCreator.enchant(item, Enchantment.DIG_SPEED, 1, false));
                items.add(ItemCreator.createBasic(Material.ENDER_PEARL, 8));
                items.add(ItemCreator.createBasic(Material.GOLDEN_APPLE, 16));
                items.add(ItemCreator.createBasic(Material.ENCHANTED_GOLDEN_APPLE, 1));
                break;
            case VILLAGER:
                items.add(ItemCreator.createBasic(Material.LECTERN, 2));
                items.add(ItemCreator.createBasic(Material.BLAST_FURNACE, 2));
                items.add(ItemCreator.createBasic(Material.SMITHING_TABLE, 2));
                items.add(ItemCreator.createBasic(Material.GRINDSTONE, 2));
                items.add(ItemCreator.createBasic(Material.FLETCHING_TABLE, 2));
                items.add(ItemCreator.createBasic(Material.EMERALD, 64));
                player.getWorld().spawn(player.getLocation(), Villager.class);
                player.getWorld().spawn(player.getLocation(), Villager.class);
        }

        HashMap<Integer, ItemStack> unadded = player.getInventory().addItem((ItemStack[])items.toArray(new ItemStack[0]));
        Iterator var5 = unadded.values().iterator();

        while(var5.hasNext()) {
            ItemStack _item = (ItemStack)var5.next();
            player.getWorld().dropItemNaturally(player.getLocation(), _item);
        }

    }

    public static enum BUNDLE {
        PVP,
        VILLAGER;

        private BUNDLE() {
        }
    }
}
