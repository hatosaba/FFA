package si.f5.hatosaba.uhcffa.commands.ffa.subCommands;

import com.lielamar.lielsutils.bukkit.commands.StandaloneCommand;
import com.lielamar.lielsutils.bukkit.commands.SuperCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.kit.Kit;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;

import java.util.List;
import java.util.stream.Collectors;

public class RemoveKitCommand extends StandaloneCommand {

    private final KitManager kitManager = KitManager.getInstance();

    private final Uhcffa plugin;
    private final SuperCommand parent;

    public RemoveKitCommand(@NotNull Uhcffa plugin, @NotNull SuperCommand parent) {
        super("removeKit", "ffa.admin");

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

        if(args.length == 0) {
            player.sendMessage("足りない");
            return false;
        }

        String arenaName = args[0];
        /*if (arenaName == null) {
            player.sendMessage("Kit名を指定設定してください");
            return false;
        }*/

        if (kitManager.getKit(arenaName) == null) {
            player.sendMessage("存在しません");
            return false;
        }
        kitManager.removeKit(arenaName);
        player.sendMessage("削除しました");
        return true;
    }

    @Override
    public List<String> tabOptions(@NotNull CommandSender commandSender, @NotNull String[] args) {
        return kitManager.getKits().stream().map(Kit::getName).collect(Collectors.toList());
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
