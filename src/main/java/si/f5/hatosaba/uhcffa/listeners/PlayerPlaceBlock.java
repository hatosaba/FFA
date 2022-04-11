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
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;
import si.f5.hatosaba.uhcffa.schedule.Sync;
import si.f5.hatosaba.uhcffa.spectetor.SpectetorSet;
import si.f5.hatosaba.uhcffa.user.User;
import si.f5.hatosaba.uhcffa.user.UserSet;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;

import java.util.HashSet;
import java.util.Set;

public class PlayerPlaceBlock implements Listener {

    private final SpectetorSet spectetorSet = SpectetorSet.getInstance();

    private static HashSet<Location> water = new HashSet<>();
    private static Set<Location> blocksPlaced = new HashSet<>();

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        final Player p = event.getPlayer();
        final Block b = event.getBlock();
        final User u = UserSet.getInstnace().getUser(p);

        final String playerID = PlayerConverter.getID(p);
        final CustomPlayer customPlayer = Uhcffa.instance().getCustomPlayer(playerID);

        if (customPlayer.inArena()) return;

        if(p.getGameMode() == GameMode.SURVIVAL) {
            if (!p.getWorld().getName().equals("kitpvp"))
                return;
            if(event.getBlockReplacedState().getType() == Material.STATIONARY_WATER)
                water.add(b.getLocation());

            blocksPlaced.add(b.getLocation());

            Sync.define(() -> {
                    if (water.contains(b.getLocation())) {
                        b.setType(Material.STATIONARY_WATER);
                    } else {
                        b.getLocation().getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getTypeId());
                        b.setType(Material.AIR);
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
                blocksPlaced.remove(b.getLocation());
            }).executeLater(8*20);
        }
    }

    public static void removeBlocks() {
        for (Location location : blocksPlaced) {
            location.getBlock().setType(Material.AIR);
        }
        for (Location location : water) {
            location.getBlock().setType(Material.STATIONARY_WATER);
        }
    }
}
