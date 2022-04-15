package si.f5.hatosaba.uhcffa.commands.ffa;

import com.lielamar.lielsutils.bukkit.commands.Command;
import com.lielamar.lielsutils.bukkit.commands.SuperCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.commands.ffa.subCommands.*;
import si.f5.hatosaba.uhcffa.commands.kit.subCommand.CreateKitCommand;
import si.f5.hatosaba.uhcffa.commands.kit.subCommand.RemoveKitCommand;
import si.f5.hatosaba.uhcffa.commands.kit.subCommand.SetKitCommand;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;
import si.f5.hatosaba.uhcffa.utils.Translated;

import java.util.ArrayList;
import java.util.List;

public class FFACommand extends SuperCommand {

    private final Uhcffa plugin;
    private final Command[] commands;
    public FFACommand(Uhcffa plugin) {
        super("ffa", "ffa.use");

        this.plugin = plugin;

        this.commands = new Command[]{
                new SetKitCommand(plugin, this),
                new CreateKitCommand(plugin, this),
                new RemoveKitCommand(plugin, this),
                new ItemCommand(plugin, this),
                new JoinCommand(plugin, this)
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
        commandSender.sendMessage("not found command");
    }

    @Override
    public void noPermissionEvent(@NotNull CommandSender commandSender) {
        CustomPlayer customPlayer = Uhcffa.getCustomPlayer(commandSender);
        customPlayer.sendTranslated("command.no-permission");
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


