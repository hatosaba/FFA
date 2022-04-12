package si.f5.hatosaba.uhcffa.commands.admin.subcommands;

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

public class SaveCommand extends StandaloneCommand {

    private final ArenaManager arenaManager = Uhcffa.getInstance().getArenaManager();
    private final Uhcffa plugin;
    private final SuperCommand parent;

    public SaveCommand(@NotNull Uhcffa plugin, @NotNull SuperCommand parent) {
        super("save", "ffa.admin");

        this.plugin = plugin;
        this.parent = parent;
    }

    @Override
    public boolean runCommand(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("only execute player");
            return false;
        }

        Player player = (Player) commandSender;
        final String playerID = PlayerConverter.getID(player);
        final CustomPlayer customPlayer = Uhcffa.getInstance().getCustomPlayer(playerID);

        if (customPlayer.getSetupData() == null) {
            player.sendMessage("/duel create [NAME] を実行してください");
            return false;
        }

        if (customPlayer.getSetupData().compile()) {
            Arena arena = new Arena(customPlayer.getSetupData().getName(), customPlayer.getSetupData().getMaxBuildY(), customPlayer.getSetupData().getSpawn1(),
                    customPlayer.getSetupData().getSpawn2());
            arenaManager.registerArena(arena);
            player.sendMessage("作成しました");
            return true;
        } else {
            player.sendMessage("未定義です");
            return false;
        }
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
