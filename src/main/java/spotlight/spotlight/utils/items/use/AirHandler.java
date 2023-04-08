package spotlight.spotlight.utils.items.use;
import com.ronanplugins.utils.Vector3D;
import com.ronanplugins.utils.VectorUtils;
import com.ronanplugins.utils.items.Trigger_Type;
import org.bukkit.Color;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class AirHandler {
    public AirHandler() {
    }

    public static void activate(Player player, Trigger_Type triggerType) {
        if (triggerType != Trigger_Type.HELD_IN_HAND) {
            if (player.getCooldown(Material.LIGHT) <= 0) {
                Location loc_start = player.getEyeLocation().clone().subtract(0.0, 0.3, 0.0);
                double distanceFromEyes = 1.5;
                double distanceFromEyeCenter = 0.3;
                RayTraceResult result = player.getWorld().rayTrace(loc_start, loc_start.getDirection(), 150.0, FluidCollisionMode.NEVER, false, 0.25, (en) -> {
                    return en.getEntityId() != player.getEntityId();
                });
                if (result != null) {
                    Vector hit_pos = result.getHitPosition();
                    Vector hit_start = loc_start.toVector();
                    hit_start.add(VectorUtils.rotateVector(new Vector(distanceFromEyes, 0.0, distanceFromEyeCenter), loc_start.getYaw(), loc_start.getPitch()));
                    Vector vector = Vector3D.getDirectionBetweenLocations(hit_start.clone(), hit_pos.clone());

                    for(double i = 1.0; i <= loc_start.distance(new Location(loc_start.getWorld(), hit_pos.getX(), hit_pos.getY(), hit_pos.getZ())); i += 0.5) {
                        vector.multiply(i);
                        Location particle_loc = loc_start.clone().add(vector);

                        assert particle_loc.getWorld() != null;

                        particle_loc.getWorld().spawnParticle(Particle.REDSTONE, particle_loc, 1, new Particle.DustOptions(Color.WHITE, 1.0F));
                        vector.normalize();
                    }

                    if (result.getHitEntity() != null) {
                        Entity hit = result.getHitEntity();
                        if (hit instanceof LivingEntity) {
                            ((LivingEntity)hit).damage(7.0);
                        }
                    }
                }

            }
        }
    }
}
