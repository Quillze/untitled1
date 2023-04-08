package spotlight.spotlight.utils.items.use;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.utils.PlayerManager;
import com.ronanplugins.utils.items.Spotlight;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class AlterHandler {
    public AlterHandler() {
    }

    public static void activate(Player player) {
        List<Spotlight> powers = new ArrayList(List.of(Spotlight.values()));
        Iterator var2 = powers.iterator();

        while(true) {
            Spotlight power;
            do {
                do {
                    if (!var2.hasNext()) {
                        return;
                    }

                    power = (Spotlight)var2.next();
                } while(power == Spotlight.PUSH);
            } while(power.getPotionEffect() == null);

            PotionEffect[] var4 = power.getPotionEffect();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                PotionEffect effect = var4[var6];
                player.addPotionEffect(effect);
            }
        }
    }

    public static void deactivate(Player player) {
        PlayerManager playerManager = SpotlightSMP.getInstance().getManager();
        List<Spotlight> powers = new ArrayList(List.of(Spotlight.values()));
        if (playerManager.getPower(player) != null && playerManager.getPower(player) == Spotlight.ALTER) {
            Iterator var3 = powers.iterator();

            while(true) {
                Spotlight power;
                do {
                    if (!var3.hasNext()) {
                        return;
                    }

                    power = (Spotlight)var3.next();
                } while(power.getPotionEffect() == null);

                PotionEffect[] var5 = power.getPotionEffect();
                int var6 = var5.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    PotionEffect effect = var5[var7];
                    player.removePotionEffect(effect.getType());
                }
            }
        }
    }
}
