package si.f5.hatosaba.uhcffa.command;

import io.github.mrblobman.spigotcommandlib.CommandHandle;
import io.github.mrblobman.spigotcommandlib.CommandHandler;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import si.f5.hatosaba.uhcffa.Constants;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.user.UserSet;

public class KitSaveCommand implements CommandHandler {

    @CommandHandle(
            command = {"kitsave"},
            description = "キットを保存"
    )
    public void kitsave(CommandSender sender) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            UserSet.getInstnace().getUser(player).saveKit(player);

            PlayerInventory inv = player.getInventory();
            inv.clear();
            inv.setHelmet(new ItemStack(Material.AIR));
            inv.setChestplate(new ItemStack(Material.AIR));
            inv.setLeggings(new ItemStack(Material.AIR));
            inv.setBoots(new ItemStack(Material.AIR));
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }

            tryGivingDefaultItemsTo(player);
        }
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
