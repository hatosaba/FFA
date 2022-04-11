package si.f5.hatosaba.uhcffa.commands;

import com.lielamar.lielsutils.bukkit.commands.Command;
import com.lielamar.lielsutils.bukkit.commands.SuperCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.arena.Arena;
import si.f5.hatosaba.uhcffa.arena.ArenaState;
import si.f5.hatosaba.uhcffa.commands.ffa.subCommands.CreateKitCommand;
import si.f5.hatosaba.uhcffa.commands.ffa.subCommands.RemoveKitCommand;
import si.f5.hatosaba.uhcffa.commands.ffa.subCommands.SetKitCommand;
import si.f5.hatosaba.uhcffa.menu.InvSeeMenu;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;

import java.util.ArrayList;
import java.util.List;

public class InventoryCommand extends SuperCommand {

    private final Uhcffa plugin;
    private final Command[] commands;
    public InventoryCommand(Uhcffa plugin) {
        super("inventory", "");

        this.plugin = plugin;

        this.commands = new Command[]{};
    }

    @Override
    public boolean runCommand(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if(args.length == 0) {
                player.sendMessage("プレイヤー名を指定してください");
                return false;
            }

            String playerName = args[0];

            Player target = Bukkit.getPlayer(playerName);

            if (target == null) {
                player.sendMessage("プレイヤーが見つかりません");
                return false;
            }

            CustomPlayer customPlayer = Uhcffa.getCustomPlayer(PlayerConverter.getID(player));
            CustomPlayer targetPlayer = Uhcffa.getCustomPlayer(PlayerConverter.getID(target));
            String playerID = PlayerConverter.getID(player);
            String targetPlayerID = PlayerConverter.getID(target);

            if (!(customPlayer.inArena() && targetPlayer.inArena())) return false;

            if(customPlayer.getArena() == targetPlayer.getArena()) {
                Arena arena = customPlayer.getArena();

                if (arena.getState() != ArenaState.GAME_END) {
                    player.sendMessage("現在利用できません");
                    return  false;
                }

                arena.invSee(playerID, targetPlayerID);
                return  true;
            }

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
        return new String[] {
                "inv"
        };
    }
}


