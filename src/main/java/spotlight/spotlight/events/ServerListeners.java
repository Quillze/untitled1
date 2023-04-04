package spotlight.spotlight.events;

import com.ronanplugins.SpotlightSMP;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.inventory.ItemStack;
import spotlight.spotlight.*;

public class ServerListeners implements Listener {
    public ServerListeners(SpotlightSMP plugin) {
        plugin.setListener(this);
    }

    @EventHandler(
            priority = EventPriority.HIGH
    )
    public void onForbiddenItems(InventoryClickEvent clickEvent) {
        if (clickEvent.getClickedInventory() == null && !clickEvent.getSlotType().equals(SlotType.OUTSIDE)) {
            if (clickEvent.getClickedInventory().getType().equals(InventoryType.PLAYER) && !clickEvent.getWhoClicked().getGameMode().equals(GameMode.CREATIVE) && clickEvent.getCurrentItem() != null) {
                ItemStack stack = clickEvent.getCurrentItem();
                switch (stack.getType()) {
                    case NETHERITE_CHESTPLATE:
                    case NETHERITE_HELMET:
                    case NETHERITE_BOOTS:
                    case NETHERITE_LEGGINGS:
                    case NETHERITE_SWORD:
                    case NETHERITE_AXE:
                        clickEvent.getCurrentItem().setAmount(0);
                }
            }

        }
    }

    @EventHandler
    public void onTexturePack(PlayerResourcePackStatusEvent event) {
        PlayerResourcePackStatusEvent.Status status = event.getStatus();
        switch (status) {
            case DECLINED:
                event.getPlayer().kickPlayer("You have to Enable our Custom TexturePack to play.");
            default:
        }
    }
}
