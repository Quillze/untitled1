package spotlight.spotlight;

import org.bukkit.command.CommandSender;

public enum PermissionNode {
    ADMIN("admin");

    private final String node;
    private static final String prefix = "spotlight.";

    private PermissionNode(String node) {
        this.node = "spotlight." + node;
    }

    public boolean check(CommandSender sendi) {
        return SpotlightSMP.getInstance().getPerms().checkPerm(this.node, sendi);
    }

    public static boolean check(CommandSender sendi, String check) {
        return SpotlightSMP.getInstance().getPerms().checkPerm(check, sendi);
    }

    public static boolean getLocation(CommandSender sendi, String location) {
        return check(sendi, "spotlight.location." + location);
    }

    public static boolean getPermissionGroup(CommandSender sendi, String group) {
        return check(sendi, "spotlight.group." + group);
    }
}
