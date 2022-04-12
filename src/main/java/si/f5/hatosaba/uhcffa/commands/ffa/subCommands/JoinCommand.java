package si.f5.hatosaba.uhcffa.commands.ffa.subCommands;

import com.lielamar.lielsutils.bukkit.commands.StandaloneCommand;
import com.lielamar.lielsutils.bukkit.commands.SuperCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.arena.Arena;
import si.f5.hatosaba.uhcffa.arena.ArenaManager;
import si.f5.hatosaba.uhcffa.arena.ArenaState;
import si.f5.hatosaba.uhcffa.kit.KitManager;
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
        super("join", "ffa.use");

        this.plugin = plugin;
        this.parent = parent;
    }

    @Override
    public boolean runCommand(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("only execute player");
            return false;
        }
        CustomPlayer customPlayer = Uhcffa.getCustomPlayer(commandSender);
        KitManager.getInstance().selectToKit(customPlayer.getPlayer(), KitManager.getInstance().getKit("standard"), false);
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

