package si.f5.hatosaba.uhcffa.commands.admin.subcommands;

import com.lielamar.lielsutils.bukkit.commands.StandaloneCommand;
import com.lielamar.lielsutils.bukkit.commands.SuperCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.arena.ArenaManager;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;

import java.util.ArrayList;
import java.util.List;

public class CreateCommand extends StandaloneCommand {

    private final ArenaManager arenaManager = Uhcffa.instance().getArenaManager();

    private final Uhcffa plugin;
    private final SuperCommand parent;

    public CreateCommand(@NotNull Uhcffa plugin, @NotNull SuperCommand parent) {
        super("create", "ffa.admin");

        this.plugin = plugin;
        this.parent = parent;
    }

    @Override
    public boolean runCommand(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("only execute player");
            return false;
        }

        CustomPlayer customPlayer = Uhcffa.getCustomPlayer((Player) commandSender);

        if(args.length == 0) {
            customPlayer.sendTranslated("not_arg");
            return false;
        }

        String arenaName = args[0];
        if (arenaName == null) {
            customPlayer.sendTranslated("set.to.arena");
            return false;
        }
        if (arenaManager.getArenas().containsKey(arenaName)) {
            customPlayer.sendTranslated("map.already");
            return false;
        }
        customPlayer.setSetupData();
        customPlayer.getSetupData().setName(arenaName);
        customPlayer.sendTranslated("map.created");
        customPlayer.sendTranslated("map.set");
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