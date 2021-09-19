package si.f5.hatosaba.uhcffa.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import si.f5.hatosaba.uhcffa.Constants;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.schedule.Sync;
import si.f5.hatosaba.uhcffa.spectetor.SpectetorSet;
import si.f5.hatosaba.uhcffa.user.UserSet;

import static org.bukkit.ChatColor.*;

public class PlayerJoinListener implements Listener {

    private final SpectetorSet spectetorSet = SpectetorSet.getInstance();
    private final UserSet userSet = UserSet.getInstnace();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        userSet.onJoin(event);

        player.teleport(Uhcffa.instance().config().getLobby());

        PlayerInventory inv = player.getInventory();
        inv.clear();
        inv.setHelmet(new ItemStack(Material.AIR));
        inv.setChestplate(new ItemStack(Material.AIR));
        inv.setLeggings(new ItemStack(Material.AIR));
        inv.setBoots(new ItemStack(Material.AIR));
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        player.setMaxHealth(40);
        if (player.getHealth() == 20) {
            player.setHealth(40);
        }

        tryGivingDefaultItemsTo(player);

        spectetorSet.onPlayerJoin(player);

        if(player.getGameMode() == GameMode.SURVIVAL) {
            player.setAllowFlight(false);
            player.setFlying(false);
        }

        Sync.define(() -> player.sendMessage(Constants.UPDATE_MESSAGE)).executeLater(5*20L);
    }

    private void tryGivingDefaultItemsTo(Player player) {
        if(KitManager.getInstance().isSelected(player)) {
            tryGivingItemTo(player, Constants.INSTANT_RESPAWN_ITEM, 0);
            tryGivingItemTo(player, Constants.KIT_SELECTOR, 2);
            tryGivingItemTo(player, Constants.SHOP, 4);
        } else {
            tryGivingItemTo(player, Constants.KIT_SELECTOR, 0);
            tryGivingItemTo(player, Constants.SHOP, 4);
        }

    }

    private void tryGivingItemTo(Player player, ItemStack item, int slot) {
        Inventory inventory = player.getInventory();
        if (!inventory.contains(item)) inventory.setItem(slot,item);
    }
}
