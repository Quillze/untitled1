package spotlight.spotlight.commands.types;
import com.ronanplugins.PermissionNode;
import com.ronanplugins.commands.SpotCommand;
import com.ronanplugins.invs.SpotInv;
import com.ronanplugins.messages.MessagesCore;
import com.ronanplugins.messages.Messenger;
import com.ronanplugins.utils.items.Drops;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdDrops implements SpotCommand {
    public CmdDrops() {
    }

    public String getName() {
        return "get_drops";
    }

    public void execute(CommandSender sendi, String label, String[] args) {
        if (args.length >= 2) {
            if (Objects.equals(args[1].toLowerCase(), "__view__")) {
                SpotInv.showDrops((Player)sendi);
            } else {
                try {
                    Drops item = Drops.valueOf(args[1]);
                    ((Player)sendi).getInventory().addItem(new ItemStack[]{item.item()});
                    Messenger.sms(sendi, MessagesCore.DROPS_GIVE.get().replace("{0}", item.name()));
                } catch (IllegalArgumentException var5) {
                    Messenger.sms(sendi, MessagesCore.DROPS_UNKNOWN.get().replace("{0}", args[1]));
                }
            }
        } else {
            Messenger.sms(sendi, MessagesCore.DROPS_USAGE.get().replace("%command%", label));
        }

    }

    public List<String> tabComplete(CommandSender sendi, String[] args) {
        List<String> list = new ArrayList();
        if (args.length == 2) {
            if ("__view__".startsWith(args[1].toLowerCase())) {
                list.add("__view__");
            }

            Drops[] var4 = Drops.values();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Drops c = var4[var6];
                if (c.name().toLowerCase().startsWith(args[1].toLowerCase())) {
                    list.add(c.name());
                }
            }
        }

        return list;
    }

    public boolean permission(CommandSender sendi) {
        return PermissionNode.ADMIN.check(sendi);
    }

    public boolean isPlayerOnly() {
        return true;
    }
}
