package si.f5.hatosaba.uhcffa.commands.admin.subcommands.ffa;

import com.lielamar.lielsutils.bukkit.commands.StandaloneCommand;
import com.lielamar.lielsutils.bukkit.commands.SuperCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.arena.ArenaManager;

import java.util.ArrayList;
import java.util.List;

public class SetLobby extends StandaloneCommand {

    private final ArenaManager arenaManager = Uhcffa.getInstance().getArenaManager();
    private final Uhcffa plugin;
    private final SuperCommand parent;

    public SetLobby(@NotNull Uhcffa plugin, @NotNull SuperCommand parent) {
        super("setlobby", "ffa.admin");

        this.plugin = plugin;
        this.parent = parent;
    }

    @Override
    public boolean runCommand(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("only execute player");
            return false;
        }

        Player player = (Player) commandSender;
        Uhcffa.getInstance().config().setLobby(player.getLocation());
        player.sendMessage("ロビーを設定しました");

        return true;
    }

    @Override
    public List<String> tabOptions(@NotNull CommandSender commandSender, @NotNull String[] args) {
        return new ArrayList<>();
    }

    @Override
    public void noPermissionEvent(@NotNull CommandSender commandSender) {
        this.parent.noPermissionEvent(commandSender);
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }
}
