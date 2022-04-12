package si.f5.hatosaba.uhcffa.commands.duel;

import com.lielamar.lielsutils.bukkit.commands.Command;
import com.lielamar.lielsutils.bukkit.commands.SuperCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.arena.ArenaManager;
import si.f5.hatosaba.uhcffa.commands.duel.subcommands.LeaveCommand;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.menu.KitSelectorMenu;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;

import java.util.List;
import java.util.stream.Collectors;

public class DuelCommand extends SuperCommand {

    private final ArenaManager arenaManager = Uhcffa.getInstance().getArenaManager();

    private final Uhcffa plugin;
    private final Command[] commands;
    public DuelCommand(Uhcffa plugin) {
        super("duel", "duel.use");

        this.plugin = plugin;

        this.commands = new Command[]{
                new LeaveCommand(plugin, this)
        };
    }

    @Override
    public boolean runCommand(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if (commandSender instanceof Player) {
            CustomPlayer sender = Uhcffa.getCustomPlayer(commandSender);

            if(args.length == 0) {
                sender.sendTranslated("specify.player.name");
                //sender.sendMessage("プレイヤー名を指定してください");
                return false;
            }

            String playerName = args[0];
            Player player = Bukkit.getPlayer(playerName);

            if (player == null) {
                sender.sendTranslated("not.find.player", playerName);
                //sender.sendMessage(playerName + "というプレイヤーが見つかりません");
                return false;
            }

        /*if (sender.getName().equals(playerName)) {
            sender.sendMessage("自分自身にリクエストを送信できません");
            return;
        }*/
            if(KitManager.getInstance().isSelected(sender.getPlayer())) {
                sender.sendTranslated("duel.playing.ffa");
                return false;
            }

            if(KitManager.getInstance().isSelected(player)) {
                sender.sendTranslated("duel.playing.ffa");
                return false;
            }

            CustomPlayer customPlayer = Uhcffa.getCustomPlayer(PlayerConverter.getID(player));

            if (customPlayer.isBusy()) {
                sender.sendTranslated("duel.playing");
                //sender.sendMessage("duelをプレイ中です");
                return false;
            }

            final String playerID = PlayerConverter.getID(sender.getPlayer());
            final String toPlayerID = PlayerConverter.getID(player);

            if (customPlayer.isAlreadyRequested(playerID)) {
                sender.sendTranslated("request.already");
                //sender.sendMessage("すでにリクエストを送信しています");
                return false;
            }

            if (!arenaManager.canRequest()) {
                sender.sendTranslated("request.cannot");
                //sender.sendMessage("リクエストを送信できません");
                return false;
            }

            KitSelectorMenu.INVENTORY(playerID, toPlayerID).open(sender.getPlayer());
            return true;
        }
        return false;
    }

    @Override
    public List<String> tabOptions(@NotNull CommandSender commandSender, @NotNull String[] strings) {
        if (strings.length == 1) {
            return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).filter(s -> !s.equals(commandSender.getName())).collect(Collectors.toList());
        }
        return null;
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
