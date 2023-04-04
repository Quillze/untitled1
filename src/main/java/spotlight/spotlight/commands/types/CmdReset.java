package spotlight.spotlight.commands.types;

import com.ronanplugins.PermissionNode;
import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.commands.SpotCommand;
import com.ronanplugins.messages.MessagesCore;
import com.ronanplugins.messages.Messenger;
import com.ronanplugins.utils.LightTypes;
import com.ronanplugins.utils.PlayerManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdReset implements SpotCommand {
    public CmdReset() {
    }

    public String getName() {
        return "reset";
    }

    public void execute(CommandSender sendi, String label, String[] args) {
        if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target != null) {
                PlayerManager manager = SpotlightSMP.getInstance().getManager();
                manager.remove(target, LightTypes.DOWN_LIGHT);
                manager.remove(target, LightTypes.SPOTLIGHT);
                manager.remove(target, LightTypes.IS_ACTIVE);
                target.getInventory().setItem(8, (ItemStack)null);
                Messenger.sms(sendi, MessagesCore.RESET_SUCCESS.get().replace("{0}", target.getDisplayName()));
            } else {
                Messenger.sms(sendi, MessagesCore.RESET_INVALID.get().replace("{0}", args[1]));
            }
        } else {
            Messenger.sms(sendi, MessagesCore.RESET_USAGE.get().replace("%command%", label));
        }

    }

    public List<String> tabComplete(CommandSender sendi, String[] args) {
        List<String> list = new ArrayList();
        if (args.length == 2) {
            Iterator var4 = Bukkit.getOnlinePlayers().iterator();

            while(var4.hasNext()) {
                Player p = (Player)var4.next();
                if (p.getName().toLowerCase().startsWith(args[1])) {
                    list.add(p.getName());
                }
            }
        }

        return list;
    }

    public boolean permission(CommandSender sendi) {
        return PermissionNode.ADMIN.check(sendi);
    }
}
