package spotlight.spotlight.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerCoolDownEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Material type;
    private final Player player;
    private boolean cancelled;

    public PlayerCoolDownEvent(Player player, Material material) {
        this.player = player;
        this.type = material;
    }

    public boolean getHasCooldown() {
        return this.player.hasCooldown(this.type);
    }

    public Player getPlayer() {
        return this.player;
    }

    public Material getType() {
        return this.type;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
