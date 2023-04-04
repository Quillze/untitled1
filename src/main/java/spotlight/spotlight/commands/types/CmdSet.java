package spotlight.spotlight.commands.types;

import com.ronanplugins.PermissionNode;
import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.commands.SpotCommand;
import com.ronanplugins.messages.MessagesCore;
import com.ronanplugins.messages.Messenger;
import com.ronanplugins.utils.LightTypes;
import com.ronanplugins.utils.PlayerManager;
import com.ronanplugins.utils.SpotlightHelper;
import com.ronanplugins.utils.items.ItemHandler;
import com.ronanplugins.utils.items.Spotlight;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CmdSet implements SpotCommand {
    public CmdSet() {
    }

    public String getName() {
        return "set";
    }

    public boolean isPlayerOnly() {
        return true;
    }

    public void execute(CommandSender sendi, String label, String[] args) {
        PlayerManager manager = SpotlightSMP.getInstance().getManager();
        if (args.length == 3) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                Messenger.sms(sendi, MessagesCore.SET_PLAYER.get().replace("{0}", args[1]));
                return;
            }

            try {
                Spotlight spotlight = Spotlight.valueOf(args[2]);
                if (SpotlightSMP.getInstance().getManager().isActive(target)) {
                    SpotlightSMP.getInstance().getManager().toggleActive(target);
                    SpotlightHelper.deactivate(target);
                }

                ItemStack[] var7 = target.getInventory().getContents();
                int var8 = var7.length;

                for(int var9 = 0; var9 < var8; ++var9) {
                    ItemStack stack = var7[var9];
                    if (stack != null && ItemHandler.getSpotlight(stack) != null) {
                        ItemMeta meta = stack.getItemMeta();

                        assert meta != null;

                        meta.setCustomModelData(spotlight.getModelID());
                        meta.setDisplayName(spotlight.getLabel());
                        stack.setItemMeta(meta);
                        manager.set(target, LightTypes.SPOTLIGHT, spotlight.name());
                        manager.remove(target, LightTypes.IS_ACTIVE);
                        stack.removeEnchantment(Enchantment.ARROW_KNOCKBACK);
                        target.setCooldown(Material.LIGHT, 0);
                        Messenger.sms(sendi, MessagesCore.SET_SUCCESS.get().replace("{0}", spotlight.getLabel()).replace("{1}", target.getName()));
                        PlayerManager.actionBar(target, "&aNew &f" + spotlight.getLabel() + " &aset!");
                        return;
                    }
                }

                manager.set(target, LightTypes.SPOTLIGHT, spotlight.name());
                manager.remove(target, LightTypes.IS_ACTIVE);
                target.getInventory().addItem(new ItemStack[]{spotlight.item()});
                Messenger.sms(sendi, MessagesCore.SET_SUCCESS.get().replace("{0}", spotlight.getLabel()).replace("{1}", target.getName()));
            } catch (IllegalArgumentException var12) {
                Messenger.sms(sendi, MessagesCore.SET_INVALID.get().replace("{0}", args[2]));
            }
        } else {
            Messenger.sms(sendi, MessagesCore.SET_USAGE.get().replace("%command%", label));
        }

    }

    public List<String> tabComplete(CommandSender sendi, String[] args) {
        List<String> list = new ArrayList();
        if (args.length == 2) {
            Iterator var4 = Bukkit.getOnlinePlayers().iterator();

            while(var4.hasNext()) {
                Player p = (Player)var4.next();
                if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                    list.add(p.getName());
                }
            }
        } else if (args.length == 3) {
            Spotlight[] var8 = Spotlight.values();
            int var9 = var8.length;

            for(int var6 = 0; var6 < var9; ++var6) {
                Spotlight power = var8[var6];
                if (power.name().toLowerCase().startsWith(args[2].toLowerCase())) {
                    list.add(power.name());
                }
            }
        }

        return list;
    }

    public boolean permission(CommandSender sendi) {
        return PermissionNode.ADMIN.check(sendi);
    }
}
