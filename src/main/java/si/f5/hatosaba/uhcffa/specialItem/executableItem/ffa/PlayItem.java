package si.f5.hatosaba.uhcffa.specialItem.executableItem.ffa;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItem;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;

public class PlayItem extends ExecutableItem {

    public PlayItem(String id) {
        super(id);
    }

    @Override
    public ItemStack buildItemStack() {
        return define(
                ItemBuilder.of(Material.BOOK)
                        .name("&a&lPlay")
                        .build()
        );
    }

    @Override
    public void onClick(PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        KitManager.getInstance().selectToKit(player, KitManager.getInstance().getKit("standard"), false);

        event.setCancelled(true);
    }
}
