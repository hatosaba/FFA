package si.f5.hatosaba.uhcffa.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDropListener implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equals("kitpvp"))
            return;
        if(player.getGameMode() == GameMode.SURVIVAL) event.setCancelled(true);
    }
}
