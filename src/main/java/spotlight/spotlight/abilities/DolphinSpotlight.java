package spotlight.spotlight;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DolphinSpotlight implements Listener {

    private final NamespacedKey recipeKey = new NamespacedKey(this, "dolphin_spotlight_recipe");

    @Override
    public void onEnable() {
        // Register crafting recipe for Dolphin Spotlight
        ItemStack dolphinSpotlight = createDolphinSpotlight();
        ShapedRecipe recipe = new ShapedRecipe(recipeKey, dolphinSpotlight);
        recipe.shape("PGP", "SBS", "SWS");
        recipe.setIngredient('P', Material.PRISMARINE_SHARD);
        recipe.setIngredient('G', Material.GLOWSTONE_DUST);
        recipe.setIngredient('S', Material.SEAGRASS);
        recipe.setIngredient('B', Material.BUBBLE_CORAL_FAN);
        recipe.setIngredient('W', Material.WATER_BUCKET);
        getServer().addRecipe(recipe);

        // Register event listener for Dolphin Spotlight
        getServer().getPluginManager().registerEvents(this, this);
    }

    private ItemStack createDolphinSpotlight() {
        // Create a new ItemStack for the Dolphin Spotlight with custom display name and lore
        ItemStack item = new ItemStack(Material.TORCH);
        item.setAmount(1);
        item.getItemMeta().setDisplayName("Dolphin Spotlight");
        item.getItemMeta().setLore(Arrays.asList("Hold in hotbar or off-hand to gain", "Dolphin's Grace and Water Breathing"));
        return item;

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && item.getType() == Material.TORCH && item.getItemMeta().getDisplayName().equals("Dolphin Spotlight")) {
            if (!player.isSneaking()) {
                // First stage: give Dolphin's Grace and Water Breathing
                player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 20 * 10, 0, true, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 20 * 10, 0, true, false));
            } else {
                // Second stage: shoot Bubble Beam and spawn water floor
                player.launchProjectile(BubbleBeam.class);
                player.getLocation().add(0, -1, 0).getBlock().setType(Material.WATER);
                player.getLocation().add(0, -1, 1).getBlock().setType(Material.WATER);
                player.getLocation().add(1, -1, 0).getBlock().setType(Material.WATER);
                player.getLocation().add(1, -1, 1).getBlock().setType(Material.WATER);
                player.getLocation().add(-1, -1, 0).getBlock().setType(Material.WATER);
                player.getLocation().add(-1, -1, 1).getBlock().setType(Material.WATER);
                player.getLocation().add(0, -1, -1).getBlock().setType(Material.WATER);
                player.getLocation().add(1, -1, -1).getBlock().setType(Material.WATER);


            }
        }
    }
}
