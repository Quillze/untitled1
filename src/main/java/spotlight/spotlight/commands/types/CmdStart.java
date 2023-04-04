package spotlight.spotlight.commands.types;

import com.ronanplugins.PermissionNode;
import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.commands.SpotCommand;
import com.ronanplugins.file.FileOther;
import com.ronanplugins.file.FileOther.FILETYPE;
import com.ronanplugins.messages.Messenger;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdStart implements SpotCommand {
    public CmdStart() {
    }

    public String getName() {
        return "start";
    }

    public void execute(CommandSender sendi, String label, String[] args) {
        FileOther.FILETYPE config = SpotlightSMP.getInstance().getFiles().getType(FILETYPE.CONFIG);
        config.setValue("Enabled", true);
        config.save();
        SpotlightSMP.getInstance().setSmpEnabled(config.getBoolean("Enabled"));
        Iterator var5 = Bukkit.getOnlinePlayers().iterator();

        while(var5.hasNext()) {
            Player player = (Player)var5.next();
            SpotlightSMP.getInstance().getListeners().startPower(player);
        }

        Messenger.sms(sendi, "&bSpotlight SMP has commenced! Everyone has been given their spotlights!");
    }

    public List<String> tabComplete(CommandSender sendi, String[] args) {
        return null;
    }

    public boolean permission(CommandSender sendi) {
        return PermissionNode.ADMIN.check(sendi);
    }

    public boolean isPlayerOnly() {
        return true;
    }
}
