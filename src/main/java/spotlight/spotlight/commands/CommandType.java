package spotlight.spotlight.commands;

import com.ronanplugins.commands.types.CmdCraftings;
import com.ronanplugins.commands.types.CmdDownlight;
import com.ronanplugins.commands.types.CmdDrop;
import com.ronanplugins.commands.types.CmdDrops;
import com.ronanplugins.commands.types.CmdInfo;
import com.ronanplugins.commands.types.CmdMoons;
import com.ronanplugins.commands.types.CmdReload;
import com.ronanplugins.commands.types.CmdReset;
import com.ronanplugins.commands.types.CmdSet;
import com.ronanplugins.commands.types.CmdStart;

public enum CommandType {
    DROPS(new CmdDrops()),
    CRAFTABLES(new CmdCraftings()),
    GET(new CmdInfo()),
    RELOAD(new CmdReload()),
    SET(new CmdSet()),
    RESET(new CmdReset()),
    MOONS(new CmdMoons()),
    START(new CmdStart()),
    DOWNLIGHT(new CmdDownlight()),
    DROP(new CmdDrop());

    private final SpotCommand cmd;
    private boolean debugOnly = false;

    private CommandType(SpotCommand cmd) {
        this.cmd = cmd;
    }

    private CommandType(SpotCommand cmd, boolean debugOnly) {
        this.cmd = cmd;
        this.debugOnly = debugOnly;
    }

    public boolean isDebugOnly() {
        return this.debugOnly;
    }

    public SpotCommand getCmd() {
        return this.cmd;
    }
}
