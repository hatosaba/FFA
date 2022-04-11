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

public class DragonSword extends ExecutableItem {

    public DragonSword(String id) {
        super(id);
    }

    @Override
    public ItemStack buildItemStack() {
        return define(
                ItemBuilder.of(Material.DIAMOND_SWORD)
                        .name("&aDragon Sword")
                        .lore(Arrays.asList(
                                "&7Weaponsmith Ultimate",
                                "",
                                "&9+8 Attack Damage"
                        ))
                        .flags(ItemFlag.HIDE_ATTRIBUTES)
                        .build()
        );
    }

    @Override
    public void onDamageEntity(PlayerDamageEntityEvent event) {
        if (!(event.damagee() instanceof Player)) return;
        Player damagee = (Player) event.damagee();
        ItemStack item = event.damager().getItemInHand();
        NBTItem nbtItem = new NBTItem(item);

        double damage = Math.max(0, nbtItem.getDouble("damage"));
        double health = Math.max(0, damagee.getHealth() - (1 + damage));

        event.setDamage(event.damage() + damage + 1);
    }

    @Override
    public void onDrop(PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1F, 1F);
        player.getInventory().setItemInHand(new ItemStack(Material.AIR));
        event.setCancelled(true);
    }

    @Override
    public void onPlayerKill(PlayerDeathEvent event) {
        ItemStack item = event.getEntity().getKiller().getItemInHand();
        NBTItem nbtItem = new NBTItem(item);
        DecimalFormat df = new DecimalFormat("#.##");

        int kills = Math.max(1, nbtItem.getInteger("kills") + 1);
        double damage = Math.min(1, Math.max(0.2, kills * 0.2));

        nbtItem.setDouble("damage", damage);
        nbtItem.setInteger("kills", kills);

        event.getEntity().getKiller().setItemInHand(
                ItemBuilder.modify(nbtItem.getItem())
                        .lore(Arrays.asList(
                                "&7Weaponsmith Ultimate",
                                "Sword Master: " + kills + " kills (+" + df.format(damage) + " dmg)",
                                "",
                                "&9+" + df.format(8 + damage) + " Attack Damage"
                        ))
                        .build());


    }

}
