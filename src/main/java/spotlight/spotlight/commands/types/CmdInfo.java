package spotlight.spotlight.commands.types;

import com.ronanplugins.PermissionNode;
import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.commands.SpotCommand;
import com.ronanplugins.messages.MessagesCore;
import com.ronanplugins.messages.Messenger;
import com.ronanplugins.utils.PlayerManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdInfo implements SpotCommand {
    public CmdInfo() {
    }

    public String getName() {
        return "get";
    }

    public void execute(CommandSender sendi, String label, String[] args) {
        PlayerManager manager = SpotlightSMP.getInstance().getManager();
        Player player = (Player)sendi;
        String var10001;
        Integer var10003;
        if (args.length != 1 && PermissionNode.ADMIN.check(sendi)) {
            if (args.length == 2) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null) {
                    if (manager.getSpotlightAsString(target).length() == 0) {
                        Messenger.sms(sendi, MessagesCore.GET_OTHER_NONE.get().replace("{0}", target.getName()));
                    } else {
                        Messenger.sms(sendi, MessagesCore.GET_OTHER_VALUE.get().replace("{0}", target.getName()).replace("{1}", manager.getSpotlightAsString(target)));
                    }

                    var10001 = MessagesCore.GET_OTHER_DOWNLIGHTS.get().replace("{0}", target.getName());
                    var10003 = manager.getDownLights(target);
                    Messenger.sms(sendi, var10001.replace("{1}", "" + var10003));
                }
            }
        } else {
            if (manager.getSpotlightAsString(player).length() == 0) {
                MessagesCore.GET_NONE.send(sendi);
            } else {
                Messenger.sms(sendi, MessagesCore.GET_VALUE.get().replace("{0}", manager.getSpotlightAsString(player)));
            }

            var10001 = MessagesCore.GET_DOWNLIGHTS.get();
            var10003 = manager.getDownLights(player);
            Messenger.sms(sendi, var10001.replace("{0}", "" + var10003));
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
        return true;
    }
}
