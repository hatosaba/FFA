package si.f5.hatosaba.uhcffa.commands.kit.subCommand;

import com.lielamar.lielsutils.bukkit.commands.StandaloneCommand;
import com.lielamar.lielsutils.bukkit.commands.SuperCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.kit.Kit;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SetKitCommand extends StandaloneCommand {

    private final Uhcffa plugin;
    private final SuperCommand parent;

    public SetKitCommand(@NotNull Uhcffa plugin, @NotNull SuperCommand parent) {
        super("setKit", "ffa.admin");

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

        if(args.length == 0) {
            player.sendMessage("引数が足りません");
            return false;
        }

        String kitName = args[0];
        if (kitName == null) {
            player.sendMessage("キット名を指定設定してください");
            return false;
        }
        Kit kit = KitManager.getInstance().getKit(kitName);
        if (kit != null) {
            kit.saveKit(player);
            return true;
        } else {
            player.sendMessage("キットが見つかりません");
            return false;
        }
    }

    @Override
    public List<String> tabOptions(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if (args.length == 1) {
            return KitManager.getInstance().getKits().stream().map(Kit::getName).collect(Collectors.toList());
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
