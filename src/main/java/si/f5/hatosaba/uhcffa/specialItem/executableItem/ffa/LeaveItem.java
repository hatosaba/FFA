package si.f5.hatosaba.uhcffa.specialItem.executableItem.ffa;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.menu.ShopMenu;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItem;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;

public class LeaveItem extends ExecutableItem {

    public LeaveItem(String id) {
        super(id);
    }

    @Override
    public ItemStack buildItemStack() {
        return define(
                ItemBuilder.of(Material.BED)
                        .name("&cLeave")
                        .build()
        );
    }

    @Override
    public void onClick(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        player.teleport(Uhcffa.instance().config().getLobby());
        Uhcffa.instance().setLobbyItem(player);
        KitManager.getInstance().removeSelectedKit(player);
        if(player.getGameMode() == GameMode.SURVIVAL) {
            player.setAllowFlight(false);
            player.setFlying(false);
        }
        event.setCancelled(true);
    }
}