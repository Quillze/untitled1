package spotlight.spotlight.utils.items.use;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.utils.PlayerManager;
import com.ronanplugins.utils.items.use.PhaseHandler.PhaseBlockInfo;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;

public class OreHandler {
    private static final HashMap<UUID, BukkitRunnable> runnable = new HashMap();
    static final Material[] ores;
    static final int[] weight;

    public OreHandler() {
    }

    public static void activate(Player player) {
        if (player.getCooldown(Material.LIGHT) <= 0) {
            start(player);
            player.setCooldown(Material.LIGHT, 6000);
        }
    }

    private static void start(final Player player) {
        final int totalweight = Arrays.stream(weight).sum();
        (new BukkitRunnable() {
            int steps = 0;
            int seconds = 0;
            Block prev;

            public void run() {
                Block down = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
                PlayerManager.actionBar(player, "&aOre Spotlight activated for &f" + (30 - this.seconds) + "&a seconds!");
                if (!down.equals(this.prev) && !Arrays.asList(PhaseBlockInfo.blacklist).contains(down.getType()) && !(down.getState() instanceof InventoryHolder)) {
                    int rng = (new Random()).nextInt(totalweight);
                    int ore = 0;
                    int current_weight = 0;

                    for(int i = 0; i < OreHandler.ores.length; ++i) {
                        if (rng <= OreHandler.weight[i] + current_weight) {
                            ore = i;
                            break;
                        }

                        current_weight += OreHandler.weight[i];
                    }

                    down.setType(OreHandler.ores[ore]);
                    this.prev = down;
                }

                ++this.steps;
                if (this.steps >= 4) {
                    this.steps = 0;
                    ++this.seconds;
                }

                if (this.seconds >= 30) {
                    player.setCooldown(Material.LIGHT, 6000);
                    this.cancel();
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§cOre Spotlight terminated!"));
                }

            }
        }).runTaskTimer(SpotlightSMP.getInstance(), 5L, 5L);
    }

    static {
        ores = new Material[]{Material.ANCIENT_DEBRIS, Material.DIAMOND_ORE, Material.EMERALD_ORE, Material.IRON_ORE, Material.COPPER_ORE, Material.COAL_ORE};
        weight = new int[]{1, 100, 350, 500, 700, 800};
    }
}
