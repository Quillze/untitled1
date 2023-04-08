package spotlight.spotlight.utils;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.messages.Message;
import com.ronanplugins.utils.items.Drops;
import com.ronanplugins.utils.items.ItemCreator;
import com.ronanplugins.utils.items.ItemHandler;
import com.ronanplugins.utils.items.Spotlight;
import com.ronanplugins.utils.items.ItemCreator.ITEM_TYPE;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.BanList.Type;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerManager {
    private final SpotlightSMP plugin;
    private final Map<UUID, Integer> cooldown = new HashMap();

    public PlayerManager(SpotlightSMP plugin) {
        this.plugin = plugin;
    }

    public static void actionBar(Player player, String s) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Message.color(s)));
    }

    public void setCooldown(Player p, int amt) {
        this.cooldown.put(p.getUniqueId(), amt);
    }

    public int getSavedCooldown(Player p) {
        return (Integer)this.cooldown.getOrDefault(p.getUniqueId(), 0);
    }

    public NamespacedKey getKey(Player player, LightTypes types) {
        UUID uuid = player.getUniqueId();
        NamespacedKey key = null;
        switch (types) {
            case CRAFTING:
            case TRAILS:
            case IS_ACTIVE:
            case DOWN_LIGHT:
            case SPOTLIGHT:
                return new NamespacedKey(this.plugin, types.getKey());
            default:
                return (NamespacedKey)key;
        }
    }

    public boolean has(Player player, LightTypes type) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        if (type == LightTypes.CRAFTING) {
            return false;
        } else {
            switch (type) {
                case IS_ACTIVE:
                case DOWN_LIGHT:
                    return container.has(this.getKey(player, type), PersistentDataType.INTEGER);
                case SPOTLIGHT:
                    return container.has(this.getKey(player, type), PersistentDataType.STRING);
                case COOLDOWN:
                    return this.cooldown.containsKey(player.getUniqueId());
                default:
                    return false;
            }
        }
    }

    public void set(Player player, LightTypes type, Object obj) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        switch (type) {
            case TRAILS:
            case IS_ACTIVE:
            case DOWN_LIGHT:
            case COOLDOWN:
                container.set(this.getKey(player, type), PersistentDataType.INTEGER, (Integer)obj);
                break;
            case SPOTLIGHT:
                container.set(this.getKey(player, type), PersistentDataType.STRING, (String)obj);
        }

    }

    public void remove(Player player, LightTypes types) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        if (this.has(player, types)) {
            container.remove(this.getKey(player, types));
        }

    }

    public Integer getCoolDown(Player player) {
        return this.has(player, LightTypes.COOLDOWN) ? (Integer)player.getPersistentDataContainer().get(this.getKey(player, LightTypes.COOLDOWN), PersistentDataType.INTEGER) : 0;
    }

    public void removeDownLight(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        if (this.has(player, LightTypes.DOWN_LIGHT)) {
            int downLight = this.getDownLights(player);
            if (downLight == 1) {
                this.remove(player, LightTypes.DOWN_LIGHT);
            } else if (downLight > 1) {
                this.set(player, LightTypes.DOWN_LIGHT, downLight - 1);
                this.setDownLight(player);
            }
        }

    }

    public void addDownLight(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        if (this.has(player, LightTypes.DOWN_LIGHT)) {
            int downLight = this.getDownLights(player);
            this.set(player, LightTypes.DOWN_LIGHT, downLight + 1);
        } else {
            this.set(player, LightTypes.DOWN_LIGHT, 1);
        }

        this.setDownLight(player);
    }

    public void toggleActive(Player player) {
        if (this.isActive(player)) {
            this.remove(player, LightTypes.IS_ACTIVE);
        } else {
            this.set(player, LightTypes.IS_ACTIVE, 1);
        }

    }

    public Integer getDownLights(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        return this.has(player, LightTypes.DOWN_LIGHT) ? (Integer)container.get(this.getKey(player, LightTypes.DOWN_LIGHT), PersistentDataType.INTEGER) : 0;
    }

    public String getSpotlightAsString(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        return this.has(player, LightTypes.SPOTLIGHT) ? (String)container.get(this.getKey(player, LightTypes.SPOTLIGHT), PersistentDataType.STRING) : "";
    }

    public Spotlight getPower(Player player) {
        String powerString = this.getSpotlightAsString(player);

        try {
            if (powerString.length() != 0) {
                return Spotlight.valueOf(powerString);
            }
        } catch (IllegalArgumentException var4) {
        }

        return null;
    }

    public boolean isActive(Player player) {
        return this.has(player, LightTypes.IS_ACTIVE);
    }

    public ItemStack getSpotlightItem(Player player) {
        if (this.has(player, LightTypes.SPOTLIGHT)) {
            Spotlight spotlight = this.getPower(player);
            return spotlight.item();
        } else {
            return null;
        }
    }

    public void setDownLight(Player player) {
        Inventory inventory = player.getInventory();
        ItemStack[] var3 = inventory.getContents();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            ItemStack item = var3[var5];
            if (ItemHandler.isDownLight(item)) {
                item.setAmount(0);
            }
        }

        if (this.getDownLights(player) > 0) {
            PlayerInventory var10000 = player.getInventory();
            ItemStack[] var10001 = new ItemStack[1];
            ItemCreator var10004 = (new ItemCreator(Material.LIGHT)).setDisplayName(Drops.DOWNLIGHT.getLabel()).setModelID(Drops.DOWNLIGHT.getModelID());
            Integer var10005 = this.getDownLights(player);
            var10001[0] = var10004.addLore("Amount: " + var10005).create(ITEM_TYPE.DROP);
            var10000.addItem(var10001);
        }

    }

    public void downLightEffects(Player player) {
        player.setWalkSpeed(0.2F);
        player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
        player.removePotionEffect(PotionEffectType.WEAKNESS);
        if (this.has(player, LightTypes.DOWN_LIGHT)) {
            int downLights = this.getDownLights(player);
            if (downLights >= 1) {
                player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 1200, 0, false, false));
            }

            if (downLights >= 3) {
                player.setWalkSpeed(0.1F);
            }

            if (downLights >= 6) {
                this.set(player, LightTypes.DOWN_LIGHT, 1);
                player.kickPlayer("§cTo many Downlights.");
                Bukkit.getBanList(Type.NAME).addBan(player.getName(), "To many Downlights", (Date)null, "You have to Many Downlights");
            }

        }
    }
}
