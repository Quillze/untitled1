package spotlight.spotlight.utils;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.utils.items.ItemCreator;
import com.ronanplugins.utils.items.ItemHandler;
import com.ronanplugins.utils.items.Spotlight;
import com.ronanplugins.utils.items.Trigger_Type;
import com.ronanplugins.utils.items.use.AirHandler;
import com.ronanplugins.utils.items.use.AlterHandler;
import com.ronanplugins.utils.items.use.AwayHandler;
import com.ronanplugins.utils.items.use.DolhinHandler;
import com.ronanplugins.utils.items.use.FireballHandler;
import com.ronanplugins.utils.items.use.FlightHandler;
import com.ronanplugins.utils.items.use.FreezeHandler;
import com.ronanplugins.utils.items.use.InvisibleHandler;
import com.ronanplugins.utils.items.use.LightningHandler;
import com.ronanplugins.utils.items.use.OreHandler;
import com.ronanplugins.utils.items.use.PhaseHandler;
import com.ronanplugins.utils.items.use.PullHandler;
import com.ronanplugins.utils.items.use.RiptideHandler;
import com.ronanplugins.utils.items.use.SummonHandler;
import com.ronanplugins.utils.items.use.TeleportHandler;
import com.ronanplugins.utils.items.use.ThornsHandler;
import com.ronanplugins.utils.items.use.TimeHandler;
import com.ronanplugins.utils.items.use.WeatherHandler;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

public class SpotlightHelper {
    public SpotlightHelper() {
    }

    public static void activate(Player player, Trigger_Type triggerType) {
        PlayerManager playerManager = SpotlightSMP.getInstance().getManager();
        Spotlight spotlight = playerManager.getPower(player);
        if (spotlight != null) {
            if (triggerType == Trigger_Type.HELD_IN_HAND && Arrays.stream(spotlight.getTriggerType()).anyMatch((type) -> {
                return type == Trigger_Type.HELD_IN_HAND;
            })) {
                applyPotionEffects(player, spotlight);
                ItemStack[] var4 = player.getInventory().getContents();
                int var5 = var4.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    ItemStack item = var4[var6];
                    if (ItemHandler.getSpotlight(item) != null) {
                        ItemCreator.enchant(item, Enchantment.ARROW_KNOCKBACK, 1, true);
                    }
                }
            }

            switch (spotlight) {
                case FLIGHT:
                    FlightHandler.activate(player);
                    return;
                case ALTER:
                    AlterHandler.activate(player);
                    return;
                case FIRE:
                    FireballHandler.shoot(player, triggerType);
                    break;
                case SUMMON:
                    SummonHandler.activate(player);
                    break;
                case TIME:
                    TimeHandler.activate(player);
                    break;
                case WEATHER:
                    WeatherHandler.activate(player);
                    break;
                case TELEPORT:
                    TeleportHandler.activate(player);
                    break;
                case ORE:
                    OreHandler.activate(player);
                    break;
                case FREEZE:
                    FreezeHandler.activate(player, triggerType);
                case MOB_FRIENDLY:
                default:
                    break;
                case PHASE:
                    PhaseHandler.activate(player);
                    break;
                case AWAY:
                    AwayHandler.activate(player, triggerType);
                    break;
                case PULL:
                    PullHandler.activate(player, triggerType);
                    break;
                case LIGHTNING:
                    LightningHandler.activate(player, triggerType);
                    break;
                case AIR:
                    AirHandler.activate(player, triggerType);
                    break;
                case DOLPHIN:
                    DolhinHandler.activate(player);
                    break;
                case INVISIBLE:
                    InvisibleHandler.activate(player);
                    break;
                case AVOIDER:
                    ThornsHandler.activate(player);
                    break;
                case RIPTIDE:
                    RiptideHandler.activate(player);
            }

        }
    }

    private static void applyPotionEffects(Player player, Spotlight spotlight) {
        if (spotlight != Spotlight.PUSH) {
            if (spotlight.getPotionEffect() != null) {
                PotionEffect[] var2 = spotlight.getPotionEffect();
                int var3 = var2.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    PotionEffect effect = var2[var4];
                    player.addPotionEffect(effect);
                }
            }

        }
    }

    public static void deactivate(Player player) {
        Spotlight spotlight = SpotlightSMP.getInstance().getManager().getPower(player);
        if (spotlight != null) {
            switch (spotlight) {
                case FLIGHT:
                    FlightHandler.deactivate(player);
                    break;
                case ALTER:
                    AlterHandler.deactivate(player);
                    break;
                case FREEZE:
                    FreezeHandler.deactivate(player);
            }

            removePotionEffects(player, spotlight);
            ItemStack[] var2 = player.getInventory().getContents();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                ItemStack item = var2[var4];
                if (ItemHandler.getSpotlight(item) != null) {
                    item.removeEnchantment(Enchantment.ARROW_KNOCKBACK);
                }
            }

        }
    }

    private static void removePotionEffects(Player player, Spotlight spotlight) {
        if (spotlight != Spotlight.PUSH) {
            if (spotlight.getPotionEffect() != null) {
                PotionEffect[] var2 = spotlight.getPotionEffect();
                int var3 = var2.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    PotionEffect effect = var2[var4];
                    player.removePotionEffect(effect.getType());
                }
            }

        }
    }

    public static void switchSpotlight(Player player) {
        PlayerManager playerManager = SpotlightSMP.getInstance().getManager();
        Spotlight old = playerManager.getPower(player);
        List<Spotlight> allAvailablePowers = Spotlight.getRerollable();
        Spotlight newPower = (Spotlight)allAvailablePowers.get((new Random()).nextInt(allAvailablePowers.size()));
        if (old == null) {
            old = newPower;
        }

        playerManager.set(player, LightTypes.SPOTLIGHT, newPower.name());
        if (playerManager.isActive(player)) {
            Iterator var5 = player.getActivePotionEffects().iterator();

            while(var5.hasNext()) {
                PotionEffect activePotionEffect = (PotionEffect)var5.next();
                player.removePotionEffect(activePotionEffect.getType());
            }

            playerManager.toggleActive(player);
        }

        boolean hasSpot = false;
        ItemStack[] var12 = player.getInventory().getContents();
        int var7 = var12.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            ItemStack stack = var12[var8];
            if (stack != null && stack.hasItemMeta()) {
                ItemMeta meta = stack.getItemMeta();

                assert meta != null;

                if (meta.hasCustomModelData() && meta.getCustomModelData() == old.getModelID()) {
                    meta.setCustomModelData(newPower.getModelID());
                    meta.setDisplayName(newPower.getLabel());
                    stack.setItemMeta(meta);
                    hasSpot = true;
                    stack.removeEnchantment(Enchantment.ARROW_KNOCKBACK);
                    break;
                }
            }
        }

        if (!hasSpot) {
            player.getInventory().addItem(new ItemStack[]{playerManager.getSpotlightItem(player)});
        }

        player.setCooldown(Material.LIGHT, 0);
    }
}
