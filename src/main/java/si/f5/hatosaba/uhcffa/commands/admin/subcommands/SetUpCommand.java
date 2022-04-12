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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SetUpCommand extends StandaloneCommand {

    private final ArenaManager arenaManager = Uhcffa.getInstance().getArenaManager();

    private final Uhcffa plugin;
    private final SuperCommand parent;

    public SetUpCommand(@NotNull Uhcffa plugin, @NotNull SuperCommand parent) {
        super("set", "ffa.admin");

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
            player.sendMessage("specify.duel.name");
            return false;
        }

        if(args[0] != null) {
            if (customPlayer.getSetupData() == null) {
                player.sendMessage("/duel create [NAME] を実行してください");
                return false;
            }

            switch (args[0]) {
                case "spawn1":
                    customPlayer.getSetupData().setSpawn1(player.getLocation());
                    player.sendMessage("spawn1を設定しました");
                    return true;
                case "spawn2":
                    customPlayer.getSetupData().setSpawn2(player.getLocation());
                    player.sendMessage("spawn2を設定しました");
                    return true;
                case "maxBuildY":
                    customPlayer.getSetupData().setMaxBuildY(player.getLocation().getY());
                    player.sendMessage("maxBuildYを設定しました");
                    return true;
            }
        }
        return false;
    }

    @Override
    public List<String> tabOptions(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("maxBuildY", "spawn1", "spawn2");
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
