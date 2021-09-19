package si.f5.hatosaba.uhcffa.command;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.mrblobman.spigotcommandlib.CommandHandle;
import io.github.mrblobman.spigotcommandlib.CommandHandler;
import org.bukkit.command.CommandSender;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.socket.ArenaSocket;

public class AutomaticEventCommand implements CommandHandler {

    private final Uhcffa plugin = Uhcffa.instance();

    @CommandHandle(
            command = {"event", "open" },
            description = "",
            permission = "automatic"
    )
    public void open(CommandSender sender) {
        ArenaSocket.sendMessage("open");
        sender.sendMessage("開催しました");
    }

    @CommandHandle(
            command = {"event", "close" },
            description = "",
            permission = "automatic"
    )
    public void close(CommandSender sender) {
        ArenaSocket.sendMessage("close");
        sender.sendMessage("閉鎖しました");
    }
}

