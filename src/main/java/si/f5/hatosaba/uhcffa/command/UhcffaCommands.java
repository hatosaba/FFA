package si.f5.hatosaba.uhcffa.command;

import io.github.mrblobman.spigotcommandlib.CommandHandle;
import io.github.mrblobman.spigotcommandlib.CommandHandler;
import io.github.mrblobman.spigotcommandlib.args.ArgDescription;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.Constants;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.cosmetics.manager.KillManager;
import si.f5.hatosaba.uhcffa.kit.Kit;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.user.UserSet;

import static org.bukkit.ChatColor.GRAY;

public class UhcffaCommands implements CommandHandler {

    @CommandHandle(
            command = {"ffa", "reload"},
            permission = "ffa.reload",
            description = "設定を更新する"
    )
    public void reload(CommandSender sender) {
        UserSet.getInstnace().saveAll();
        Uhcffa.instance().config().reload();
        Uhcffa.instance().config().load();
        sender.sendMessage(GRAY + "FFA :: kit.ymlのリロードが完了しました。");
    }

    @CommandHandle(
            command = {"ffa", "setspawn"},
            description = "ffaのスポーン地点を設定する",
            permission = "ffa.setspawn"
    )
    public boolean setspawn(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Uhcffa.instance().config().getSpawnPoints().add(player.getLocation());
            player.sendMessage("スポーン地点を設定しました");
        }
        return true;
    }

    @CommandHandle(
            command = {"ffa", "setlobby"},
            description = "ロビーのスポーン地点を設定",
            permission = "ffa.setlobby"
    )
    public void setlobby(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Uhcffa.instance().config().setLobby(player.getLocation());
            player.sendMessage("ロビーを設定しました");
        }
    }

    @CommandHandle(
            command = {"ffa", "setkit"},
            description = "キットを設定する",
            permission = "ffa.setkit"
    )
    public void setkit(CommandSender sender, @ArgDescription(name = "キットを指定してください") String kitname) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            /*if (kitname) {
                sender.sendMessage("キットを指定してください");
            } else {*/
            Kit kit = KitManager.getInstance().getKit(kitname);
            if (kit != null) {
                kit.saveKit(player);
            } else {
                sender.sendMessage("キットが見つかりません");
            }

        }
    }

    @CommandHandle(
            command = {"ffa", "seteditroom"},
            description = "編集部屋を設定する",
            permission = "ffa.seteditroom"
    )
    public void seteditroom(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Uhcffa.instance().config().setLocation(player.getLocation());
            player.sendMessage("編集部屋を設定しました");
        }
    }

    @CommandHandle(
            command = {"ffa", "item"},
            description = "初期アイテムを入手する",
            permission = "ffa.item"
    )
    public boolean item(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            tryGivingDefaultItemsTo(player);
        }
        return true;
    }

    private void tryGivingDefaultItemsTo(Player player) {
        if (KitManager.getInstance().isSelected(player)) {
            tryGivingItemTo(player, Constants.INSTANT_RESPAWN_ITEM, 0);
            tryGivingItemTo(player, Constants.KIT_SELECTOR, 2);
            tryGivingItemTo(player, Constants.SHOP, 4);
            tryGivingItemTo(player, Constants.BACK_TO_LOBBY, 8);
        } else {
            tryGivingItemTo(player, Constants.KIT_SELECTOR, 0);
            tryGivingItemTo(player, Constants.SHOP, 4);
        }
    }

    private void tryGivingItemTo(Player player, ItemStack item, int slot) {
        Inventory inventory = player.getInventory();
        if (!inventory.contains(item)) inventory.setItem(slot, item);
    }
}