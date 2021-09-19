package si.f5.hatosaba.uhcffa.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import si.f5.hatosaba.uhcffa.spectetor.SpectetorSet;

public class PlayerDamageListener implements Listener {

    private final SpectetorSet set = SpectetorSet.getInstance();

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player)) return;

        Player player = ((Player) entity).getPlayer();

        if (player.getGameMode() != GameMode.SURVIVAL) return;

        if(set.isHideMode(player))
            event.setCancelled(true);

        switch (event.getCause()){
            case FALL:
                event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled = false)
    public void onAttack(EntityDamageByEntityEvent event) {
        Entity defender = event.getEntity();
        Entity damager = event.getDamager();

        if (damager instanceof Projectile) {
            damager = (Entity) ((Projectile)damager).getShooter();
        }

        if (defender instanceof Player && damager != null) {
            Player p = (Player)damager;
            if(set.isHideMode(p))
                event.setCancelled(true);
        }
    }
}


