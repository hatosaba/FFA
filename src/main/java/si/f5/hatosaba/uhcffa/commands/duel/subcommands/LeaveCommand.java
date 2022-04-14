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

import java.util.ArrayList;
import java.util.List;

public class LeaveCommand extends StandaloneCommand {

    private final ArenaManager arenaManager = Uhcffa.getInstance().getArenaManager();
    private final Uhcffa plugin;
    private final SuperCommand parent;

    public LeaveCommand(@NotNull Uhcffa plugin, @NotNull SuperCommand parent) {
        super("leave", "");

        this.plugin = plugin;
        this.parent = parent;
    }

    @Override
    public boolean runCommand(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("only execute player");
            return false;
        }
        final Player player = (Player) commandSender;
        final CustomPlayer customPlayer = Uhcffa.getInstance().getCustomPlayer(player);

        if (customPlayer.inArena()) {
            Arena arena = customPlayer.getArena();

            if (arena.getArenaState() == ArenaState.IN_GAME)
                arena.onDeath(customPlayer);
            else
                arena.removePlayer(customPlayer);
            return true;
        }
        return false;
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