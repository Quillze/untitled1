package spotlight.spotlight.commands;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.utils.LightTypes;
import com.ronanplugins.utils.PlayerManager;
import com.ronanplugins.utils.items.Drops;
import com.ronanplugins.utils.items.Spotlight;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpotlightCommand implements CommandExecutor, TabCompleter {
    private final SpotlightSMP spotlights;
    private final PlayerManager manager;

    public SpotlightCommand(SpotlightSMP spotlights) {
        this.manager = spotlights.getManager();
        this.spotlights = spotlights;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("spotlight") && args.length == 2) {
            List<String> results = new ArrayList();
            if (args[0].equalsIgnoreCase("get")) {
                Spotlight[] var6 = Spotlight.values();
                int var7 = var6.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    Spotlight spotlight = var6[var8];
                    results.add(spotlight.name());
                }

                return results;
            }
        }

        return null;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length == 0) {
                player.sendMessage("Spotlight plugin v-" + this.spotlights.getDescription().getVersion());
                return true;
            }

            String var10001;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("get")) {
                    if (this.manager.getSpotlightAsString(player).length() == 0) {
                        player.sendMessage("§7Your Current Spotlight: §cNONE");
                    } else {
                        var10001 = this.manager.getSpotlightAsString(player);
                        player.sendMessage("§7Your Current Spotlight: " + var10001);
                    }

                    Integer var18 = this.manager.getDownLights(player);
                    player.sendMessage("§7You have §c" + var18 + " §7Downlights.");
                    return true;
                }
            } else if (args.length == 2) {
                Player target;
                if (args[0].equalsIgnoreCase("get")) {
                    if (args[1].equalsIgnoreCase("--all")) {
                        Spotlight[] var14 = Spotlight.values();
                        int var15 = var14.length;

                        for(int var16 = 0; var16 < var15; ++var16) {
                            Spotlight power = var14[var16];
                            player.getInventory().addItem(new ItemStack[]{power.item()});
                        }

                        return true;
                    }

                    target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        if (this.manager.getSpotlightAsString(target).length() == 0) {
                            player.sendMessage("§7" + target.getName() + "'s Current Spotlight: §cNONE");
                        } else {
                            var10001 = target.getName();
                            player.sendMessage("§7" + var10001 + "'s Current Spotlight: " + this.manager.getSpotlightAsString(target));
                        }

                        var10001 = target.getName();
                        player.sendMessage("§7" + var10001 + " have §c" + this.manager.getDownLights(target) + " §7Downlights.");
                        return true;
                    }

                    try {
                        Spotlight spotlight = Spotlight.valueOf(args[1]);
                        ItemStack[] var8 = player.getInventory().getContents();
                        int var9 = var8.length;

                        for(int var10 = 0; var10 < var9; ++var10) {
                            ItemStack stack = var8[var10];
                            if (stack != null && stack.hasItemMeta()) {
                                ItemMeta meta = stack.getItemMeta();

                                assert meta != null;

                                if (meta.hasCustomModelData() && meta.getCustomModelData() != Drops.DOWNLIGHT.getModelID()) {
                                    meta.setCustomModelData(spotlight.getModelID());
                                    meta.setDisplayName(spotlight.getLabel());
                                    stack.setItemMeta(meta);
                                    this.manager.set(player, LightTypes.SPOTLIGHT, spotlight.name());
                                    this.manager.remove(player, LightTypes.IS_ACTIVE);
                                    stack.removeEnchantment(Enchantment.ARROW_KNOCKBACK);
                                    player.setCooldown(Material.LIGHT, 0);
                                    return true;
                                }
                            }
                        }

                        this.manager.set(player, LightTypes.SPOTLIGHT, spotlight.name());
                        this.manager.remove(player, LightTypes.IS_ACTIVE);
                        player.getInventory().addItem(new ItemStack[]{spotlight.item()});
                    } catch (IllegalArgumentException var13) {
                        player.sendMessage("That Power didnt work");
                    }
                } else if (args[0].equalsIgnoreCase("reset")) {
                    target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        this.manager.remove(player, LightTypes.DOWN_LIGHT);
                        this.manager.remove(player, LightTypes.SPOTLIGHT);
                        this.manager.remove(player, LightTypes.IS_ACTIVE);
                        player.getInventory().setItem(8, (ItemStack)null);
                        player.sendMessage("Reset of " + target.getName() + " is Done.");
                        return true;
                    }
                }
            }
        }

        return false;
    }
}