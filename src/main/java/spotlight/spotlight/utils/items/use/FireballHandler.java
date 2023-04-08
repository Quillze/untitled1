package spotlight.spotlight.utils.items.use;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.utils.Moon_Types;
import com.ronanplugins.utils.items.Trigger_Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

public class FireballHandler {
    private static final Map<UUID, Integer> fireBallMap = new HashMap();

    public FireballHandler() {
    }

    public static void shoot(Player player, Trigger_Type triggerType) {
        if (triggerType == Trigger_Type.LEFT_CLICK) {
            if (player.getCooldown(Material.LIGHT) <= 1) {
                boolean moon_active = SpotlightSMP.getInstance().getMoonHandler().getMoonType(player.getWorld()) == Moon_Types.FIRE;
                int clip_size = moon_active ? 6 : 3;
                if (!fireBallMap.containsKey(player.getUniqueId())) {
                    fireBallMap.put(player.getUniqueId(), 1);
                } else {
                    fireBallMap.put(player.getUniqueId(), (Integer)fireBallMap.get(player.getUniqueId()) + 1);
                }

                Player.Spigot var10000 = player.spigot();
                ChatMessageType var10001 = ChatMessageType.ACTION_BAR;
                Object var10004 = fireBallMap.get(player.getUniqueId());
                var10000.sendMessage(var10001, new TextComponent("Fireball: " + var10004 + " / " + clip_size));
                Fireball fireball = (Fireball)player.getWorld().spawnEntity(player.getLocation().add(0.0, 1.0, 0.0).add(player.getEyeLocation().getDirection()).add(player.getEyeLocation().getDirection()), EntityType.FIREBALL);
                fireball.setYield(3.0F);
                if (fireBallMap.containsKey(player.getUniqueId()) && (Integer)fireBallMap.get(player.getUniqueId()) >= clip_size) {
                    player.setCooldown(Material.LIGHT, 300);
                    fireBallMap.remove(player.getUniqueId());
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Fireball §aCharging.... "));
                } else {
                    player.setCooldown(Material.LIGHT, 40);
                }
            }

        }
    }
}
