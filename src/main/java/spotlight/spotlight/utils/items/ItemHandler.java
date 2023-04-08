package spotlight.spotlight.utils.items;

import com.ronanplugins.utils.items.ItemCreator.ITEM_TYPE;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ItemHandler {
    public ItemHandler() {
    }

    public static boolean isSMPItem(ItemStack item, ItemCreator.ITEM_TYPE type) {
        if (item == null) {
            return false;
        } else {
            ItemMeta meta = item.getItemMeta();
            return meta == null ? false : meta.getPersistentDataContainer().has(ItemCreator.getKey(type), PersistentDataType.INTEGER);
        }
    }

    public static boolean isSMPItem(ItemStack item) {
        ItemCreator.ITEM_TYPE[] var1 = ITEM_TYPE.values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ItemCreator.ITEM_TYPE type = var1[var3];
            if (isSMPItem(item, type)) {
                return true;
            }
        }

        return false;
    }

    public static Spotlight getSpotlight(ItemStack item) {
        if (!isSMPItem(item, ITEM_TYPE.SPOTLIGHT)) {
            return null;
        } else {
            Spotlight[] var1 = Spotlight.values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                Spotlight spotlight = var1[var3];
                if (spotlight.getModelID() == ((ItemMeta)Objects.requireNonNull(item.getItemMeta())).getCustomModelData()) {
                    return spotlight;
                }
            }

            return null;
        }
    }

    public static boolean isDownLight(ItemStack stack) {
        if (stack != null && stack.getType().equals(Material.LIGHT) && stack.hasItemMeta()) {
            ItemMeta meta = stack.getItemMeta();

            assert meta != null;

            if (meta.hasCustomModelData()) {
                return meta.getCustomModelData() == Drops.DOWNLIGHT.getModelID();
            }
        }

        return false;
    }

    public static Drops getCraftable(ItemStack item) {
        if (!isSMPItem(item, ITEM_TYPE.DROP)) {
            return null;
        } else {
            Drops[] var1 = Drops.values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                Drops craftable = var1[var3];
                if (craftable.getModelID() == ((ItemMeta)Objects.requireNonNull(item.getItemMeta())).getCustomModelData()) {
                    return craftable;
                }
            }

            return null;
        }
    }

    public static boolean isRiptide(ItemStack item) {
        return isSMPItem(item, ITEM_TYPE.OTHER);
    }
}
