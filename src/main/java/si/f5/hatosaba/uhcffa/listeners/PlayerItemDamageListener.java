package si.f5.hatosaba.uhcffa.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerItemDamageListener implements Listener {

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null)
            return;

        if (!player.getWorld().getName().equals("kitpvp"))
            return;

            event.setCancelled(true);
            player.updateInventory();

    }
}

