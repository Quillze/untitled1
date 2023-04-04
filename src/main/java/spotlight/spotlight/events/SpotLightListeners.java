package spotlight.spotlight.events;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.invs.SpotInv;
import com.ronanplugins.utils.LightTypes;
import com.ronanplugins.utils.PlayerManager;
import com.ronanplugins.utils.Runable;
import com.ronanplugins.utils.SpotlightHelper;
import com.ronanplugins.utils.items.Drops;
import com.ronanplugins.utils.items.ItemHandler;
import com.ronanplugins.utils.items.Spotlight;
import com.ronanplugins.utils.items.Trigger_Type;
import com.ronanplugins.utils.items.use.LavaHandler;
import com.ronanplugins.utils.items.use.ReviveHandler;
import com.ronanplugins.utils.items.use.RiptideHandler;
import com.ronanplugins.utils.items.use.UseItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class SpotLightListeners implements Listener {
    private final PlayerManager playerManager;
    private final SpotlightSMP plugin;

    public SpotLightListeners(SpotlightSMP plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getManager();
        plugin.setListener(this);
    }

    @EventHandler
    public void smith(SmithItemEvent e) {
        if (e.getCurrentItem() != null && e.getCurrentItem().getType().name().contains("NETHERITE")) {
            e.setCancelled(true);
            PlayerManager.actionBar((Player)e.getWhoClicked(), "&cSmithing Netherrite Tools are Disallowed!");
        }

    }

    @EventHandler
    void craft(PrepareItemCraftEvent e) {
        if (e.getInventory().getResult() != null && e.getInventory().getResult().getType().name().contains("NETHERITE")) {
            ItemStack item = e.getInventory().getResult();
            if (item == null) {
                return;
            }

            Map<Enchantment, Integer> enchants = new HashMap();
            ItemStack[] var4 = e.getInventory().getMatrix();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                ItemStack matrix = var4[var6];
                if (matrix != null && !matrix.getEnchantments().isEmpty()) {
                    enchants.putAll(matrix.getEnchantments());
                }
            }

            item.addEnchantments(enchants);
        }

    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        if (SpotlightSMP.getInstance().isSmpEnabled()) {
            Drops[] var2 = Drops.values();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Drops drop = var2[var4];
                if (e.getRecipe().getResult().isSimilar(drop.item())) {
                    e.setCancelled(true);
                    break;
                }
            }

        }
    }

    @EventHandler
    public void onCombust(EntityDamageByBlockEvent event) {
        if (event.getEntity() instanceof Item) {
            ItemStack item = ((Item)event.getEntity()).getItemStack();
            if (ItemHandler.getSpotlight(item) == Spotlight.ALTER) {
                event.getEntity().setFireTicks(0);
                event.getEntity().setInvulnerable(true);
            }
        }

    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player)e.getEntity();
            if (this.playerManager.has(player, LightTypes.DOWN_LIGHT)) {
                int amount = this.playerManager.getDownLights(player);
                if (amount >= 5) {
                    e.setDamage(e.getDamage() + 1.0);
                    PlayerManager.actionBar(player, "&cIncoming Damage amped +1 due to Downlight Level 5");
                }
            }

            if (e.getCause() == DamageCause.FALL) {
                ItemStack item = player.getInventory().getItemInMainHand();
                Spotlight spotlight = ItemHandler.getSpotlight(item);
                if (spotlight == null) {
                    item = player.getInventory().getItemInOffHand();
                    spotlight = ItemHandler.getSpotlight(item);
                }

                if (spotlight == Spotlight.AIR) {
                    e.setCancelled(true);
                    PlayerManager.actionBar(player, "&fAir Spotlight stopped fall damage!");
                }
            }
        }
    }

    @EventHandler
    public void onExplode(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player player = (Player)e.getDamager();
            if (this.playerManager.has(player, LightTypes.DOWN_LIGHT) && this.playerManager.getDownLights(player) >= 2) {
                ItemStack itemUsed = player.getInventory().getItemInMainHand();
                if (player.getCooldown(itemUsed.getType()) > 0) {
                    e.setCancelled(true);
                } else {
                    player.setCooldown(itemUsed.getType(), 25);
                }
            }
        }

        if (e.getEntity() instanceof Item) {
            ItemStack item = ((Item)e.getEntity()).getItemStack();
            if (ItemHandler.getSpotlight(item) == Spotlight.ALTER) {
                e.setCancelled(true);
            }
        }

    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onSpotlightUse(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getClickedBlock() != null && e.getClickedBlock().getState() instanceof InventoryHolder) {
            InventoryHolder holder = (InventoryHolder)e.getClickedBlock().getState();
            switch (holder.getInventory().getType()) {
                case CHEST:
                case BARREL:
                    if (this.playerManager.has(player, LightTypes.DOWN_LIGHT)) {
                        int amount = this.playerManager.getDownLights(player);
                        if (amount >= 4) {
                            e.setCancelled(true);
                            PlayerManager.actionBar(player, "&cCan't open container due to Downlight Level 4");
                        }
                    }
            }
        }

        UseItem.handle(e);
    }

    @EventHandler
    public void interactAt(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        Spotlight spotlight = ItemHandler.getSpotlight(item);
        if (spotlight == null) {
            item = player.getInventory().getItemInOffHand();
            spotlight = ItemHandler.getSpotlight(item);
        }

        if (spotlight != null) {
            event.setCancelled(true);
            if (event.getRightClicked() instanceof LivingEntity) {
                final LivingEntity target = (LivingEntity)event.getRightClicked();
                if (event.getPlayer().getInventory().getItemInMainHand().hasItemMeta()) {
                    if (event.getPlayer().getCooldown(Material.LIGHT) > 0) {
                        return;
                    }

                    if (spotlight == Spotlight.PUSH) {
                        target.teleport(target.getLocation().add(0.0, 10.0, 0.0));
                        target.setVelocity(event.getPlayer().getEyeLocation().getDirection().setY(0.2).multiply(2.5));
                        (new BukkitRunnable() {
                            public void run() {
                                if (target.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR)) {
                                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 40, 1));
                                } else {
                                    this.cancel();
                                }

                            }
                        }).runTaskTimer(this.plugin, 0L, 20L);
                        SpotlightHelper.deactivate(player);
                        target.addPotionEffect(Spotlight.PUSH.getPotionEffect()[0]);
                        if (target instanceof Player) {
                            this.playerManager.set((Player)target, LightTypes.TRAILS, 1);
                        }

                        event.getPlayer().setCooldown(Material.LIGHT, 400);
                        Runable.uuids.add(player.getUniqueId());
                    }
                }
            }
        }

        item = event.getPlayer().getInventory().getItemInMainHand();
        Drops drop = ItemHandler.getCraftable(item);
        if (drop == null) {
            item = player.getInventory().getItemInOffHand();
            drop = ItemHandler.getCraftable(item);
        }

        if (drop != null) {
            event.setCancelled(true);
        }

        if (ItemHandler.isRiptide(event.getPlayer().getInventory().getItemInMainHand())) {
            event.setCancelled(true);
        } else if (ItemHandler.isRiptide(event.getPlayer().getInventory().getItemInOffHand())) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onCoolDown(PlayerCoolDownEvent e) {
        Player player = e.getPlayer();
        if (!e.getHasCooldown() && this.playerManager.has(player, LightTypes.SPOTLIGHT) && this.playerManager.getPower(player) == Spotlight.PUSH) {
            if (player.getInventory().getItemInMainHand().getType().equals(Material.LIGHT)) {
                player.getInventory().getItemInMainHand().addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
            } else {
                ItemStack[] var3 = player.getInventory().getContents();
                int var4 = var3.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    ItemStack stack = var3[var5];
                    if (stack.hasItemMeta()) {
                        ItemMeta meta = stack.getItemMeta();

                        assert meta != null;

                        if (meta.hasCustomModelData() && meta.getCustomModelData() == Spotlight.PUSH.getModelID()) {
                            player.getInventory().getItemInMainHand().addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
                            break;
                        }
                    }
                }
            }

            Runable.uuids.remove(player.getUniqueId());
        }

    }

    @EventHandler(
            priority = EventPriority.HIGH
    )
    public void onDrop(PlayerDropItemEvent e) {
        ItemStack stack = e.getItemDrop().getItemStack();
        if (stack.getType().equals(Material.LIGHT)) {
            if (ItemHandler.getSpotlight(stack) != null) {
                e.setCancelled(true);
            }

            if (ItemHandler.getCraftable(stack) == Drops.DOWNLIGHT) {
                e.setCancelled(true);
            }
        }

        if (ItemHandler.isRiptide(stack)) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onPick(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Spotlight spotlight = ItemHandler.getSpotlight(e.getItem().getItemStack());
            if (spotlight != null) {
                PlayerManager manager = SpotlightSMP.getInstance().getManager();
                Player target = (Player)e.getEntity();
                if (SpotlightSMP.getInstance().getManager().isActive(target)) {
                    SpotlightSMP.getInstance().getManager().toggleActive(target);
                    SpotlightHelper.deactivate(target);
                }

                e.setCancelled(true);
                e.getItem().remove();
                ItemStack[] var5 = target.getInventory().getContents();
                int var6 = var5.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    ItemStack stack = var5[var7];
                    if (stack != null && ItemHandler.getSpotlight(stack) != null) {
                        ItemMeta meta = stack.getItemMeta();

                        assert meta != null;

                        meta.setCustomModelData(spotlight.getModelID());
                        meta.setDisplayName(spotlight.getLabel());
                        stack.setItemMeta(meta);
                        manager.set(target, LightTypes.SPOTLIGHT, spotlight.name());
                        manager.remove(target, LightTypes.IS_ACTIVE);
                        stack.removeEnchantment(Enchantment.ARROW_KNOCKBACK);
                        target.setCooldown(Material.LIGHT, 0);
                        PlayerManager.actionBar(target, "&aNew &f" + spotlight.getLabel() + " &aset!");
                        return;
                    }
                }

                manager.set(target, LightTypes.SPOTLIGHT, spotlight.name());
                manager.remove(target, LightTypes.IS_ACTIVE);
                target.getInventory().addItem(new ItemStack[]{spotlight.item()});
            }

        }
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onItemMove(InventoryClickEvent e) {
        Player player = (Player)e.getWhoClicked();
        if (!e.getSlotType().equals(SlotType.OUTSIDE)) {
            if (e.getClickedInventory() != null) {
                SpotInv.click(e);
                if (!e.isCancelled()) {
                    ReviveHandler.onInv(e);
                    if (!e.isCancelled()) {
                        if (e.getHotbarButton() >= 0) {
                            ItemStack hotbar = player.getInventory().getItem(e.getHotbarButton());
                            if (hotbar != null && (hotbar.getType().equals(Material.LIGHT) || ItemHandler.isRiptide(hotbar))) {
                                e.setCancelled(true);
                                return;
                            }
                        }

                        if (e.getClickedInventory().getType().equals(InventoryType.PLAYER) && !e.getView().getTopInventory().getType().equals(InventoryType.CRAFTING) && !e.getView().getTopInventory().getType().equals(InventoryType.WORKBENCH) && e.getCurrentItem() != null && (e.getCurrentItem().getType().equals(Material.LIGHT) || ItemHandler.isRiptide(e.getCurrentItem()))) {
                            e.setCancelled(true);
                        }

                    }
                }
            }
        }
    }

    @EventHandler
    public void onDragonDeath(EntityDeathEvent deathEvent) {
        if (deathEvent.getEntity().getType().equals(EntityType.ENDER_DRAGON)) {
            deathEvent.getDrops().add(Spotlight.ALTER.item());
        }

    }

    @EventHandler
    public void onItemChangeContainer(InventoryMoveItemEvent e) {
        if (e.getItem().getType() == Material.LIGHT) {
            e.setCancelled(true);
        }

    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onItemPick(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        Bukkit.getScheduler().runTaskLater(SpotlightSMP.getInstance(), () -> {
            checkSpotlightInHands(p, p.getInventory().getItemInMainHand(), false);
        }, 1L);
    }

    @EventHandler
    public void onItemHover(PlayerItemHeldEvent e) {
        checkSpotlightInHands(e.getPlayer(), e.getPlayer().getInventory().getItem(e.getNewSlot()), true);
    }

    public static void checkSpotlightInHands(Player player, ItemStack item, boolean switched_item) {
        PlayerManager playerManager = SpotlightSMP.getInstance().getManager();
        Spotlight spotlight = ItemHandler.getSpotlight(item);
        if (spotlight == null) {
            item = player.getInventory().getItemInOffHand();
            spotlight = ItemHandler.getSpotlight(item);
        }

        String var10000;
        if (spotlight == null) {
            if (playerManager.isActive(player.getPlayer())) {
                playerManager.toggleActive(player.getPlayer());
                SpotlightHelper.deactivate(player.getPlayer());
                Spotlight power = SpotlightSMP.getInstance().getManager().getPower(player);
                if (power != null) {
                    String str = power.name();
                    var10000 = str.substring(0, 1).toUpperCase();
                    str = var10000 + str.substring(1).toLowerCase();
                    PlayerManager.actionBar(player, "&7Spotlight &f" + str + " &7deactivated");
                }
            }

        } else {
            if (Arrays.asList(spotlight.getTriggerType()).contains(Trigger_Type.HELD_IN_HAND)) {
                if (!playerManager.isActive(player.getPlayer())) {
                    playerManager.toggleActive(player.getPlayer());
                }

                SpotlightHelper.activate(player.getPlayer(), Trigger_Type.HELD_IN_HAND);
                if (switched_item) {
                    String str = spotlight.name();
                    var10000 = str.substring(0, 1).toUpperCase();
                    str = var10000 + str.substring(1).toLowerCase();
                    switch (spotlight) {
                        case ALTER:
                            PlayerManager.actionBar(player, "&dSpotlight " + str + " activated");
                            break;
                        default:
                            PlayerManager.actionBar(player, "&aSpotlight &f" + str + " &aactivated");
                    }
                }
            }

        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent joinEvent) {
        Player player = joinEvent.getPlayer();
        if (player.getGameMode() == GameMode.SPECTATOR && !player.isOp()) {
            player.setGameMode(GameMode.SURVIVAL);
        }

        if (!player.isOp() && player.isFlying()) {
            player.setFlying(false);
        }

        if (this.playerManager.has(player, LightTypes.COOLDOWN)) {
            player.setCooldown(Material.LIGHT, this.playerManager.getSavedCooldown(player));
        }

        if (player.hasCooldown(Material.LIGHT)) {
            Runable.uuids.add(player.getUniqueId());
        }

        if (this.playerManager.has(player, LightTypes.DOWN_LIGHT)) {
            this.playerManager.setDownLight(player);
            this.playerManager.downLightEffects(player);
        }

        if (SpotlightSMP.getInstance().isSmpEnabled()) {
            this.startPower(player);
        }

        RiptideHandler.removeRiptideItem(player);
    }

    public void startPower(Player player) {
        if (!this.playerManager.has(player, LightTypes.SPOTLIGHT)) {
            List<Spotlight> powers = Spotlight.getRerollable();
            Spotlight currentPower = (Spotlight)powers.get((new Random()).nextInt(powers.size()));
            this.playerManager.set(player, LightTypes.SPOTLIGHT, currentPower.name());
            player.getInventory().addItem(new ItemStack[]{this.playerManager.getSpotlightItem(player)});
        } else {
            boolean hasCorrect = false;
            ItemStack[] var9 = player.getInventory().getContents();
            int var4 = var9.length;

            int var5;
            ItemStack item;
            Spotlight spotlight;
            for(var5 = 0; var5 < var4; ++var5) {
                item = var9[var5];
                spotlight = ItemHandler.getSpotlight(item);
                if (spotlight != null && this.playerManager.getPower(player) == spotlight) {
                    hasCorrect = true;
                    break;
                }
            }

            if (!hasCorrect) {
                var9 = player.getInventory().getContents();
                var4 = var9.length;

                for(var5 = 0; var5 < var4; ++var5) {
                    item = var9[var5];
                    spotlight = ItemHandler.getSpotlight(item);
                    if (spotlight != null) {
                        item.setAmount(0);
                    }
                }

                player.getInventory().addItem(new ItemStack[]{this.playerManager.getSpotlightItem(player)});
            }
        }

        Spotlight var10001 = this.playerManager.getPower(player);
        PlayerManager.actionBar(player, "&f" + var10001.getLabel() + " &aloaded!");
    }

    @EventHandler
    public void lavaWalk(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (e.getTo() != null) {
            ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
            Spotlight spotlight = ItemHandler.getSpotlight(item);
            if (spotlight == null) {
                item = player.getInventory().getItemInOffHand();
                spotlight = ItemHandler.getSpotlight(item);
                if (spotlight == null) {
                    return;
                }
            }

            switch (spotlight) {
                case LAVA:
                    LavaHandler.handle(e);
                default:
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent quitEvent) {
        Player player = quitEvent.getPlayer();
        Runable.uuids.remove(player.getUniqueId());
        if (player.hasCooldown(Material.LIGHT)) {
            this.playerManager.setCooldown(player, player.getCooldown(Material.LIGHT));
        }

    }

    @EventHandler
    public void onReSpawn(PlayerRespawnEvent deathEvent) {
        Player player = deathEvent.getPlayer();
        if (SpotlightSMP.getInstance().isSmpEnabled()) {
            this.playerManager.addDownLight(player);
            this.startPower(player);
        }

        this.playerManager.set(player, LightTypes.IS_ACTIVE, 0);
        this.playerManager.downLightEffects(player);
    }

    @EventHandler
    public void onPotionEffectRemove(PlayerItemConsumeEvent consumeEvent) {
        Player player = consumeEvent.getPlayer();
        if (consumeEvent.getItem().getType().equals(Material.MILK_BUCKET) && (this.playerManager.isActive(player) || this.playerManager.has(player, LightTypes.DOWN_LIGHT))) {
            consumeEvent.setCancelled(true);
        }

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent deathEvent) {
        Player player = deathEvent.getEntity();
        List<ItemStack> drops = new ArrayList();
        Iterator var4 = deathEvent.getDrops().iterator();

        ItemStack stack;
        while(var4.hasNext()) {
            stack = (ItemStack)var4.next();
            if (stack != null) {
                if (stack.hasItemMeta()) {
                    ItemMeta meta = stack.getItemMeta();

                    assert meta != null;

                    if (!meta.hasCustomModelData() && !ItemHandler.isRiptide(stack)) {
                        drops.add(stack);
                    }
                } else {
                    drops.add(stack);
                }
            }
        }

        this.playerManager.remove(player, LightTypes.IS_ACTIVE);
        deathEvent.getDrops().clear();
        var4 = drops.iterator();

        while(var4.hasNext()) {
            stack = (ItemStack)var4.next();
            deathEvent.getDrops().add(stack);
        }

        deathEvent.getDrops().add(Drops.SHARD.item());
    }

    @EventHandler
    public void onDrillSpot(BlockBreakEvent breakEvent) {
        Player player = breakEvent.getPlayer();
        Spotlight spotlight = this.playerManager.getPower(player);
        if (spotlight != null && spotlight == Spotlight.DRILL && this.playerManager.isActive(player)) {
            Block block = breakEvent.getBlock();
            List<Block> lastTwoBlocks = player.getLastTwoTargetBlocks((Set)null, 6);
            if (lastTwoBlocks.size() != 2 || !((Block)lastTwoBlocks.get(1)).getType().isOccluding()) {
                return;
            }

            Block target = (Block)lastTwoBlocks.get(0);
            Block adjacentlock = (Block)lastTwoBlocks.get(1);
            Location[] locations_Up_Down;
            Location[] locations_DIRECT;
            Location[] var10;
            int var11;
            int var12;
            Location per;
            if (target.getFace(adjacentlock) == BlockFace.NORTH || target.getFace(adjacentlock) == BlockFace.SOUTH) {
                locations_Up_Down = new Location[]{this.getLocation(block, false, BlockFace.UP), this.getLocation(block, false, BlockFace.DOWN), this.getLocation(block, true, BlockFace.UP), this.getLocation(block, true, BlockFace.DOWN)};
                locations_DIRECT = new Location[]{this.getLocation(block, false, BlockFace.WEST), this.getLocation(block, true, BlockFace.WEST), this.getLocation(block, false, BlockFace.EAST), this.getLocation(block, true, BlockFace.EAST)};
                var10 = locations_DIRECT;
                var11 = locations_DIRECT.length;

                for(var12 = 0; var12 < var11; ++var12) {
                    per = var10[var12];
                    per.getBlock().breakNaturally();
                }

                var10 = locations_Up_Down;
                var11 = locations_Up_Down.length;

                for(var12 = 0; var12 < var11; ++var12) {
                    per = var10[var12];
                    per.getBlock().breakNaturally();
                    per.getBlock().getRelative(BlockFace.WEST).breakNaturally();
                    per.getBlock().getRelative(BlockFace.EAST).breakNaturally();
                    this.getLocation(per.getBlock(), true, BlockFace.WEST).getBlock().breakNaturally();
                    this.getLocation(per.getBlock(), true, BlockFace.EAST).getBlock().breakNaturally();
                }
            }

            if (target.getFace(adjacentlock) == BlockFace.EAST || target.getFace(adjacentlock) == BlockFace.WEST) {
                locations_Up_Down = new Location[]{this.getLocation(block, false, BlockFace.UP), this.getLocation(block, false, BlockFace.DOWN), this.getLocation(block, true, BlockFace.UP), this.getLocation(block, true, BlockFace.DOWN)};
                locations_DIRECT = new Location[]{this.getLocation(block, false, BlockFace.NORTH), this.getLocation(block, true, BlockFace.NORTH), this.getLocation(block, false, BlockFace.SOUTH), this.getLocation(block, true, BlockFace.SOUTH)};
                var10 = locations_DIRECT;
                var11 = locations_DIRECT.length;

                for(var12 = 0; var12 < var11; ++var12) {
                    per = var10[var12];
                    per.getBlock().breakNaturally();
                }

                var10 = locations_Up_Down;
                var11 = locations_Up_Down.length;

                for(var12 = 0; var12 < var11; ++var12) {
                    per = var10[var12];
                    per.getBlock().breakNaturally();
                    per.getBlock().getRelative(BlockFace.NORTH).breakNaturally();
                    per.getBlock().getRelative(BlockFace.SOUTH).breakNaturally();
                    this.getLocation(per.getBlock(), true, BlockFace.NORTH).getBlock().breakNaturally();
                    this.getLocation(per.getBlock(), true, BlockFace.SOUTH).getBlock().breakNaturally();
                }
            }

            if (target.getFace(adjacentlock) == BlockFace.DOWN || target.getFace(adjacentlock) == BlockFace.UP) {
                locations_Up_Down = new Location[]{this.getLocation(block, false, BlockFace.WEST), this.getLocation(block, true, BlockFace.WEST), this.getLocation(block, false, BlockFace.EAST), this.getLocation(block, true, BlockFace.EAST)};
                locations_DIRECT = new Location[]{this.getLocation(block, false, BlockFace.NORTH), this.getLocation(block, true, BlockFace.NORTH), this.getLocation(block, false, BlockFace.SOUTH), this.getLocation(block, true, BlockFace.SOUTH)};
                var10 = locations_DIRECT;
                var11 = locations_DIRECT.length;

                for(var12 = 0; var12 < var11; ++var12) {
                    per = var10[var12];
                    per.getBlock().breakNaturally();
                    per.getBlock().getRelative(BlockFace.EAST).breakNaturally();
                    per.getBlock().getRelative(BlockFace.WEST).breakNaturally();
                    this.getLocation(per.getBlock(), true, BlockFace.WEST).getBlock().breakNaturally();
                    this.getLocation(per.getBlock(), true, BlockFace.EAST).getBlock().breakNaturally();
                }

                var10 = locations_Up_Down;
                var11 = locations_Up_Down.length;

                for(var12 = 0; var12 < var11; ++var12) {
                    per = var10[var12];
                    per.getBlock().breakNaturally();
                    per.getBlock().getRelative(BlockFace.NORTH).breakNaturally();
                    per.getBlock().getRelative(BlockFace.SOUTH).breakNaturally();
                }
            }

            ItemStack stack = player.getInventory().getItemInMainHand();
            if (stack.hasItemMeta()) {
                Damageable damageable = (Damageable)stack.getItemMeta();
                if (damageable != null) {
                    damageable.setDamage(damageable.getDamage() + 9);
                    stack.setItemMeta(damageable);
                }
            }
        }

    }

    private Location getLocation(Block block, boolean multi, BlockFace face) {
        return multi ? block.getRelative(face).getLocation().getBlock().getRelative(face).getLocation().clone() : block.getRelative(face).getLocation().clone();
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onMobAgro(EntityTargetEvent e) {
        if (e.getTarget() instanceof Player && e.getEntity() instanceof Monster) {
            Player target = (Player)e.getTarget();
            ItemStack item = target.getInventory().getItemInMainHand();
            Spotlight spotlight = ItemHandler.getSpotlight(item);
            if (spotlight == null) {
                item = target.getInventory().getItemInOffHand();
                spotlight = ItemHandler.getSpotlight(item);
            }

            if (spotlight == Spotlight.MOB_FRIENDLY || spotlight == Spotlight.FIRE) {
                e.setCancelled(true);
                e.setTarget((Entity)null);
            }
        }

    }
}
