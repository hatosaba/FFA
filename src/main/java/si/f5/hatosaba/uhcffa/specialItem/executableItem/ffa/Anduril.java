package si.f5.hatosaba.uhcffa.specialItem.executableItem.ffa;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItem;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;

public class Anduril extends ExecutableItem {

    public Anduril(String id) {
        super(id);
    }

    @Override
    public ItemStack buildItemStack() {
        return define(
                ItemBuilder.of(Material.IRON_SWORD)
                        .name("&aAnduril")
                        .enchantment(Enchantment.DAMAGE_ALL, 1)
                        .build()
        );
    }

    @Override
    public void onDrop(PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1F, 1F);
        player.getInventory().setItemInHand(new ItemStack(Material.AIR));
        event.setCancelled(true);
    }
}
