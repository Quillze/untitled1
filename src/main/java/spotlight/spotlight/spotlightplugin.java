package spotlight.spotlight;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpotlightPlugin extends JavaPlugin implements Listener {

    // Define the list of available spotlights
    private final List<String> spotlightTypes = List.of(
            "Dolphin", "Phase", "Power", "Avoid", "Push", "Invisibility", "Speed", "Resistance", "Teleport", "Fire"
    );

    // Define the two stages for each spotlight
    private final String STAGE_1 = "Stage 1";
    private final String STAGE_2 = "Stage 2";

    // Define the key used to store the spotlight type in the item's metadata
    private final NamespacedKey spotlightTypeKey = new NamespacedKey(this, "spotlight-type");

    // Define the random generator used to assign a random spotlight to each player
    private final Random random = new Random();

    // Define the custom recipe for crafting a fire spotlight
    private final ShapedRecipe fireSpotlightRecipe = new ShapedRecipe(
            new NamespacedKey(this, "fire-spotlight"),
            createFireSpotlight()
    ).shape(
            "AAA",
            "ABA",
            "AAA"
    ).setIngredient('A', Material.BLAZE_ROD).setIngredient('B', Material.NETHER_STAR);

    @Override
    public void onEnable() {
        // Register the plugin's event listener
        getServer().getPluginManager().registerEvents(this, this);

        // Register the custom recipe for the fire spotlight
        getServer().addRecipe(fireSpotlightRecipe);
    }

    @Override
    public void onDisable() {
        // Clean up any resources used by the plugin
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // When a player joins, assign them a random spotlight
        Player player = event.getPlayer();
        ItemStack spotlight = createRandomSpotlight();
        player.getInventory().addItem(spotlight);
        player.sendMessage(ChatColor.GREEN + "You have been given a " + spotlight.getItemMeta().getDisplayName());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // When a player right-clicks with a spotlight, activate its ability
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item != null && isSpotlight(item)) {
            event.setCancelled(true); // Prevent the item from being used as a regular item
            if (getSpotlightStage(item) == 1) {
                activateStage1Ability(player, item);
            } else {
                activateStage2Ability(player, item);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // When a player moves, check if they have a push spotlight equipped and push entities in front of them
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemIn
