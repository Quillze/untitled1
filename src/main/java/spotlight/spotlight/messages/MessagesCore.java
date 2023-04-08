package spotlight.spotlight.messages;

import org.bukkit.command.CommandSender;

public enum MessagesCore {
    NOPERMISSION("NoPermission.Basic"),
    INVALID("Invalid"),
    RELOAD("Reload"),
    USAGE("Usage"),
    GET_NONE("Get.Current.None"),
    GET_VALUE("Get.Current.Value"),
    GET_DOWNLIGHTS("Get.Current.Downlights"),
    GET_OTHER_NONE("Get.Player.None"),
    GET_OTHER_VALUE("Get.Player.Value"),
    GET_OTHER_DOWNLIGHTS("Get.Player.Downlights"),
    SET_USAGE("Set.Usage"),
    SET_INVALID("Set.Invalid"),
    SET_PLAYER("Set.Player"),
    SET_SUCCESS("Set.Success"),
    RESET_USAGE("Reset.Usage"),
    RESET_SUCCESS("Reset.Success"),
    RESET_INVALID("Reset.Invalid"),
    MOON_START_FIRE("Moon.Start.Fire"),
    MOON_START_WATER("Moon.Start.Water"),
    MOON_ACTION_FIRE("Moon.Action.Fire"),
    MOON_ACTION_WATER("Moon.Action.Water"),
    MOON_SET("Moon.Set"),
    MOON_USAGE("Moon.Usage"),
    MOON_UNKNOWN("Moon.Unknown"),
    MOON_WORLD("Moon.World"),
    MOON_INACTIVE("Moon.Inactive"),
    DROPS_GIVE("Drops.Give"),
    DROPS_UNKNOWN("Drops.Unknown"),
    DROPS_USAGE("Drops.Usage"),
    CRAFTABLE_GIVE("Craftable.Give"),
    CRAFTABLE_UNKNOWN("Craftable.Unknown"),
    CRAFTABLE_USAGE("Craftable.Usage"),
    DOWNLIGHT_USAGE("Downlight.Usage"),
    DOWNLIGHT_PLAYER("Downlight.Player"),
    DOWNLIGHT_SUCCESS("Downlight.Success"),
    DOWNLIGHT_INVALID("Downlight.Invalid"),
    DROP_INVALID("Drop.Invalid"),
    DROP_USAGE("Drop.Usage"),
    UNBAN_MESSAGE("Unbanned");

    final String section;
    private static final String pre = "Messages.";

    private MessagesCore(String section) {
        this.section = section;
    }

    public void send(CommandSender sendi) {
        Messenger.sms(sendi, Messenger.getLang().getString("Messages." + this.section));
    }

    public String get() {
        return Messenger.getLang().getString("Messages." + this.section);
    }
}
