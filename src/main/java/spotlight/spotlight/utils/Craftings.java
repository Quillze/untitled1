package spotlight.spotlight.utils;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.utils.items.Drops;
import com.ronanplugins.utils.items.ItemCreator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

public class Craftings {
    private final List<ShapedRecipe> recipeList = new ArrayList();

    public Craftings() {
    }

    public void load() {
        this.reRoll();
        this.netherite();
        this.revive();
        this.bundles();
    }

    public void revive() {
        ShapedRecipe recipe = new ShapedRecipe(Drops.REVIVE.getNameSpacedKey(), Drops.REVIVE.item());
        recipe.shape(new String[]{"SDS", "TWT", "TDT"});
        recipe.setIngredient('S', Material.LIGHT);
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        recipe.setIngredient('T', Material.TOTEM_OF_UNDYING);
        recipe.setIngredient('W', Material.WITHER_SKELETON_SKULL);
        Bukkit.addRecipe(recipe);
        this.recipeList.add(recipe);
    }

    public void reRoll() {
        ShapedRecipe recipe = new ShapedRecipe(Drops.REROLL.getNameSpacedKey(), Drops.REROLL.item());
        recipe.shape(new String[]{" L ", "LSL", " L "});
        recipe.setIngredient('L', Material.LIGHT);
        recipe.setIngredient('S', Material.NETHER_STAR);
        Bukkit.addRecipe(recipe);
        this.recipeList.add(recipe);
    }

    public void shard() {
        ShapedRecipe recipe = new ShapedRecipe(Drops.SHARD.getNameSpacedKey(), Drops.SHARD.item());
        recipe.shape(new String[]{" S ", "SNS", " S "});
        recipe.setIngredient('N', Material.NETHER_STAR);
        recipe.setIngredient('S', Material.LIGHT);
        Bukkit.addRecipe(recipe);
        this.recipeList.add(recipe);
    }

    public void netherite() {
        ShapedRecipe pickaxe = new ShapedRecipe(this.getNameSpacedKey("PICKAXE"), (new ItemCreator(Material.NETHERITE_PICKAXE)).create((ItemCreator.ITEM_TYPE)null));
        ShapedRecipe shovel = new ShapedRecipe(this.getNameSpacedKey("SHOVEL"), (new ItemCreator(Material.NETHERITE_SHOVEL)).create((ItemCreator.ITEM_TYPE)null));
        ShapedRecipe hoe = new ShapedRecipe(this.getNameSpacedKey("HOE"), (new ItemCreator(Material.NETHERITE_HOE)).create((ItemCreator.ITEM_TYPE)null));
        pickaxe.shape(new String[]{"NNN", " I ", " S "});
        pickaxe.setIngredient('N', Material.NETHERITE_INGOT);
        pickaxe.setIngredient('I', Material.DIAMOND_PICKAXE);
        pickaxe.setIngredient('S', Material.STICK);
        shovel.shape(new String[]{" N ", " I ", " S "});
        shovel.setIngredient('N', Material.NETHERITE_INGOT);
        shovel.setIngredient('I', Material.DIAMOND_SHOVEL);
        shovel.setIngredient('S', Material.STICK);
        hoe.shape(new String[]{" NN", " I ", " S "});
        hoe.setIngredient('N', Material.NETHERITE_INGOT);
        hoe.setIngredient('I', Material.DIAMOND_HOE);
        hoe.setIngredient('S', Material.STICK);
        Bukkit.addRecipe(pickaxe);
        Bukkit.addRecipe(shovel);
        Bukkit.addRecipe(hoe);
        this.recipeList.add(pickaxe);
        this.recipeList.add(shovel);
        this.recipeList.add(hoe);
    }

    public void bundles() {
        ShapedRecipe bundle_pvp = new ShapedRecipe(Drops.BUNDLE_PVP.getNameSpacedKey(), Drops.BUNDLE_PVP.item());
        ShapedRecipe bundle_villager = new ShapedRecipe(Drops.BUNDLE_VILLAGER.getNameSpacedKey(), Drops.BUNDLE_VILLAGER.item());
        bundle_pvp.shape(new String[]{"DDD", "DND", "DDD"});
        bundle_pvp.setIngredient('D', Material.DIAMOND_BLOCK);
        bundle_pvp.setIngredient('N', Material.NETHERITE_INGOT);
        bundle_villager.shape(new String[]{"EEE", "ENE", "EEE"});
        bundle_villager.setIngredient('E', Material.EMERALD_BLOCK);
        bundle_villager.setIngredient('N', Material.NETHERITE_INGOT);
        Bukkit.addRecipe(bundle_pvp);
        Bukkit.addRecipe(bundle_villager);
        this.recipeList.add(bundle_pvp);
        this.recipeList.add(bundle_villager);
    }

    private NamespacedKey getNameSpacedKey(String key) {
        return new NamespacedKey(SpotlightSMP.getInstance(), "SPOTLIGHT_ITEM_" + key.toUpperCase(Locale.ROOT));
    }

    public List<ShapedRecipe> getRecipeList() {
        return this.recipeList;
    }
}
