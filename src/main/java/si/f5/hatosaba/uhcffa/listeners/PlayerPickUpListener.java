package si.f5.hatosaba.uhcffa.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import si.f5.hatosaba.uhcffa.spectetor.SpectetorSet;

public class PlayerPickUpListener implements Listener {

    private final SpectetorSet spectetorSet = SpectetorSet.getInstance();

    @EventHandler
    public void onPickUp(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();

        if (!player.getWorld().getName().equals("kitpvp"))
            return;

        if(spectetorSet.isHideMode(player)) event.setCancelled(true);

    }
}
