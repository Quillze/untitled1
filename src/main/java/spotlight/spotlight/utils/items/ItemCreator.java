package spotlight.spotlight.utils.items;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.messages.Message;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.Nullable;

public class ItemCreator {
    private final ItemStack stack;
    private final ItemMeta meta;

    public ItemCreator(Material material) {
        this.stack = new ItemStack(material, 1);
        this.meta = this.stack.getItemMeta();
    }

    public static ItemStack createPotion(PotionType effect, int power, int amount) {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta)potion.getItemMeta();
        PotionData data = new PotionData(effect);

        assert meta != null;

        meta.setBasePotionData(data);
        potion.setItemMeta(meta);
        potion.setAmount(amount);
        return potion;
    }

    public ItemCreator setAmount(int amount) {
        this.stack.setAmount(amount);
        return this;
    }

    public ItemCreator setDisplayName(String name) {
        this.meta.setDisplayName(Message.color(name));
        return this;
    }

    public ItemCreator addLore(String loreText) {
        Object lore;
        if (this.meta.hasLore()) {
            lore = this.meta.getLore();
        } else {
            lore = new ArrayList();
        }

        assert lore != null;

        ((List)lore).add(loreText);
        this.meta.setLore((List)lore);
        return this;
    }

    public ItemCreator setUnBrakeAble() {
        this.meta.setUnbreakable(true);
        return this;
    }

    public ItemCreator setModelID(int modelID) {
        this.meta.setCustomModelData(modelID);
        return this;
    }

    public ItemCreator setEnchant(Enchantment enchant, int id) {
        this.stack.addUnsafeEnchantment(enchant, id);
        return this;
    }

    public ItemStack create(@Nullable @Nullable ITEM_TYPE type) {
        if (type != null) {
            this.meta.getPersistentDataContainer().set(getKey(type), PersistentDataType.INTEGER, 1);
        }

        this.stack.setItemMeta(this.meta);
        return this.stack;
    }

    public static NamespacedKey getKey(ITEM_TYPE type) {
        switch (type) {
            case SPOTLIGHT:
                return new NamespacedKey(SpotlightSMP.getInstance(), "_SPOTLIGHT");
            case DROP:
                return new NamespacedKey(SpotlightSMP.getInstance(), "_DROP");
            default:
                return new NamespacedKey(SpotlightSMP.getInstance(), "_OTHER");
        }
    }

    public static ItemStack createBasic(Material mat, int amount) {
        ItemStack item = new ItemStack(mat);
        item.setAmount(amount);
        return item;
    }

    public static ItemStack enchant(ItemStack item, Enchantment enchantment, int power, boolean hide) {
        item.addUnsafeEnchantment(enchantment, power);
        ItemMeta meta = item.getItemMeta();
        if (hide && meta != null) {
            meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack getPotion(PotionType type, int level, boolean extend, boolean upgraded, String displayName) {
        ItemStack potion = new ItemStack(Material.POTION, 1);
        PotionMeta meta = (PotionMeta)potion.getItemMeta();

        assert meta != null;

        meta.setBasePotionData(new PotionData(type, extend, upgraded));
        potion.setItemMeta(meta);
        return potion;
    }

    public ItemCreator addData(NamespacedKey key, PersistentDataType type, Object value) {
        this.meta.getPersistentDataContainer().set(key, type, value);
        return this;
    }

    public static enum ITEM_TYPE {
        SPOTLIGHT,
        DROP,
        OTHER;

        private ITEM_TYPE() {
        }
    }
}
