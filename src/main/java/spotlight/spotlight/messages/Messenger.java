package spotlight.spotlight.messages;
import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.file.FileData;
import org.bukkit.command.CommandSender;

public class Messenger implements Message {
    public static Messenger msg = new Messenger();

    public Messenger() {
    }

    public static FileData getLang() {
        return SpotlightSMP.getInstance().getFiles().getLang();
    }

    public FileData lang() {
        return getLang();
    }

    public static void sms(CommandSender sendi, String msg) {
        Message.sms(Messenger.msg, sendi, msg);
    }

    public static String getPrefix() {
        return Message.getPrefix(msg);
    }
}
