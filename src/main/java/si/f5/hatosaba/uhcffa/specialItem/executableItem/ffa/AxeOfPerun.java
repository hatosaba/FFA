package si.f5.hatosaba.uhcffa.specialItem.executableItem.ffa;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.event.PlayerDamageEntityEvent;
import si.f5.hatosaba.uhcffa.schedule.Sync;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItem;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;

import java.util.HashSet;

public class AxeOfPerun extends ExecutableItem {

    private final HashSet<Player> cooldownPlayers = new HashSet<>();

    public AxeOfPerun(String id) {
        super(id);
    }

    @Override
    public ItemStack buildItemStack() {
        return define(
                ItemBuilder.of(Material.DIAMOND_AXE)
                        .name("&aAxe of Perun")
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

    @Override
    public void onDamageEntity(PlayerDamageEntityEvent event) {
        if (!(event.damagee() instanceof Player)) return;
        Player damager = event.damager();
        Player damagee = (Player) event.damagee();

        ////クールダウン中なら戻る
        if (cooldownPlayers.contains(damager)) return;

        damagee.getWorld().strikeLightningEffect(damagee.getLocation());

        double damage = 3;
        double health = Math.max(0, damagee.getHealth() - damage);

        event.setDamage(3);
        damagee.setHealth(health);
        //クールダウンさせる
        cooldownPlayers.add(damager);

        //5秒後にクールダウンを完了させる
        Sync.define(() -> cooldownPlayers.remove(damager)).executeLater(8 * 20L);
    }
}

