package si.f5.hatosaba.uhcffa.specialItem.executableItem.ffa;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.event.PlayerDamageEntityEvent;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItem;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;

import java.text.DecimalFormat;
import java.util.Arrays;

public class BloodLust extends ExecutableItem {

    public BloodLust(String id) {
        super(id);
    }

    @Override
    public ItemStack buildItemStack() {
        return define(
                ItemBuilder.of(Material.WOOD_SWORD)
                        .name("&cBloodlust")
                        .enchantment(Enchantment.DAMAGE_ALL, 1)
                        .lore(Arrays.asList(
                                "→&bGains Sharpness II after 3kill",
                                "→&bGains Sharpness III after 6kill",
                                "→&bGains Sharpness IV after 9kill",
                                "→&bGains Sharpness V after 10kill"
                        ))
                        .build()
        );
    }

    @Override
    public void onDrop(PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        /*player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1F, 1F);
        player.getInventory().setItemInHand(new ItemStack(Material.AIR));*/
        event.setCancelled(true);
    }

    @Override
    public void onPlayerKill(PlayerDeathEvent event) {
        ItemStack item = event.getEntity().getKiller().getItemInHand();
        NBTItem nbtItem = new NBTItem(item);

        int kills = Math.max(0, nbtItem.getInteger("kills") + 1);
        int level = kills >= 10 ? 5 : kills >= 9 ? 4 : kills >= 6 ? 3 : kills >= 3 ? 2 : 1;

        nbtItem.setInteger("kills", kills);

        event.getEntity().getKiller().setItemInHand(
                ItemBuilder.modify(nbtItem.getItem())
                        .enchantment(Enchantment.DAMAGE_ALL, level)
                        .build());
    }
}

