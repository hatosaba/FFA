package si.f5.hatosaba.uhcffa.listeners;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.schedule.Sync;
import si.f5.hatosaba.uhcffa.spectetor.SpectetorSet;
import si.f5.hatosaba.uhcffa.user.User;
import si.f5.hatosaba.uhcffa.user.UserSet;
import si.f5.hatosaba.uhcffa.utils.ParticleEffect;

import java.util.HashSet;

public class PlayerPlaceBlock implements Listener {

    private final SpectetorSet spectetorSet = SpectetorSet.getInstance();
    private final HashSet<Location> water = new HashSet<>();

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        final Player p = event.getPlayer();
        final Block b = event.getBlock();
        final User u = UserSet.getInstnace().getUser(p);

        if(p.getGameMode() == GameMode.SURVIVAL) {
            if (!p.getWorld().getName().equals("kitpvp"))
                return;
            if(event.getBlockReplacedState().getType() == Material.STATIONARY_WATER)
                water.add(b.getLocation());

            Sync.define(() -> {
                    if (water.contains(b.getLocation())) {
                        b.setType(Material.STATIONARY_WATER);
                    } else {
                        b.setType(Material.AIR);
                       /* UserSet.getInstnace().getOnlineUsers().forEach(user -> {
                            user.asBukkitPlayer().playEffect(b.getLocation(), Effect.STEP_SOUND);
                        });*/
                    }
                if (!spectetorSet.isHideMode(p)) {
                    int total = 0;
                    for (ItemStack item : p.getInventory().getContents()) {
                        if(item == null) continue;
                        if(item.isSimilar(u.blockItem)){
                            total += item.getAmount();
                        }
                    }
                    if(total < 64)
                        p.getInventory().addItem(u.blockItem);
                }


            }).executeLater(8*20);
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        event.setCancelled(true);
    }
}
