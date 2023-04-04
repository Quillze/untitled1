package spotlight.spotlight.invs;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.utils.items.Drops;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class SpotInv {
    private static final String title_drops = "Drops";
    private static final String title_craft = "Craftable";

    public SpotInv() {
    }

    public static void showDrops(Player p) {
        Inventory inv = Bukkit.createInventory((InventoryHolder)null, 9, "Drops");
        Drops[] var2 = Drops.values();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Drops item = var2[var4];
            inv.addItem(new ItemStack[]{item.item()});
        }

        p.openInventory(inv);
    }

    public static void showCraft(Player p) {
        Inventory inv = Bukkit.createInventory((InventoryHolder)null, 9, "Craftable");
        List<ShapedRecipe> recipeList = SpotlightSMP.getInstance().getCrafting().getRecipeList();
        Iterator var3 = recipeList.iterator();

        while(var3.hasNext()) {
            ShapedRecipe recipe = (ShapedRecipe)var3.next();
            inv.addItem(new ItemStack[]{recipe.getResult()});
        }

        p.openInventory(inv);
    }

    public static void click(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase("Craftable") || e.getView().getTitle().equalsIgnoreCase("Drops")) {
            e.setCancelled(true);
        }
    }
}
