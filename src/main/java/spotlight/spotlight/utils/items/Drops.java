package spotlight.spotlight.utils.items;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.utils.items.ItemCreator.ITEM_TYPE;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public enum Drops {
    SHARD(0, "Spotlight Shard", "_SHARD"),
    DOWNLIGHT(8, "Downlight", "_DOWNLIGHT"),
    REVIVE(9, "Revive Spotlight", "_REVIVE"),
    REROLL(10, "ReRoll Spotlight", "_REROLL"),
    BUNDLE_PVP(16, "PvP Bundle", "_BUNDLE_PVP"),
    BUNDLE_VILLAGER(15, "Villager Bundle", "_BUNDLE_VILLAGER");

    private final String label;
    private final String key;
    private final int modelID;

    private Drops(int modelID, String label, String key) {
        this.label = label;
        this.modelID = modelID;
        this.key = key;
    }

    public static Drops ofValue(int modelID) {
        Drops[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Drops p = var1[var3];
            if (p.modelID == modelID) {
                return p;
            }
        }

        return null;
    }

    public int getModelID() {
        return this.modelID;
    }

    public String getLabel() {
        return this.label;
    }

    public String getKey() {
        return this.key;
    }

    public ItemStack item() {
        return (new ItemCreator(Material.LIGHT)).setModelID(this.getModelID()).setDisplayName(this.getLabel()).create(ITEM_TYPE.DROP);
    }

    public NamespacedKey getNameSpacedKey() {
        return new NamespacedKey(SpotlightSMP.getInstance(), "SPOT_CRAFTABLE" + this.getKey());
    }
}
