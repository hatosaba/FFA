package si.f5.hatosaba.uhcffa.commands.admin.subcommands.duel;

import com.lielamar.lielsutils.bukkit.commands.StandaloneCommand;
import com.lielamar.lielsutils.bukkit.commands.SuperCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.arena.Arena;
import si.f5.hatosaba.uhcffa.arena.ArenaManager;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveCommand extends StandaloneCommand {

    private final ArenaManager arenaManager = Uhcffa.getInstance().getArenaManager();
    private final Uhcffa plugin;
    private final SuperCommand parent;

    public RemoveCommand(@NotNull Uhcffa plugin, @NotNull SuperCommand parent) {
        super("remove", "ffa.admin");

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
        final String playerID = PlayerConverter.getID(player);
        final CustomPlayer customPlayer = Uhcffa.getInstance().getCustomPlayer(playerID);

        if(args.length == 0) {
            player.sendMessage("足りない");
            return false;
        }

        String arenaName = args[0];
        if (arenaName == null) {
            player.sendMessage("アリーナ名を指定設定してください");
            return false;
        }

        if (!arenaManager.getArenas().containsKey(arenaName)) {
            player.sendMessage("存在しません");
            return false;
        }

        arenaManager.removeArena(arenaName);
        player.sendMessage("削除しました");
        return true;
    }

    @Override
    public List<String> tabOptions(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if (args.length == 1) {
            return arenaManager.getArenas().values().stream().map(Arena::getName).collect(Collectors.toList());
        }
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
