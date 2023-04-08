package spotlight.spotlight.utils.items.use;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.utils.PlayerManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PhaseHandler {
    public PhaseHandler() {
    }

    public static void activate(Player player) {
        if (player.getCooldown(Material.LIGHT) <= 0) {
            (new PhaseTask(player)).start();
            player.setCooldown(Material.LIGHT, 1200);
        }
    }

    private static class PhaseTask implements Listener {
        Player player;
        List<Vector> spherePoints = new ArrayList();
        HashMap<Location, PhaseBlockInfo> changedBlocks = new HashMap();
        boolean running = false;

        PhaseTask(Player player) {
            this.player = player;
        }

        void start() {
            this.spherePoints = this.getRad(this.player.getLocation(), 3.0);
            Bukkit.getPluginManager().registerEvents(this, SpotlightSMP.getInstance());
            this.timer(this);
        }

        @EventHandler
        void move(PlayerMoveEvent e) {
            if (e.getPlayer() == this.player) {
                if (e.getTo() != null && e.getTo() != e.getFrom()) {
                    Location locRef = e.getTo();
                    if (locRef.getWorld() == null) {
                        return;
                    }

                    Iterator var3 = this.spherePoints.iterator();

                    while(var3.hasNext()) {
                        Vector vector = (Vector)var3.next();
                        Location _loc = locRef.clone().add(vector);
                        if (this.changedBlocks.containsKey(_loc)) {
                            ((PhaseBlockInfo)this.changedBlocks.get(_loc)).timer = (new Random()).nextInt(20) - 10;
                        } else {
                            PhaseBlockInfo info = new PhaseBlockInfo(_loc.getBlock());
                            if (info.isAdded()) {
                                this.changedBlocks.put(_loc, info);
                            }
                        }
                    }
                }

            }
        }

        @EventHandler
        void fall(BlockPhysicsEvent e) {
            if (e.getBlock().getLocation().distance(this.player.getLocation()) < 50.0) {
                e.setCancelled(true);
            }

        }

        private List<Vector> getRad(Location loc, double radius) {
            Block center = loc.getBlock();
            double distanceSquared = radius * radius;
            List<Vector> inRadius = new ArrayList();

            for(int x = (int)(-radius) - 1; x < (int)radius + 1; ++x) {
                for(int y = 0; y < (int)radius + 1; ++y) {
                    for(int z = (int)(-radius) - 1; z < (int)radius + 1; ++z) {
                        Block b = center.getRelative(x, y, z);
                        double manHattanDistance = Math.abs((double)x - loc.getX()) + Math.abs((double)y - loc.getY()) + Math.abs((double)z - loc.getZ());
                        if (manHattanDistance <= radius) {
                            inRadius.add(new Vector(x, y, z));
                        } else if (b.getLocation().distance(loc) <= distanceSquared) {
                            inRadius.add(new Vector(x, y, z));
                        }
                    }
                }
            }

            return inRadius;
        }

        private void timer(final PhaseTask handler) {
            this.running = true;
            (new BukkitRunnable() {
                int ticks = 0;
                int seconds = 0;

                public void run() {
                    if (this.seconds >= 10) {
                        HandlerList.unregisterAll(handler);
                        this.cancel();
                        PhaseTask.this.player.setCooldown(Material.LIGHT, 1200);
                        PhaseTask.this.running = false;
                    } else {
                        ++this.ticks;
                        if (this.ticks >= 20) {
                            this.ticks = 0;
                            ++this.seconds;
                        }

                        PlayerManager.actionBar(PhaseTask.this.player, "&aPhasing through blocks for &f" + (10 - this.seconds) + "&a seconds");
                    }

                }
            }).runTaskTimer(SpotlightSMP.getInstance(), 0L, 1L);
            (new BukkitRunnable() {
                public void run() {
                    if (!PhaseTask.this.running && PhaseTask.this.changedBlocks.isEmpty()) {
                        this.cancel();
                    }

                    List<Location> removing = new ArrayList();
                    Iterator var2 = PhaseTask.this.changedBlocks.entrySet().iterator();

                    while(var2.hasNext()) {
                        Map.Entry<Location, PhaseBlockInfo> data = (Map.Entry)var2.next();
                        PhaseBlockInfo block = (PhaseBlockInfo)data.getValue();
                        if (block.getTimer() >= 60) {
                            block.block.setType(block.getOldMaterial());
                            removing.add((Location)data.getKey());
                        } else {
                            block.timer++;
                        }
                    }

                    var2 = removing.iterator();

                    while(var2.hasNext()) {
                        Location blockx = (Location)var2.next();
                        PhaseTask.this.changedBlocks.remove(blockx);
                    }

                }
            }).runTaskTimer(SpotlightSMP.getInstance(), 0L, 1L);
        }
    }

    public static class PhaseBlockInfo {
        private final Material oldMaterial;
        private final Block block;
        private int timer = (new Random()).nextInt(20) - 10;
        private boolean added;
        public static final Material[] blacklist;

        PhaseBlockInfo(Block block) {
            this.block = block;
            Material type = block.getType();
            this.oldMaterial = type;
            if (type.isBlock() && !Arrays.asList(blacklist).contains(type) && !(block.getState() instanceof InventoryHolder)) {
                block.setType(Material.AIR);
                this.added = true;
            }

        }

        public Material getOldMaterial() {
            return this.oldMaterial;
        }

        public Block getBlock() {
            return this.block;
        }

        public int getTimer() {
            return this.timer;
        }

        public boolean isAdded() {
            return this.added;
        }

        static {
            blacklist = new Material[]{Material.BEDROCK, Material.OBSIDIAN, Material.NETHER_PORTAL, Material.END_PORTAL, Material.END_PORTAL_FRAME, Material.WATER, Material.LAVA, Material.AIR, Material.CHEST, Material.TRAPPED_CHEST, Material.FURNACE, Material.ANVIL, Material.SPAWNER};
        }
    }
}
