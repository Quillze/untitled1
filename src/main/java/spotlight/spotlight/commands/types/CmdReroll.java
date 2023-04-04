package spotlight.spotlight.commands.types;

import com.ronanplugins.PermissionNode;
import com.ronanplugins.commands.SpotCommand;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;

public class CmdReroll implements SpotCommand {
    public CmdReroll() {
    }

    public String getName() {
        return "reroll";
    }

    public void execute(CommandSender sendi, String label, String[] args) {
    }

    public List<String> tabComplete(CommandSender sendi, String[] args) {
        List<String> list = new ArrayList();
        return list;
    }

    public boolean permission(CommandSender sendi) {
        return PermissionNode.ADMIN.check(sendi);
    }
}
