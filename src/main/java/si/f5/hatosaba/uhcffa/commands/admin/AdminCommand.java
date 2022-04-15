package si.f5.hatosaba.uhcffa.commands.admin;

import com.lielamar.lielsutils.bukkit.commands.Command;
import com.lielamar.lielsutils.bukkit.commands.SuperCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.commands.admin.subcommands.duel.CreateCommand;
import si.f5.hatosaba.uhcffa.commands.admin.subcommands.duel.RemoveCommand;
import si.f5.hatosaba.uhcffa.commands.admin.subcommands.duel.SaveCommand;
import si.f5.hatosaba.uhcffa.commands.admin.subcommands.duel.SetUpCommand;
import si.f5.hatosaba.uhcffa.commands.admin.subcommands.ffa.SetLobby;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;

import java.util.ArrayList;
import java.util.List;

public class AdminCommand extends SuperCommand {

    private final Uhcffa plugin;
    private final Command[] commands;
    public AdminCommand(Uhcffa plugin) {
        super("dueladmin", "");

        this.plugin = plugin;

        this.commands = new Command[]{
                new SetUpCommand(plugin, this),
                new CreateCommand(plugin, this),
                new RemoveCommand(plugin, this),
                new SaveCommand(plugin, this),
                new SetLobby(plugin, this)
        };
    }

    @Override
    public boolean runCommand(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if(args.length == 0) {
                player.sendMessage("引数が足りません");
                return false;
            }

            Command subCommand = super.getSubCommand(args[0]);

            if (subCommand != null)
            subCommand.execute(player, args);

            return false;
        }
        return false;
    }

    @Override
    public List<String> tabOptions(@NotNull CommandSender commandSender, @NotNull String[] strings) {
        return new ArrayList<>();
    }

    @Override
    public void subCommandNotFoundEvent(@NotNull CommandSender commandSender) {
        CustomPlayer customPlayer = Uhcffa.getCustomPlayer(commandSender);
        customPlayer.sendTranslated("command.no-permission");
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

