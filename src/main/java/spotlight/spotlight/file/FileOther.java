package spotlight.spotlight.file;
import com.ronanplugins.SpotlightSMP;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import spotlight.spotlight.*;

public class FileOther {
    List<FILETYPE> types = new ArrayList();

    public FileOther() {
    }

    void load() {
        this.types.clear();
        FILETYPE[] var1 = FileOther.FILETYPE.values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            FILETYPE type = var1[var3];
            type.load();
            this.types.add(type);
        }

    }

    public static enum FILETYPE implements FileData {
        CONFIG("config");

        private final String fileName;
        private final YamlConfiguration config = new YamlConfiguration();
        private final File file;

        private FILETYPE(String str) {
            this.fileName = str + ".yml";
            this.file = new File(this.plugin().getDataFolder(), this.fileName);
        }

        public Plugin plugin() {
            return SpotlightSMP.getInstance();
        }

        public YamlConfiguration getConfig() {
            return this.config;
        }

        public File getFile() {
            return this.file;
        }

        public String fileName() {
            return this.fileName;
        }
    }
}
