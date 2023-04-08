package spotlight.spotlight.utils.items.use;

import com.ronanplugins.utils.Vector3D;
import io.papermc.lib.PaperLib;
import java.util.Set;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class TeleportHandler {
    public TeleportHandler() {
    }

    public static void activate(Player player) {
        if (player.getCooldown(Material.LIGHT) <= 0) {
            Block target = player.getTargetBlock((Set)null, 16);
            if (target.getType() == Material.AIR) {
                for(int i = 1; i < 10; ++i) {
                    target = target.getRelative(BlockFace.DOWN);
                    if (target.getType() != Material.AIR) {
                        break;
                    }
                }

                if (target.getType() == Material.AIR) {
                    return;
                }
            }

            Location oldLoc = player.getLocation();
            Location newLoc = target.getLocation().add(0.0, 1.0, 0.0).setDirection(player.getLocation().getDirection());
            newLoc.setPitch(player.getLocation().getPitch());
            PaperLib.teleportAsync(player, newLoc);
            player.setCooldown(Material.LIGHT, 300);
            Vector vector = Vector3D.getDirectionBetweenLocations(oldLoc.toVector(), newLoc.toVector());

            for(double i = 1.0; i <= oldLoc.distance(newLoc); i += 0.5) {
                vector.multiply(i);
                Location particle_loc = oldLoc.clone().add(vector);

                assert particle_loc.getWorld() != null;

                particle_loc.getWorld().spawnParticle(Particle.REDSTONE, particle_loc, 1, new Particle.DustOptions(Color.BLUE, 1.0F));
                vector.normalize();
            }

        }
    }
}
