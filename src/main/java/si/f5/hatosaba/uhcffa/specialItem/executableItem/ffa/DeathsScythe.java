package si.f5.hatosaba.uhcffa.specialItem.executableItem.ffa;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.event.PlayerDamageEntityEvent;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItem;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItemType;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;

public class DeathsScythe extends ExecutableItem {

    public DeathsScythe(String id) {
        super(id);
    }

    @Override
    public ItemStack buildItemStack() {
        return define(
                ItemBuilder.of(Material.IRON_HOE)
                        .name("&dDeath's Scythe")
                        .lore("&9Oblivion")
                        .build()
        );
    }

    @Override
    public void onClick(PlayerInteractEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onDrop(PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1F, 1F);
        player.getInventory().setItemInHand(new ItemStack(Material.AIR));
        event.setCancelled(true);
    }

    //エンティティに攻撃した時のアクションを設定する
    @Override
    public void onDamageEntity(PlayerDamageEntityEvent event) {
        if (!(event.damagee() instanceof Player)) return;
        Player damager = event.damager();
        Player damagee = (Player) event.damagee();
        ItemStack item = event.damager().getItemInHand();

        double damage = Math.max(0, damagee.getHealth() - (damagee.getHealth() * .2));
        double health = Math.min(damager.getMaxHealth(), damager.getHealth() + (damagee.getHealth() * .25));
        short durability = (short) Math.min(250, item.getDurability() + 20.83);

        damagee.setHealth(damage);
        damager.setHealth(health);
        item.setDurability(durability);

        if (item.getDurability() == item.getType().getMaxDurability()) {
            damager.playSound(damager.getLocation(), Sound.ITEM_BREAK, 1F, 1F);
            damager.getInventory().setItemInHand(new ItemStack(Material.AIR));
        }
    }
}

