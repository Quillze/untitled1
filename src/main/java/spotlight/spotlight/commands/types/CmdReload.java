package spotlight.spotlight.commands.types;

import com.ronanplugins.PermissionNode;
import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.commands.SpotCommand;
import com.ronanplugins.messages.MessagesCore;
import java.util.List;
import org.bukkit.command.CommandSender;

public class CmdReload implements SpotCommand {
    public CmdReload() {
    }

    public String getName() {
        return "reload";
    }

    public void execute(CommandSender sendi, String label, String[] args) {
        SpotlightSMP.getInstance().loadAll();
        MessagesCore.RELOAD.send(sendi);
    }

    public List<String> tabComplete(CommandSender sendi, String[] args) {
        return null;
    }

    public boolean permission(CommandSender sendi) {
        return PermissionNode.ADMIN.check(sendi);
    }
}
