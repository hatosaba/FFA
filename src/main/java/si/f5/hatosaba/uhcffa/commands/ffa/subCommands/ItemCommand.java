package si.f5.hatosaba.uhcffa.commands.ffa.subCommands;

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

public class ItemCommand extends StandaloneCommand {

    private final KitManager kitManager = KitManager.getInstance();

    private final Uhcffa plugin;
    private final SuperCommand parent;

    public ItemCommand(@NotNull Uhcffa plugin, @NotNull SuperCommand parent) {
        super("item", "ffa.item");

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
        tryGivingDefaultItemsTo(player);

        return true;
    }

    private void tryGivingDefaultItemsTo(Player player) {
        if (KitManager.getInstance().isSelected(player)) {
            //tryGivingItemTo(player, Constants.INSTANT_RESPAWN_ITEM, 0);
            Uhcffa.getInstance().setSpectatorItem(player);
        } else {
            Uhcffa.getInstance().setLobbyItem(player);
        }
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

