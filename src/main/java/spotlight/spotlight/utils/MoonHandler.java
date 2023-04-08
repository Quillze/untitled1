package spotlight.spotlight.utils;

import com.ronanplugins.SpotlightSMP;
import com.ronanplugins.file.FileOther;
import com.ronanplugins.file.FileOther.FILETYPE;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

public class MoonHandler {
    List<Moon> worldMoons = new ArrayList();

    public MoonHandler() {
    }

    public void load() {
        FileOther.FILETYPE config = SpotlightSMP.getInstance().getFiles().getType(FILETYPE.CONFIG);
        Iterator var2 = config.getStringList("moon_worlds").iterator();

        while(var2.hasNext()) {
            String world = (String)var2.next();
            World _w = Bukkit.getWorld(world);
            if (_w != null) {
                this.worldMoons.add(new Moon(_w));
            }
        }

    }

    public @Nullable Moon getMoon(World world) {
        Iterator var2 = this.worldMoons.iterator();

        Moon moon;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            moon = (Moon)var2.next();
        } while(moon.getWorld() != world);

        return moon;
    }

    public boolean isActive(World world) {
        Iterator var2 = this.worldMoons.iterator();

        Moon moon;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            moon = (Moon)var2.next();
        } while(moon.getWorld() != world);

        return moon.isActive();
    }

    public Moon_Types getMoonType(World world) {
        Moon moon = this.getMoon(world);
        return moon != null && moon.isActive() ? moon.getCurrent() : Moon_Types.NONE;
    }
}
