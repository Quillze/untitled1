package spotlight.spotlight.commands.types;


import com.ronanplugins.PermissionNode;
import com.ronanplugins.commands.SpotCommand;
import com.ronanplugins.messages.MessagesCore;
import com.ronanplugins.messages.Messenger;
import com.ronanplugins.utils.items.Spotlight;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdDrop implements SpotCommand {
    public CmdDrop() {
    }

    public String getName() {
        return "drop_spotlight";
    }

    public boolean isPlayerOnly() {
        return true;
    }

    public void execute(CommandSender sendi, String label, String[] args) {
        if (args.length >= 2) {
            try {
                Spotlight spotlight = Spotlight.valueOf(args[1]);
                Player player = (Player)sendi;
                player.getWorld().dropItemNaturally(player.getLocation(), spotlight.item());
            } catch (IllegalArgumentException var6) {
                Messenger.sms(sendi, MessagesCore.DROP_INVALID.get().replace("{0}", args[2]));
            }
        } else {
            Messenger.sms(sendi, MessagesCore.DROP_USAGE.get().replace("%command%", label));
        }

    }

    public List<String> tabComplete(CommandSender sendi, String[] args) {
        List<String> list = new ArrayList();
        if (args.length == 2) {
            Spotlight[] var4 = Spotlight.values();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Spotlight power = var4[var6];
                if (power.name().toLowerCase().startsWith(args[1].toLowerCase())) {
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
