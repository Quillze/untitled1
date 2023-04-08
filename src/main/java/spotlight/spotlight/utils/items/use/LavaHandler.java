package spotlight.spotlight.utils.items.use;

import com.ronanplugins.SpotlightSMP;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class LavaHandler {
    static HashMap<Location, BlockInfo> blocks = new HashMap();
    static BukkitTask task;

    public LavaHandler() {
    }

    public static void handle(PlayerMoveEvent e) {
        assert e.getTo() != null;

        Block bloc = e.getTo().clone().subtract(0.0, 1.0, 0.0).getBlock();
        checkAndSet(bloc);
        checkAndSet(bloc.getRelative(BlockFace.NORTH));
        checkAndSet(bloc.getRelative(BlockFace.EAST));
        checkAndSet(bloc.getRelative(BlockFace.SOUTH));
        checkAndSet(bloc.getRelative(BlockFace.WEST));
        removeBlockLater();
    }

    private static void checkAndSet(Block bloc) {
        if (bloc.isLiquid() && bloc.getType().equals(Material.LAVA)) {
            Location loc = bloc.getLocation();
            if (blocks.containsKey(loc)) {
                ((BlockInfo)blocks.get(loc)).timer = 0;
            } else {
                blocks.put(loc, new BlockInfo(bloc));
            }
        }

    }

    private static void removeBlockLater() {
        if (task == null || task.isCancelled()) {
            task = (new BukkitRunnable() {
                public void run() {
                    List<Location> remove = new ArrayList();
                    Iterator var2 = LavaHandler.blocks.entrySet().iterator();

                    while(var2.hasNext()) {
                        Map.Entry<Location, BlockInfo> info = (Map.Entry)var2.next();
                        BlockInfo binfo = (BlockInfo)info.getValue();
                        binfo.timer += 5;
                        if (binfo.timer > 100) {
                            remove.add((Location)info.getKey());
                            binfo.block.setType(binfo.previous, false);
                        }
                    }

                    var2 = remove.iterator();

                    while(var2.hasNext()) {
                        Location loc = (Location)var2.next();
                        LavaHandler.blocks.remove(loc);
                    }

                    if (LavaHandler.blocks.isEmpty()) {
                        this.cancel();
                    }

                }
            }).runTaskTimer(SpotlightSMP.getInstance(), 5L, 5L);
        }

    }

    private static class BlockInfo {
        Block block;
        Material previous;
        int timer;

        BlockInfo(Block block) {
            this.block = block;
            this.previous = block.getType();
            block.setType(Material.OBSIDIAN);
        }
    }
}
