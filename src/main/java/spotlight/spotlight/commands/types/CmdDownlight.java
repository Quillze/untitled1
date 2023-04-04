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

public class CmdDownlight implements SpotCommand {
    public CmdDownlight() {
    }

    public String getName() {
        return "set_downlights";
    }

    public boolean isPlayerOnly() {
        return true;
    }

    public void execute(CommandSender sendi, String label, String[] args) {
        PlayerManager manager = SpotlightSMP.getInstance().getManager();
        if (args.length == 3) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                Messenger.sms(sendi, MessagesCore.DOWNLIGHT_PLAYER.get().replace("{0}", args[1]));
                return;
            }

            try {
                int amount = Integer.parseInt(args[2]);
                manager.set(target, LightTypes.DOWN_LIGHT, amount);
                manager.setDownLight(target);
                manager.downLightEffects(target);
                Messenger.sms(sendi, MessagesCore.DOWNLIGHT_SUCCESS.get().replace("{0}", target.getName()).replace("{1}", "" + amount));
            } catch (NumberFormatException var7) {
                Messenger.sms(sendi, MessagesCore.DOWNLIGHT_INVALID.get().replace("{0}", args[2]));
            }
        } else {
            Messenger.sms(sendi, MessagesCore.DOWNLIGHT_USAGE.get().replace("%command%", label));
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
        }

        return list;
    }

    public boolean permission(CommandSender sendi) {
        return PermissionNode.ADMIN.check(sendi);
    }
}
