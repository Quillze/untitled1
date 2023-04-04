package spotlight.spotlight.commands.types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import spotlight.spotlight.commands.*;

public class CmdCraftings implements SpotCommand {
    public CmdCraftings() {
    }

    public String getName() {
        return "get_craftable";
    }

    public void execute(CommandSender sendi, String label, String[] args) {
        if (args.length >= 2) {
            if (Objects.equals(args[1].toLowerCase(), "__view__")) {
                SpotInv.showCraft((Player)sendi);
            } else {
                ItemStack result = null;
                Iterator var5 = SpotlightSMP.getInstance().getCrafting().getRecipeList().iterator();

                while(var5.hasNext()) {
                    ShapedRecipe c = (ShapedRecipe)var5.next();
                    ItemMeta meta = c.getResult().getItemMeta();

                    assert meta != null;

                    if (meta.getDisplayName().equalsIgnoreCase(args[1].replace("|", " "))) {
                        result = c.getResult();
                        break;
                    }
                }

                if (result != null) {
                    ((Player)sendi).getInventory().addItem(new ItemStack[]{result.clone()});
                    Messenger.sms(sendi, MessagesCore.CRAFTABLE_GIVE.get().replace("{0}", result.getItemMeta().getDisplayName()));
                } else {
                    Messenger.sms(sendi, MessagesCore.CRAFTABLE_UNKNOWN.get().replace("{0}", args[1]));
                }
            }
        } else {
            Messenger.sms(sendi, MessagesCore.CRAFTABLE_USAGE.get().replace("%command%", label));
        }

    }

    public List<String> tabComplete(CommandSender sendi, String[] args) {
        List<String> list = new ArrayList();
        if (args.length == 2) {
            if ("__view__".startsWith(args[1].toLowerCase())) {
                list.add("__view__");
            }

            Iterator var4 = SpotlightSMP.getInstance().getCrafting().getRecipeList().iterator();

            while(var4.hasNext()) {
                ShapedRecipe c = (ShapedRecipe)var4.next();
                ItemMeta meta = c.getResult().getItemMeta();

                assert meta != null;

                if (meta.getDisplayName().toLowerCase().startsWith(args[1].toLowerCase())) {
                    list.add(meta.getDisplayName().replace(" ", "|"));
                }
            }
        }

        return list;
    }

    public boolean permission(CommandSender sendi) {
        return PermissionNode.ADMIN.check(sendi);
    }

    public boolean isPlayerOnly() {
        return true;
    }
}
