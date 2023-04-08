package spotlight.spotlight.utils.items.use;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class FlightHandler {
    public FlightHandler() {
    }

    public static void activate(Player player) {
        if (!canTrigger(player)) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }
    }

    public static void deactivate(Player player) {
        if (!canTrigger(player)) {
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }

    private static boolean canTrigger(Player player) {
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("&cSpotlight can't enabled while in Creative Mode."));
            return true;
        } else {
            return false;
        }
    }
}
