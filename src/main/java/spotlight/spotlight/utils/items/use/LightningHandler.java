package spotlight.spotlight.utils.items.use;

import com.ronanplugins.utils.items.Trigger_Type;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class LightningHandler {
    public LightningHandler() {
    }

    public static void activate(Player player, Trigger_Type triggerType) {
        if (triggerType == Trigger_Type.RIGHT_CLICK) {
            if (player.getCooldown(Material.LIGHT) <= 0) {
                Block block = player.getTargetBlock((Set)null, 50);
                if (block.getType() != Material.AIR) {
                    player.getWorld().strikeLightning(block.getLocation());
                    player.setCooldown(Material.LIGHT, 200);
                }

            }
        }
    }
}
