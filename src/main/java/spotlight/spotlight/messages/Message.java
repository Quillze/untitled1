package spotlight.spotlight.messages;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.file.FileData;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public interface Message {
    FileData lang();

    static void sms(Message messenger, CommandSender sendi, String msg) {
        if (!msg.isEmpty()) {
            Bukkit.getScheduler().runTask(SpotlightSMP.getInstance(), () -> {
                String var10001 = getPrefix(messenger);
                sendi.sendMessage(color(var10001 + msg));
            });
        }

    }

    static String getPrefix(Message messenger) {
        return messenger.lang().getString("Messages.Prefix");
    }

    static String color(String str) {
        return translateHexColorCodes(str);
    }

    static String translateHexColorCodes(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

        for(Matcher matcher = pattern.matcher(message); matcher.find(); matcher = pattern.matcher(message)) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');
            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            char[] var7 = ch;
            int var8 = ch.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                char c = var7[var9];
                builder.append("&").append(c);
            }

            message = message.replace(hexCode, builder.toString());
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
