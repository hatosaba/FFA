package si.f5.hatosaba.uhcffa.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if(player.getGameMode() != GameMode.SURVIVAL) return;

        if (player.getWorld().getName().equals("kitpvp") || player.getWorld().getName().equals("lobby")) {
            event.setCancelled(true);
        }
    }
}

