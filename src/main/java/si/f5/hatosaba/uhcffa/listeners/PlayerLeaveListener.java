package si.f5.hatosaba.uhcffa.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.spectetor.SpectetorSet;

public class PlayerLeaveListener implements Listener {

    private final KitManager kitManager = KitManager.getInstance();
    private final SpectetorSet spectetorSet = SpectetorSet.getInstance();

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(kitManager.isSelected(player))
            kitManager.getSelectedPlayer().remove(player);
        spectetorSet.onPlayerQuit(player);
    }
}
