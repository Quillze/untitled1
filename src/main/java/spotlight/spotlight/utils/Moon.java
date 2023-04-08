package spotlight.spotlight.utils;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.messages.MessagesCore;
import com.ronanplugins.messages.Messenger;
import java.util.Objects;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Moon implements Listener {
    Moon_Types current;
    final World world;
    boolean active;
    private BukkitTask task;
    private static final int TIME_MOON = 14500;

    Moon(World world) {
        this.current = Moon_Types.NONE;
        this.world = world;
        this.load();
    }

    private void load() {
        this.timer();
    }

    public void start(Moon_Types type) {
        if (type == Moon_Types.NONE) {
            this.current = Moon_Types.NONE;
            this.end();
        } else {
            this.current = type;
            this.active = true;
            MessagesCore msg = type == Moon_Types.FIRE ? MessagesCore.MOON_START_FIRE : MessagesCore.MOON_START_WATER;
            Bukkit.getOnlinePlayers().forEach((player) -> {
                Messenger.sms(player, msg.get());
            });
            this.world.setTime(14500L);
            this.moonTask(type);
        }
    }

    private void end() {
        this.active = false;
        if (this.current != Moon_Types.NONE) {
            MessagesCore msg = this.current == Moon_Types.FIRE ? MessagesCore.MOON_ACTION_FIRE : MessagesCore.MOON_ACTION_WATER;
            Bukkit.getOnlinePlayers().forEach((player) -> {
                PlayerManager.actionBar(player, msg.get());
            });
        }

        this.current = Moon_Types.NONE;
        HandlerList.unregisterAll(this);
    }

    private void moonTask(final Moon_Types type) {
        if (this.task != null && !this.task.isCancelled()) {
            this.task.cancel();
        }

        this.task = (new BukkitRunnable() {
            int seconds = 0;

            public void run() {
                this.seconds += 2;
                if (!Moon.this.isActive()) {
                    this.cancel();
                } else {
                    if (this.seconds > 600) {
                        Moon.this.end();
                        this.cancel();
                    } else {
                        MessagesCore msg = type == Moon_Types.FIRE ? MessagesCore.MOON_ACTION_FIRE : MessagesCore.MOON_ACTION_WATER;
                        Bukkit.getOnlinePlayers().forEach((player) -> {
                            PlayerManager.actionBar(player, msg.get());
                        });
                    }

                }
            }
        }).runTaskTimer(SpotlightSMP.getInstance(), 20L, 40L);
        Bukkit.getPluginManager().registerEvents(this, SpotlightSMP.getInstance());
    }

    private void timer() {
        (new BukkitRunnable() {
            int minutes = 0;
            int days = 0;

            public void run() {
                ++this.minutes;
                if (this.minutes >= 20) {
                    this.minutes = 0;
                    ++this.days;
                }

                if (this.days > 20) {
                    if (Moon.this.world.getTime() > 14500L) {
                        this.days = 0;
                        this.minutes = 0;
                        Moon_Types[] moons = new Moon_Types[]{Moon_Types.FIRE, Moon_Types.FIRE, Moon_Types.WATER};
                        Moon.this.start(moons[(new Random()).nextInt(moons.length)]);
                    }
                } else if (Moon.this.isActive()) {
                    this.days = 0;
                    this.minutes = 0;
                }

            }
        }).runTaskTimer(SpotlightSMP.getInstance(), 1200L, 1200L);
    }

    @EventHandler
    void onSpawn(final EntitySpawnEvent e) {
        if (e.getLocation().getWorld() == this.getWorld()) {
            if (e.getEntity() instanceof LivingEntity) {
                (new BukkitRunnable() {
                    public void run() {
                        if (!e.getEntity().getPersistentDataContainer().has(Moon.this.getKey(), PersistentDataType.INTEGER)) {
                            int i;
                            if (Moon.this.getCurrent() == Moon_Types.FIRE) {
                                if (e.getLocation().getY() >= 60.0) {
                                    if (!(e.getEntity() instanceof Monster)) {
                                        return;
                                    }

                                    if (e.getEntityType() == EntityType.DROWNED) {
                                        return;
                                    }

                                    for(i = 0; i < 4; ++i) {
                                        Moon.this.createMob(e.getLocation(), e.getEntityType());
                                    }
                                }
                            } else if (Moon.this.getCurrent() == Moon_Types.WATER) {
                                if (e.getEntity() instanceof Monster && e.getEntityType() != EntityType.DROWNED) {
                                    return;
                                }

                                for(i = 0; i < 4; ++i) {
                                    Moon.this.createMob(e.getLocation(), e.getEntityType());
                                }
                            }

                        }
                    }
                }).runTaskLater(SpotlightSMP.getInstance(), 1L);
            }
        }
    }

    private void createMob(Location loc, EntityType type) {
        Entity entity = ((World)Objects.requireNonNull(loc.getWorld())).spawnEntity(loc, type, true);
        entity.getPersistentDataContainer().set(this.getKey(), PersistentDataType.INTEGER, 1);
    }

    private NamespacedKey getKey() {
        return new NamespacedKey(SpotlightSMP.getInstance(), "MOON_MOB");
    }

    public Moon_Types getCurrent() {
        return this.current;
    }

    public World getWorld() {
        return this.world;
    }

    public boolean isActive() {
        return this.active;
    }
}
