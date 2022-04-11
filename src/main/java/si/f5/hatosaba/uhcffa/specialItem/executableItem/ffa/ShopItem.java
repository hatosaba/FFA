package si.f5.hatosaba.uhcffa.specialItem.executableItem.ffa;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.menu.ShopMenu;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItem;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;

public class ShopItem extends ExecutableItem {

    public ShopItem(String id) {
        super(id);
    }

    @Override
    public ItemStack buildItemStack() {
        return define(
                ItemBuilder.of(Material.EMERALD)
                        .name("&eShop")
                        .build()
        );
    }

    @Override
    public void onClick(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        ShopMenu.INVENTORY.open(player);
        event.setCancelled(true);
    }
}