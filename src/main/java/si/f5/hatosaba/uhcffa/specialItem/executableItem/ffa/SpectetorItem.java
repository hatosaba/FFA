package si.f5.hatosaba.uhcffa.specialItem.executableItem.ffa;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItem;
import si.f5.hatosaba.uhcffa.spectetor.SpectetorSet;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;

import java.util.Random;

public class SpectetorItem extends ExecutableItem {

    public SpectetorItem(String id) {
        super(id);
    }

    @Override
    public ItemStack buildItemStack() {
        return define(
                ItemBuilder.of(Material.WATCH)
                        .name("&bFFAを観戦する")
                        .build()
        );
    }

    @Override
    public void onClick(PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        player.setAllowFlight(true);
        player.setFlying(true);
        SpectetorSet.getInstance().applyHideMode(player);
        Uhcffa.instance().setSpectatorItem(player);
        player.teleport(Uhcffa.instance().config().getSpawnPoints().stream().skip((new Random()).nextInt(Uhcffa.instance().config().getSpawnPoints().size())).findFirst().get());

        event.setCancelled(true);
    }
}