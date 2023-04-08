package spotlight.spotlight.utils;

import com.ronanplugins.utils.items.Spotlight;
import org.bukkit.entity.Player;

public class SpotLightPlayer {
    private final Player player;
    private final PlayerManager manager;

    public SpotLightPlayer(Player player, PlayerManager manager) {
        this.manager = manager;
        this.player = player;
    }

    public Spotlight getPower() {
        return this.manager.getPower(this.player);
    }

    public void setPower(Spotlight power) {
        this.manager.set(this.player, LightTypes.SPOTLIGHT, power.name());
        this.manager.toggleActive(this.player);
    }

    public void addDownLight() {
        int downlights = this.manager.getDownLights(this.player);
        this.manager.set(this.player, LightTypes.DOWN_LIGHT, downlights + 1);
    }

    public Player getPlayer() {
        return this.player;
    }
}
