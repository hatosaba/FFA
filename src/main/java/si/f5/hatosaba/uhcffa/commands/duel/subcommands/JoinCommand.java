package si.f5.hatosaba.uhcffa.commands.duel.subcommands;

import com.lielamar.lielsutils.bukkit.commands.StandaloneCommand;
import com.lielamar.lielsutils.bukkit.commands.SuperCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.arena.Arena;
import si.f5.hatosaba.uhcffa.arena.ArenaManager;
import si.f5.hatosaba.uhcffa.arena.ArenaState;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class JoinCommand extends StandaloneCommand {

    private final ArenaManager arenaManager = Uhcffa.getInstance().getArenaManager();
    private final Uhcffa plugin;
    private final SuperCommand parent;

    public JoinCommand(@NotNull Uhcffa plugin, @NotNull SuperCommand parent) {
        super("remove", "duel.use");

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
        final CustomPlayer customPlayer = Uhcffa.getCustomPlayer(playerID);

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
        List<Arena> availableGames = new LinkedList<>();
        for (Arena arena : arenaManager.getArenas().values()) {
            if (arena.getState() == ArenaState.WAITING_FOR_PLAYERS && !arena.isFull()) {
                availableGames.add(arena);
            }
        }
        if (availableGames.isEmpty()) return new ArrayList<>();

        return availableGames.stream().map(Arena::getName).collect(Collectors.toList());
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

