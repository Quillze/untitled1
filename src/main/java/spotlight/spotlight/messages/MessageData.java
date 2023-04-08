package spotlight.spotlight.messages;

import com.ronanplugins.file.FileData;

public interface MessageData {
    String section();

    String prefix();

    FileData file();

    default String get() {
        FileData var10000 = this.file();
        String var10001 = this.prefix();
        return var10000.getString(var10001 + this.section());
    }
}
