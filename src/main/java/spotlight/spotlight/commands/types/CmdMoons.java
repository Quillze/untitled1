package spotlight.spotlight.commands.types;
import com.ronanplugins.PermissionNode;
import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.commands.SpotCommand;
import com.ronanplugins.messages.MessagesCore;
import com.ronanplugins.messages.Messenger;
import com.ronanplugins.utils.Moon;
import com.ronanplugins.utils.Moon_Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class CmdMoons implements SpotCommand {
    public CmdMoons() {
    }

    public String getName() {
        return "set_moon";
    }

    public void execute(CommandSender sendi, String label, String[] args) {
        if (args.length >= 3) {
            try {
                Moon_Types type = Moon_Types.valueOf(args[1].toUpperCase());
                World world = Bukkit.getWorld(args[2]);
                if (world == null) {
                    Messenger.sms(sendi, MessagesCore.MOON_WORLD.get().replace("{0}", args[2]));
                    return;
                }

                Moon moon = SpotlightSMP.getInstance().getMoonHandler().getMoon(world);
                if (moon == null) {
                    Messenger.sms(sendi, MessagesCore.MOON_INACTIVE.get().replace("{0}", args[2]));
                    return;
                }

                moon.start(type);
                Messenger.sms(sendi, MessagesCore.MOON_SET.get().replace("{0}", type.name()));
            } catch (IllegalArgumentException var7) {
                Messenger.sms(sendi, MessagesCore.MOON_UNKNOWN.get().replace("{0}", args[1]));
            }
        } else {
            Messenger.sms(sendi, MessagesCore.MOON_USAGE.get().replace("%command%", label));
        }

    }

    public List<String> tabComplete(CommandSender sendi, String[] args) {
        List<String> list = new ArrayList();
        if (args.length == 2) {
            Moon_Types[] var4 = Moon_Types.values();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Moon_Types type = var4[var6];
                if (type.name().toLowerCase().startsWith(args[1].toLowerCase())) {
                    list.add(type.name());
                }
            }
        } else if (args.length == 3) {
            Iterator var8 = Bukkit.getWorlds().iterator();

            while(var8.hasNext()) {
                World world = (World)var8.next();
                if (world.getName().toLowerCase().startsWith(args[2].toLowerCase())) {
                    list.add(world.getName());
                }
            }
        }

        return list;
    }

    public boolean permission(CommandSender sendi) {
        return PermissionNode.ADMIN.check(sendi);
    }
}
