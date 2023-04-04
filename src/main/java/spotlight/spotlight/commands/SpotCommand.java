package spotlight.spotlight.commands;

import java.util.List;
import org.bukkit.command.CommandSender;

public interface SpotCommand {
    void execute(CommandSender var1, String var2, String[] var3);

    List<String> tabComplete(CommandSender var1, String[] var2);

    boolean permission(CommandSender var1);

    String getName();

    default boolean isDebugOnly() {
        return false;
    }

    default boolean isPlayerOnly() {
        return false;
    }
}
