package si.f5.hatosaba.uhcffa.commands.duel;

import com.lielamar.lielsutils.bukkit.commands.Command;
import com.lielamar.lielsutils.bukkit.commands.SuperCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.arena.ArenaManager;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AcceptCommand extends SuperCommand {

    private final ArenaManager arenaManager = Uhcffa.getInstance().getArenaManager();

    private final Uhcffa plugin;
    private final Command[] commands;
    public AcceptCommand(Uhcffa plugin) {
        super("accept", "duel.use");

        this.plugin = plugin;

        this.commands = new Command[]{};
    }

    @Override
    public boolean runCommand(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if (commandSender instanceof Player) {
            CustomPlayer sender = Uhcffa.getCustomPlayer(commandSender);

            if(args.length == 0) {
                sender.sendTranslated("specify.player.name");
                return false;
            }

            String playerName = args[0];

            Player player = Bukkit.getPlayer(playerName);

            if (player == null) {
                sender.sendTranslated("not.find.player", playerName);
                return false;
            }

        /*if (sender.getName().equals(playerName)) {
            sender.sendMessage("実行できません");
            return;
        }*/

            final String playerID = PlayerConverter.getID(player);
            final String toPlayerID = sender.getPlayerID();

            CustomPlayer customPlayer = Uhcffa.getInstance().getCustomPlayer(toPlayerID);
            ArenaManager.Request request = customPlayer.getRequest(playerID);

            if (request == null) {
                sender.sendTranslated("request.not.receive" ,player.getName());
                return false;
            }

            customPlayer.removeRequest(playerID);

            arenaManager.createMatch(request);

            return true;
        }
        return false;
    }

    @Override
    public List<String> tabOptions(@NotNull CommandSender commandSender, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            if (strings.length == 1) {
                Player player = (Player) commandSender;
                String playerID = PlayerConverter.getID(player);
                CustomPlayer customPlayer = Uhcffa.getCustomPlayer(playerID);
                return customPlayer.getRequests().values().stream().map(request -> PlayerConverter.getName(request.getPlayerID())).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    @Override
    public void subCommandNotFoundEvent(@NotNull CommandSender commandSender) {
        commandSender.sendMessage("not found command");
    }

    @Override
    public void noPermissionEvent(@NotNull CommandSender commandSender) {
        commandSender.sendMessage("no permission");
    }

    @Override
    public @NotNull Command[] getSubCommands() {
        return this.commands;
    }


    @Override
    public @NotNull String getUsage() {
        return "";
    }

    @Override
    public @NotNull String getDescription() {
        return "";
    }

    @Override
    public @NotNull String[] getAliases() {
        return new String[0];
    }
}

