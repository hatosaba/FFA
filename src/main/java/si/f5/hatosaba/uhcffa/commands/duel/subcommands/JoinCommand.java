package si.f5.hatosaba.uhcffa.commands.duel.subcommands;

import com.lielamar.lielsutils.bukkit.commands.StandaloneCommand;
import com.lielamar.lielsutils.bukkit.commands.SuperCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.arena.ArenaManager;
import si.f5.hatosaba.uhcffa.kit.Kit;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;

import java.util.List;
import java.util.stream.Collectors;

public class JoinCommand extends StandaloneCommand {

    private final ArenaManager arenaManager = Uhcffa.getInstance().getArenaManager();
    private final Uhcffa plugin;
    private final SuperCommand parent;

    public JoinCommand(@NotNull Uhcffa plugin, @NotNull SuperCommand parent) {
        super("join", "duel.use");

        this.plugin = plugin;
        this.parent = parent;
    }

    @Override
    public boolean runCommand(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("only execute player");
            return false;
        }

        final CustomPlayer sender = Uhcffa.getCustomPlayer(commandSender);

        if(args.length == 0) {
            sender.sendTranslated("specify.kit-name");
            //player.sendMessage("キット名を指定設定してください");
            return false;
        }

        String kitName = args[0];
        if (!KitManager.getInstance().containsKit(kitName)) {
            sender.sendTranslated("kit.not-found");
            //player.sendMessage("存在しません");
            return false;
        }

        arenaManager.joinMatch(sender, KitManager.getInstance().getKit(kitName));
        return true;
    }

    @Override
    public List<String> tabOptions(@NotNull CommandSender commandSender, @NotNull String[] args) {
        return KitManager.getInstance().getKits().stream().map(Kit::getName).collect(Collectors.toList());
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

