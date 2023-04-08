package spotlight.spotlight.utils.items.use;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SummonHandler {
    public SummonHandler() {
    }

    public static void activate(Player player) {
        if (player.getCooldown(Material.LIGHT) <= 0) {
            List<EntityType> et = new ArrayList();
            EntityType[] allowed = new EntityType[]{EntityType.COW, EntityType.SHEEP, EntityType.PIG};

            while(et.size() < 5) {
                et.add(allowed[(new Random()).nextInt(allowed.length)]);
            }

            Iterator var3 = et.iterator();

            while(var3.hasNext()) {
                EntityType e = (EntityType)var3.next();
                player.getWorld().spawnEntity(player.getLocation(), e);
            }

            player.setCooldown(Material.LIGHT, 6000);
        }

    }
}
