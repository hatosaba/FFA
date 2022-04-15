package si.f5.hatosaba.uhcffa.commands.kit.subCommand;

import com.lielamar.lielsutils.bukkit.commands.StandaloneCommand;
import com.lielamar.lielsutils.bukkit.commands.SuperCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.kit.Kit;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;

import java.util.ArrayList;
import java.util.List;

public class CreateKitCommand extends StandaloneCommand {

    private final KitManager kitManager = KitManager.getInstance();

    private final Uhcffa plugin;
    private final SuperCommand parent;

    public CreateKitCommand(@NotNull Uhcffa plugin, @NotNull SuperCommand parent) {
        super("createKit", "ffa.admin");

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
        if (arenaName == null) {
            player.sendMessage("キット名を指定設定してください");
            return false;
        }
        if (kitManager.getKit(arenaName) != null) {
            player.sendMessage("すでにあります");
            return false;
        }
        kitManager.registerKit(new Kit(arenaName, player.getEquipment().getArmorContents(), player.getInventory().getContents()));
        player.sendMessage("作成しました");
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
