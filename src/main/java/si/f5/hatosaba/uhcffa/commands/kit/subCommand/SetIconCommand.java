package si.f5.hatosaba.uhcffa.commands.kit.subCommand;

import com.lielamar.lielsutils.bukkit.commands.StandaloneCommand;
import com.lielamar.lielsutils.bukkit.commands.SuperCommand;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.kit.Kit;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SetIconCommand extends StandaloneCommand {

    private final Uhcffa plugin;
    private final SuperCommand parent;

    public SetIconCommand(@NotNull Uhcffa plugin, @NotNull SuperCommand parent) {
        super("setIcon", "ffa.admin");

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
            kit.setIcon(player.getItemInHand().getType() != null ? player.getItemInHand() : ItemBuilder.of(Material.BARRIER).build());
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