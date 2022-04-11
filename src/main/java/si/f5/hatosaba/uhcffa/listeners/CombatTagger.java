package si.f5.hatosaba.uhcffa.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatTagger implements Listener {

    private static final Map<UUID, Boolean> TAGGED = new HashMap<>();
    private static final Map<UUID, BukkitRunnable> UNTAGGERS = new HashMap<>();
    private final int delay = 15;


    public static boolean isTagged(final UUID uuid) {
        boolean result = false;
        final Boolean state = TAGGED.get(uuid);
        if (state != null) {
            result = state;
        }
        return result;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamage(final EntityDamageByEntityEvent event) {
        final ArrayList<UUID> ids = new ArrayList<>();
        if (event.getEntity() instanceof Player) {
            ids.add(event.getEntity().getUniqueId());
        }
        if (event.getDamager() instanceof Player) {
            ids.add(event.getDamager().getUniqueId());
        }
        for (final UUID uuid : ids) {
            CustomPlayer customPlayer = Uhcffa.getCustomPlayer(uuid.toString());
            if (customPlayer.inArena()) return;
            TAGGED.put(uuid, true);
            final BukkitRunnable run = UNTAGGERS.get(uuid);
            if (run != null) {
                run.cancel();
            }
            UNTAGGERS.put(uuid, new BukkitRunnable() {
                @Override
                public void run() {
                    TAGGED.put(uuid, false);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (PlayerJoinQuitListener.combatPlayers.containsKey(uuid)) {
                                PlayerJoinQuitListener.combatPlayers.get(uuid).getEntity().getWorld().strikeLightningEffect(PlayerJoinQuitListener.combatPlayers.get(uuid).getEntity().getLocation());
                                PlayerJoinQuitListener.combatPlayers.get(uuid).destroy();
                                PlayerJoinQuitListener.combatPlayers.remove(uuid);
                            }
                        }
                    }.runTaskLater(Uhcffa.instance(), delay * 20L);
                }
            });
            UNTAGGERS.get(uuid).runTaskLater(Uhcffa.instance(), delay * 20L);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(final PlayerDeathEvent event) {
        final UUID uuid = event.getEntity().getUniqueId();
        final CustomPlayer customPlayer = Uhcffa.getCustomPlayer(PlayerConverter.getID(event.getEntity()));

        if (customPlayer.inArena()) return;
        TAGGED.remove(uuid);
        final BukkitRunnable runnable = UNTAGGERS.remove(uuid);
        if (runnable != null) {
            runnable.cancel();
        }
    }
}
